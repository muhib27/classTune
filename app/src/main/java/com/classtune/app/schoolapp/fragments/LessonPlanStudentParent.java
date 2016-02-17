package com.classtune.app.schoolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.freeversion.LessonPlanSubjectDetailsActivity;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.LessonPlanStudentParentSubject;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLACK HAT on 08-Apr-15.
 */
public class LessonPlanStudentParent extends Fragment {

    private View view;
    private UIHelper uiHelper;
    private UserHelper userHelper;


    private ListView listViewLessonPlanStudentParent;

    private List<LessonPlanStudentParentSubject> listSubject;

    private LessonPlanSubjectAdapter adapter;

    private TextView txtMessage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listSubject = new ArrayList<LessonPlanStudentParentSubject>();

        userHelper = new UserHelper(getActivity());
        uiHelper = new UIHelper(getActivity());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initApicall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lessonplan_student_parent, container, false);

        initView(view);

        return view;
    }


    private void initView(View view)
    {
        listViewLessonPlanStudentParent = (ListView)view.findViewById(R.id.listViewLessonPlanStudentParent);
        adapter = new LessonPlanSubjectAdapter();
        listViewLessonPlanStudentParent.setAdapter(adapter);

        /*listViewLessonPlanStudentParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LessonPlanStudentParentSubject data = (LessonPlanStudentParentSubject)adapter.getItem(position);

                Intent intent = new Intent(getActivity(), LessonPlanSubjectDetailsActivity.class);
                Gson gson = new Gson();
                String strData = gson.toJson(data, LessonPlanStudentParentSubject.class);
                intent.putExtra(AppConstant.DATA_LESSON_PLAN_SUBJECT, strData);
                startActivity(intent);

            }
        });*/

        txtMessage = (TextView)view.findViewById(R.id.txtMessage);

    }

    private void initApicall()
    {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
        {
            params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
            params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());

            Log.e("STU_ID", userHelper.getUser().getSelectedChild().getId());
            Log.e("STU_PROFILE_ID", userHelper.getUser().getSelectedChild().getProfileId());
            Log.e("STU_BATCH_ID", userHelper.getUser().getSelectedChild().getBatchId());
        }


        AppRestClient.post(URLHelper.URL_GET_LESSONPLAN_SUBJECT_STUDENT_PARENT, params, lessonPlanStudentParentHandler);
    }

    AsyncHttpResponseHandler lessonPlanStudentParentHandler = new AsyncHttpResponseHandler() {

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

            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arraySubject = modelContainer.getData().get("subject").getAsJsonArray();

                for (int i = 0; i < arraySubject.size(); i++)
                {
                    listSubject.add(parseLessonPlanSubject(arraySubject.toString()).get(i));
                }

                adapter.notifyDataSetChanged();


                if(listSubject.size() <= 0)
                {
                    txtMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtMessage.setVisibility(View.GONE);
                }

            }

            else {

            }



        };
    };

    private ArrayList<LessonPlanStudentParentSubject> parseLessonPlanSubject(String object) {
        ArrayList<LessonPlanStudentParentSubject> data = new ArrayList<LessonPlanStudentParentSubject>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<LessonPlanStudentParentSubject>>() {}.getType());
        return data;
    }


    private class LessonPlanSubjectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listSubject.size();
        }

        @Override
        public Object getItem(int position) {
            return listSubject.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(LessonPlanStudentParent.this.getActivity()).inflate(R.layout.row_lessonplan_studentparent_subject, parent, false);

                holder.layoutRoot = (LinearLayout)convertView.findViewById(R.id.layoutRoot);
                holder.imgViewSubjectIcon = (ImageView)convertView.findViewById(R.id.imgViewSubjectIcon);
                holder.txtSubjectName = (TextView)convertView.findViewById(R.id.txtSubjectName);
                holder.txtPublishDate = (TextView)convertView.findViewById(R.id.txtPublishDate);
                holder.btnView = (ImageButton)convertView.findViewById(R.id.btnView);


                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btnView.setTag(position);
            holder.layoutRoot.setTag(position);

            holder.imgViewSubjectIcon.setImageResource(AppUtility.getImageResourceId(listSubject.get(position).getIcon(), getActivity()));
            holder.txtSubjectName.setText(listSubject.get(position).getName() + " (" + listSubject.get(position).getTotal() + ")");
            holder.txtPublishDate.setText("Last Updated "+AppUtility.getDateString(listSubject.get(position).getLastUpdated(), AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));

            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageButton btn = (ImageButton) v;
                    int position = Integer.parseInt(btn.getTag().toString());

                    LessonPlanStudentParentSubject data = listSubject.get(position);

                    Intent intent = new Intent(getActivity(), LessonPlanSubjectDetailsActivity.class);
                    Gson gson = new Gson();
                    String strData = gson.toJson(data, LessonPlanStudentParentSubject.class);
                    intent.putExtra(AppConstant.DATA_LESSON_PLAN_SUBJECT, strData);
                    startActivity(intent);

                }
            });

            holder.layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LinearLayout btn = (LinearLayout) view;
                    int position = Integer.parseInt(btn.getTag().toString());

                    LessonPlanStudentParentSubject data = listSubject.get(position);

                    Intent intent = new Intent(getActivity(), LessonPlanSubjectDetailsActivity.class);
                    Gson gson = new Gson();
                    String strData = gson.toJson(data, LessonPlanStudentParentSubject.class);
                    intent.putExtra(AppConstant.DATA_LESSON_PLAN_SUBJECT, strData);
                    startActivity(intent);
                }
            });


            return convertView;
        }


        class ViewHolder {

            LinearLayout layoutRoot;
            ImageView imgViewSubjectIcon;
            TextView txtSubjectName;
            TextView txtPublishDate;
            ImageButton btnView;


        }


    }
}
