package com.classtune.app.schoolapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.SingleLessonPlan;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.LessonPlan;
import com.classtune.app.schoolapp.model.LessonPlanCategory;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogLessonPlanConfirmation;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by BLACK HAT on 24-Mar-15.
 */
public class LessonPlanView extends Fragment {


    private View view;
    private UIHelper uiHelper;
    private UserHelper userHelper;

    private PullToRefreshListView listViewLessonPlan;


    private LessonPlanViewAdapter adapter;


    private boolean hasNext = false;
    private int pageNumber = 1;
    private boolean isRefreshing = false;
    private boolean loading = false;
    private boolean stopLoadingData = false;

    private List<LessonPlan> listLessonPlan;


    private String selectedBatchId = null;
    private String selectedCategoryId = null;

    private ImageButton btnSelectBatch;
    private ImageButton btnSelectCategory;

    private List<Batch> listBatch;

    private TextView txtSelectBatch;
    private TextView txtSelectCategory;

    private List<LessonPlanCategory> listCategory;


    private LinearLayout layoutSelectBatchActionHolder;
    private LinearLayout layoutSelectCategoryActionHolder;

    private List<String> listSelectedId;


    private Button btnDeleteLesson;
    private Button btnAssignLesson;

    private TextView txtMessage;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(getActivity());

        listLessonPlan = new ArrayList<LessonPlan>();

        listLessonPlan.clear();

