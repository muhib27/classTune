package com.classtune.app.schoolapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.classtune.app.schoolapp.model.Reminder;

import java.util.HashMap;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ReminderHelper.getInstance().constructReminderFromSharedPreference();
		for (HashMap.Entry<String, Reminder> entry : ReminderHelper.getInstance().reminder_map.entrySet()) {
		    String key = entry.getKey();
		    Reminder rm = entry.getValue();
		    ReminderHelper.getInstance().setReminder(key, rm.getReminderTitle(), rm.getReminderDescription(), rm.getReminderTime(), context);
		}
	}

}
