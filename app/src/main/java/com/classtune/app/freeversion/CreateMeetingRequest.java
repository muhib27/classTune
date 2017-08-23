package com.classtune.app.freeversion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.StudentParent;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.CustomDateTimePicker;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMeetingRequest extends ChildContainerActivity {


    private String batchId = "42";

    private UIHelper uiHelper;

    private TextView txtCurrentDate;

    private ImageButton btnSelectBatch;
    private TextView txtSelectBatch;
    private ImageButton btnSelectParent;
    private TextView txtSelectParent;
    private ImageButton btnSelectDateTime;
    private TextView txtSelectDateTime;
    private EditText txtDescription;

    private String selectedId = "";
    private String selectedBatchId = "";

    private List<StudentParent> listStudentParent = new ArrayList<StudentParent>();

    private Button btnGoBack;
    private Button btnSendMeetingRequest;

    private LinearLayout layoutSelectBatchSegmnent;
    private TextView txtHeaderParent;

    private LinearLayout layoutSelectBatchActionHolder;
    private LinearLayout layoutSelectParentActionHolder;
    private LinearLayout layoutSelectSelectDateTimeActionHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_meeting_request);

        uiHelper = new UIHelper(this);


        initView();
        initAction();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);

        if(AppUtility.isInternetConnected() == false){
            Toast.makeText(CreateMeetingRequest.this, R.string.internet_error_text, Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        this.txtCurrentDate = (TextView) this.findViewById(R.id.txtCurrentDate);

        this.btnSelectBatch = (ImageButton) this.findViewById(R.id.btnSelectBatch);
        this.txtSelectBatch = (TextView) this.findViewById(R.id.txtSelectBatch);

        this.btnSelectParent = (ImageButton) this.findViewById(R.id.btnSelectParent);
        this.txtSelectParent = (TextView) this.findViewById(R.id.txtSelectParent);

        this.btnSelectDateTime = (ImageButton) this.findViewById(R.id.btnSelectDateTime);
        this.txtSelectDateTime = (TextView) this.findViewById(R.id.txtSelectDateTime);

        this.txtDescription = (EditText) this.findViewById(R.id.txtDescription);

        this.btnGoBack = (Button) this.findViewById(R.id.btnGoBack);
        this.btnSendMeetingRequest = (Button) this.findViewById(R.id.btnSendMeetingRequest);

        this.layoutSelectBatchSegmnent = (LinearLayout) this.findViewById(R.id.layoutSelectBatchSegmnent);
        this.txtHeaderParent = (TextView) this.findViewById(R.id.txtHeaderParent);


        this.layoutSelectBatchActionHolder = (LinearLayout) this.findViewById(R.id.layoutSelectBatchActionHolder);
        this.layoutSelectParentActionHolder = (LinearLayout) this.findViewById(R.id.layoutSelectParentActionHolder);
        this.layoutSelectSelectDateTimeActionHolder = (LinearLayout) this.findViewById(R.id.layoutSelectSelectDateTimeActionHolder);

    }

    private void initAction() {
        // TODO Auto-generated method stub

        if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
            this.layoutSelectBatchSegmnent.setVisibility(View.GONE);
            this.txtHeaderParent.setText(R.string.java_createmeetingrequest_select_teacher);
            this.txtSelectParent.setHint(R.string.java_createmeetingrequest_select_teacher);//Select Parent by Student Name

            initApiCallParent();
        } else {
            this.layoutSelectBatchSegmnent.setVisibility(View.VISIBLE);
            this.txtHeaderParent.setText(R.string.java_createmeetingrequest_select_parent);
            this.txtSelectParent.setHint(R.string.java_createmeetingrequest_select_parent_by_student);
        }


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        this.txtCurrentDate.setText(currentDateTimeString);


        this.btnSelectBatch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //showBatchPicker(PickerType.TEACHER_BATCH);
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
                //AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
                getBatch(params);

            }
        });


        // this layout also consumes the click event same as this.btnSelectBatch
        this.layoutSelectBatchActionHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //showBatchPicker(PickerType.TEACHER_BATCH);

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
                //AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
                getBatch(params);


            }
        });


        this.btnSelectParent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (TextUtils.isEmpty(txtSelectBatch.getText().toString())) {
                    if(userHelper.getUser().getType() != UserTypeEnum.PARENTS)
                        Toast.makeText(CreateMeetingRequest.this, R.string.java_createmeetingrequest_select_batch_first, Toast.LENGTH_SHORT).show();
                }

                PopupMenu popup = new PopupMenu(CreateMeetingRequest.this, btnSelectParent);
                //popup.getMenuInflater().inflate(R.menu.popup_menu_medium, popup.getMenu());
                for (int i = 0; i < listStudentParent.size(); i++)
                    popup.getMenu().add(listStudentParent.get(i).getName());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(SchoolSearchFragment.this.getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        txtSelectParent.setText(item.getTitle().toString());

                        for (StudentParent sp : listStudentParent) {
                            if (item.getTitle().toString().equalsIgnoreCase(sp.getName()))
                                selectedId = sp.getId();
                        }

                        Log.e("ITEM_ID", "id: " + selectedId);

                        return true;
                    }
                });

                popup.show();
            }
        });

        // this layout also consumes the click event same as this.btnSelectParent
        this.layoutSelectParentActionHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /*if (TextUtils.isEmpty(txtSelectBatch.getText().toString())) {
                    Toast.makeText(CreateMeetingRequest.this, " ", Toast.LENGTH_SHORT).show();
                }*/

                // TODO Auto-generated method stub
                PopupMenu popup = new PopupMenu(CreateMeetingRequest.this, btnSelectParent);
                //popup.getMenuInflater().inflate(R.menu.popup_menu_medium, popup.getMenu());
                for (int i = 0; i < listStudentParent.size(); i++)
                    popup.getMenu().add(listStudentParent.get(i).getName());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(SchoolSearchFragment.this.getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        txtSelectParent.setText(item.getTitle().toString());

                        for (StudentParent sp : listStudentParent) {
                            if (item.getTitle().toString().equalsIgnoreCase(sp.getName()))
                                selectedId = sp.getId();
                        }

                        Log.e("ITEM_ID", "id: " + selectedId);

                        return true;
                    }
                });

                popup.show();

            }
        });


        this.btnSelectDateTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateTimePicker();
            }
        });


        // this layout also consumes the click event same as this.btnSelectDateTime
        this.layoutSelectSelectDateTimeActionHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateTimePicker();
            }
        });


        this.btnGoBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        this.btnSendMeetingRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (isValidate())
                    initApiCallSendRequest();
            }
        });

    }


    private void getBatch(HashMap<String, String> params){
        if (!uiHelper.isDialogActive())
            uiHelper.showLoadingDialog(getString(R.string.loading_text));
        else
            uiHelper.updateLoadingDialog(getString(R.string.loading_text));

        ApplicationSingleton.getInstance().getNetworkCallInterface().getBatch(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();

                        if (response.body() != null){
                            Log.e("Response", ""+response.body());
                            Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(response.body());
                            if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
                                PaidVersionHomeFragment.isBatchLoaded = true;
                                PaidVersionHomeFragment.batches.clear();
                                String data = wrapper.getData().get("batches").toString();
                                PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
                                //showPicker(PickerType.TEACHER_BATCH);
                                showBatchPicker(PickerType.TEACHER_BATCH);
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        uiHelper.dismissLoadingDialog();
                    }
                }
        );
    }
    AsyncHttpResponseHandler getBatchEventsHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            super.onFailure(arg0, arg1);
            //uiHelper.showMessage(arg1);
            uiHelper.dismissLoadingDialog();
        }

        @Override
        public void onStart() {
            super.onStart();

            if (!uiHelper.isDialogActive())
                uiHelper.showLoadingDialog(getString(R.string.loading_text));
            else
                uiHelper.updateLoadingDialog(getString(R.string.loading_text));

        }

        @Override
        public void onSuccess(int arg0, String responseString) {
            super.onSuccess(arg0, responseString);
            uiHelper.dismissLoadingDialog();
            Log.e("Response", responseString);
            Wrapper wrapper = GsonParser.getInstance().parseServerResponse(responseString);
            if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
                PaidVersionHomeFragment.isBatchLoaded = true;
                PaidVersionHomeFragment.batches.clear();
                String data = wrapper.getData().get("batches").toString();
                PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
                //showPicker(PickerType.TEACHER_BATCH);
                showBatchPicker(PickerType.TEACHER_BATCH);
            }

        }

    };


    private void showDateTimePicker() {
        // TODO Auto-generated method stub
        CustomDateTimePicker custom = new CustomDateTimePicker(this,
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        // TODO Auto-generated method stub

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        String dateStr = format.format(dateSelected);
                        Log.e("DATE_SELECTED", "is: " + dateStr);

                        txtSelectDateTime.setText(dateStr);

                    }
                });
        /**
         * Pass Directly current time format it will return AM and PM if you set
         * false
         */
        custom.set24HourFormat(false);
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom.setDate(Calendar.getInstance());
        custom.showDialog();
    }


    private void showBatchPicker(PickerType type) {

        Picker picker = Picker.newInstance(0);
        picker.setData(type, PaidVersionHomeFragment.batches, PickerCallback, getString(R.string.spinner_prompt_select_batch));
        picker.show(this.getSupportFragmentManager(), null);
    }

    PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

        @Override
        public void onPickerItemSelected(BaseType item) {

            switch (item.getType()) {
                case TEACHER_BATCH:
                    Batch selectedBatch = (Batch) item;
                    PaidVersionHomeFragment.selectedBatch = selectedBatch;
                    Intent i = new Intent("com.classtune.app.schoolapp.batch");
                    i.putExtra("batch_id", selectedBatch.getId());
                    CreateMeetingRequest.this.sendBroadcast(i);


                    Log.e("BATCH", "name: " + selectedBatch.getName());
                    Log.e("BATCH", "id: " + selectedBatch.getId());

                    txtSelectBatch.setText(selectedBatch.getName());

                    selectedBatchId = selectedBatch.getId();
                    initApiCall(selectedBatch.getId());

                    break;
                default:
                    break;
            }

        }
    };


    private void initApiCall(String batchId) {

        HashMap<String,String> params = new HashMap<>();

        params.put("batch_id", batchId);
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());


        //AppRestClient.post(URLHelper.URL_MEETING_GETSTUDENTPARENT, params, getStudentParentHandler);
        studentParent(params, URLHelper.URL_MEETING_GETSTUDENTPARENT );
    }


    private void initApiCallParent() {

        HashMap<String,String> params = new HashMap<>();


        params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        Log.e("BATCH_ID", "is: " + userHelper.getUser().getSelectedChild().getBatchId());

        //AppRestClient.post(URLHelper.URL_MEETING_GETTEACHERPARENT, params, getStudentParentHandler);
        studentParent(params,URLHelper.URL_MEETING_GETTEACHERPARENT );
    }


    private void studentParent(HashMap<String,String> params, String url){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().mettingStudentParent(params, url).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();

                        if (response.body() != null){
                            Log.e("RES_PARENT", "is: " + response.body());

                            Wrapper modelContainer = GsonParser.getInstance()
                                    .parseServerResponse2(response.body());


                            if (modelContainer.getStatus().getCode() == 200) {

                                //do parsing
                                JsonArray array = modelContainer.getData().get("student").getAsJsonArray();

                                for (int i = 0; i < parseStudentParent(array.toString()).size(); i++) {
                                    listStudentParent.add(parseStudentParent(array.toString()).get(i));
                                }


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
    private AsyncHttpResponseHandler getStudentParentHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            uiHelper.dismissLoadingDialog();
        }

        ;

        @Override
        public void onStart() {
            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        }

        ;

        @Override
        public void onSuccess(String responseString) {

            uiHelper.dismissLoadingDialog();
            Log.e("RES_PARENT", "is: " + responseString);

            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);


            if (modelContainer.getStatus().getCode() == 200) {

                //do parsing
                JsonArray array = modelContainer.getData().get("student").getAsJsonArray();

                for (int i = 0; i < parseStudentParent(array.toString()).size(); i++) {
                    listStudentParent.add(parseStudentParent(array.toString()).get(i));
                }


            } else {

            }
        }

        ;
    };


    private boolean isValidate() {
        boolean isValid = true;
        if (layoutSelectBatchSegmnent.getVisibility() == View.VISIBLE && TextUtils.isEmpty(txtSelectBatch.getText().toString())) {
            Toast.makeText(this, getString(R.string.spinner_prompt_select_batch), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (TextUtils.isEmpty(txtSelectParent.getText().toString())) {
            if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
                Toast.makeText(this, R.string.java_createmeetingrequest_select_teacher_name, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, R.string.java_createmeetingrequest_select_parent_by_student, Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (TextUtils.isEmpty(txtSelectDateTime.getText().toString())) {
            Toast.makeText(this, R.string.java_createmeetingrequest_select_date_and_time, Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (TextUtils.isEmpty(txtDescription.getText().toString())) {
            Toast.makeText(this, R.string.java_createmeetingrequest_enter_meeting_description, Toast.LENGTH_SHORT).show();
            isValid = false;
        }


        return isValid;
    }

    private void initApiCallSendRequest() {

        HashMap<String,String> params = new HashMap<>();

        if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
            params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
        } else {
            params.put("batch_id", selectedBatchId);

        }


        params.put("parent_id", selectedId);
        params.put("description", txtDescription.getText().toString());
        params.put("datetime", txtSelectDateTime.getText().toString());

        if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
            params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
        }

        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());


        if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
            //AppRestClient.post(URLHelper.URL_MEETING_SEND_REQUEST_PARENT, params, sendMeetingRequestHandler);
            meetingSendRequest(params, URLHelper.URL_MEETING_SEND_REQUEST_PARENT);
        } else {
            //AppRestClient.post(URLHelper.URL_MEETING_SEND_REQUEST, params, sendMeetingRequestHandler);
            meetingSendRequest(params, URLHelper.URL_MEETING_SEND_REQUEST);
        }

    }

    private void meetingSendRequest(HashMap<String,String> params,String url){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().mettingSendRequest(params, url).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();

                        if (response.body() != null){
                            Wrapper modelContainer = GsonParser.getInstance()
                                    .parseServerResponse2(response.body());


                            if (modelContainer.getStatus().getCode() == 200) {

                                //do parsing
                                Toast.makeText(CreateMeetingRequest.this, R.string.java_createmeetingrequest_meeting_request_success, Toast.LENGTH_SHORT).show();

                                CreateMeetingRequest.this.finish();

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
    private AsyncHttpResponseHandler sendMeetingRequestHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            uiHelper.dismissLoadingDialog();
        }

        ;

        @Override
        public void onStart() {
            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        }

        ;

        @Override
        public void onSuccess(String responseString) {

            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);


            if (modelContainer.getStatus().getCode() == 200) {

                //do parsing
                Toast.makeText(CreateMeetingRequest.this, R.string.java_createmeetingrequest_meeting_request_success, Toast.LENGTH_SHORT).show();

                CreateMeetingRequest.this.finish();

            } else {

            }
        }

        ;
    };


    public ArrayList<StudentParent> parseStudentParent(String object) {
        ArrayList<StudentParent> data = new ArrayList<StudentParent>();
        data = new Gson().fromJson(object,
                new TypeToken<ArrayList<StudentParent>>() {
                }.getType());
        return data;
    }


}
