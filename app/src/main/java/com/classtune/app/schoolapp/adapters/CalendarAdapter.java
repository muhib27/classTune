/*
 * Copyright (C) 2014 Mukesh Y authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.CalenderEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CalendarAdapter extends BaseAdapter {
	private Context mContext;

	private java.util.Calendar month;
	public GregorianCalendar pmonth; // calendar instance for previous month
	/**
	 * calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar pmonthmaxset;
	private GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat df;
	

	private ArrayList<String> items;
	public static List<String> dayString;
	private View previousView;
	private List<Integer> weekends;
	private HashMap<String, CalenderEvent> eventMap;

	private boolean flag;
	
	public enum CalenderEventType
	{
		ABSENT,LATE,HOLIDAY,LEAVE
	}
	
	
	
	public CalendarAdapter(Context c, GregorianCalendar monthCalendar,List<Integer> weekend) {
		CalendarAdapter.dayString = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		this.eventMap=new HashMap<String, CalenderEvent>();
		this.weekends=weekend;
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();
	}

	
	
	
	
	public int getCount() {
		return dayString.size();
	}

	public String getItem(int position) {
		return dayString.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		TextView dayView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);

		}
		dayView = (TextView) v.findViewById(R.id.date);
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		
		
		dayView.setTextColor(mContext.getResources().getColor(R.color.calender_onday_text_color));
		//setting text color of weekends
				for(int i=0;i<weekends.size();i++)
				{
					if(position==weekends.get(i) || (position-weekends.get(i))%7==0)
					{
						dayView.setTextColor(mContext.getResources().getColor(R.color.red));
						
					}
				}
		
		// checking whether the day is in current month or not.
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
			// setting offdays to white color.
			dayView.setTextColor(mContext.getResources().getColor(R.color.calender_offday_text_color));
			dayView.setClickable(false);
			dayView.setFocusable(false);
			
		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			dayView.setTextColor(mContext.getResources().getColor(R.color.calender_offday_text_color));
			dayView.setClickable(false);
			dayView.setFocusable(false);
			
		} 
		else {
			// setting curent month's days in blue color.
			
		}

		
		
		
		if (dayString.get(position).equals(curentDateString)) {
			setSelected(v);
			previousView = v;
		} else {
			v.setBackgroundResource(R.drawable.list_item_background);
		}
		dayView.setText(gridvalue);

		// create date string for comparison
		String date = dayString.get(position);

		if (date.length() == 1) {
			date = "0" + date;
		}
		String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}

		if(eventMap.containsKey(dayString.get(position)))
		{
			CalenderEvent event=eventMap.get(dayString.get(position));
			switch (event.getType()) {
			case ABSENT:
				dayView.setTextColor(mContext.getResources().getColor(R.color.white));
				v.setBackgroundResource(R.drawable.calender_grid_item_orange);
				v.setTag(null);
				break;
			case LEAVE:
				dayView.setTextColor(mContext.getResources().getColor(R.color.white));
				v.setBackgroundResource(R.drawable.calender_grid_item_blue);
				v.setTag(event);
				break;
			case HOLIDAY:
				//v.setBackgroundResource(R.drawable.calender_grid_item_orange);
				dayView.setTextColor(mContext.getResources().getColor(R.color.red));
				v.setTag(event);
				break;
			case LATE:
				dayView.setTextColor(mContext.getResources().getColor(R.color.white));
				v.setBackgroundResource(R.drawable.calender_grid_item_yellow);
				v.setTag(null);
				break;
			default:
				v.setTag(null);
				break;
			}
		}
		
		return v;
	}

	public View setSelected(View view) {
		if (previousView != null) {
			previousView.setBackgroundResource(R.drawable.list_item_background);
		}
		previousView = view;
		view.setBackgroundResource(R.drawable.calender_grid_item_bg);
		
		return view;
	}

	public void refreshDays() {
		// clear items
		
		eventMap.clear();
		dayString.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}
	
	public String getStartDate()
	{
		return dayString.get(0);
	}
	
	public String getEndDate()
	{
		return dayString.get(dayString.size()-1);
	}


	public void setEvents(HashMap<String, CalenderEvent> map) {
		eventMap.clear();
		this.eventMap.putAll(map);
	}
	
	

}