package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.ExamRoutineTeacherModel;
import com.classtune.app.schoolapp.model.Wrapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BLACK HAT on 27-Apr-15.
 */
public class ExamRoutineTeacherFragment extends Fragment {


    private View view;
    private UIHelper uiHelper;
    private UserHelper userHelper;

    private ListView listViewExamRoutine;

    private List<ExamRoutineTeacherModel> listExamRoutine;

    private ExamRoutineTeacherAdapter adapter;

    private TextView txtMessage;
    //private TextView txtDate;
    private TextView txtSubjectNameUpper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UIHelper(getActivity());
        userHelper = new UserHelper(getActivity());

        listExamRoutine = new ArrayList<ExamRoutineTeacherModel>();
        listExamRoutine.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initApiCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_exam_routine_teacher, container, false);

        initView(view);

        return view;
    }

    private void initView(View view)
    {
        listViewExamRoutine = (ListView)view.findViewById(R.id.listViewExamRoutine);
        adapter = new ExamRoutineTeacherAdapter();
        listViewExamRoutine.setAdapter(adapter);

        txtMessage = (TextView)view.findViewById(R.id.txtMessage);
        //txtDate = (TextView)view.findViewById(R.id.txtDate);
        txtSubjectNameUpper = (TextView)view.findViewById(R.id.txtSubjectNameUpper);

        //txtDate.setText(AppUtility.getCurrentDate(AppUtility.DATE_FORMAT_APP));
    }

    private void initApiCall()
    {
        HashMap<String,String> params = new HashMap<>();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("limit", "25");


       // AppRestClient.post(URLHelper.URL_EXAM_ROUTINE_TEACHER, params, examRoutineHandler);
        examRoutineTeacher(params);
    }

    private void examRoutineTeacher(HashMap<String,String> params){
        uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
        ApplicationSingleton.getInstance().getNetworkCallInterface().examRoutineTeacher(params).enqueue(
                new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                        uiHelper.dismissLoadingDialog();
                        if (response.body() != null){
                            Wrapper modelContainer = GsonParser.getInstance()
                                    .parseServerResponse2(response.body());

                            if (modelContainer.getStatus().getCode() == 200) {


                                JsonArray arrayExam = modelContainer.getData().get("time_table").getAsJsonArray();

                                for (int i = 0; i < arrayExam.size(); i++)
                                {
                                    listExamRoutine.add(parseExamRoutine(arrayExam.toString()).get(i));
                                }

                                if(listExamRoutine.size() == 0)
                                {
                                    txtMessage.setVisibility(View.VISIBLE);
                                    listViewExamRoutine.setVisibility(View.GONE);
                                }
                                else
                                {
                                    txtMessage.setVisibility(View.GONE);
                                    listViewExamRoutine.setVisibility(View.VISIBLE);
                                }

                                adapter.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        uiHelper.showMessage(getString(R.string.internet_error_text));
                        if (uiHelper.isDialogActive()) {
                            uiHelper.dismissLoadingDialog();
                        }
                    }
                }
        );
    }
    AsyncHttpResponseHandler examRoutineHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {
            uiHelper.showMessage(getString(R.string.internet_error_text));
            if (uiHelper.isDialogActive()) {
                uiHelper.dismissLoadingDialog();
            }
        };

        @Override
        public void onStart() {

            uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));


        };

        @Override
        public void onSuccess(int arg0, String responseString) {


            uiHelper.dismissLoadingDialog();


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {


                JsonArray arrayExam = modelContainer.getData().get("time_table").getAsJsonArray();

                for (int i = 0; i < arrayExam.size(); i++)
                {
                    listExamRoutine.add(parseExamRoutine(arrayExam.toString()).get(i));
                }

                if(listExamRoutine.size() == 0)
                {
                    txtMessage.setVisibility(View.VISIBLE);
                    listViewExamRoutine.setVisibility(View.GONE);
                }
                else
                {
                    txtMessage.setVisibility(View.GONE);
                    listViewExamRoutine.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();

            }

            else {

            }



        };
    };

    private ArrayList<ExamRoutineTeacherModel> parseExamRoutine(String object) {
        ArrayList<ExamRoutineTeacherModel> data = new ArrayList<ExamRoutineTeacherModel>();
        data = new Gson().fromJson(object, new TypeToken<ArrayList<ExamRoutineTeacherModel>>() {}.getType());
        return data;
    }


    private class ExamRoutineTeacherAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listExamRoutine.size();
        }

        @Override
        public Object getItem(int position) {
            return listExamRoutine.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_exam_routine_teacher, parent, false);

                holder.imgViewSubjectIcon = (ImageView)convertView.findViewById(R.id.imgViewSubjectIcon);
                holder.txtSubjectName = (TextView)convertView.findViewById(R.id.txtSubjectName);
                holder.txtStartTime = (TextView)convertView.findViewById(R.id.txtStartTime);
                holder.txtEndTime = (TextView)convertView.findViewById(R.id.txtEndTime);
                holder.txtExamName = (TextView)convertView.findViewById(R.id.txtExamName);
                holder.txtBatch = (TextView)convertView.findViewById(R.id.txtBatch);


                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imgViewSubjectIcon.setImageResource(AppUtility.getImageResourceId(listExamRoutine.get(position).getSubjectIcon(), getActivity()));
            holder.txtSubjectName.setText(listExamRoutine.get(position).getSubject());
            holder.txtStartTime.setText(getActivity().getString(R.string.java_examroutineteacherfragment_start_time)+listExamRoutine.get(position).getStartTime());
            holder.txtEndTime.setText(getActivity().getString(R.string.java_examroutineteacherfragment_end_time)+listExamRoutine.get(position).getEndTime());
            holder.txtExamName.setText(getActivity().getString(R.string.java_examroutineteacherfragment_exam_name)+listExamRoutine.get(position).getExamName());
            holder.txtBatch.setText(getActivity().getString(R.string.java_examroutineteacherfragment_batch)+listExamRoutine.get(position).getBatch());



            return convertView;
        }


        class ViewHolder{

            ImageView imgViewSubjectIcon;
            TextView txtSubjectName;
            TextView txtStartTime;
            TextView txtEndTime;
            TextView txtExamName;
            TextView txtBatch;

        }
    }



}
