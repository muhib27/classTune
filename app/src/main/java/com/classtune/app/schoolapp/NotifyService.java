package com.classtune.app.schoolapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public class NotifyService extends Service {

	final static String ACTION = "NotifyServiceAction";
	final static String STOP_SERVICE = "";
	final static int RQS_STOP_SERVICE = 1;

	NotifyServiceReceiver notifyServiceReceiver;

	private static final int MY_NOTIFICATION_ID = 1;
	private NotificationManager notificationManager;
	private Notification myNotification;
	//private final String myBlog = "http://android-er.blogspot.com/";
	
	private String postId = "";
	
	private boolean canPlay = false;
	
	private boolean canPlayRoot = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		notifyServiceReceiver = new NotifyServiceReceiver();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		/*IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);

		//postId = intent.getStringExtra("POST_ID");
		postId = SharedPreferencesHelper.getInstance().getString("not_post_id", "");
		Log.e("PID_NOTIFY", "postId: " + postId);
		// Send Notification
		
	
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		myNotification = new Notification(R.drawable.icon, "Champs21",
				System.currentTimeMillis());
		Context context = getApplicationContext();
		String notificationTitle = "Quiz!";
		String notificationText = "One of the quizes has beed activated now for you!";
		
		
		 Intent myIntent=new Intent(this, SingleItemShowActivity.class);
		 
		 Bundle bundle = new Bundle();
		 bundle.putString(AppConstant.ITEM_ID, postId);
		 myIntent.putExtras(bundle);
		 
		 *//*intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	        	    Intent.FLAG_ACTIVITY_NEW_TASK);*//*
	     
	     

		*//*PendingIntent pendingIntent = PendingIntent.getActivity(
				getBaseContext(), 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		myNotification.defaults |= Notification.DEFAULT_SOUND;
		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		myNotification.setLatestEventInfo(context, notificationTitle,
				notificationText, pendingIntent);
		notificationManager.notify(MY_NOTIFICATION_ID, myNotification);*//*


		PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent, 0);

		Notification.Builder builder = new Notification.Builder(getBaseContext());
		builder.setAutoCancel(true);
		//builder.setTicker("this is ticker text");
		builder.setContentTitle(notificationTitle);
		builder.setContentText(notificationText);
		builder.setSmallIcon(R.drawable.icon);
		builder.setContentIntent(pendingIntent);
		builder.setOngoing(true);

		builder.build();

		myNotification = builder.getNotification();
		notificationManager.notify(MY_NOTIFICATION_ID, myNotification);*/



		return super.onStartCommand(intent, flags, startId);
	}
	
	
	

	private boolean isNetworkConnected() 
	{
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) 
		  {
			  // There are no active networks.
			  return false;
		  } 
		  else
		   return true;
			  
	}

	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(notifyServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}