package com.classtune.app.schoolapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.classtune.app.R;

public class MyAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e("Notification PAISE", "HAHAHAHAHAHHAHA");
		String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        int icon = R.drawable.eye_gray;
        CharSequence tickerText = context.getString(R.string.java_myalarm_new_invite);
        long when = System.currentTimeMillis();

//        Notification notification = new Notification(icon, tickerText, when);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.defaults |= Notification.DEFAULT_VIBRATE;

        Context context1 = context.getApplicationContext();
        CharSequence contentTitle = intent.getExtras().getString(AppConstant.REMINDER_TITLE);
        CharSequence contentText = intent.getExtras().getString(AppConstant.REMINDER_TEXT);
        String contentTime = intent.getExtras().getString(AppConstant.REMINDER_TIME);
//        Intent notificationIntent = new Intent(this, null);
        
        //removeReminderFromMap(contentTime);
        Bundle partyBundle = new Bundle();

        //PendingIntent contentIntent = PendingIntent.getActivity(this, SOME_ID, notificationIntent, 0);

       // notification.setLatestEventInfo(context, contentTitle, contentText, null);

        int NOTIFICATION_ID = (int) System.currentTimeMillis();

//        Log.d("NOTIFICATION_ID", "" + NOTIFICATION_ID);
//        mNotificationManager.notify(NOTIFICATION_ID, notification);
        Notification noti;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        /*if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB){

             noti = new Notification(icon, tickerText, when);
            noti.flags |= Notification.FLAG_AUTO_CANCEL;
            noti.defaults |= Notification.DEFAULT_VIBRATE;

            noti.setLatestEventInfo(context, contentTitle, contentText, null);


            mNotificationManager.notify(NOTIFICATION_ID, noti);
        } 
        else
        {*/
        	NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context);
            noti = builder.setContentIntent(null)
                    .setSmallIcon(icon).setTicker(tickerText).setWhen(when)
                    .setAutoCancel(true).setContentTitle(contentTitle)
                    .setContentText(contentText).build();

            mNotificationManager.notify(NOTIFICATION_ID, noti);
        //}
        
        
        
        try {
            Uri notificationRing = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notificationRing);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	private void removeReminderFromMap(String contentTime) {
		// TODO Auto-generated method stub
		ReminderHelper.getInstance().reminder_map.remove(contentTime);
	}

}
