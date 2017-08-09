package com.classtune.app.schoolapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.schoolapp.adapters.GroupExamAdapters;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.GroupExam;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.StudentAttendance;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupTermTestFragment  extends UserVisibleHintFragment implements
        UserAuthListener {
    private Batch selectedBatch;
    private StudentAttendance selectedStudent;
    private View view;
    private ListView examList;
    private UserHelper userHelper;
    private UIHelper uiHelper;
    private List<GroupExam> items;
    private Context con;
    private GroupExamAdapters adapter;
    private LinearLayout pbs;
    private static final String TAG = "Exam Routine";
    public static IPickedStudentName pickedStudentNameListenerTermTest;

    private TextView nodataMsg;
    private String mAuth_id ="";
    private String mDomainName = "";




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.e("HAAHHAHA", "Oncreate!!!");
        init();
    }


    public GroupTermTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_group_term_test, container, false);
        ((LinearLayout) view.findViewById(R.id.top_panel_for_others))
                .setVisibility(View.GONE);
        examList = (ListView) view.findViewById(R.id.exam_listview);
        pbs = (LinearLayout) view.findViewById(R.id.pb);
        nodataMsg = (TextView)view.findViewById(R.id.nodataMsg);


        setUpList();
        //loadDataInToList();
        return view;
    }
    private void loadDataInToList() {

        if (AppUtility.isInternetConnected()) {
            fetchDataFromServer();
            callApiforAuthExam();
        } else
            uiHelper.showMessage(con.getString(R.string.internet_error_text));

    }

    private void fetchDataFromServer() {

        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put(RequestKeyHelper.CATEGORY_ID, "3");
        params.put("no_exams", "1");

        if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS) {


            if(getActivity().getIntent().getExtras()!=null)
            {
                if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                    params.put(RequestKeyHelper.STUDENT_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                    params.put(RequestKeyHelper.BATCH_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


                    GcmIntentService.initApiCall(rid, rtype);
                }
                else
                {
                    params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                    params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
                }
            }
            else
            {
                params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
            }


            //params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
            //params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());

			/*Log.e("SCHOOL_BATCH_ID_STUDENT_ID", userHelper.getUser()
					.getPaidInfo().getSchoolId()
					+ " "
					+ userHelper.getUser().getPaidInfo().getBatchId()
					+ " "
					+ userHelper.getUser().getSelectedChild().getProfileId());*/
        }else if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.TEACHER){
            params.put(RequestKeyHelper.BATCH_ID, selectedBatch.getId());
            params.put(RequestKeyHelper.STUDENT_ID, selectedStudent.getId());
        }

		/*
		 * params.put(RequestKeyHelper.PAGE_NUMBER,"1");
		 * params.put(RequestKeyHelper.PAGE_SIZE, "500");
		 * params.put(RequestKeyHelper.ORIGIN, "1");
		 */

        // params.put(RequestKeyHelper.BATCH_ID,
        // userHelper.getUser().getPaidInfo().getBatchId());

        // params.put("category", pageSize+"");

       // AppRestClient.post(URLHelper.URL_GET_RESULT_GROUP_REPORT, params, getAcademicEventsHandler);
        getResultGroupReport(params);

    }

    private void getResultGroupReport(HashMap<String,String> params){

        pbs.setVisibility(View.VISIBLE);
        ApplicationSingleton.getInstance().getNetworkCallInterface().getResultGroupReport(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        // uiHelper.dismissLoadingDialog();
                        pbs.setVisibility(View.GONE);
                        Log.e("report term response", ""+response.body());
                        Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
                                response.body());
                        if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
                            items.clear();
                            items.addAll(GsonParser.getInstance()
                                    .parseGroupRoutine(
                                            wrapper.getData().getAsJsonArray("exams")
                                                    .toString()));
                            adapter.notifyDataSetChanged();

                            if(items.size()>0) {
                                nodataMsg.setVisibility(View.GONE);
                            }
                            else {
                                nodataMsg.setVisibility(View.VISIBLE);
                            }


                        } else if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SESSION_EXPIRED) {
                            // userHelper.doLogIn();
                        }
                        //Log.e("Events", responseString);

                        initListActionClick();
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        pbs.setVisibility(View.GONE);
                    }
                }
        );
    }
    AsyncHttpResponseHandler getAcademicEventsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onFailure(Throwable arg0, String arg1) {
            super.onFailure(arg0, arg1);
            pbs.setVisibility(View.GONE);
        }

        @Override
        public void onStart() {
            super.onStart();
			/*
			 * if(uiHelper.isDialogActive())
			 * uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			 * else
			 * uiHelper.showLoadingDialog(getString(R.string.loading_text));
			 */
            pbs.setVisibility(View.VISIBLE);

        }

        @Override
        public void onSuccess(int arg0, String responseString) {
            super.onSuccess(arg0, responseString);
            // uiHelper.dismissLoadingDialog();
            pbs.setVisibility(View.GONE);
            Log.e("report term response", responseString);
            Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                    responseString);
            if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
                items.clear();
                items.addAll(GsonParser.getInstance()
                        .parseGroupRoutine(
                                wrapper.getData().getAsJsonArray("exams")
                                        .toString()));
                adapter.notifyDataSetChanged();

                if(items.size()>0) {
                    nodataMsg.setVisibility(View.GONE);
                }
                else {
                    nodataMsg.setVisibility(View.VISIBLE);
                }


            } else if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SESSION_EXPIRED) {
                // userHelper.doLogIn();
            }
            //Log.e("Events", responseString);

            initListActionClick();

        }

    };

    private void callApiforAuthExam(){
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS){
            params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
        }
       // AppRestClient.post(URLHelper.URL_GET_RESULT_GROUP_AUTH_DOWNLOAD, params,getGroupReportHandler);

        resultGroupAuthDownload(params);
    }
    private void resultGroupAuthDownload(HashMap<String,String> params){
        pbs.setVisibility(View.VISIBLE);
        ApplicationSingleton.getInstance().getNetworkCallInterface().resultGrouptAuthDounload(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
// uiHelper.dismissLoadingDialog();
                        pbs.setVisibility(View.GONE);
                        Log.e("report term response", ""+response.body());
                        Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
                                response.body());
                        if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                            mAuth_id =  wrapper.getData().get("auth_id").getAsString();
                            mDomainName = wrapper.getData().get("domain").getAsString();
                            Log.e(TAG, "onSuccess: authod: "+wrapper.getData().get("auth_id").getAsString() );

                        } else if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SESSION_EXPIRED) {
                            // userHelper.doLogIn();
                        }


                        initListActionClick();
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        pbs.setVisibility(View.GONE);
                    }
                }
        );
    }
    AsyncHttpResponseHandler getGroupReportHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onFailure(Throwable arg0, String arg1) {
            super.onFailure(arg0, arg1);
            pbs.setVisibility(View.GONE);
        }

        @Override
        public void onStart() {
            super.onStart();
			/*
			 * if(uiHelper.isDialogActive())
			 * uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			 * else
			 * uiHelper.showLoadingDialog(getString(R.string.loading_text));
			 */
            pbs.setVisibility(View.VISIBLE);

        }

        @Override
        public void onSuccess(int arg0, String responseString) {
            super.onSuccess(arg0, responseString);
            // uiHelper.dismissLoadingDialog();
            pbs.setVisibility(View.GONE);
            Log.e("report term response", responseString);
            Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                    responseString);
            if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                mAuth_id =  wrapper.getData().get("auth_id").getAsString();
                mDomainName = wrapper.getData().get("domain").getAsString();
                Log.e(TAG, "onSuccess: authod: "+wrapper.getData().get("auth_id").getAsString() );

            } else if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SESSION_EXPIRED) {
                // userHelper.doLogIn();
            }


            initListActionClick();

        }

    };

    private void initListActionClick() {
        examList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
               /* GroupExam data = (GroupExam) adapter.getItem(position);
                Intent intent = new Intent(getActivity(),
                        SingleItemTermReportActivity.class);
                intent.putExtra("id",
                        data.getId());
                if(userHelper.getUser().getType()== UserHelper.UserTypeEnum.TEACHER){
                    intent.putExtra("batch_id", selectedBatch.getId());
                    intent.putExtra("student_id", selectedStudent.getId());
                }
                intent.putExtra("term_name", data.getName());
                startActivity(intent);*/
                if (userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS){
                    if (!mAuth_id.equals("") && !mDomainName.equals("")){
                        Log.e(TAG, "onItemClick: "+mDomainName );
                        String url = "http://"+mDomainName+"/user/login?username="+userHelper.getUser().getUsername()+"&password="+userHelper.getUser().getPassword()+"&user_id="+userHelper.getUser().getPaidInfo().getId()+"&auth_id="+ mAuth_id+"&connect_exam="+items.get(position).getId()+"&batch_id="+items.get(position).getBatch_id()+"&student="+userHelper.getUser().getSelectedChild().getProfileId();
                        Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(url)) ;
                        startActivity( browse );
                       // groupReportDownload(userHelper.getUser().getUsername(), userHelper.getUser().getPassword(), userHelper.getUser().getUserId(), mAuth_id , items.get(position).getId(), items.get(position).getBatch_id(), userHelper.getUser().getSelectedChild().getProfileId())
                    }

                }else{
                    if (!mAuth_id.equals("")){
                        String url = "http://"+mDomainName+"/user/login?username="+userHelper.getUser().getUsername()+"&password="+userHelper.getUser().getPassword()+"&user_id="+userHelper.getUser().getPaidInfo().getId()+"&auth_id="+ mAuth_id+"&connect_exam="+items.get(position).getId()+"&batch_id="+items.get(position).getBatch_id()+"&student="+userHelper.getUser().getPaidInfo().getProfileId();
                        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url));
                        startActivity( browse );
                    }

                }


            }
        });
    }

    private void setUpList() {
        adapter = new GroupExamAdapters(con, items);
        examList.setAdapter(adapter);
    }

    private void init() {
        con = getActivity();
        items = new ArrayList<>();
        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(this, con);

    }

    @Override
    public void onAuthenticationStart() {

        if (uiHelper.isDialogActive())
            uiHelper.updateLoadingDialog(getString(R.string.authenticating_text));
        else
            uiHelper.showLoadingDialog(getString(R.string.authenticating_text));

    }

    @Override
    public void onAuthenticationSuccessful() {
        fetchDataFromServer();
    }

    @Override
    public void onAuthenticationFailed(String msg) {
        // TODO Auto-generated method stub

    }

    public void showPicker(PickerType type) {

        Picker picker = Picker.newInstance(0);
        picker.setData(type, PaidVersionHomeFragment.batches, PickerCallback , getString(R.string.fragment_lessonplan_view_txt_select_batch));
        picker.show(getChildFragmentManager(), null);
    }

    public void showStudentPicker(PickerType type) {

        CustomPickerWithLoadData picker = CustomPickerWithLoadData.newInstance(0);
        HashMap<String, String> params = new HashMap<>();
        params.put(RequestKeyHelper.BATCH_ID,selectedBatch.getId());
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        picker.setData(PickerType.TEACHER_STUDENT,params,URLHelper.URL_GET_STUDENTS_ATTENDANCE, PickerCallback , getString(R.string.java_parentreportcardfragment_select_student));
        picker.show(getChildFragmentManager(), null);
    }
    Picker.PickerItemSelectedListener PickerCallback = new Picker.PickerItemSelectedListener() {

        @Override
        public void onPickerItemSelected(BaseType item) {

            switch (item.getType()) {
                case TEACHER_BATCH:
                    selectedBatch=(Batch)item;
                    showStudentPicker(PickerType.TEACHER_STUDENT);
                    break;
                case TEACHER_STUDENT:
                    selectedStudent = (StudentAttendance)item;
				/*Intent i = new Intent("com.champs21.schoolapp.batch");
                i.putExtra("batch_id", selectedBatch.getId());
                i.putExtra("student_id", selectedStudent.getId());
                getActivity().sendBroadcast(i);*/
                    //fetchDataFromServer();
                    if(pickedStudentNameListenerTermTest != null) {
                        pickedStudentNameListenerTermTest.onStudentPicked(selectedStudent.getStudentName());
                    }
                    loadDataInToList();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onVisible() {
        // TODO Auto-generated method stub
        if(AppUtility.isInternetConnected() == false){
            Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
        }

        if(userHelper.getUser().getType()== UserHelper.UserTypeEnum.TEACHER){
            if(!PaidVersionHomeFragment.isBatchLoaded)
            {
                HashMap<String, String> params=new HashMap<>();
                params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
                //AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
                getBatch(params);
            }else {
                showPicker(PickerType.TEACHER_BATCH);
            }
        }else {
            loadDataInToList();
        }

    }


    private void getBatch(HashMap<String, String> params){
        if(!uiHelper.isDialogActive())
            uiHelper.showLoadingDialog(getString(R.string.loading_text));
        else
            uiHelper.updateLoadingDialog(getString(R.string.loading_text));

        ApplicationSingleton.getInstance().getNetworkCallInterface().getBatch(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        Log.e("Response", ""+response.body());
                        Wrapper wrapper=GsonParser.getInstance().parseServerResponse2(response.body());
                        if(wrapper.getStatus().getCode()==AppConstant.RESPONSE_CODE_SUCCESS)
                        {
                            PaidVersionHomeFragment.isBatchLoaded=true;
                            PaidVersionHomeFragment.batches.clear();
                            String data=wrapper.getData().get("batches").toString();
                            PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
                            showPicker(PickerType.TEACHER_BATCH);
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        uiHelper.dismissLoadingDialog();
                    }
                }
        );
    }
    AsyncHttpResponseHandler getBatchEventsHandler=new AsyncHttpResponseHandler()
    {



        @Override
        public void onFailure(Throwable arg0, String arg1) {
            super.onFailure(arg0, arg1);
            //uiHelper.showMessage(arg1);
            uiHelper.dismissLoadingDialog();
        }

        @Override
        public void onStart() {
            super.onStart();

            if(!uiHelper.isDialogActive())
                uiHelper.showLoadingDialog(getString(R.string.loading_text));
            else
                uiHelper.updateLoadingDialog(getString(R.string.loading_text));

        }

        @Override
        public void onSuccess(int arg0, String responseString) {
            super.onSuccess(arg0, responseString);
            uiHelper.dismissLoadingDialog();
            Log.e("Response", responseString);
            Wrapper wrapper=GsonParser.getInstance().parseServerResponse(responseString);
            if(wrapper.getStatus().getCode()==AppConstant.RESPONSE_CODE_SUCCESS)
            {
                PaidVersionHomeFragment.isBatchLoaded=true;
                PaidVersionHomeFragment.batches.clear();
                String data=wrapper.getData().get("batches").toString();
                PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
                showPicker(PickerType.TEACHER_BATCH);
            }

        }

    };
    @Override
    protected void onInvisible() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPaswordChanged() {
        // TODO Auto-generated method stub

    }
}
