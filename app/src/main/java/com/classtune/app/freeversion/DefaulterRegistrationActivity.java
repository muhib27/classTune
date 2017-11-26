package com.classtune.app.freeversion;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.DefaulterRegistrationAdapter;
import com.classtune.app.schoolapp.adapters.TeacherTakeSubjectAttendanceAdapter;
import com.classtune.app.schoolapp.fragments.DefaulterListFragment;
import com.classtune.app.schoolapp.fragments.DefaulterRegistrationFragment;
import com.classtune.app.schoolapp.model.DefaulterModel;
import com.classtune.app.schoolapp.model.StudentAssociated;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaulterRegistrationActivity extends ChildContainerActivity{
    private List<DefaulterModel> defaulterModelList;
    private ListView listView;
    DefaulterRegistrationAdapter defaulterRegistrationAdapter;
     private String id = "";
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private String registerId = "";
    public static CheckBox checkBoxRegistered;
    DefaulterModel defaulterModel;
    private Button btnSubmit;
    private int defaulter_registration;
    Bundle bundle;
    private int list_pos;
    public ImageView homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaulter_registration);
        homeBtn = (ImageView)findViewById(R.id.back_btn_home);
        homeBtn.setVisibility(View.VISIBLE);
        if(getIntent().getExtras() != null){
            id = getIntent().getExtras().getString(AppConstant.KEY_HOMEWORK_ID);
            defaulter_registration = getIntent().getExtras().getInt(AppConstant.KEY_REGISTERED);
            list_pos = getIntent().getExtras().getInt("list_pos");

        }
        bundle=new Bundle();
        bundle.putString("id", id);
        bundle.putInt("list_pos", list_pos);
        if(defaulter_registration==1)
            gotoDefaulterList();
        else
            gotoDefaulterRegistration();

        //initView();




       // Log.e("SUBJECT_ID", "is: "+id);
        //uiHelper = new UIHelper(this);
        //userHelper = new UserHelper(this);
       // initApiCallStudent();



//        defaulterModel = new DefaulterModel("1", "sss", "s", "1");
//        studentList.add(defaulterModel);
//        defaulterModel = new DefaulterModel("1", "eee", "s", "1");
//        studentList.add(defaulterModel);
//        defaulterRegistrationAdapter = new DefaulterRegistrationAdapter(this, studentList);
//        listView.setAdapter(defaulterRegistrationAdapter);

//        checkBoxRegistered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(!isChecked)
//                    listView.removeAllViews();
//            }
//        });


    }




    public void gotoDefaulterRegistration(){
        DefaulterRegistrationFragment fragment = new DefaulterRegistrationFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }
    public void gotoDefaulterList(){
        DefaulterListFragment fragment = new DefaulterListFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.listView);
        //listView.setOnItemClickListener(this);
        btnSubmit = (Button)this.findViewById(R.id.btnSubmit);
        checkBoxRegistered = (CheckBox)findViewById(R.id.checkBoxRegistered);
        defaulterModelList = new ArrayList<DefaulterModel>();
    }

    private void initApiCallStudent() {
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", id);

        //AppRestClient.post(URLHelper.URL_TEACHER_ASSOCIATED_GET_STUDENT, params, subjectHandler);
        getDefaulterStudentList(params);
    }

    private void getDefaulterStudentList(HashMap<String, String> params) {
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().getDefaulterStudentList(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        if (response.body() != null) {
                            Log.e("Response", "" + response.body());

                            Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(response.body());
                            if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                                registerId = wrapper.getData().get("assignment_register").getAsString();

                                JsonArray arrayStudent = wrapper.getData().get("d_list").getAsJsonArray();
                                defaulterModelList.addAll(parseStudent(arrayStudent.toString()));
                                defaulterRegistrationAdapter = new DefaulterRegistrationAdapter(DefaulterRegistrationActivity.this, defaulterModelList);
                                defaulterRegistrationAdapter.notifyDataSetChanged();
                                listView.setAdapter(defaulterRegistrationAdapter);
                                initAction();

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

    public List<DefaulterModel> parseStudent(String object) {

        List<DefaulterModel> dList = new ArrayList<>();
        Type listType = new TypeToken<List<DefaulterModel>>() {}.getType();
        dList = (List<DefaulterModel>) new Gson().fromJson(object, listType);
        return dList;
    }



    private void initAction(){

        if(!registerId.equals("0")){
            checkBoxRegistered.setChecked(true);
            checkBoxRegistered.setText(R.string.defaulter_registration);
            checkBoxRegistered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    compoundButton.setChecked(true);
                }
            });

            defaulterRegistrationAdapter.setClickable(true);
            defaulterRegistrationAdapter.setUpdate(true);


        }else{

            checkBoxRegistered.setChecked(false);
            checkBoxRegistered.setText(getString(R.string.defaulter_registration));
            checkBoxRegistered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(defaulterModelList.size() > 0){
                        registerId = "1";
                        defaulterRegistrationAdapter.setAllPresent(true);
                        defaulterRegistrationAdapter.setClickable(true);
                        defaulterRegistrationAdapter.notifyDataSetChanged();

                        checkBoxRegistered.setChecked(true);
                        checkBoxRegistered.setText(getString(R.string.defaulter_registration));
                        checkBoxRegistered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                compoundButton.setChecked(true);
                            }
                        });


                    }else{
                        checkBoxRegistered.setChecked(false);
                        Toast.makeText(DefaulterRegistrationActivity.this, R.string.no_student, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            defaulterRegistrationAdapter.setClickable(false);
        }


        if(defaulterRegistrationAdapter.isUpdate()){
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
                    Toast.makeText(DefaulterRegistrationActivity.this, R.string.message_register_room, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void initApiCallSubmitAttandance(){
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", id);
        params.put("student_id", appendListWithComma(defaulterRegistrationAdapter.getListStudentDataId()));
        //params.put("late", appendListWithComma(adapter.getListStudentStatusNew()));


        //AppRestClient.post(URLHelper.URL_TEACHER_SUBJECT_ATTENDANCE_ADD, params, submitHandler);

        teacherDefaulterAddApiCall(params);

    }

    private void teacherDefaulterAddApiCall(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().teacherDefaulterAdd(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        if(response.body() != null){
                            Log.e("Response", ""+response.body());

                            Wrapper wrapper= GsonParser.getInstance().parseServerResponse2(response.body());
                            if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {

                                if(defaulterRegistrationAdapter.isUpdate()){
                                    Toast.makeText(DefaulterRegistrationActivity.this, R.string.defaulter_updated_successfully, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(DefaulterRegistrationActivity.this, R.string.defaulter_saved_successfully, Toast.LENGTH_SHORT).show();
                                }


                                finish();

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

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        CheckBox checkbox = (CheckBox) view.getTag(R.id.defaulterSet);
//    }
}
