package com.classtune.app.freeversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleSubjectAttendance extends ChildContainerActivity {

    private String selectedSubjectId = "";
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private TextView txtSubjectName;
    private TextView txtTotalStudent;
    private TextView txtPresent;
    private TextView txtAbsent;
    private TextView txtLate;
    private TextView txtMessage;

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
        setContentView(R.layout.activity_single_subject_attendance);
        if(getIntent().getExtras() != null){
            selectedSubjectId = getIntent().getExtras().getString(AppConstant.ID_SINGLE_SUBJECT_ATTENDANCE);
        }

        uiHelper = new UIHelper(this);
        userHelper = new UserHelper(this);


        initView();
        initApiCallReport();
    }

    private void initView(){

        txtSubjectName = (TextView)this.findViewById(R.id.txtSubjectName);

        txtTotalStudent = (TextView)this.findViewById(R.id.txtTotalStudent);
        txtPresent = (TextView)this.findViewById(R.id.txtPresent);
        txtAbsent = (TextView)this.findViewById(R.id.txtAbsent);
        txtLate = (TextView)this.findViewById(R.id.txtLate);


        txtMessage = (TextView)this.findViewById(R.id.txtMessage);
        txtMessage.setVisibility(View.GONE);

    }


    private void initApiCallReport() {
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("subject_id", selectedSubjectId);
        //params.put("date", selectedDate);
        if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS) {
            params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
            params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
        }

       // AppRestClient.post(URLHelper.URL_STD_PARENT_SUBJECT_REPORT, params, reportHandler);

        parentSubjectReport(params);
    }

    private void parentSubjectReport(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().stdParentSubjectReport(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        if (response.body() != null){
                            Log.e("Response", ""+response.body());

                            Wrapper wrapper= GsonParser.getInstance().parseServerResponse2(response.body());
                            if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {


                                JsonObject jsonObject = wrapper.getData().get("report").getAsJsonObject();
                                int total = jsonObject.get("total").getAsInt();
                                int present = jsonObject.get("present").getAsInt();
                                int absent = jsonObject.get("absent").getAsInt();
                                int late = jsonObject.get("late").getAsInt();
                                String subjectName = jsonObject.get("subject_name").getAsString();

                                initActionAfterReportCall(total, present, absent, late, subjectName);

                                txtMessage.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(SingleSubjectAttendance.this, wrapper.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                                txtMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        uiHelper.showMessage(getString(R.string.internet_error_text));
                        uiHelper.dismissLoadingDialog();
                    }
                }
        );
    }
    AsyncHttpResponseHandler reportHandler = new AsyncHttpResponseHandler() {

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


                JsonObject jsonObject = wrapper.getData().get("report").getAsJsonObject();
                int total = jsonObject.get("total").getAsInt();
                int present = jsonObject.get("present").getAsInt();
                int absent = jsonObject.get("absent").getAsInt();
                int late = jsonObject.get("late").getAsInt();
                String subjectName = jsonObject.get("subject_name").getAsString();

                initActionAfterReportCall(total, present, absent, late, subjectName);

                txtMessage.setVisibility(View.GONE);

            } else {
                Toast.makeText(SingleSubjectAttendance.this, wrapper.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                txtMessage.setVisibility(View.VISIBLE);
            }

        }

    };

    private void initActionAfterReportCall(int total, int present, int absent, int late, String subjectName){

        if(total <= 0){
            txtTotalStudent.setText(R.string.not_applicable);
            txtPresent.setText(R.string.not_applicable);
            txtAbsent.setText(R.string.not_applicable);
            txtLate.setText(R.string.not_applicable);
        }else{
            txtTotalStudent.setText(String.valueOf(total));
            txtPresent.setText(String.valueOf(present));
            txtAbsent.setText(String.valueOf(absent));
            txtLate.setText(String.valueOf(late));
        }

        txtSubjectName.setText(subjectName);


    }
}
