package com.classtune.app.schoolapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.classtune.app.freeversion.CreateMeetingRequest;
import com.classtune.app.freeversion.SingleMeetingRequestActivity;
import com.classtune.app.R;
import com.classtune.app.schoolapp.fragments.DatePickerFragment.DatePickerOnSetDateListener;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.MeetingStatus;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.CustomDateTimePicker;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.PopupDialog;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogMeetingStatus;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogMeetingStatus.PopupOkButtonClickListener;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingFragment extends Fragment implements UserAuthListener{
	
	
	
	
	private UIHelper uiHelper;
	private View view;
	
	private List<MeetingStatus> listStatus = new ArrayList<MeetingStatus>();
	
	//private LinearLayout layoutListHolder;
	
	
	private MeetingStatusAdapter adapter;
	
	private PullToRefreshListView listViewStatus;
	
	private Button btnCreateRequest;
	
	private RadioButton radioIncoming;
	private RadioButton radioOutgoing;
	private String selectedType = "1";
	
	//private TextView txtSelectDateFrom;
	//private TextView txtSelectDateTo;
	//private ImageButton btnSelectDateFrom;
	//private ImageButton btnSelectDateTo;
	//private Button btnCreateFilter;
	
	//private String startDate = "";
	//private String endDate = "";
	//private String currentDate = "";
	//private boolean isFromSelected = false;

	
	private boolean hasNext = false;
	private int pageNumber = 1;
	private ProgressBar progressBar;
	private UserHelper userHelper;
	private boolean isRefreshing = false;
	private boolean loading = false;
	private boolean stopLoadingData = false;

	private TextView txtMessage;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		userHelper = new UserHelper(getActivity());
		
	}
	
	
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		
		
		initApiCall(pageNumber, selectedType, null, null);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		uiHelper = new UIHelper(this.getActivity());

		view = inflater.inflate(R.layout.fragment_meeting2, container, false);

		
		//dummy data for list checking
		
		
		
		initView(view);		
		initAction();

		txtMessage = (TextView)view.findViewById(R.id.txtMessage);


		return view;
	}
	
	
	
	
	private void initView(View view)
	{
		//this.layoutListHolder = (LinearLayout)view.findViewById(R.id.layoutListHolder);
		
		this.listViewStatus = (PullToRefreshListView)view.findViewById(R.id.listViewStatus);
		/*this.adapter = new MeetingStatusAdapter();
		this.listViewStatus.setAdapter(this.adapter);
		this.adapter.notifyDataSetChanged();*/
		setUpList();
		
		this.btnCreateRequest = (Button)view.findViewById(R.id.btnCreateRequest);
		
		
		this.progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
		
		
		this.radioIncoming = (RadioButton)view.findViewById(R.id.radioIncoming);
		this.radioOutgoing = (RadioButton)view.findViewById(R.id.radioOutgoing);
		
		/*this.txtSelectDateFrom = (TextView)view.findViewById(R.id.txtSelectDateFrom);
		this.txtSelectDateTo = (TextView)view.findViewById(R.id.txtSelectDateTo);
		
		this.btnSelectDateFrom = (ImageButton)view.findViewById(R.id.btnSelectDateFrom);
		this.btnSelectDateTo = (ImageButton)view.findViewById(R.id.btnSelectDateTo);
		
		this.btnCreateFilter = (Button)view.findViewById(R.id.btnCreateFilter);*/
	}
	
	private void initAction()
	{
		/*for(int i=0;i<listStatus.size();i++)
		{
			populateRowData(i, listStatus.get(i), this.layoutListHolder);
		}*/
		
		this.btnCreateRequest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MeetingFragment.this.getActivity(), CreateMeetingRequest.class);
				startActivityForResult(intent, 75);
			}
		});
		
		
		if(this.radioIncoming.isChecked())
		{
			this.selectedType = "1";
		}
		if(this.radioOutgoing.isChecked())
		{
			this.selectedType = "2";
		}
		
		
		this.radioIncoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					selectedType = "1";
					initializePageing();
					initApiCall(pageNumber, selectedType, null, null);
				}
			}
		});
		
		
		this.radioOutgoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					selectedType = "2";
					initializePageing();
					initApiCall(pageNumber, selectedType, null, null);
				}
			}
		});
		
		
		
		/*this.btnSelectDateFrom.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatepicker();
				isFromSelected = true;
				
				
			}
		});
		
		
		this.btnSelectDateTo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatepicker();
				isFromSelected = false;
				
				
			}
		});*/
		
		
		
		/*this.btnCreateFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				initApiCall(pageNumber, selectedType, startDate, endDate);
				
			}
		});*/
		
		
		
		
		listViewStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				MeetingStatus data = (MeetingStatus)adapter.getItem(position-1);
				Intent intent = new Intent(getActivity(), SingleMeetingRequestActivity.class);
				intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, data.getId());
				startActivityForResult(intent, 45);
				
				
			}
		});
		
		
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 75)
		{
			initializePageing();
			initApiCall(pageNumber, selectedType, null, null);
		}
		
		if(requestCode == 45)
		{
			initializePageing();
			initApiCall(pageNumber, selectedType, null, null);
		}
		
	}
	
	
	private void setUpList() {

		initializePageing();
		listViewStatus.setMode(Mode.PULL_FROM_END);
		// Set a listener to be invoked when the list should be refreshed.
		listViewStatus.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				Mode m = listViewStatus.getCurrentMode();
				if (m == Mode.PULL_FROM_START) {
					stopLoadingData = false;
					isRefreshing = true;
					pageNumber = 1;
					loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
					initApiCall(pageNumber, selectedType, null, null);
				} else if (!stopLoadingData) {
					pageNumber++;
					loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
					initApiCall(pageNumber, selectedType, null, null);
					
				} else {
					new NoDataTask().execute();
				}
			}
		});

		
		
		this.adapter = new MeetingStatusAdapter();
		this.adapter.clearList();
		this.listViewStatus.setAdapter(adapter);
	}
	
	
	private void initApiCall(int pageNumber, String type, String startDate, String endDate) {

		RequestParams params = new RequestParams();

		params.put("page_size", "10");
		params.put("page_number", String.valueOf(pageNumber));
		
        Log.e("Meeting", String.valueOf(pageNumber));

		if(type != null)
			params.put("type", type);
		
		if(startDate != null)
			params.put("start_date", startDate);
		
		if(endDate != null)
			params.put("end_date", endDate);
		
		
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
		{
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
			
			Log.e("STU_ID", userHelper.getUser().getSelectedChild().getId());
		}
		
		
		
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			
		

		AppRestClient.post(URLHelper.URL_MEETING_REQUEST, params,
				meetingRequestHandler);
	}
	
	private void initApiCallStatus(String meetingId, String status) {

		RequestParams params = new RequestParams();

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("meeting_id", meetingId);
		params.put("status", status);
		
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
		{
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}
		
		
		
		Log.e("STAUSSSS", "meeting_id: " + meetingId);
		Log.e("STAUSSSS", "status: " + status);
		
		AppRestClient.post(URLHelper.URL_MEETING_STATUS, params,
				meetingStatusHandler);
	}
	
	private AsyncHttpResponseHandler meetingStatusHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			// uiHelper.showMessage(arg1);
			// uiHelper.dismissLoadingDialog();

		};

		@Override
		public void onStart() {
			// uiHelper.showLoadingDialog("Please wait...");

			
		};

		@Override
		public void onSuccess(String responseString) {
			
			

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			Log.e("RES", "is: " + responseString);
			

			if (modelContainer.getStatus().getCode() == 200) {
				
				initApiCall(pageNumber, selectedType, null, null);
				
				
			}

			else {

			}
		};
	};
	
	
	private void initializePageing() {
		pageNumber = 1;
		isRefreshing = false;
		loading = false;
		stopLoadingData = false;
	}
	
	
	private class NoDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			listViewStatus.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	
	
	
	private AsyncHttpResponseHandler meetingRequestHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			// uiHelper.showMessage(arg1);
			// uiHelper.dismissLoadingDialog();

		};

		@Override
		public void onStart() {
			// uiHelper.showLoadingDialog("Please wait...");

			if (pageNumber == 1) {
				progressBar.setVisibility(View.VISIBLE);
			} else {

			}
		};

		@Override
		public void onSuccess(String responseString) {
			
			progressBar.setVisibility(View.GONE);

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			hasNext = modelContainer.getData().get("has_next").getAsBoolean();
			Log.e("HAS_NEXT_MEETING", "is: " + hasNext);
			
			
			if (pageNumber == 1)
			{
				adapter.clearList();
			}
				
			if (!hasNext) 
			{
				stopLoadingData = true;
			}

			if (modelContainer.getStatus().getCode() == 200) {

				
				//do parsing
				JsonArray array = modelContainer.getData().get("meetings").getAsJsonArray();
				
				for (int i = 0; i < parseMeetingStatus(array.toString()).size(); i++) 
				{
					listStatus.add(parseMeetingStatus(array.toString()).get(i));
				}
				
				for(int i=0;i<listStatus.size();i++)
					Log.e("MEET_POS", listStatus.get(i).getName());
				
				if (pageNumber != 0 || isRefreshing) 
				{
					listViewStatus.onRefreshComplete();
					loading = false;
				}
				
				
				adapter.notifyDataSetChanged();
				
				/*if(listStatus.size() <= 0 && selectedType.equalsIgnoreCase("1"))
				{
					Toast.makeText(MeetingFragment.this.getActivity(), "No incoming meeting request", Toast.LENGTH_SHORT).show();
				}
				
				else if(listStatus.size() <= 0 && selectedType.equalsIgnoreCase("2"))
				{
					Toast.makeText(MeetingFragment.this.getActivity(), "No outgoing meeting request", Toast.LENGTH_SHORT).show();
				}*/

				if(listStatus.size() <= 0)
				{
					txtMessage.setVisibility(View.VISIBLE);
				}
				else
				{
					txtMessage.setVisibility(View.GONE);
				}

			}

			else {

			}
		};
	};
	
	/*private void populateRowData(int position, MeetingStatus data, LinearLayout layout)
	{
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View row = inflater.inflate(R.layout.row_meeting_status, null, false);
		
		TextView txtPosition = (TextView)row.findViewById(R.id.txtPosition);
		TextView txtName = (TextView)row.findViewById(R.id.txtName);
		TextView txtDate = (TextView)row.findViewById(R.id.txtDate);
		TextView txtTime = (TextView)row.findViewById(R.id.txtTime);
		TextView txtStatus = (TextView)row.findViewById(R.id.txtStatus);
		LinearLayout layoutStatus = (LinearLayout)row.findViewById(R.id.layoutStatus);
		
		
		txtPosition.setText(String.valueOf(position+1)+". ");
		txtName.setText(data.getName());
		txtDate.setText(data.getDate());
		txtTime.setText(data.getTime());
		//holder.txtStatus.setText(listStatus.get(position).getStatus());
		
		
		if(data.getStatus().equalsIgnoreCase("0"))
		{
			txtStatus.setText("Pending");
			txtStatus.setTextColor(Color.BLACK);
			layoutStatus.setBackgroundColor(Color.parseColor("#e7ecee"));
		}
		else if(data.getStatus().equalsIgnoreCase("1"))
		{
			txtStatus.setText("Done");
			txtStatus.setTextColor(Color.WHITE);
			layoutStatus.setBackgroundColor(Color.parseColor("#4f9611"));
		}
		else if(data.getStatus().equalsIgnoreCase("2"))
		{
			txtStatus.setText("Declined");
			txtStatus.setTextColor(Color.RED);
			layoutStatus.setBackgroundColor(Color.parseColor("#231f20"));
		}
		else
		{
			txtStatus.setText("-----");
			//holder.layoutStatus.setBackgroundColor(Color.parseColor("#231f20"));
		}
		
		layout.addView(row);
	}*/
	
	
	private void showDatepicker() {
		DatePickerFragment picker = new DatePickerFragment();
		picker.setCallbacks(datePickerCallback);
		picker.show(getFragmentManager(), "datePicker");
		
		
		
	}

	DatePickerOnSetDateListener datePickerCallback=new DatePickerOnSetDateListener() {

		@Override
		public void onDateSelected(int month, String monthName, int day,
				int year, String dateFormatServer, String dateFormatApp,
				Date date) {
			// TODO Auto-generated method stub
			
		}
		
		/*@Override
		public void onDateSelected(String monthName, int day, int year) {
			// TODO Auto-generated method stub
			
			//Log.e("DATA_ALL", "Y: "+year+" M: "+monthName+" D: "+day);
			
			currentDate = getDateString(monthName, day, year);
			
			if(isFromSelected == true)
			{
				txtSelectDateFrom.setText(currentDate);
				startDate = currentDate;
			}
			else
			{
				txtSelectDateTo.setText(currentDate);
				endDate = currentDate;
			}
			
			
			//Log.e("DATA_ALL_APPEND", "is: "+currentDate);
			
		}*/
	};
	
	
	/*private String getDateString(String monthName, int day, int year)
	{
		String date = "";
		
		String month = "";
		
		
		if(monthName.equalsIgnoreCase("January"))
		{
			month = "01";
		}
		else if(monthName.equalsIgnoreCase("February"))
		{
			month = "02";
		}
		else if(monthName.equalsIgnoreCase("March"))
		{
			month = "03";
		}
		else if(monthName.equalsIgnoreCase("April"))
		{
			month = "04";
		}
		else if(monthName.equalsIgnoreCase("May"))
		{
			month = "05";
		}
		else if(monthName.equalsIgnoreCase("June"))
		{
			month = "06";
		}
		else if(monthName.equalsIgnoreCase("July"))
		{
			month = "07";
		}
		else if(monthName.equalsIgnoreCase("August"))
		{
			month = "08";
		}
		else if(monthName.equalsIgnoreCase("September"))
		{
			month = "09";
		}
		else if(monthName.equalsIgnoreCase("October"))
		{
			month = "10";
		}
		else if(monthName.equalsIgnoreCase("November"))
		{
			month = "11";
		}
		else if(monthName.equalsIgnoreCase("December"))
		{
			month = "12";
		}
		
		date = year+"-"+month+"-"+day;
		
		return date;
	}*/
	
	
	private void showshowshowCustomDialogStatus(final String meetingId, String headerText, int imgResId,
			String descriptionText) {

		PopupDialogMeetingStatus picker = PopupDialogMeetingStatus.newInstance(0);
		picker.setData(headerText, descriptionText, imgResId, getActivity());
		
		
		picker.setCallBack(new PopupOkButtonClickListener() {
			
			@Override
			public void onOkButtonClicked(String status) {
				// TODO Auto-generated method stub
				Log.e("STATUS", "is: " + status);
				
				if(status.equalsIgnoreCase("accepted"))
				{
					initApiCallStatus(meetingId, "1");
				}
				
				else if(status.equalsIgnoreCase("declined"))
				{
					initApiCallStatus(meetingId, "2");
				}
				
				
			}
		});
		
		picker.show(getChildFragmentManager(), null);
	}
	
	
	private void showDateTimePicker() {
		// TODO Auto-generated method stub
		CustomDateTimePicker custom = new CustomDateTimePicker(getActivity(),
				new CustomDateTimePicker.ICustomDateTimeListener() {

					@Override
					public void onCancel() {

					}

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {
						// TODO Auto-generated method stub


						Log.e("DATE", "Y: " + year + " M: " + monthFullName + " D: " + date + " H: " + hour12 + " Min: " + min + " S: " + sec + " AM/PM: " + AM_PM);
					}
				});
		/**
		 * Pass Directly current time format it will return AM and PM if you set
		 * false
		 */
		custom.set24HourFormat(false);
		/**
		 * Pass Directly current data and time to show when it pop up
		 */
		custom.setDate(Calendar.getInstance());
		custom.showDialog();
	}
	
	
	
	public ArrayList<MeetingStatus> parseMeetingStatus(String object) {
		ArrayList<MeetingStatus> data = new ArrayList<MeetingStatus>();
		data = new Gson().fromJson(object,
				new TypeToken<ArrayList<MeetingStatus>>() {
				}.getType());
		return data;
	}
	
	
	
	public class MeetingStatusAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listStatus.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listStatus.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void clearList() {
			listStatus.clear();
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = LayoutInflater.from(MeetingFragment.this.getActivity()).inflate(R.layout.row_meeting_status2, parent, false);

				holder.txtPosition = (TextView)convertView.findViewById(R.id.txtPosition);
				holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
				holder.txtBatch = (TextView)convertView.findViewById(R.id.txtBatch);
				holder.txtDate = (TextView)convertView.findViewById(R.id.txtDate);
				holder.txtTime = (TextView)convertView.findViewById(R.id.txtTime);
				holder.txtStatus = (TextView)convertView.findViewById(R.id.txtStatus);
				holder.layoutStatus = (LinearLayout)convertView.findViewById(R.id.layoutStatus);
				
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.txtPosition.setText(String.valueOf(position + 1)+". ");
			holder.txtName.setText(listStatus.get(position).getName());
			
			if(!TextUtils.isEmpty(listStatus.get(position).getBatch()))
			{
				holder.txtBatch.setVisibility(View.VISIBLE);
				holder.txtBatch.setText(listStatus.get(position).getBatch());
			}
			else
			{
				holder.txtBatch.setVisibility(View.GONE);
			}
			//holder.txtDate.setText(listStatus.get(position).getDate());
			//holder.txtTime.setText(listStatus.get(position).getTime());
			//holder.txtStatus.setText(listStatus.get(position).getStatus());
			
			String[] separated = listStatus.get(position).getDate().split("\\s+");
			holder.txtDate.setText(separated[0]);
            //holder.txtTime.setText(separated[1]);

            String time = separated[1];

            if (time.length() > 0 ) {
                time = time.substring(0, time.length()-3);
            }
            holder.txtTime.setText(time);


			
			
			
			holder.layoutStatus.setTag(position);
			
			
			if(listStatus.get(position).getTimeOver() == 1)
			{
				holder.txtStatus.setText("Time Over");
				holder.txtStatus.setTextColor(Color.BLACK);
				holder.layoutStatus.setBackgroundColor(Color.parseColor("#fff200"));
			}
			else
			{
				if(listStatus.get(position).getStatus().equalsIgnoreCase("0"))
				{
					holder.txtStatus.setText("Pending");
					holder.txtStatus.setTextColor(Color.BLACK);
					holder.layoutStatus.setBackgroundColor(Color.parseColor("#e7ecee"));
				}
				else if(listStatus.get(position).getStatus().equalsIgnoreCase("1"))
				{
					//holder.txtStatus.setText("Done");
					holder.txtStatus.setText("Accepted");
					holder.txtStatus.setTextColor(Color.WHITE);
					holder.layoutStatus.setBackgroundColor(Color.parseColor("#4f9611"));
				}
				else if(listStatus.get(position).getStatus().equalsIgnoreCase("2"))
				{
					holder.txtStatus.setText("Declined");
					holder.txtStatus.setTextColor(Color.WHITE);
					holder.layoutStatus.setBackgroundColor(Color.parseColor("#d71921"));
				}
				else
				{
					holder.txtStatus.setText("-----");
					//holder.layoutStatus.setBackgroundColor(Color.parseColor("#231f20"));
				}
				
				if(listStatus.get(position).getTimeOver() == 1)
				{
					holder.txtStatus.setText("Time Over");
					holder.txtStatus.setTextColor(Color.BLACK);
					holder.layoutStatus.setBackgroundColor(Color.parseColor("#fff200"));
				}
			}
			
			
			
			
			holder.layoutStatus.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(listStatus.get((Integer) ((LinearLayout)v).getTag()).getTimeOver() == 0 && selectedType.equalsIgnoreCase("1"))
						showCustomDialogStatus(listStatus.get((Integer) ((LinearLayout)v).getTag()).getId(), "MEETING STATUS", R.drawable.icon_meeting_request_status, "You have pending meeting request");
					
				}
			});
		
			
			
			return convertView;
		}

	}

	public void showCustomDialog(String headerText, int imgResId,
			String descriptionText) {

		PopupDialog picker = PopupDialog.newInstance(0);
		picker.setData(headerText, descriptionText, imgResId, getActivity());
		picker.show(getChildFragmentManager(), null);
	}

	class ViewHolder {
		
		TextView txtPosition;
		TextView txtName;
		TextView txtBatch;
		TextView txtDate;
		TextView txtTime;
		TextView txtStatus;
		LinearLayout layoutStatus;
		
	}
	
	
	private void showCustomDialogStatus(final String meetingId, String headerText, int imgResId,
			   String descriptionText) {

			  PopupDialogMeetingStatus picker = PopupDialogMeetingStatus.newInstance(0);
			  picker.setData(headerText, descriptionText, imgResId, getActivity());
			  
			  
			  picker.setCallBack(new PopupOkButtonClickListener() {
			   
			   @Override
			   public void onOkButtonClicked(String status) {
			    // TODO Auto-generated method stub
			    Log.e("STATUS", "is: " + status);
			    
			    if(status.equalsIgnoreCase("accepted"))
			    {
			     initApiCallStatus(meetingId, "1");
			    }
			    
			    else if(status.equalsIgnoreCase("declined"))
			    {
			     initApiCallStatus(meetingId, "2");
			    }
			    
			    
			   }
			  });
			  
			  picker.show(getChildFragmentManager(), null);
			 }


	@Override
	public void onAuthenticationStart() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAuthenticationSuccessful() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}


	
	
	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case TEACHER_BATCH:
				Batch selectedBatch=(Batch)item;
				Log.e("BATCH", "name: " + selectedBatch.getName());
				Log.e("BATCH", "id: " + selectedBatch.getId());
			
				break;
			default:
				break;
			}

		}
	};



}
