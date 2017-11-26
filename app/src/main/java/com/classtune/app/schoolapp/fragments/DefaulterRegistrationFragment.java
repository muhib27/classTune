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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.DefaulterRegistrationActivity;
import com.classtune.app.schoolapp.adapters.DefaulterRegistrationAdapter;
import com.classtune.app.schoolapp.model.DefaulterModel;
import com.classtune.app.schoolapp.model.TeacherHomework;
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
public class DefaulterRegistrationFragment extends Fragment {
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
    private int list_pos;
    private TextView noData;
    LinearLayout listLayout, layoutBottom;

    CustomButtonListener customListner;

    public interface CustomButtonListener {
        public void onButtonClickListner(int list_pos);
    }

    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customListner = listener;
    }


    public DefaulterRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id = getArguments().getString("id");
        list_pos = getArguments().getInt("list_pos");
        View view = inflater.inflate(R.layout.fragment_defaulter_registration, container, false);
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

    private void initView(View view) {
        listLayout = (LinearLayout) view.findViewById(R.id.listLayout);
        layoutBottom = (LinearLayout) view.findViewById(R.id.layoutBottom);
        noData = (TextView) view.findViewById(R.id.noData);
        listView = (ListView) view.findViewById(R.id.listView);
        //listView.setOnItemClickListener(this);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        checkBoxRegistered = (CheckBox) view.findViewById(R.id.checkBoxRegistered);
        defaulterModelList = new ArrayList<DefaulterModel>();

//        ArrayList<TeacherHomework> allGooadReadPost = new ArrayList<TeacherHomework>();
//        TeacherHomework teacherHomework = new TeacherHomework();
//        teacherHomework.setDefaulter_registration(1);
    }

    private void initApiCallStudent() {
        HashMap<String, String> params = new HashMap<>();
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

                                if (defaulterModelList.size() <= 0) {
                                    noData.setVisibility(View.VISIBLE);
                                    listLayout.setVisibility(View.INVISIBLE);
                                    layoutBottom.setVisibility(View.INVISIBLE);
                                } else {
                                    defaulterRegistrationAdapter = new DefaulterRegistrationAdapter(getActivity(), defaulterModelList);
                                    defaulterRegistrationAdapter.notifyDataSetChanged();
                                    listView.setAdapter(defaulterRegistrationAdapter);
                                    listLayout.setVisibility(View.VISIBLE);
                                    layoutBottom.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.INVISIBLE);
                                    initAction();
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

    public List<DefaulterModel> parseStudent(String object) {

        List<DefaulterModel> dList = new ArrayList<>();
        Type listType = new TypeToken<List<DefaulterModel>>() {
        }.getType();
        dList = (List<DefaulterModel>) new Gson().fromJson(object, listType);
        return dList;
    }


    private void initAction() {

        if (!registerId.equals("0")) {
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


        } else {

            checkBoxRegistered.setChecked(false);
            checkBoxRegistered.setText(getString(R.string.defaulter_registration));
            checkBoxRegistered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (defaulterModelList.size() > 0) {
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


                    } else {
                        checkBoxRegistered.setChecked(false);
                        Toast.makeText(getActivity(), R.string.no_student, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            defaulterRegistrationAdapter.setClickable(false);
        }


        if (defaulterRegistrationAdapter.isUpdate()) {
            btnSubmit.setText(getString(R.string.attendance_subject_btn_update));
        } else {
            btnSubmit.setText(getString(R.string.attendance_subject_btn_submit));
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!registerId.equals("0")) {
                    initApiCallSubmitAttandance();
                } else {
                    Toast.makeText(getActivity(), R.string.message_defaulter_register, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initApiCallSubmitAttandance() {
        HashMap<String, String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", id);
        params.put("student_id", appendListWithComma(defaulterRegistrationAdapter.getListStudentDataId()));
        //params.put("late", appendListWithComma(adapter.getListStudentStatusNew()));

        //AppRestClient.post(URLHelper.URL_TEACHER_SUBJECT_ATTENDANCE_ADD, params, submitHandler);

        teacherDefaulterAddApiCall(params);
//        if (customListner != null) {
//            customListner.onButtonClickListner(list_pos);
//        }


    }

    private void teacherDefaulterAddApiCall(HashMap<String, String> params) {
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().teacherDefaulterAdd(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        uiHelper.dismissLoadingDialog();
                        if (response.body() != null) {
                            Log.e("Response", "" + response.body());

                            Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(response.body());
                            if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {

                                TeacherHomeWorkFeedFragment.allGooadReadPost.get(list_pos).setDefaulter_registration(1);
                                if (defaulterRegistrationAdapter.isUpdate()) {
                                    Toast.makeText(getActivity(), R.string.defaulter_updated_successfully, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), R.string.defaulter_saved_successfully, Toast.LENGTH_SHORT).show();
                                }
                                ((DefaulterRegistrationActivity) getActivity()).gotoDefaulterList();

                                //finish();

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


    private String appendListWithComma(List<String> sellItems) {
        StringBuilder sb = new StringBuilder();
        for (String item : sellItems) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(item);
        }
        String result = sb.toString();

        return result;
    }

}
