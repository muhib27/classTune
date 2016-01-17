package com.classtune.app.schoolapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.classtune.app.schoolapp.model.Reminder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReminderHelper {

	public static ReminderHelper instance;
	public HashMap<String, Reminder> reminder_map;
	
	public static ReminderHelper getInstance(){
		if(instance==null) {
			instance = new ReminderHelper();
		}
		if(instance.reminder_map==null)
			instance.constructReminderFromSharedPreference();
		return instance;
	}
	
	public void constructReminderFromSharedPreference(){
		String json = SharedPreferencesHelper.getInstance().getString(AppConstant.REMINDER_KEY, "");
		if(!json.equals("")){
			reminder_map = new Gson().fromJson(json, new TypeToken<HashMap<String, Reminder>>(){
			}.getType());
		} else {
			reminder_map = new HashMap<String, Reminder>();
		}
		
	}
	 
	public void setReminder(String key, String title, String description, String timeString, Context context) {
		Reminder reminder = new Reminder();
		reminder.setReminderTime(title);
		reminder.setReminderDescription(description);
		reminder.setReminderTime(timeString);
		reminder_map.put(key, reminder);
		
		//Setting Reminder Notification
		Intent alarmIntent = new Intent(context, MyAlarm.class);
		alarmIntent.putExtra(AppConstant.REMINDER_TEXT, description);
		alarmIntent.putExtra(AppConstant.REMINDER_TITLE, title);
		alarmIntent.putExtra(AppConstant.REMINDER_TIME, timeString);
	    long scTime = getTimeDiffInMilliSec(timeString);// 10 minutes
	    Log.e("SC TIME OF OVI VAI", " " + scTime);
	    if(scTime>0) {
	    	Log.e("SC TIME OF OVI VAI", " " + scTime);
	    	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), alarmIntent, PendingIntent.FLAG_ONE_SHOT);
		    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
	    }
	    
	    saveMap();
	}
	
	
	
	private void saveMap()
	{
		String value = new Gson().toJson(reminder_map);
		
		SharedPreferencesHelper.getInstance().setString(AppConstant.REMINDER_KEY, value);
	}
	
	 private long getTimeDiffInMilliSec(String dtStart) {
         //SimpleDateFormat  format = new SimpleDateFormat("EEE, dd MMM, yyyy HH:mm a");
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

         Date date = null;
         try {
             date = format.parse(dtStart);
         } catch (java.text.ParseException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }

	  Calendar c = Calendar.getInstance();
	  long ms = c.getTimeInMillis();

      String ss = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss",c.getTime()).toString();
         Date d2=null;
         try {
             d2 = format.parse(ss);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         String sm = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss",date).toString();
	  if(date.compareTo(d2) > 0)
	  {
	   return (date.getTime() - d2.getTime()); // -1 is for invalid date i.e input date is greater than current date
	  }
	  else
	   return -1;
	 }


         /*Date currentDate = new Date();

         long diff =  date.getTime() - currentDate.getTime();

         if (diff < 0) {
             return -1;
         } else {
             return diff;
         }*/



}
