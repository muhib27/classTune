package com.classtune.app.schoolapp;

/*
 * Copyright (C) 2013 The Android Open Source Project
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


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.classtune.app.R;
import com.classtune.app.freeversion.AnyFragmentLoadActivity;
import com.classtune.app.freeversion.SingleExamRoutine;
import com.classtune.app.freeversion.SingleHomeworkActivity;
import com.classtune.app.freeversion.SingleMeetingRequestActivity;
import com.classtune.app.freeversion.SingleNoticeActivity;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SharedPreferencesHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    private static Context context;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString(),"101");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString(),"101");
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                /*// This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }*/
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.


                for (String key: extras.keySet())
                {
                    Log.e("myApplication", key + " ---is a key in the bundle");
                    Log.e("DDD", "d:" + extras.getString(key));
                }

                Log.e("GCS_DATA", "is: " + extras.getString("message"));


                if(extras.getString("key") != null)
                {
                    /*if(extras.getString("key").equals("news"))
                        sendNotification(extras.getString("message"),extras.getString("post_id"));*/

                    if(extras.getString("key").equals("paid") && UserHelper.isLoggedIn())
                    {
                        sendNotificationPaid(extras, extras.getString("message"), extras.getString("rtype"), extras.getString("rid"));
                    }


                    Log.e("TOTAL_UNREAD", "is: " + extras.getString("total_unread"));

                    context = this;
                    UserHelper userHelper = new UserHelper(this);

                    if(userHelper.getUser().getAccessType() == UserHelper.UserAccessType.PAID && extras.getString("key").equals("paid"))
                    {

                        userHelper.saveTotalUnreadNotification(extras.getString("total_unread"));

                        if(listener != null)
                            listener.onNotificationCountChanged(Integer.parseInt(extras.getString("total_unread")));

                    /*if(!TextUtils.isEmpty(extras.getString("total_unread")))
                        listener.onNotificationCountChanged(Integer.parseInt(extras.getString("total_unread")));
                    else
                        listener.onNotificationCountChanged(Integer.parseInt(SharedPreferencesHelper.getInstance().getString(
                                SPKeyHelper.TOTAL_UNREAD_NOTIFICATION_FREE, "")));*/

                    }

                    //SharedPreferencesHelper.getInstance().setString("total_unread", extras.getString("total_unread"));

                    //listener.onNotificationCountChanged(100);
                    //SharedPreferencesHelper.getInstance().setString("total_unread", "100");



                    //SharedPreferencesHelper.getInstance().setString("total_unread", extras.getString("total_unread"));


                }





                
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    /*private void sendNotification(String msg,String postId) {
    	Log.e("PostID Notification", postId);
    	
    	
    	NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent=new Intent(getApplicationContext(), SingleItemShowActivity.class);
        intent.putExtra(AppConstant.ITEM_ID, postId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
        	    Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.icon)
        .setContentTitle("Champs21")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        //mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
    }*/

    private void sendNotificationPaid(Bundle extras, String msg, String rType,String rId) {
       // Log.e("PostID Notification", postId);


        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = invokeClassesPaidNotification(extras, rType, rId);//new Intent(getApplicationContext(), SingleItemShowActivity.class);
        //intent.putExtra(AppConstant.ITEM_ID, postId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);

        //mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());




    }





    private Intent invokeClassesPaidNotification(Bundle extras, String rType, String rId)
    {
        int type = Integer.parseInt(rType);
        Intent intent = null;

        switch (type) {


            case 0:
                intent = new Intent(this, NotificationActivity.class);
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 1:

                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "ParentEventFragment");
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 2:

                intent = new Intent(this, SingleExamRoutine.class);
                intent.putExtra(AppConstant.ID_SINGLE_CALENDAR_EVENT, rId);
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 3:

                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "ParentReportCardFragment");
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;


            case 4:
                intent = new Intent(this, SingleHomeworkActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_HOMEWORK, rId);
                intent.putExtra("total_unread_extras", extras);


               //initApiCall(extras.getString("rid"), rType);

                break;

            case 5:
                intent = new Intent(this, SingleNoticeActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_NOTICE, rId);
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 6:
                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "ParentAttendenceFragment");
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 7:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivity(intent);*/

                break;

            case 8:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivityForResult(intent, REQUEST_REMINDER);*/

                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "MyLeaveFragment");
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 9:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivityForResult(intent, REQUEST_REMINDER);*/

                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "StudentLeaveFragment");
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 10:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivityForResult(intent, REQUEST_REMINDER);*/

                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "MyLeaveFragment");
                intent.putExtra("total_unread_extras", extras);


                //initApiCall(extras.getString("rid"), rType);

                break;

            case 11:
                intent = new Intent(this, SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rId);
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 12:
                intent = new Intent(this, SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rId);
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            case 13:
                intent = new Intent(this, SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rId);
                intent.putExtra("total_unread_extras", extras);


                //initApiCall(extras.getString("rid"), rType);

                break;

            case 14:
                intent = new Intent(this, SingleMeetingRequestActivity.class);
                intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, rId);
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);


                break;

            case 15:
                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "QuizFragment");
                intent.putExtra("total_unread_extras", extras);

                //initApiCall(extras.getString("rid"), rType);

                break;

            default:



                break;
        }



        return  intent;
    }


    public static void initApiCall(String rId, String rTtype)
    {

        RequestParams params = new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());


        if(rId != null)
        {
            params.put("rid", rId);
        }

        if(rTtype != null)
        {
            params.put("rtype", rTtype);
        }



        AppRestClient.post(URLHelper.URL_EVENT_REMINDER, params, reminderHandler);

    }


    static AsyncHttpResponseHandler reminderHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(Throwable arg0, String arg1) {

        };

        @Override
        public void onStart() {


        };

        @Override
        public void onSuccess(int arg0, String responseString) {


            Wrapper modelContainer = GsonParser.getInstance()
                    .parseServerResponse(responseString);

            if (modelContainer.getStatus().getCode() == 200) {

               //listener.onNotificationCountChanged(Integer.parseInt(modelContainer.getData().get("unread_total").getAsString()));
               //SharedPreferencesHelper.getInstance().setString("total_unread", modelContainer.getData().get("unread_total").getAsString());

               //
               SharedPreferencesHelper.getInstance().setString("total_unread", modelContainer.getData().get("unread_total").getAsString());
               UserHelper userHelper = new UserHelper(context);
               userHelper.saveTotalUnreadNotification( modelContainer.getData().get("unread_total").getAsString());

               if(listener != null)
                    listener.onNotificationCountChanged(Integer.parseInt(modelContainer.getData().get("unread_total").getAsString()));


            }

            else {

            }



        };
    };


    public static INotificationCount listener;

    public interface INotificationCount{

        public void onNotificationCountChanged(int count);


    }






}
