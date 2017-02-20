package com.classtune.app.freeversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.TeacherSubjectAttendanceFullReportAdapter;
import com.classtune.app.schoolapp.model.StdAtt;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLACK HAT on 19-Feb-17.
 */

public class TeacherSubjectAttendanceFullReportActivity extends ChildContainerActivity{

    private String subjectId = "";
    private ListView listView;
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private TeacherSubjectAttendanceFullReportAdapter adapter;
    private List<StdAtt> studentList;

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
        setContentView(R.layout.activity_teacher_subject_attendance_full_report);

        if(getIntent().getExtras() != null){
            subjectId = getIntent().getExtras().getString(AppConstant.KEY_ASSOCIATED_SUBJECT_ID_REPORT);
        }

        uiHelper = new UIHelper(this);
        userHelper = new UserHelper(this);

        studentList = new ArrayList<>();

        initView();
        initApiCallStudent();
    }

    private void initView(){
        listView = (ListView)this.findViewById(R.id.listView);
    }

    private void initApiCallStudent() {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("subject_id", subjectId);

        AppRestClient.post(URLHelper.URL_TEACHER_SUBJECT_REPORT_ALL, params,
                studentHandler);
    }

    AsyncHttpResponseHandler studentHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            uiHelper.dismissLoadingDialog();
        }

        @Override
        public void onStart() {
            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        }

        @Override
        public void onSuccess(int arg0, String responseString) {
            super.onSuccess(arg0, responseString);
            uiHelper.dismissLoadingDialog();
            Log.e("Response", responseString);

            Wrapper wrapper= GsonParser.getInstance().parseServerResponse(responseString);
            if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {


                JsonArray arrayStudent = wrapper.getData().get("std_att").getAsJsonArray();
                studentList.addAll(parseStudent(arrayStudent.toString()));
                adapter = new TeacherSubjectAttendanceFullReportAdapter(TeacherSubjectAttendanceFullReportActivity.this, studentList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            } else {

            }
        }

    };

    public List<StdAtt> parseStudent(String object) {

        List<StdAtt> tags = new ArrayList<>();
        Type listType = new TypeToken<List<StdAtt>>() {}.getType();
        tags = (List<StdAtt>) new Gson().fromJson(object, listType);
        return tags;
    }
}
