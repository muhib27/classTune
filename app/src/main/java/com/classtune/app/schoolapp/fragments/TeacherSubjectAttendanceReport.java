package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.Subject;
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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by BLACK HAT on 15-Feb-17.
 */

public class TeacherSubjectAttendanceReport extends UserVisibleHintFragment {

    private View mainView;
    private List<BaseType> subjectList;
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private String selectedDate = "";
    private String selectedSubjectId = "";
    private Calendar now;

    private LinearLayout layoutSelectSubjectRoot;
    private TextView txtSubjectName;
    private ImageButton btnSelectSubject;
    private LinearLayout layoutSelectDateRoot;
    private TextView txtDate;
    private ImageButton btnSelectDate;
    private TextView txtClassCompleted;
    private TextView txtTotalStudent;
    private TextView txtPresent;
    private TextView txtAbsent;
    private TextView txtLate;
    private Button btnFullReport;
    private LinearLayout layoutTeacherReport;
    private boolean isDatePickerCalledOnce = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(getActivity());
        subjectList = new ArrayList<>();
        now = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_teacher_subject_report, container, false);
        initView(mainView);

        now.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(now.getTime());
        selectedDate = formatted;

        return mainView;
    }

    private String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        return AppUtility.getFormatedDateString(AppUtility.DATE_FORMAT_APP, c);
    }


    @Override
    protected void onVisible() {


        Log.e("INITIAL_DATE", "is: "+selectedDate);

        initApiCallSubjet();
    }

    @Override
    protected void onInvisible() {

    }

    private void showDatepicker() {
        DatePickerFragment picker = new DatePickerFragment();
        picker.setCallbacks(datePickerCallback);
        picker.show(getChildFragmentManager(), "datePicker");
    }

    DatePickerFragment.DatePickerOnSetDateListener datePickerCallback = new DatePickerFragment.DatePickerOnSetDateListener() {

        @Override
        public void onDateSelected(int month, String monthName, int day,
                                   int year, String dateFormatServer, String dateFormatApp,
                                   Date date) {
            // TODO Auto-generated method stub
            selectedDate = dateFormatServer;
            Log.e("SELECTED_ DATE", "is: " + selectedDate);
            if(!TextUtils.isEmpty(selectedSubjectId)){
                if(isDatePickerCalledOnce == false){
                    isDatePickerCalledOnce = true;
                    layoutTeacherReport.setVisibility(View.GONE);
                    initApiCallReport();
                }

            }



        }

    };

    private void initApiCallSubjet() {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        AppRestClient.post(URLHelper.URL_TEACHER_ASSOCIATED_SUBJECT, params,
                subjectHandler);
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
            subjectList.clear();

            Wrapper wrapper= GsonParser.getInstance().parseServerResponse(responseString);
            if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {

                JsonArray arraySubject = wrapper.getData().get("subjects").getAsJsonArray();
                subjectList.addAll(parseSubject(arraySubject.toString()));

            } else {

            }

            showSubjectPicker();
        }

    };

    public List<Subject> parseSubject(String object) {

        List<Subject> tags = new ArrayList<>();
        Type listType = new TypeToken<List<Subject>>() {}.getType();
        tags = (List<Subject>) new Gson().fromJson(object, listType);
        return tags;
    }

    private void showSubjectPicker() {
        Picker picker = Picker.newInstance(0);
        picker.setData(PickerType.TEACHER_SUBJECT, subjectList, PickerCallback,
                getActivity().getString(R.string.java_homeworkfragment_choose_your_subject));
        picker.show(getChildFragmentManager(), null);
    }

    Picker.PickerItemSelectedListener PickerCallback = new Picker.PickerItemSelectedListener() {

        @Override
        public void onPickerItemSelected(BaseType item) {

            switch (item.getType()) {
                case TEACHER_SUBJECT:
                    Subject hs = (Subject) item;
                    selectedSubjectId = hs.getId();
                    Log.e("SELECTED_SUBJECT_ID", "is: "+selectedSubjectId);
                    txtSubjectName.setText(hs.getName());

                    layoutTeacherReport.setVisibility(View.GONE);
                    initApiCallReport();

                    break;
                default:
                    break;
            }

        }
    };

    private void initView(View view){
        layoutSelectSubjectRoot = (LinearLayout)view.findViewById(R.id.layoutSelectSubjectRoot);
        txtSubjectName = (TextView)view.findViewById(R.id.txtSubjectName);
        btnSelectSubject = (ImageButton) view.findViewById(R.id.btnSelectSubject);
        layoutSelectDateRoot = (LinearLayout)view.findViewById(R.id.layoutSelectDateRoot);
        txtDate = (TextView)view.findViewById(R.id.txtDate);
        btnSelectDate = (ImageButton)view.findViewById(R.id.btnSelectDate);
        txtClassCompleted = (TextView)view.findViewById(R.id.txtClassCompleted);
        txtTotalStudent = (TextView)view.findViewById(R.id.txtTotalStudent);
        txtPresent = (TextView)view.findViewById(R.id.txtPresent);
        txtAbsent = (TextView)view.findViewById(R.id.txtAbsent);
        txtLate = (TextView)view.findViewById(R.id.txtLate);
        btnFullReport = (Button)view.findViewById(R.id.btnFullReport);
        layoutTeacherReport = (LinearLayout)view.findViewById(R.id.layoutTeacherReport);
        layoutTeacherReport.setVisibility(View.GONE);

        initAction();
    }

    private void initAction(){
        layoutSelectSubjectRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubjectPicker();
            }
        });

        btnSelectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubjectPicker();
            }
        });

        txtDate.setText(getCurrentDate());

        layoutSelectDateRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker();
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker();
            }
        });

        btnFullReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(selectedSubjectId)){
                    // go to full list activity
                }else{
                    Toast.makeText(getActivity(), "Please select a subject first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initApiCallReport() {
        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("subject_id", selectedSubjectId);
        params.put("date", selectedDate);

        AppRestClient.post(URLHelper.URL_TEACHER_SUBJECT_REPORT, params,
                reportHandler);
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

                layoutTeacherReport.setVisibility(View.VISIBLE);

                int classCompleted = wrapper.getData().get("class_completed").getAsInt();
                int total = wrapper.getData().get("total").getAsInt();
                int present = wrapper.getData().get("present").getAsInt();
                int absent = wrapper.getData().get("absent").getAsInt();
                int late = wrapper.getData().get("late").getAsInt();

                initActionAfterReportCall(classCompleted, total, present, absent, late);

                isDatePickerCalledOnce = false;

            } else {

            }

        }

    };

    private void initActionAfterReportCall(int classCompleted, int total, int present, int absent, int late){

        txtClassCompleted.setText(String.valueOf(classCompleted));
        txtTotalStudent.setText(String.valueOf(total));
        txtPresent.setText(String.valueOf(present));
        txtAbsent.setText(String.valueOf(absent));
        txtLate.setText(String.valueOf(late));

    }
}
