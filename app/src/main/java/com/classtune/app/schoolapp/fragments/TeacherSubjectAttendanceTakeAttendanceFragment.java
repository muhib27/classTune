package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.TeacherAssociatedSubjectAdapter;
import com.classtune.app.schoolapp.model.RoutineTimeTable;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BLACK HAT on 15-Feb-17.
 */

public class TeacherSubjectAttendanceTakeAttendanceFragment extends UserVisibleHintFragment {

    private View mainView;
    private List<RoutineTimeTable> subjectList;
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private TeacherAssociatedSubjectAdapter adapter;
    private ListView listView;
    private TextView txtMessage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectList = new ArrayList<>();
        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_teacher_take_attendance, container, false);
        initView(mainView);
        return mainView;
    }


    private void initView(View view){
        listView = (ListView)view.findViewById(R.id.listView);
        txtMessage = (TextView)view.findViewById(R.id.txtMessage);
    }


    private void initApiCallSubjet() {
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

        //AppRestClient.post(URLHelper.URL_GET_WEEK_DAY_CLASSES, params, subjectHandler);

        getWeekDayClasses(params);
    }

    private void getWeekDayClasses(HashMap<String,String > params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().getWeekDayClasses(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        Log.e("Response", ""+response.body());

                        Wrapper wrapper= GsonParser.getInstance().parseServerResponse2(response.body());
                        if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {

                            JsonArray arraySubject = wrapper.getData().get("time_table").getAsJsonArray();
                            subjectList.addAll(parseSubject(arraySubject.toString()));
                            adapter = new TeacherAssociatedSubjectAdapter(getActivity(), subjectList);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);

                            if(subjectList.size()<=0){
                                txtMessage.setVisibility(View.VISIBLE);
                            }else{
                                txtMessage.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.you_have_no_class_today, Toast.LENGTH_SHORT).show();
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

                JsonArray arraySubject = wrapper.getData().get("time_table").getAsJsonArray();
                subjectList.addAll(parseSubject(arraySubject.toString()));
                adapter = new TeacherAssociatedSubjectAdapter(getActivity(), subjectList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                if(subjectList.size()<=0){
                    txtMessage.setVisibility(View.VISIBLE);
                }else{
                    txtMessage.setVisibility(View.GONE);
                }

            } else {
                Toast.makeText(getActivity(), R.string.you_have_no_class_today, Toast.LENGTH_SHORT).show();
            }
        }

    };

    public List<RoutineTimeTable> parseSubject(String object) {

        List<RoutineTimeTable> tags = new ArrayList<>();
        Type listType = new TypeToken<List<RoutineTimeTable>>() {}.getType();
        tags = (List<RoutineTimeTable>) new Gson().fromJson(object, listType);
        return tags;
    }

    @Override
    protected void onVisible() {
        if(AppUtility.isInternetConnected() == false){
            Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
        }

        if(subjectList != null){
            subjectList.clear();
        }
        initApiCallSubjet();
    }

    @Override
    protected void onInvisible() {

    }
}
