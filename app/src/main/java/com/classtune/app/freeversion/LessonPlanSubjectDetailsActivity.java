package com.classtune.app.freeversion;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.LessonPlanStudentParentSubject;
import com.classtune.app.schoolapp.model.LessonPlanSubjectDetails;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLACK HAT on 08-Apr-15.
 */
public class LessonPlanSubjectDetailsActivity extends ChildContainerActivity{


    private LessonPlanStudentParentSubject data;
    private UIHelper uiHelper;
    private UserHelper userHelper;

    private Gson gson;

    private ImageView imgSubjectIcon;
    private TextView txtSubjectName;

    private PullToRefreshListView listViewLessonPlanSubjectDetails;

    private boolean hasNext = false;
    private int pageNumber = 1;
    private boolean isRefreshing = false;
    private boolean loading = false;
    private boolean stopLoadingData = false;

    private List<LessonPlanSubjectDetails> listSubjectDetails;

    private LessonPlanSubjectDetailsAdapter adapter;



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lessonplan_subject_details);

        userHelper = new UserHelper(this);
        uiHelper = new UIHelper(this);

        gson = new Gson();

        if(getIntent().getExtras() != null)
        {
            String strData  = getIntent().getExtras().getString(AppConstant.DATA_LESSON_PLAN_SUBJECT);
            data = gson.fromJson(strData, LessonPlanStudentParentSubject.class);
        }

        Log.e("SUBJECT_NAME_LESSON", "is: " + data.getName());

        listSubjectDetails = new ArrayList<LessonPlanSubjectDetails>();


        initView();

        initApiCall();


    }

    private void initView()
    {
        imgSubjectIcon = (ImageView)this.findViewById(R.id.imgSubjectIcon);
        txtSubjectName = (TextView)this.findViewById(R.id.txtSubjectName);

        imgSubjectIcon.setImageResource(AppUtility.getImageResourceId(data.getIcon(), this));
        txtSubjectName.setText(data.getName());

        listViewLessonPlanSubjectDetails = (PullToRefreshListView)this.findViewById(R.id.listViewLessonPlanSubjectDetails);
        setUpList();


        listViewLessonPlanSubjectDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LessonPlanSubjectDetails data = (LessonPlanSubjectDetails)adapter.getItem(position-1);

                Log.e("LIST", "clicked" + position);

                Intent intent = new Intent(LessonPlanSubjectDetailsActivity.this, SingleLessonPlan.class);
                intent.putExtra(AppConstant.ID_SINGLE_LESSON_PLAN, data.getId());
                startActivity(intent);

            }
        });


    }


    private void initApiCall()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("subject_id", data.getId());

        params.put("page_size", "10");
        params.put("page_number", String.valueOf(pageNumber));

        if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
        {
            params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
            params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());

            Log.e("STU_ID", userHelper.getUser().getSelectedChild().getId());
            Log.e("STU_PROFILE_ID", userHelper.getUser().getSelectedChild().getProfileId());
            Log.e("STU_BATCH_ID", userHelper.getUser().getSelectedChild().getBatchId());
        }


        AppRestClient.post(URLHelper.URL_GET_LESSONPLAN_SUBJECT_DETAILS, params, lessonSubjectDetailsHandler);
    }


    AsyncHttpResponseHandler lessonSubjectDetailsHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(arg1);
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        };

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog("Please wait...");


        };

        @Override
        public void onSuccess(int arg0, String responseString) {


            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            hasNext = modelContainer.getData().get("has_next").getAsBoolean();
            Log.e("HAS_NEXT_MEETING", "is: " + hasNext);


            if (pageNumber == 1) {
                adapter.clearList();
            }

            if (!hasNext) {
                stopLoadingData = true;
            }


            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arrayLesson = modelContainer.getData().get("lessonplans").getAsJsonArray();

                //listLessonPlan = parseLessonPlan(arrayLesson.toString());

                for (int i = 0; i < parseLessonPlanSubjectDetails(arrayLesson.toString()).size(); i++)
                {
                    listSubjectDetails.add(parseLessonPlanSubjectDetails(arrayLesson.toString()).get(i));
                }


                if (pageNumber != 0 || isRefreshing)
                {
                    listViewLessonPlanSubjectDetails.onRefreshComplete();
                    loading = false;
                }


                adapter.notifyDataSetChanged();

                Log.e("S_SIZE", "is: " + listSubjectDetails.size());


            }

            else {

            }



        };
    };

    private ArrayList<LessonPlanSubjectDetails> parseLessonPlanSubjectDetails(String object) {
        ArrayList<LessonPlanSubjectDetails> data = new ArrayList<LessonPlanSubjectDetails>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<LessonPlanSubjectDetails>>() {}.getType());
        return data;
    }

    private void initializePageing() {
        pageNumber = 1;
        isRefreshing = false;
        loading = false;
        stopLoadingData = false;
    }

    private void setUpList() {

        initializePageing();
        listViewLessonPlanSubjectDetails.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        // Set a listener to be invoked when the list should be refreshed.
        listViewLessonPlanSubjectDetails.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(LessonPlanSubjectDetailsActivity.this,
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                PullToRefreshBase.Mode m = listViewLessonPlanSubjectDetails.getCurrentMode();
                if (m == PullToRefreshBase.Mode.PULL_FROM_START) {
                    stopLoadingData = false;
                    isRefreshing = true;
                    pageNumber = 1;
                    loading = true;
                    /*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
                    initApiCall();
                } else if (!stopLoadingData) {
                    pageNumber++;
                    loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
                    initApiCall();

                } else {
                    new NoDataTask().execute();
                }
            }
        });


        this.adapter = new LessonPlanSubjectDetailsAdapter();
        this.adapter.clearList();
        this.listViewLessonPlanSubjectDetails.setAdapter(adapter);
    }

    private class NoDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {

            adapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            listViewLessonPlanSubjectDetails.onRefreshComplete();

            super.onPostExecute(result);
        }
    }


    private class LessonPlanSubjectDetailsAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return listSubjectDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return listSubjectDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clearList()
        {
            listSubjectDetails.clear();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(LessonPlanSubjectDetailsActivity.this).inflate(R.layout.row_lessonplan_subject_details, parent, false);


                holder.layoutRoot = (LinearLayout)convertView.findViewById(R.id.layoutRoot);
                holder.txtDate = (TextView)convertView.findViewById(R.id.txtDate);
                holder.txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);


                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(position%2 != 0)
            {
                holder.layoutRoot.setBackgroundColor(getResources().getColor(R.color.bg_row_odd));
            }
            else
            {
                holder.layoutRoot.setBackgroundColor(Color.WHITE);
            }

            holder.txtDate.setText(AppUtility.getDateString(listSubjectDetails.get(position).getPublishDate(), AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));
            holder.txtTitle.setText(listSubjectDetails.get(position).getTitle());

            return convertView;
        }


        class ViewHolder {

            LinearLayout layoutRoot;
            TextView txtDate;
            TextView txtTitle;

        }
    }


}
