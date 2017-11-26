package com.classtune.app.schoolapp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.DefaulterRegistrationActivity;
import com.classtune.app.schoolapp.adapters.DefaulterListAdapter;
import com.classtune.app.schoolapp.adapters.DefaulterRegistrationAdapter;
import com.classtune.app.schoolapp.model.DefaulterModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DefaulterListFragment extends Fragment implements View.OnClickListener{


    private List<DefaulterModel> defaulterModelList;
    private ListView listView;
    DefaulterListAdapter defaulterListAdapter;
    private String id = "";
    private UIHelper uiHelper;
    private UserHelper userHelper;
    private String registerId = "";
    DefaulterModel defaulterModel;
    private Button btnUpdate;
    private TextView noData;
    LinearLayout listLayout, layoutBottom;


    public DefaulterListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id=getArguments().getString("id");
        View view =  inflater.inflate(R.layout.fragment_defaulter_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        // Log.e("SUBJECT_ID", "is: "+id);
        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(getActivity());
        initApiCallStudent();
    }

    private void initView(View view){
        listLayout = (LinearLayout) view.findViewById(R.id.listLayout);
        layoutBottom = (LinearLayout) view.findViewById(R.id.layoutBottom);
        noData = (TextView) view.findViewById(R.id.noData);
        listView = (ListView) view.findViewById(R.id.listView);
        //listView.setOnItemClickListener(this);
        btnUpdate = (Button)view.findViewById(R.id.defaulter_update);
        btnUpdate.setOnClickListener(this);
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
        ApplicationSingleton.getInstance().getNetworkCallInterface().getDefaultertList(params).enqueue(
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

                                if (defaulterModelList.size() <= 0) {
                                    noData.setVisibility(View.VISIBLE);
                                    listLayout.setVisibility(View.INVISIBLE);
                                    //layoutBottom.setVisibility(View.INVISIBLE);
                                } else {
                                    defaulterListAdapter = new DefaulterListAdapter(getActivity(), defaulterModelList);
                                    defaulterListAdapter.notifyDataSetChanged();
                                    listView.setAdapter(defaulterListAdapter);
                                    listLayout.setVisibility(View.VISIBLE);
                                    //layoutBottom.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.INVISIBLE);
                                }

                                //initAction();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.defaulter_update:
                ((DefaulterRegistrationActivity)getActivity()).gotoDefaulterRegistration();
                break;
        }
    }

    private void gotoDefaulterRegistration(){

    }
}