        listBatch = new ArrayList<Batch>();
        listCategory = new ArrayList<LessonPlanCategory>();
        listSelectedId = new ArrayList<String>();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initApiCall(null, null);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lessonplan_view, container, false);

        initView(view);
        return view;

    }


    private void initView(View view) {
        listViewLessonPlan = (PullToRefreshListView) view.findViewById(R.id.listViewLessonPlan);
        setUpList();

        txtSelectBatch = (TextView)view.findViewById(R.id.txtSelectBatch);
        txtSelectCategory = (TextView)view.findViewById(R.id.txtSelectCategory);


        btnSelectBatch = (ImageButton)view.findViewById(R.id.btnSelectBatch);
        btnSelectCategory = (ImageButton)view.findViewById(R.id.btnSelectCategory);

        btnSelectBatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                initApiCallBatch();
            }
        });

        btnSelectCategory = (ImageButton)view.findViewById(R.id.btnSelectCategory);

        btnSelectCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                initApiCallCategory();
            }
        });


        layoutSelectBatchActionHolder = (LinearLayout)view.findViewById(R.id.layoutSelectBatchActionHolder);

        layoutSelectBatchActionHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                initApiCallBatch();
            }
        });


        layoutSelectCategoryActionHolder = (LinearLayout)view.findViewById(R.id.layoutSelectCategoryActionHolder);

        layoutSelectCategoryActionHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initApiCallCategory();
            }
        });

        btnDeleteLesson = (Button)view.findViewById(R.id.btnDeleteLesson);

        btnDeleteLesson.setEnabled(false);

        btnDeleteLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listSelectedId.size() > 0)
                    showCustomDialogConfirmationDelete(getActivity().getString(R.string.java_lessonplanview_lesson_plan), getActivity().getString(R.string.java_lessonplanview_delete), getActivity().getString(R.string.java_lessonplanview_content_delete), R.drawable.lessonplan_icon_red, getActivity());


            }
        });

        btnAssignLesson = (Button)view.findViewById(R.id.btnAssignLesson);

        btnAssignLesson.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(listSelectedId.size() > 0)
                    showCustomDialogConfirmationAssign(getString(R.string.java_lessonplanview_lesson_plan), getActivity().getString(R.string.java_lessonplanview_assign), getActivity().getString(R.string.java_lessonplanview_content_assign), R.drawable.lessonplan_icon_red, getActivity());


            }
        });


        txtMessage = (TextView)view.findViewById(R.id.txtMessage);



        listViewLessonPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LessonPlan data = (LessonPlan)adapter.getItem(position-1);

                Log.e("LIST", "clicked" + position);

                Intent intent = new Intent(getActivity(), SingleLessonPlan.class);
                intent.putExtra(AppConstant.ID_SINGLE_LESSON_PLAN, data.getId());
                startActivityForResult(intent, 76);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 76)
        {
            initializePageing();
            initApiCall(selectedBatchId, selectedCategoryId);
        }



    }



    private void initAction() {

    }


    private void initializePageing() {
        pageNumber = 1;
        isRefreshing = false;
        loading = false;
        stopLoadingData = false;
    }

    private void setUpList() {

        initializePageing();
        listViewLessonPlan.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        // Set a listener to be invoked when the list should be refreshed.
        listViewLessonPlan.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                PullToRefreshBase.Mode m = listViewLessonPlan.getCurrentMode();
                if (m == PullToRefreshBase.Mode.PULL_FROM_START) {
                    stopLoadingData = false;
                    isRefreshing = true;
                    pageNumber = 1;
                    loading = true;
                    /*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
                    initApiCall(selectedBatchId, selectedCategoryId);
                } else if (!stopLoadingData) {
                    pageNumber++;
                    loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
                    initApiCall(selectedBatchId, selectedCategoryId);

                } else {
                    new NoDataTask().execute();
                }
            }
        });


        this.adapter = new LessonPlanViewAdapter();
        this.adapter.clearList();
        this.listViewLessonPlan.setAdapter(adapter);
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
            listViewLessonPlan.onRefreshComplete();

            super.onPostExecute(result);
        }
    }






    private void initApiCall(String batchId, String categoryId) {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("page_size", "10");
        params.put("page_number", String.valueOf(pageNumber));

        if(batchId != null)
            params.put("batch_id", batchId);

        if(categoryId != null)
            params.put("lessonplan_category_id", categoryId);


        AppRestClient.post(URLHelper.URL_LESSONPLAN, params, lessonplanHandler);
    }

    AsyncHttpResponseHandler lessonplanHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

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

                for (int i = 0; i < parseLessonPlan(arrayLesson.toString()).size(); i++)
                {
                    listLessonPlan.add(parseLessonPlan(arrayLesson.toString()).get(i));
                }


                if (pageNumber != 0 || isRefreshing)
                {
                    listViewLessonPlan.onRefreshComplete();
                    loading = false;
                }


                adapter.notifyDataSetChanged();

                Log.e("S_SIZE", "is: " + listLessonPlan.size());

                if(listLessonPlan.size() <= 0)
                {
                    //Toast.makeText(getActivity(), "No data found!", Toast.LENGTH_SHORT).show();

                    txtMessage.setVisibility(View.VISIBLE);
                }

                else
                {
                    txtMessage.setVisibility(View.GONE);
                }

                initAction();



            } else {

            }


        }

        ;
    };



    private void initApiCallBatch()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, batchHandler);
    }


    AsyncHttpResponseHandler batchHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            listBatch.clear();

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arrayBatch = modelContainer.getData().get("batches").getAsJsonArray();
                for (int i = 0; i < parseBatch(arrayBatch.toString()).size(); i++)
                {
                    listBatch.add(parseBatch(arrayBatch.toString()).get(i));
                }

                showBatchPopup(btnSelectBatch);


            } else {

            }


        }

        ;
    };


    private void initApiCallCategory()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        AppRestClient.post(URLHelper.URL_LESSON_CATEGORY, params, lessonCategoryHandler);
    }


    AsyncHttpResponseHandler lessonCategoryHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {

            listCategory.clear();

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arrayCategory = modelContainer.getData().get("category").getAsJsonArray();
                for (int i = 0; i < parseCategory(arrayCategory.toString()).size(); i++)
                {
                    listCategory.add(parseCategory(arrayCategory.toString()).get(i));
                }

                showCategoryPopup(btnSelectCategory);


            } else {

            }


        }

        ;
    };



    private void showBatchPopup(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //popup.getMenuInflater().inflate(R.menu.popup_menu_medium, popup.getMenu());

        popup.getMenu().add("All");

        for(int i=0;i<listBatch.size();i++)
            popup.getMenu().add(listBatch.get(i).getName());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(SchoolSearchFragment.this.getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                for(Batch b : listBatch)
                {
                    if(item.getTitle().toString().equalsIgnoreCase(b.getName()))
                        selectedBatchId = b.getId();
                }

                Log.e("ITEM_ID", "id: " + selectedBatchId);

                txtSelectBatch.setText(item.getTitle());


                if(item.getTitle().toString().equalsIgnoreCase("All"))
                {
                    selectedBatchId = null;
                    initApiCall(selectedBatchId, selectedCategoryId);
                }
                else
                    initApiCall(selectedBatchId, selectedCategoryId);

                return true;
            }
        });

        popup.show();
    }


    private void showCategoryPopup(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //popup.getMenuInflater().inflate(R.menu.popup_menu_medium, popup.getMenu());

        popup.getMenu().add("All");

        for(int i=0;i<listCategory.size();i++)
            popup.getMenu().add(listCategory.get(i).getName());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(SchoolSearchFragment.this.getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                for(LessonPlanCategory b : listCategory)
                {
                    if(item.getTitle().toString().equalsIgnoreCase(b.getName()))
                        selectedCategoryId = b.getId();
                }

                Log.e("ITEM_ID", "id: " + selectedCategoryId);

                txtSelectCategory.setText(item.getTitle());


                if(item.getTitle().toString().equalsIgnoreCase("All"))
                {
                    selectedCategoryId = null;
                    initApiCall(selectedBatchId, selectedCategoryId);
                }
                else
                    initApiCall(selectedBatchId, selectedCategoryId);

                return true;
            }
        });

        popup.show();
    }

    private void showCustomDialogConfirmationDelete(String titleText, String actionButtonText, String description,
                                              int iconResId, Context context) {

        PopupDialogLessonPlanConfirmation picker = PopupDialogLessonPlanConfirmation.newInstance(0);


        picker.setData(titleText, actionButtonText, description, iconResId, context, new PopupDialogLessonPlanConfirmation.ActionCallback() {
            @Override
            public void onActionCalled() {

                initApiCallDeleteLessonPlan();
            }

            @Override
            public void onCancelCalled() {

            }
        });


        picker.show(getChildFragmentManager(), null);
    }


    private void showCustomDialogConfirmationAssign(String titleText, String actionButtonText, String description,
                                                    int iconResId, Context context) {

        PopupDialogLessonPlanConfirmation picker = PopupDialogLessonPlanConfirmation.newInstance(0);


        picker.setData(titleText, actionButtonText, description, iconResId, context, new PopupDialogLessonPlanConfirmation.ActionCallback() {
            @Override
            public void onActionCalled() {

                initApiCallAssignLessonPlan();
            }

            @Override
            public void onCancelCalled() {

            }
        });


        picker.show(getChildFragmentManager(), null);
    }


    private void initApiCallDeleteLessonPlan()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", getIdWithComma());

        AppRestClient.post(URLHelper.URL_LESSON_DELETE, params, lessonDeleteHandler);
    }

    AsyncHttpResponseHandler lessonDeleteHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {


            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                Toast.makeText(getActivity(), R.string.java_lessonplanview_successfully_deleted, Toast.LENGTH_SHORT).show();
                initApiCall(selectedBatchId, selectedCategoryId);

                listSelectedId.clear();
                btnDeleteLesson.setEnabled(false);
                btnDeleteLesson.setBackgroundResource(R.drawable.delete_lesson_deactive);


            } else {

            }


        }

        ;
    };


    private void initApiCallAssignLessonPlan()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", getIdWithComma());
        params.put("is_show", "1");

        AppRestClient.post(URLHelper.URL_LESSON_ASSIGN, params, lessonAssignHandler);
    }

    AsyncHttpResponseHandler lessonAssignHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        }

        ;

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        }

        ;

        @Override
        public void onSuccess(int arg0, String responseString) {



            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);



            if (modelContainer.getStatus().getCode() == 200) {


                Toast.makeText(getActivity(), R.string.java_lessonplanview_successfully_assigned, Toast.LENGTH_SHORT).show();
                initApiCall(selectedBatchId, selectedCategoryId);

                listSelectedId.clear();
                btnAssignLesson.setEnabled(false);
                btnAssignLesson.setBackgroundResource(R.drawable.assign_class_deactive);
                btnAssignLesson.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_assign_text));


            } else {

            }


        }

        ;
    };






    private ArrayList<LessonPlan> parseLessonPlan(String object) {
        ArrayList<LessonPlan> data = new ArrayList<LessonPlan>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<LessonPlan>>() {}.getType());
        return data;
    }

    private ArrayList<Batch> parseBatch(String object) {
        ArrayList<Batch> data = new ArrayList<Batch>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<Batch>>() {}.getType());
        return data;
    }


    private ArrayList<LessonPlanCategory> parseCategory(String object) {
        ArrayList<LessonPlanCategory> data = new ArrayList<LessonPlanCategory>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<LessonPlanCategory>>() {}.getType());
        return data;
    }



    private class LessonPlanViewAdapter extends BaseAdapter {



        @Override
        public int getCount() {
            return listLessonPlan.size();
        }

        @Override
        public Object getItem(int position) {
            return listLessonPlan.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clearList()
        {
            listLessonPlan.clear();
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(LessonPlanView.this.getActivity()).inflate(R.layout.row_lessonplan_view, parent, false);

                //holder.layoutSelect = (LinearLayout)convertView.findViewById(R.id.layoutSelect);

                holder.layoutRoot = (LinearLayout)convertView.findViewById(R.id.layoutRoot);
                holder.btnCheckSelect = (CheckBox)convertView.findViewById(R.id.btnCheckSelect);
                holder.txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
                holder.txtCategory = (TextView)convertView.findViewById(R.id.txtCategory);
                holder.txtSubject = (TextView)convertView.findViewById(R.id.txtSubject);
                holder.txtStatus = (TextView)convertView.findViewById(R.id.txtStatus);
                holder.txtStar = (TextView)convertView.findViewById(R.id.txtStar);


                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //holder.layoutSelect.setTag(holder.imgViewSelect);

            //holder.layoutSelect.setTag(position);

            if(position%2 != 0)
            {
                holder.layoutRoot.setBackgroundColor(getResources().getColor(R.color.bg_row_odd));
            }
            else
            {
                holder.layoutRoot.setBackgroundColor(Color.WHITE);
            }


            holder.txtTitle.setText(listLessonPlan.get(position).getTitle());
            holder.txtCategory.setText(listLessonPlan.get(position).getCategory());
            holder.txtSubject.setText(listLessonPlan.get(position).getSubjects());

            if(listLessonPlan.get(position).getIs_show().equalsIgnoreCase("0"))
            {
               holder.txtStatus.setText(R.string.java_lessonplanview_inactive);
            }
            else
            {
                holder.txtStatus.setText(R.string.java_lessonplanview_active);
            }


            holder.btnCheckSelect.setTag(position);


            //holder.layoutSelect.setSelected(false);

            /*holder.layoutSelect.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    LinearLayout lay = (LinearLayout)v;


                    int position = (Integer)v.getTag();

                    *//*if(lay.isSelected() == false)
                    {
                        lay.setSelected(true);
                        imgViewSelect.setImageResource(R.drawable.select_lesson_tap);

                        listSelectedId.add(data.getId());
                    }
                    else
                    {
                        lay.setSelected(false);
                        imgViewSelect.setImageResource(R.drawable.select_lesson_normal);

                        listSelectedId.remove(data.getId());
                    }*//*


                    *//*for (int i=0; i<listSelectedId.size();i++) {
                        Log.e("ON_CLICK", "called id: " + listSelectedId.get(i));

                    }*//*

                    *//*if(listSelectedId.size() > 0)
                    {
                        btnDeleteLesson.setEnabled(true);
                        btnDeleteLesson.setImageResource(R.drawable.btn_delete_lesson);
                    }
                    else
                    {
                        btnDeleteLesson.setEnabled(false);
                        btnDeleteLesson.setImageResource(R.drawable.delete_lesson_deactive);
                    }*//*

                    Log.e("ON_CLICK", "============="+position);








                }
            });*/


            holder.btnCheckSelect.setChecked(listLessonPlan.get(position).isChecked());


            holder.btnCheckSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    int position = Integer.parseInt(buttonView.getTag().toString());

                    if(isChecked)
                    {
                        listLessonPlan.get(position).setChecked(true);
                        listSelectedId.add(listLessonPlan.get(position).getId());
                        refreshData(listSelectedId);
                    }

                    else
                    {
                        listLessonPlan.get(position).setChecked(false);
                        listSelectedId.remove(listLessonPlan.get(position).getId());
                    }

                    for (int i=0; i<listSelectedId.size();i++) {
                        Log.e("ON_CLICK", "called id: " + listSelectedId.get(i));

                    }
                    Log.e("ON_CLICK", "=============" + position);

                    if(listSelectedId.size() > 0)
                    {
                        btnDeleteLesson.setEnabled(true);
                        //btnDeleteLesson.setImageResource(R.drawable.btn_delete_lesson);
                        btnDeleteLesson.setBackgroundResource(R.drawable.btn_delete_lesson);



                        btnAssignLesson.setEnabled(true);
                        btnAssignLesson.setBackgroundResource(R.drawable.btn_assign_class);
                        btnAssignLesson.setTextColor(Color.BLACK);
                    }
                    else
                    {
                        btnDeleteLesson.setEnabled(false);
                        btnDeleteLesson.setBackgroundResource(R.drawable.delete_lesson_deactive);

                        btnAssignLesson.setEnabled(false);
                        btnAssignLesson.setBackgroundResource(R.drawable.assign_class_deactive);
                        btnAssignLesson.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_assign_text));
                    }


                }
            });

            //holder.txtStar
            if(!TextUtils.isEmpty(listLessonPlan.get(position).getAttachmentFileName())){
                holder.txtStar.setVisibility(View.VISIBLE);
            }else{
                holder.txtStar.setVisibility(View.GONE);
            }


            return convertView;
        }

        class ViewHolder {

            //LinearLayout layoutSelect;
            LinearLayout layoutRoot;

            CheckBox btnCheckSelect;
            TextView txtTitle;
            TextView txtCategory;
            TextView txtSubject;
            TextView txtStatus;
            TextView txtStar;


        }


        private void refreshData(List<String> list)
        {
            HashSet hs = new HashSet();
            hs.addAll(list);
            list.clear();
            list.addAll(hs);
        }




    }



    private String getIdWithComma()
    {
        StringBuilder result = new StringBuilder();
        for ( String p : listSelectedId )
        {
            if (result.length() > 0) result.append( "," );
            result.append( p );
        }

        return  result.toString();
    }





    private class Tags
    {
        ImageView imgView;
        LessonPlan lessonData;

        public Tags(ImageView imgView, LessonPlan lessonData)
        {
            this.imgView = imgView;
            this.lessonData = lessonData;
        }

        public LessonPlan getLessonData() {
            return lessonData;
        }

        public void setLessonData(LessonPlan lessonData) {
            this.lessonData = lessonData;
        }

        public ImageView getImgView() {
            return imgView;
        }

        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }





    }




}
