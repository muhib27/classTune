package com.classtune.app.schoolapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.classtune.app.R;
import com.classtune.app.freeversion.HomePageFreeVersion;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.ReminderHelper;
import com.classtune.app.schoolapp.utils.SPKeyHelper;
import com.classtune.app.schoolapp.utils.SharedPreferencesHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreenActivity extends Activity {
	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 3000; // 2 seconds
    private static final int ICON_DURATION = 1000;
    private RelativeLayout splashBg;
    private ImageView icon;
	private Context con;
	private UserHelper userHelper;
	private UIHelper uiHelper;


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
		navigateToNextPage();

        //startActivity(new Intent(this, SpellingbeeTestActivity.class));
		//startActivity(new Intent(this, LeaderBoardActivity.class));
		//startActivity(new Intent(this, SpellingbeeRulesActivity.class));


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
			AppRestClient.postCmart(URLHelper.URL_FREE_VERSION_C_MART_INDEX, null, indexCart);
		}

	};
	
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

}
