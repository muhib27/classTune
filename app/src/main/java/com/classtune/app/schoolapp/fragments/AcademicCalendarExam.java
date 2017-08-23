/**
 * 
 */
package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.SingleCalendarEvent;
import com.classtune.app.schoolapp.adapters.AcademicCalendarListAdapter;
import com.classtune.app.schoolapp.model.AcademicCalendarDataItem;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
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
 *
 */
public class AcademicCalendarExam extends UserVisibleHintFragment implements UserAuthListener {

	private View view;
	private ListView examList;
	private UserHelper userHelper;
	private UIHelper uiHelper;
	private List<AcademicCalendarDataItem> items;
	private Context con;
	private AcademicCalendarListAdapter adapter;
	private TextView gridTitleText;
	boolean _areContentLoaded = false;
	private LinearLayout pbs;
	private static final String TAG = "Academic Calendar Exam";
	private TextView nodata;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "OnActivityCreated!!!");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "Oncreate!!!");
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_academic_calendar_exam,
				container, false);
		examList = (ListView) view.findViewById(R.id.exam_listview);
		gridTitleText = (TextView) view.findViewById(R.id.grid_title_textview);
		gridTitleText.setText(getString(R.string.title_exam_calendar_tab));
		pbs = (LinearLayout)view.findViewById(R.id.pb);
		nodata = (TextView)view.findViewById(R.id.nodataMsg);
		setUpList();
		loadDataInToList();
		return view;
	}

	private void loadDataInToList() {

		if (AppUtility.isInternetConnected()) {
			fetchDataFromServer();
		} else
			uiHelper.showMessage(con.getString(R.string.internet_error_text));

	}

	private void fetchDataFromServer() {
		HashMap<String, String> params = new HashMap<>();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo()
				.getSchoolId());
		params.put(RequestKeyHelper.PAGE_NUMBER, "1");
		params.put(RequestKeyHelper.PAGE_SIZE, "500");
		params.put(RequestKeyHelper.ORIGIN, "0");
		if(userHelper.getUser().getType()==UserTypeEnum.PARENTS){
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
//			/params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}else {
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser()
					.getPaidInfo().getBatchId());
		}
		
		//AppRestClient.post(URLHelper.URL_GET_ACADEMIC_CALENDAR_EVENTS, params, getAcademicEventsHandler);
		getAcademicClendar(params);
	}

	private void getAcademicClendar(HashMap<String, String> params){
		pbs.setVisibility(View.VISIBLE);
		ApplicationSingleton.getInstance().getNetworkCallInterface().academicClender(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						//			uiHelper.dismissLoadingDialog();
						pbs.setVisibility(View.GONE);
						if (response.body() != null ){
							Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
									response.body());
							if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
								items.clear();
								items.addAll(GsonParser.getInstance()
										.parseAcademicCalendarData(
												wrapper.getData().getAsJsonArray("events")
														.toString()));
								adapter.notifyDataSetChanged();
								nodata.setVisibility((items.size()>0)? View.GONE: View.VISIBLE);
							} else if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SESSION_EXPIRED) {
								// userHelper.doLogIn();
							}

							initListActionClick();

							Log.e("Events", ""+response.body());
						}
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
			/*if (uiHelper.isDialogActive())
				uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			else
				uiHelper.showLoadingDialog(getString(R.string.loading_text));*/
			pbs.setVisibility(View.VISIBLE);
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
//			uiHelper.dismissLoadingDialog();
			pbs.setVisibility(View.GONE);
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
				items.clear();
				items.addAll(GsonParser.getInstance()
						.parseAcademicCalendarData(
								wrapper.getData().getAsJsonArray("events")
										.toString()));
				adapter.notifyDataSetChanged();
				nodata.setVisibility((items.size()>0)? View.GONE: View.VISIBLE);
			} else if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SESSION_EXPIRED) {
				// userHelper.doLogIn();
			}
			
			initListActionClick();
			
			Log.e("Events", responseString);
		}
	};
	
	
	private void initListActionClick()
	{
		examList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				AcademicCalendarDataItem data = (AcademicCalendarDataItem)adapter.getItem(position);
				Intent intent = new Intent(getActivity(), SingleCalendarEvent.class);
				intent.putExtra(AppConstant.ID_SINGLE_CALENDAR_EVENT, data.getEventId());
				startActivity(intent);
			}
		});
	}

	private void setUpList() {
		adapter = new AcademicCalendarListAdapter(con, items);
		examList.setAdapter(adapter);
	}
	private void init() {
		con = getActivity();
		items = new ArrayList<AcademicCalendarDataItem>();
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

	@Override
	protected void onVisible() {
		fetchDataFromServer();
	}

	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}

}
