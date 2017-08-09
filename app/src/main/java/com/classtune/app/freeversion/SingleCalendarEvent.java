package com.classtune.app.freeversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.AcademicCalendarDataItem;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SingleCalendarEvent extends ChildContainerActivity {
	
	
	
	private UIHelper uiHelper;
	private Gson gson;
	private String id;
	
	private AcademicCalendarDataItem data;
	//private TextView txtEventCatName;
	private TextView txtEventTitle;
	private TextView txtStartDate;
	private TextView txtEndDate;
	private TextView txtDescription;
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_calendar_event);
		
		uiHelper = new UIHelper(SingleCalendarEvent.this);
		gson = new Gson();
		
		if(getIntent().getExtras() != null)
			this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_CALENDAR_EVENT);
		
		/*JsonObject objHomework = modelContainer.getData().get("homework").getAsJsonObject();
		data = gson.fromJson(objHomework.toString(), HomeworkData.class);*/
		
		
		Log.e("SINGLE_EVENT", "id: " + id);
		
		initView();
		
		initApiCall();
		
	}
	
	
	private void initView()
	{
		//this.txtEventCatName = (TextView)this.findViewById(R.id.txtEventCatName);
		this.txtEventTitle = (TextView)this.findViewById(R.id.txtEventTitle);
		this.txtStartDate = (TextView)this.findViewById(R.id.txtStartDate);
		this.txtEndDate = (TextView)this.findViewById(R.id.txtEndDate);
		this.txtDescription = (TextView)this.findViewById(R.id.txtDescription);
	}
	
	
	private void initApiCall()
	{
		HashMap<String,String> params = new HashMap<>();

		//app.showLog("adfsdfs", app.getUserSecret());

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("id", this.id);
		
		//AppRestClient.post(URLHelper.URL_SINGLE_CALENDAR_EVENT, params, singleCalendarEvent);
		singleCalendarEvent(params);
	}
	
	private void initAction()
	{
		//this.txtEventCatName.setText(data.getEventCategoryName());
		this.txtEventTitle.setText(data.getEventTitle());
		this.txtStartDate.setText(data.getEventDate());
		this.txtEndDate.setText(data.getEventEndDate());
		this.txtDescription.setText(data.getEventDescription());
	}
	
	
	private void singleCalendarEvent(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().singleCalendarEvent(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();


						Wrapper modelContainer = GsonParser.getInstance()
								.parseServerResponse2(response.body());

						if (modelContainer.getStatus().getCode() == 200) {

							JsonObject objEvent = modelContainer.getData().get("events").getAsJsonObject();
							data = gson.fromJson(objEvent.toString(), AcademicCalendarDataItem.class);

							initAction();

						}

						else {

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
	AsyncHttpResponseHandler singleCalendarEvent = new AsyncHttpResponseHandler() {

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
				
				JsonObject objEvent = modelContainer.getData().get("events").getAsJsonObject();
				data = gson.fromJson(objEvent.toString(), AcademicCalendarDataItem.class);
				
				initAction();
				
			}
			
			else {

			}
			
			

		};
	};
	
	
	

}
