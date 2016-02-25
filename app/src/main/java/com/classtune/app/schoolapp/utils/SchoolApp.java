/**
 * 
 */
package com.classtune.app.schoolapp.utils;


import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.ReportCardModel;
import com.classtune.app.schoolapp.model.User;
import com.classtune.app.schoolapp.networking.LruBitmapCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

import roboguice.activity.RoboFragmentActivity;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;


public class SchoolApp extends Application {

	public static final int REQUEST_CODE_CHILD_SELECTION = 123;
	private ProgressDialog dlog;
	private String userSecret;
	private User currentUser;
	private String session;
	private boolean loggedIn;
	private ArrayList<Integer> weekends;
	private ReportCardModel reportCardData = new ReportCardModel();
	public static final String TAG = SchoolApp.class.getSimpleName();
	private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
	public void setReportCardData(ReportCardModel reportCardData) {
		this.reportCardData = reportCardData;
	}
	public ReportCardModel getReportCardData() {
		return reportCardData;
	}
	

	public void showDialog(FragmentManager fm, String tag, String Title, String message, int resId, Class<RoboFragmentActivity> targetContext)
	{
		SimpleDialogFragment dialog = new SimpleDialogFragment();
		dialog.setParams(Title, message, resId, targetContext, null);
		dialog.show(fm, tag);
	}

	public void openInternetSettingsActivity(FragmentManager fm, String tag)
	{
		SimpleDialogFragment dialog = new SimpleDialogFragment();
		dialog.setParams(AppConstant.STR_INTERNET_PROBLEM_TITLE, AppConstant.STR_INTERNET_PROBLEM_MESSAGE, R.layout.dialog_one_button, null, new Intent(Settings.ACTION_WIFI_SETTINGS));
		dialog.show(fm, tag);
	}

	private void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	public void setupUI(View view, final Activity activity) {
		// TODO Auto-generated method stub
		//Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {

	        view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hideSoftKeyboard(activity);
	                return false;
				}

	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
			    View innerView = ((ViewGroup) view).getChildAt(i);
				setupUI(innerView, activity);
	        }
	    }
	}

	public void showLoader(Context context) {
		dlog = ProgressDialog.show(context, AppConstant.STR_LOADER_TITLE, AppConstant.STR_LOADER_MSG, false, false);
	}

	public void dismissLoader() {
		if (dlog.isShowing()) {
			dlog.dismiss();
		}
	}

	public String getUDID() {
		
		String udid = SharedPreferencesHelper.getInstance().getString(SPKeyHelper.UDID, "");
		if (udid.equalsIgnoreCase("")) {
			TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
			udid = tm.getDeviceId();
			
			SharedPreferencesHelper.getInstance().setString(SPKeyHelper.UDID, udid);
		}

		//showLog("UDID if", "Device ID : " + udid);
		
		return udid;
	}
	
	public void setUserSecret(String userSecret) {
		this.userSecret = userSecret;
	}
	public String getUserSecret() {
		return userSecret;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setSession(String session) {
		this.session = session;
	}
	public String getSession() {
		return session;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setWeekends(ArrayList<Integer> weekends) {
		this.weekends = weekends;
	}
	public ArrayList<Integer> getWeekends() {
		return weekends;
	}

	//******************************** Singleton Stuffs *********************************
	private static SchoolApp singleton;
	
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	LruBitmapCache mLruBitmapCache;

	public static SchoolApp getInstance(){
		return singleton;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			getLruBitmapCache();
			mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
		}

		return this.mImageLoader;
	}

	public LruBitmapCache getLruBitmapCache() {
		if (mLruBitmapCache == null)
			mLruBitmapCache = new LruBitmapCache();
		return this.mLruBitmapCache;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	
	//******************************** Overridden methods *********************************
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Fabric.with(this, new Crashlytics());
		//Fabric.with(this, new Crashlytics());
		singleton = this;
		
		initUnivarsalIamgeLoader();
		// initialize facebook configuration
    	/*Permission[] permissions = new Permission[] {
    		Permission.BASIC_INFO, 
    		Permission.EMAIL,
    		Permission.USER_HOMETOWN,
    		Permission.PUBLISH_STREAM,
			Permission.USER_GROUPS,
			Permission.USER_BIRTHDAY,
			Permission.USER_EVENTS,
			Permission.USER_RELATIONSHIPS,
			Permission.READ_STREAM, 
			Permission.PUBLISH_ACTION
    	};

    	SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
    		.setAppId(getString(R.string.fb_app_id))
    		.setNamespace(getString(R.string.fb_app_namespace))
    		.setPermissions(permissions)
    		.setDefaultAudience(SessionDefaultAudience.FRIENDS)
    		.setAskForAllPermissionsAtOnce(false)
    		.build();
    	
    	SimpleFacebook.setConfiguration(configuration);*/
	}
	
	private void initUnivarsalIamgeLoader()
	{
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(singleton)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.defaultDisplayImageOptions(defaultOptions)
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		//.writeDebugLogs() // Remove for release app
		.build();
		
		imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
		imageLoader.init(config);
	}
	
	
	
	public void displayUniversalImage(String imgUrl, ImageView imgView)
	{
		imageLoader.displayImage(imgUrl, imgView);
	}
	
	
	
	public void displayUniversalImage(String imgUrl, ImageView imgView, final ProgressBar pb)
	{

		imageLoader.displayImage(imgUrl, imgView, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				 pb.setVisibility(View.VISIBLE);

			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// spinner.setVisibility(View.GONE);
				pb.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri,
					View view, Bitmap loadedImage) {
				pb.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingCancelled(String imageUri,
					View view) {

			}
		});
		
	}
	public void displayUniversalImageSingle(String imgUrl, ImageView imgView, final ProgressBar pb)
	{

		imageLoader.displayImage(imgUrl, imgView,AppUtility.getOptionForSingleImage(), new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				 pb.setVisibility(View.VISIBLE);

			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// spinner.setVisibility(View.GONE);
				pb.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri,
					View view, Bitmap loadedImage) {
				pb.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingCancelled(String imageUri,
					View view) {

			}
		});
		
	}
	
	

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public Typeface getClassTuneFontRes(String fontPath) {
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/" + fontPath);

		return tf;
	}


	public void globalBroadcastThroughApp(String key, String data){
		Intent intent = new Intent();
		intent.putExtra(key, data);
		intent.setAction("com.datacontext.CUSTOM_INTENT");
		sendBroadcast(intent);
	}

}
