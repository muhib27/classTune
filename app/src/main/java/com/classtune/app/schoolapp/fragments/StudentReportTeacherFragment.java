package com.classtune.app.schoolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.schoolapp.adapters.StudentReportListAdapter;
import com.classtune.app.schoolapp.model.StudentAttendance;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.ObservableObject;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentReportTeacherFragment extends Fragment implements Observer{
	private View rootView;
	private ListView studentListView;
	private LinearLayout pbLayout;
	private EditText editsearch;
	private ArrayList<StudentAttendance> arraylist = new ArrayList<StudentAttendance>();
	private StudentReportListAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fetchData();

	}

	@Override
	public void onResume() {
		super.onResume();
		if(AppUtility.isInternetConnected() == false){
			Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
		}
	}

	private void fetchData() {
		if(PaidVersionHomeFragment.selectedBatch==null)return;
		HashMap<String,String> params = new HashMap<>();
		params.put(RequestKeyHelper.BATCH_ID,
				PaidVersionHomeFragment.selectedBatch.getId());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		
		//AppRestClient.post(URLHelper.URL_GET_STUDENTS_ATTENDANCE, params, getStudentHandler);
		getStudentAttendence(params);
	}

	private void getStudentAttendence(HashMap<String,String> params){
		pbLayout.setVisibility(View.VISIBLE);
		arraylist.clear();
		ApplicationSingleton.getInstance().getNetworkCallInterface().getStudentAttendence(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						arraylist.clear();

						pbLayout.setVisibility(View.GONE);
						Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
								response.body());
						arraylist.addAll(GsonParser.getInstance().parseStudentList(
								(wrapper.getData().get("batch_attendence")).toString()));
						adapter = new StudentReportListAdapter(getActivity(), arraylist);
						studentListView.setAdapter(adapter);
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						pbLayout.setVisibility(View.GONE);
					}
				}
		);
	}
	AsyncHttpResponseHandler getStudentHandler = new AsyncHttpResponseHandler() {
		public void onFailure(Throwable arg0, String arg1) {
			pbLayout.setVisibility(View.GONE);
			Log.e("error", arg1);
		};

		public void onStart() {
			pbLayout.setVisibility(View.VISIBLE);
			arraylist.clear();

		};

		public void onSuccess(int arg0, String responseString) {
			arraylist.clear();

			pbLayout.setVisibility(View.GONE);
			Log.e("Menu", responseString);
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			arraylist.addAll(GsonParser.getInstance().parseStudentList(
					(wrapper.getData().get("batch_attendence")).toString()));
			adapter = new StudentReportListAdapter(getActivity(), arraylist);
			studentListView.setAdapter(adapter);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("TAG_NAME", "tag: " + this.getTag());
		ObservableObject.getInstance().addObserver(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.student_report_layout, container,
				false);
		editsearch = (EditText) rootView.findViewById(R.id.search);
		studentListView = (ListView) rootView.findViewById(R.id.listview);
		pbLayout = (LinearLayout) rootView.findViewById(R.id.pb);
		// Capture Text in EditText

		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = editsearch.getText().toString()
						.toLowerCase(Locale.getDefault()) ;
				if(adapter!=null)
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});
		return rootView;
	}

	@Override
	public void update(Observable observable, Object data) {

		if(((Intent)data).getExtras().containsKey(AppConstant.KEY_BATCH_FROM_TEACHERATTENDANCE_TAB))
		{
			if(isAdded())
				fetchData();
		}
	}
}