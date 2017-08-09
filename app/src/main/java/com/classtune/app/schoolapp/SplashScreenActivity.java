package com.classtune.app.schoolapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.HomePageFreeVersion;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.ReminderHelper;
import com.classtune.app.schoolapp.utils.SPKeyHelper;
import com.classtune.app.schoolapp.utils.SharedPreferencesHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.classtune.app.freeversion.HomePageFreeVersion.PROPERTY_APP_VERSION;
import static com.classtune.app.freeversion.HomePageFreeVersion.PROPERTY_REG_ID;

public class SplashScreenActivity extends Activity {
	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 3000; // 2 seconds
    private static final int ICON_DURATION = 1000;
    private RelativeLayout splashBg;
    private ImageView icon;
	private Context con;
	private UserHelper userHelper;
	private UIHelper uiHelper;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		splashBg=(RelativeLayout)findViewById(R.id.splash_bg);
		con = SplashScreenActivity.this;
		AppRestClient.init();
		ReminderHelper.getInstance().constructReminderFromSharedPreference();
		userHelper = new UserHelper(SplashScreenActivity.this);
		uiHelper=new UIHelper(SplashScreenActivity.this);
		/*if(AppUtility.isInternetConnected())
			AppRestClient.postCmart(URLHelper.BASE_URL_CMART+URLHelper.URL_FREE_VERSION_C_MART_INIT, null,initCart);
		else
		{
			navigateToNextPage();

		}*/



		getHashKey();
		//navigateToNextPage();

        //startActivity(new Intent(this, SpellingbeeTestActivity.class));
		//startActivity(new Intent(this, LeaderBoardActivity.class));
		//startActivity(new Intent(this, SpellingbeeRulesActivity.class));


		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(getApplicationContext());

			if (TextUtils.isEmpty(regid)) {
				registerInBackground(getApplicationContext());
			} else if (!getRegisteredToServer()) {
				sendRegistrationIdToBackend();
			}else{
				navigateToNextPage();
			}
		} else {
			Log.e("GCM", "No valid Google Play Services APK found.");
		}

	}

	private boolean getRegisteredToServer() {

		return SharedPreferencesHelper.getInstance().getBoolean(
				AppConstant.GCM_REGISTRATION_SERVER, false);
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.e("GCM", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Check device for Play Services APK.
		checkPlayServices();

	}

	private void navigateToNextPage()
	{
		Handler handler = new Handler();
        /*handler.postDelayed(new Runnable() {
        	 
            @Override
            public void run() {
 
               //finish();
               if (!mIsBackButtonPressed) {
                	
            	   
            	   icon=new ImageView(SplashScreenActivity.this);
            	   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            	  
            	   icon.setLayoutParams(layoutParams);
            	   
            	   icon.setImageResource(R.drawable.splash_logo);
            	   splashBg.setGravity(Gravity.CENTER_HORIZONTAL);
            	   splashBg.addView(icon);
            	   Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.top_to_bottom);
            	   animation.setFillAfter(true);
            	   icon.startAnimation(animation);
            	   
                }
                 
            }
 
        }, ICON_DURATION); */
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
 
            @Override
            public void run() {
 
                // make sure we close the splash screen so the user won't come back when it presses back key
 
                finish();
                 
                if (!mIsBackButtonPressed) {
                	Intent intent;

					if(UserHelper.isLoggedIn()) {
						intent= new Intent(SplashScreenActivity.this, HomePageFreeVersion.class);
						SplashScreenActivity.this.startActivity(intent);
					} else {
						intent= new Intent(SplashScreenActivity.this, LoginActivity.class);
						SplashScreenActivity.this.startActivity(intent);
					}

					/*intent= new Intent(SplashScreenActivity.this, SingleItemShowFragmentActivity.class);
                	intent= new Intent(SplashScreenActivity.this, HomePageFreeVersion.class);
                    SplashScreenActivity.this.startActivity(intent);
                	
                	/*intent= new Intent(SplashScreenActivity.this, SingleItemShowActivity.class);
                    SplashScreenActivity.this.startActivity(intent);*/
                	
                }
                 
            }
 
        }, SPLASH_DURATION);// time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
 
	}
	
	
	AsyncHttpResponseHandler initCart = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {

		}

		@Override
		public void onStart() {
		}

		@Override
		public void onSuccess(int arg0, String responseString) {

			Log.e("INIT", responseString);
			//navigateToNextPage();
			//XmlParser.getInstance().parseCmartIndex(responseString);
			//AppRestClient.postCmart(URLHelper.URL_FREE_VERSION_C_MART_INDEX, null, indexCart);
			cMartIndex(null);
		}

	};

	private void cMartIndex(HashMap<String,String> params){
		ApplicationSingleton.getInstance().getNetworkCallInterface().cMartIndex(params).enqueue(new Callback<JsonElement>() {
			@Override
			public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
				Log.e("INDEX", ""+response.body());
				SharedPreferencesHelper.getInstance().setString(SPKeyHelper.CAMRT_CATS,response.toString());
				navigateToNextPage();
			}

			@Override
			public void onFailure(Call<JsonElement> call, Throwable t) {

			}
		});
	}
	AsyncHttpResponseHandler indexCart = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
		}

		@Override
		public void onStart() {

		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			Log.e("INDEX", responseString);
			SharedPreferencesHelper.getInstance().setString(SPKeyHelper.CAMRT_CATS,responseString);
			navigateToNextPage();
		}

	};
	
	
	@Override
	public void onBackPressed() {

		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}
	
	private void getHashKey() {
		try {
		    PackageInfo info = getPackageManager().getPackageInfo(
		            getPackageName(), PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        Log.e("MY_KEY_HASH:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
		    }
		} catch (NameNotFoundException e) {
		} catch (NoSuchAlgorithmException e) {
		} }


	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	String regid;
	String SENDER_ID = "441812119192";

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(
				HomePageFreeVersion.PROPERTY_REG_ID, "");
		if (TextUtils.isEmpty(registrationId)) {
			Log.i(HomePageFreeVersion.TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(
				HomePageFreeVersion.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(HomePageFreeVersion.TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private void registerInBackground(final Context context) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = getString(R.string.java_homepagefreeversion_device_registered) + regid;

					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = getString(R.string.java_homepagefreeversion_error) + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.e("GCM SERVER RESPONSE", msg);
			}
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend() {
		String deviceId = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		RequestParams params = new RequestParams();
		params.put("gcm_id", regid);
		params.put("device_id", deviceId + "classtune");
		AppRestClient.post(URLHelper.URL_GCM_REGISTER, params,
				serverResponseHandler);

	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.e("GCM_ID", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	AsyncHttpResponseHandler serverResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
           /* HttpResponseException hre = (HttpResponseException) arg0;
            int statusCode = hre.getStatusCode();
            if (statusCode == 401) {

            } else {

            }*/
			Toast.makeText(SplashScreenActivity.this, R.string.java_classtune_registration_something_went_wrong, Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onStart() {
			super.onStart();
			// uiHelper.showLoadingDialog(getString(R.string.loading_dialog_text)+" "+TAG+"...");
		}

		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);
			// uiHelper.dismissLoadingDialog();
			Log.e("RESPONSE", content);

			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					content);


			if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
				setRegisteredToServer(true);
				navigateToNextPage();
			} else {
				setRegisteredToServer(false);
			}
		}

	};

	private void setRegisteredToServer(boolean b) {

		SharedPreferencesHelper.getInstance().setBoolean(
				AppConstant.GCM_REGISTRATION_SERVER, b);
	}

	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(HomePageFreeVersion.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}


}
