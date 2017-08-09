package com.classtune.app.freeversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.TeacherTakeSubjectAttendanceAdapter;
import com.classtune.app.schoolapp.model.StudentAssociated;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BLACK HAT on 16-Feb-17.
 */

public class TeacherSubjectAttendanceTakeActivity extends ChildContainerActivity{

    private String subjectId = "";
    private ListView listView;
    private TeacherTakeSubjectAttendanceAdapter adapter;
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private List<StudentAssociated> studentAssociateds;
    private String registerId = "";
    private CheckBox checkBoxRegistered;
    private Button btnSubmit;
    private TextView txtDate;


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_subject_attendance);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if(getIntent().getExtras() != null){
            subjectId = getIntent().getExtras().getString(AppConstant.KEY_ASSOCIATED_SUBJECT_ID);
        }

        Log.e("SUBJECT_ID", "is: "+subjectId);

        uiHelper = new UIHelper(this);
        userHelper = new UserHelper(this);

        studentAssociateds = new ArrayList<>();

        initView();
        initApiCallStudent();

    }

    private void initView(){
        listView = (ListView)this.findViewById(R.id.listView);

        checkBoxRegistered = (CheckBox)this.findViewById(R.id.checkBoxRegistered);
        //checkBoxRegistered.setButtonDrawable(R.drawable.check_btn);
        btnSubmit = (Button)this.findViewById(R.id.btnSubmit);
        txtDate = (TextView)this.findViewById(R.id.txtDate);

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        txtDate.setText(AppUtility.getDateString(fDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));

    }

    private String appendListWithComma(List<String> sellItems){
        StringBuilder sb = new StringBuilder();
        for(String item: sellItems){
            if(sb.length() > 0){
                sb.append(',');
            }
            sb.append(item);
        }
        String result = sb.toString();

        return result;
    }

    private void initApiCallStudent() {
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("subject_id", subjectId);

        //AppRestClient.post(URLHelper.URL_TEACHER_ASSOCIATED_GET_STUDENT, params, subjectHandler);
        teacherAssociatedGetStudent(params);
    }

    private void teacherAssociatedGetStudent(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().teacherAssociatedGetStudent(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        Log.e("Response", ""+response.body());

                        Wrapper wrapper= GsonParser.getInstance().parseServerResponse2(response.body());
                        if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {

                            registerId = wrapper.getData().get("register").getAsString();

                            JsonArray arrayStudent = wrapper.getData().get("students").getAsJsonArray();
                            studentAssociateds.addAll(parseStudent(arrayStudent.toString()));
                            adapter = new TeacherTakeSubjectAttendanceAdapter(TeacherSubjectAttendanceTakeActivity.this, studentAssociateds);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            initAction();

                        } else {

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
    AsyncHttpResponseHandler subjectHandler = new AsyncHttpResponseHandler() {

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

                registerId = wrapper.getData().get("register").getAsString();

                JsonArray arrayStudent = wrapper.getData().get("students").getAsJsonArray();
                studentAssociateds.addAll(parseStudent(arrayStudent.toString()));
                adapter = new TeacherTakeSubjectAttendanceAdapter(TeacherSubjectAttendanceTakeActivity.this, studentAssociateds);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                initAction();

            } else {

            }
        }

    };

    public List<StudentAssociated> parseStudent(String object) {

        List<StudentAssociated> tags = new ArrayList<>();
        Type listType = new TypeToken<List<StudentAssociated>>() {}.getType();
        tags = (List<StudentAssociated>) new Gson().fromJson(object, listType);
        return tags;
    }

    private void initAction(){

        if(!registerId.equals("0")){
            checkBoxRegistered.setChecked(true);
            checkBoxRegistered.setText(R.string.registered);
            checkBoxRegistered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    compoundButton.setChecked(true);
                }
            });

            adapter.setClickable(true);
            adapter.setUpdate(true);


        }else{

            checkBoxRegistered.setChecked(false);
            checkBoxRegistered.setText(getString(R.string.not_registered));
            checkBoxRegistered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(studentAssociateds.size() > 0){
                        registerId = "1";
                        adapter.setAllPresent(true);
                        adapter.setClickable(true);
                        adapter.notifyDataSetChanged();

                        checkBoxRegistered.setChecked(true);
                        checkBoxRegistered.setText(getString(R.string.registered));
                        checkBoxRegistered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                compoundButton.setChecked(true);
                            }
                        });


                    }else{
                        checkBoxRegistered.setChecked(false);
                        Toast.makeText(TeacherSubjectAttendanceTakeActivity.this, R.string.no_student, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            adapter.setClickable(false);
        }


        if(adapter.isUpdate()){
            btnSubmit.setText(getString(R.string.attendance_subject_btn_update));
        }else{
            btnSubmit.setText(getString(R.string.attendance_subject_btn_submit));
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!registerId.equals("0")){
                    initApiCallSubmitAttandance();
                }else{
                    Toast.makeText(TeacherSubjectAttendanceTakeActivity.this, R.string.message_register_room, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initApiCallSubmitAttandance(){
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("subject_id", subjectId);
        params.put("student_id", appendListWithComma(adapter.getListStudentDataId()));
        params.put("late", appendListWithComma(adapter.getListStudentStatusNew()));


        //AppRestClient.post(URLHelper.URL_TEACHER_SUBJECT_ATTENDANCE_ADD, params, submitHandler);

        teacherSubjectAttendanceAdd(params);

    }

    private void teacherSubjectAttendanceAdd(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().teacherSubjectAttendanceAdd(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        Log.e("Response", ""+response.body());

                        Wrapper wrapper= GsonParser.getInstance().parseServerResponse2(response.body());
                        if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {

                            if(adapter.isUpdate()){
                                Toast.makeText(TeacherSubjectAttendanceTakeActivity.this, R.string.attendance_updated_successfully, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(TeacherSubjectAttendanceTakeActivity.this, R.string.attendance_saved_successfully, Toast.LENGTH_SHORT).show();
                            }


                            finish();

                        } else {

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
    AsyncHttpResponseHandler submitHandler = new AsyncHttpResponseHandler() {

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

                if(adapter.isUpdate()){
                    Toast.makeText(TeacherSubjectAttendanceTakeActivity.this, R.string.attendance_updated_successfully, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TeacherSubjectAttendanceTakeActivity.this, R.string.attendance_saved_successfully, Toast.LENGTH_SHORT).show();
                }


                finish();

            } else {

            }
        }

    };
}
