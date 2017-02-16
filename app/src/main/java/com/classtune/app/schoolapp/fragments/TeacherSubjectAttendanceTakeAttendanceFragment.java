package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.TeacherAssociatedSubjectAdapter;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLACK HAT on 15-Feb-17.
 */

public class TeacherSubjectAttendanceTakeAttendanceFragment extends UserVisibleHintFragment {

    private View mainView;
    private List<Subject> subjectList;
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private TeacherAssociatedSubjectAdapter adapter;
    private ListView listView;


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
    }


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

            Wrapper wrapper= GsonParser.getInstance().parseServerResponse(responseString);
            if(wrapper.getStatus().getCode()== AppConstant.RESPONSE_CODE_SUCCESS) {

                JsonArray arraySubject = wrapper.getData().get("subjects").getAsJsonArray();
                subjectList.addAll(parseSubject(arraySubject.toString()));
                adapter = new TeacherAssociatedSubjectAdapter(getActivity(), subjectList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            } else {

            }
        }

    };

    public List<Subject> parseSubject(String object) {

        List<Subject> tags = new ArrayList<>();
        Type listType = new TypeToken<List<Subject>>() {}.getType();
        tags = (List<Subject>) new Gson().fromJson(object, listType);
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
