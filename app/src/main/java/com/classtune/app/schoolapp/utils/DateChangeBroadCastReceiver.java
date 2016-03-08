package com.classtune.app.schoolapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.classtune.app.R;

public class DateChangeBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, R.string.jaav_datechangebroadcastreceiver_date_changed, Toast.LENGTH_LONG).show();
	}
}
