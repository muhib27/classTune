package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.PickerAdapter;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.GraphSubjectType;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomPickerWithLoadData extends DialogFragment {

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		// getDataFromDb();
		titleTextView.setText(titleText);
		adapter = new PickerAdapter(getActivity(), 0, items, null);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				CustomPickerWithLoadData.this.dismiss();
				listener.onPickerItemSelected(items.get(position));
			}
		});

		fetchData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		items = new ArrayList<BaseType>();
	}

	private String titleText;
	private TextView titleTextView;
	private Button cancelBtn;
	private ListView list;
	private List<BaseType> items;
	private PickerAdapter adapter;
	private PickerType type;
	private HashMap<String,String> params;
	private String url;
	private PickerItemSelectedListener listener;
	private LinearLayout pbLayout;

	public CustomPickerWithLoadData() {

	}

	public static CustomPickerWithLoadData newInstance(int title) {
		CustomPickerWithLoadData frag = new CustomPickerWithLoadData();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.picker, container, false);
		list = (ListView) view.findViewById(R.id.list);
		titleTextView = (TextView) view.findViewById(R.id.dialog_tv_title);
		pbLayout = (LinearLayout) view.findViewById(R.id.pb);
		cancelBtn = (Button) view.findViewById(R.id.dialog_btn_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		return view;
	}

	public void setData(PickerType type, HashMap<String,String> params, String url,
			PickerItemSelectedListener lis, String titleText) {
		this.listener = lis;
		this.type = type;
		this.titleText = titleText;
		this.params = params;
		this.url = url;
	}

	private void fetchData() {
		if (this.type == PickerType.LEAVE) {
			//AppRestClient.post(url, params, getLeaveTypeHnadler);
			getpickerLeave(params, url);
		} else if(this.type == PickerType.GRAPH){
			//AppRestClient.post(url,params,getGraphSubjectHandler);
			getPickerGraphSubject(params, url);
		} else {
			//AppRestClient.post(url, params, getStudentHandler);
			getPickerStudent(params, url);
		}
	}

	private void getpickerLeave(HashMap<String, String> params, String url){
		pbLayout.setVisibility(View.VISIBLE);
		ApplicationSingleton.getInstance().getNetworkCallInterface().getPickerTypeLeave(params, url).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

						pbLayout.setVisibility(View.GONE);
						if (response.body() != null){
							Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
									response.body());
							items.addAll(GsonParser.getInstance().parseLeaveTypeList(
									(wrapper.getData().get("type")).toString()));
							adapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						pbLayout.setVisibility(View.GONE);

					}
				}
		);

	}
	AsyncHttpResponseHandler getLeaveTypeHnadler = new AsyncHttpResponseHandler(){
		public void onFailure(Throwable arg0, String arg1) {
			pbLayout.setVisibility(View.GONE);
			Log.e("error", arg1);
		};

		public void onStart() {
			pbLayout.setVisibility(View.VISIBLE);

		};

		public void onSuccess(int arg0, String responseString) {

			pbLayout.setVisibility(View.GONE);
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			items.addAll(GsonParser.getInstance().parseLeaveTypeList(
					(wrapper.getData().get("type")).toString()));
			adapter.notifyDataSetChanged();
		};
	};

	private void getPickerGraphSubject(HashMap<String, String> params, String url){
		pbLayout.setVisibility(View.VISIBLE);
		ApplicationSingleton.getInstance().getNetworkCallInterface().getPickerTypeGraphSubject(params, url).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						pbLayout.setVisibility(View.GONE);
						if (response.body() != null){
							Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
									response.body());
							items.add(new GraphSubjectType("All","-2"));
							items.addAll(GsonParser.getInstance().parseGraphSubjectList(
									(wrapper.getData().get("subjects")).toString()));

							adapter.notifyDataSetChanged();
						}

					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						pbLayout.setVisibility(View.GONE);
					}
				}
		);

	}
    AsyncHttpResponseHandler getGraphSubjectHandler = new AsyncHttpResponseHandler(){
        public void onFailure(Throwable arg0, String arg1) {
            pbLayout.setVisibility(View.GONE);
            Log.e("error", arg1);
        };

        public void onStart() {
            pbLayout.setVisibility(View.VISIBLE);

        };

        public void onSuccess(int arg0, String responseString) {

            pbLayout.setVisibility(View.GONE);
            Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
                    responseString);
            items.add(new GraphSubjectType("All","-2"));
            items.addAll(GsonParser.getInstance().parseGraphSubjectList(
                    (wrapper.getData().get("subjects")).toString()));

            adapter.notifyDataSetChanged();
        };
    };

    private void getPickerStudent(HashMap<String,String> params, String url){
		pbLayout.setVisibility(View.VISIBLE);
		ApplicationSingleton.getInstance().getNetworkCallInterface().getPickerTypeStudent(params, url).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						pbLayout.setVisibility(View.GONE);
						if (response.body() != null){
							Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
									response.body());
							items.addAll(GsonParser.getInstance().parseStudentList(
									(wrapper.getData().get("batch_attendence")).toString()));
							adapter.notifyDataSetChanged();
						}

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
			//Log.e("error", arg1);
		};

		public void onStart() {
			pbLayout.setVisibility(View.VISIBLE);

		};

		public void onSuccess(int arg0, String responseString) {

			pbLayout.setVisibility(View.GONE);
			Log.e("Menu", responseString);
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			items.addAll(GsonParser.getInstance().parseStudentList(
					(wrapper.getData().get("batch_attendence")).toString()));
			adapter.notifyDataSetChanged();
		};
	};

}
