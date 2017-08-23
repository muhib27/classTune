package com.classtune.app.schoolapp.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.schoolapp.adapters.CalendarAdapter;
import com.classtune.app.schoolapp.model.Absent;
import com.classtune.app.schoolapp.model.AttendenceEvents;
import com.classtune.app.schoolapp.model.CalenderEvent;
import com.classtune.app.schoolapp.model.Holiday;
import com.classtune.app.schoolapp.model.Late;
import com.classtune.app.schoolapp.model.Leave;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.ExpandableGridView;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendenceFragment extends Fragment implements UserAuthListener{

	private String startDate,endDate;
	private UIHelper uiHelper;
	private UserHelper userHelper;
	private int totalClassNumber=0;
	public int totalClass,totalPresent,totalAbsent,totalLeave,totalLate,totalHolidays;
	private HashMap<String, CalenderEvent> eventMap;
	private Map<String, String> msgMap;
	
	
	public void initializeCountValues()
	{
		totalClass=0;
		totalAbsent=0;
		totalLate=0;
		totalPresent=0;
		totalLeave=0;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getParamData();

		if(AppUtility.isInternetConnected() == false){
			Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
		}

		if(AppUtility.isInternetConnected())
			fetchAttendenceData();
		
	}

	private void getAttendenceEvent(HashMap<String,String> params){
		if(!uiHelper.isDialogActive())
			uiHelper.showLoadingDialog(getString(R.string.loading_text));
		else
			uiHelper.updateLoadingDialog(getString(R.string.loading_text));
		ApplicationSingleton.getInstance().getNetworkCallInterface().getAttendenceEvent(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();

						if (response.body() != null){
							Log.e("Response", ""+response.body());
							Wrapper wrapper=GsonParser.getInstance().parseServerResponse2(response.body());
							AttendenceEvents data;
							if(wrapper.getStatus().getCode()==200)
							{
								data=GsonParser.getInstance().parseAttendence(wrapper.getData().toString());

								try {

									msgMap =GsonParser.getInstance().parseMsg(wrapper.getData().getAsJsonObject("msg").toString());
									parseEvents(data);
									adapter.setEvents(eventMap);
									adapter.notifyDataSetChanged();
									totalClassNumber-=totalHolidays;
									updateOverallReport(data.getTotalClass(), (float)data.getTotalClass()-(float)((float)totalLate*0.5)-(float)totalAbsent-(float)totalLeave, totalLeave, totalLate, totalAbsent);
									eventTitleText.setText(data.getCurrent_date());
									eventDescriptionText.setText(data.getCurrent_msg());


								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.e("Test", data.getHolidays().size() + "");
							}
							else if(wrapper.getStatus().getCode()==406)
							{
								userHelper.doLogIn();
							}
						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.dismissLoadingDialog();
					}
				}
		);
	}
	AsyncHttpResponseHandler getAttendenceEventsHandler=new AsyncHttpResponseHandler()
	{

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			//uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
			Log.e("Error", arg1);
		}

		@Override
		public void onStart() {
			super.onStart();
			//initializeCountValues();
			if(!uiHelper.isDialogActive())
				uiHelper.showLoadingDialog(getString(R.string.loading_text));
			else
				uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			uiHelper.dismissLoadingDialog();
			Log.e("Response", responseString);
			Wrapper wrapper=GsonParser.getInstance().parseServerResponse(responseString);
			AttendenceEvents data;
			if(wrapper.getStatus().getCode()==200)
			{
				data=GsonParser.getInstance().parseAttendence(wrapper.getData().toString());
				
				try {
					
					msgMap =GsonParser.getInstance().parseMsg(wrapper.getData().getAsJsonObject("msg").toString());
					parseEvents(data);
					adapter.setEvents(eventMap);
					adapter.notifyDataSetChanged();
					totalClassNumber-=totalHolidays;
					updateOverallReport(data.getTotalClass(), (float)data.getTotalClass()-(float)((float)totalLate*0.5)-(float)totalAbsent-(float)totalLeave, totalLeave, totalLate, totalAbsent);
					eventTitleText.setText(data.getCurrent_date());
					eventDescriptionText.setText(data.getCurrent_msg());
					 
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("Test", data.getHolidays().size() + "");
			}
			else if(wrapper.getStatus().getCode()==406)
			{
				userHelper.doLogIn();
			}
		}
	};
	
	private void parseEvents(AttendenceEvents data) throws ParseException
	{
		
		eventMap.clear();
		List<Absent> abs=data.getAbsents();
		totalAbsent=0;
		for(int i=0;i<abs.size();i++)
		{
			eventMap.put(abs.get(i).getDate(),abs.get(i));
			totalAbsent++;
			Log.e("Abs", totalAbsent + "");
		}
		totalLate=0;
		List<Late> lates=data.getLates();
		for(int i=0;i<lates.size();i++)
		{
			eventMap.put(lates.get(i).getDate(),lates.get(i));
			totalLate++;
			Log.e("Late", totalLate + "");
		}
		totalHolidays=0;
		List<Holiday> holidays=data.getHolidays();
		for(int i=0;i<holidays.size();i++)
		{
			Holiday temp=holidays.get(i);
			GregorianCalendar gcal = new GregorianCalendar();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Date start = sdf.parse(temp.getStartDate());
		    Date end = sdf.parse(temp.getEndDate());
		    gcal.setTime(start);
		    while (!gcal.getTime().after(end)) {
		    	
		        Holiday hd=new Holiday();
		        hd.setTitle(temp.getTitle());
		        hd.setDesc(temp.getDesc());
		        hd.setStartDate(AppUtility.getDate(gcal.getTimeInMillis()));
		        eventMap.put(hd.getStartDate(), hd);
		        gcal.add(Calendar.DAY_OF_YEAR, 1);
		        totalHolidays++;
				Log.e("hoilidays", totalHolidays + "");
		    }
			
		}
		totalLeave=0;
		List<Leave> leaves=data.getLeaves();
		for(int i=0;i<leaves.size();i++)
		{
			Leave tempLeave=leaves.get(i);
			GregorianCalendar gcal = new GregorianCalendar();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Date start = sdf.parse(tempLeave.getStartDate());
		    Date end = sdf.parse(tempLeave.getEndDate());
		    gcal.setTime(start);
		    while (!gcal.getTime().after(end)) {
		        
		    	Leave lv=new Leave();
		    	lv.setTitle(tempLeave.getTitle());
		    	lv.setDesc(tempLeave.getDesc());
		    	lv.setStartDate(AppUtility.getDate(gcal.getTimeInMillis()));
		        
		        eventMap.put(lv.getStartDate(), lv);
		        //System.out.println( gcal.getTime().toString());
		        gcal.add(Calendar.DAY_OF_YEAR, 1);
		        totalLeave++;
				Log.e("leave", totalLeave + "");
		    }
			
		}
	}
	

	private void getParamData() {
		
		initializeCountValues();
		
		GregorianCalendar cal = (GregorianCalendar) month.clone();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		Date lastDayOfMonth = cal.getTime();
		endDate=AppUtility.getDate(lastDayOfMonth.getTime());
		
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		Date firstDayOfMonth = cal.getTime();
		startDate=AppUtility.getDate(firstDayOfMonth.getTime());
		Log.e("StartDate", startDate);
		Log.e("EndDate", endDate);
		
		totalClassNumber=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for(int i=0;i<weekends.size();i++)
		{
			totalClassNumber-=getNumberOfSpecificDaysInaMonth(weekends.get(i)+1, (GregorianCalendar)cal.clone());
		}
		
		Log.e("TotalClass", totalClassNumber + "");
		
	    
	}

	private int getNumberOfSpecificDaysInaMonth(int dayNumber,GregorianCalendar c)
	{
		int count = 0;
	    int maxDayInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
	    for (int d = 1;  d <= maxDayInMonth;  d++) {
	    	c.set(Calendar.DAY_OF_MONTH, d);
	        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
	        if (dayNumber == dayOfWeek) {
	            count++;
	        }
	    }
	    return count;
	}
	

	private void fetchAttendenceData() {
		
		HashMap<String,String> params=new HashMap<>();
		//Log.e("Secret", app.getUserSecret());
		
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.START_DATE, startDate);
		params.put(RequestKeyHelper.END_DATE, endDate);
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {


            if(getActivity().getIntent().getExtras()!=null)
            {
                if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                    params.put(RequestKeyHelper.STUDENT_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                    params.put(RequestKeyHelper.BATCH_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


                    GcmIntentService.initApiCall(rid, rtype);
                }
                else
                {
                    params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                    params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
                }
            }
            else
            {
                params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
            }
			//params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getSelectedChild().getSchoolId());
			//params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			//params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}
		
		Log.e("params attendance", params.toString());
		
		//AppRestClient.post(URLHelper.URL_GET_ATTENDENCE_EVENTS, params,getAttendenceEventsHandler);
		getAttendenceEvent(params);
	}


	public GregorianCalendar month, itemmonth;// calendar instances.
	
	private String[] weekdays ;
	private LinearLayout weekDaysNameContainer;
	
	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	ArrayList<String> event;
	ArrayList<String> date;
	ArrayList<String> desc;
	private View view;
	private TextView title;
	private LinearLayout previous,next;
	List<Integer> weekends;
	private ExpandableGridView gridview;
	
	private TextView totalClassValueText,totalAbsentValueText,totalLateValueText,totalLeaveValueText,totalPresentValueText,eventTitleText,eventDescriptionText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Locale.setDefault(Locale.US);
		uiHelper=new UIHelper(getActivity());
		userHelper=new UserHelper(this, getActivity());
		eventMap=new HashMap<String, CalenderEvent>();

		weekdays = new String[] { getString(R.string.java_attendencefragment_day_sun), getString(R.string.java_attendencefragment_day_mon), getString(R.string.java_attendencefragment_day_tue),
				getString(R.string.java_attendencefragment_day_wed), getString(R.string.java_attendencefragment_day_thu), getString(R.string.java_attendencefragment_day_fri), getString(R.string.java_attendencefragment_day_sat) };
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		populateWeekDaysName();
	}

	private void populateWeekDaysName() {
		
		for(int i=0;i<weekdays.length;i++)
		{
			TextView tv = new TextView(getActivity());
			LayoutParams param=new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,1f);
			//param.setMargins(10, 0, 5, 0);
			tv.setLayoutParams(param);
			tv.setGravity(Gravity.CENTER);
			tv.setText(weekdays[i]);
			for(int j=0;j<weekends.size();j++)
			{
				if(i==weekends.get(j)){
					tv.setTextColor(getResources().getColor(R.color.red));
				}
				
			}
			weekDaysNameContainer.addView(tv);
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.calendar, container,
				false);
		weekDaysNameContainer=(LinearLayout)view.findViewById(R.id.day_name_container);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		items = new ArrayList<String>();
		weekends=new ArrayList<Integer>();
		weekends.add(5);
		weekends.add(6);
		
		adapter = new CalendarAdapter(getActivity(), month,weekends);
		
		gridview = (ExpandableGridView)view.findViewById(R.id.gridview);
		gridview.setExpanded(true);
		gridview.setAdapter(adapter);
		
		/*handler = new Handler();
		handler.post(calendarUpdater);*/

		title = (TextView)view.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		
		previous= (LinearLayout)view.findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
				
			}
		});

		next = (LinearLayout) view.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();
				
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				/*if(v.getTag()!=null)
				{
					CalenderEvent event=(CalenderEvent)v.getTag();
					updateEventDescriptionPanel(event);
				}
				else
				{*/
				
					eventTitleText.setText(adapter.getItem(position));
					if(msgMap!=null)
						eventDescriptionText.setText(msgMap.get(adapter.getItem(position)));
				//}
				
				/*desc = new ArrayList<String>();
				date = new ArrayList<String>();
				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				Log.e("Selected Grid Date", selectedGridDate);
				
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);*/

				

			}

		});
		totalClassValueText=(TextView)view.findViewById(R.id.total_class_value);
		totalPresentValueText=(TextView)view.findViewById(R.id.total_present_value);
		totalAbsentValueText=(TextView)view.findViewById(R.id.total_absent_value);
		totalLeaveValueText=(TextView)view.findViewById(R.id.total_leave_value);
		totalLateValueText=(TextView)view.findViewById(R.id.total_late_value);
		updateOverallReport(0, 0, 0, 0, 0);
		
		eventTitleText=(TextView)view.findViewById(R.id.event_title_text);
		eventDescriptionText=(TextView)view.findViewById(R.id.event_decr_text);
		return view;
	}
	
	
	
	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	private void updateEventDescriptionPanel(CalenderEvent event)
	{
		switch (event.getType()) {
		case HOLIDAY:
			Holiday holiday=(Holiday)event;
			eventTitleText.setText(holiday.getTitle());
			eventDescriptionText.setText(holiday.getDesc());
			break;
		case LEAVE:
			Leave leave=(Leave)event;
			eventTitleText.setText(leave.getTitle());
			eventDescriptionText.setText(leave.getDesc());
			break;
		default:
			break;
		}
	}
	

	public void refreshCalendar() {
		

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		//handler.post(calendarUpdater); // generate some calendar items
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		getParamData();
		updateOverallReport(0, 0, 0, 0, 0);
		fetchAttendenceData();
	}
	
	private void updateOverallReport(int total,float present,int leave,int late,int absent)
	{
		totalClassValueText.setText(total+"");
		totalPresentValueText.setText(present+"");
		totalAbsentValueText.setText(absent+"");
		totalLeaveValueText.setText(leave+"");
		totalLateValueText.setText(late+"");
	}

	@Override
	public void onAuthenticationStart() {
		if(uiHelper.isDialogActive())
			uiHelper.updateLoadingDialog(getActivity().getString(R.string.java_attendencefragment_authenticating));
		else
			uiHelper.updateLoadingDialog(getString(R.string.loading_text));
	}

	@Override
	public void onAuthenticationSuccessful() {
		fetchAttendenceData();
	}

	@Override
	public void onAuthenticationFailed(String msg) {
		
	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}

}
