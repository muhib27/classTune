package com.classtune.app.schoolapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BLACK HAT on 25-Feb-16.
 */
public class AppBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        //Log.e("broadcast_DATA", "is: "+intent.getExtras().getString("ggwp"));
        ObservableObject.getInstance().updateValue(intent);


    }

}
