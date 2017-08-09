/**
 * 
 */
package com.classtune.app.schoolapp.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
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
import com.classtune.app.schoolapp.networking.ApiClient;
import com.classtune.app.schoolapp.networking.LruBitmapCache;
import com.classtune.app.schoolapp.networking.NetworkCallInterface;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;
import roboguice.activity.RoboFragmentActivity;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;


public class ApplicationSingleton extends MultiDexApplication {

	public static final int REQUEST_CODE_CHILD_SELECTION = 123;
	private ProgressDialog dlog;
	private String userSecret;
	private User currentUser;
	private String session;
	private boolean loggedIn;
	private ArrayList<Integer> weekends;
	private ReportCardModel reportCardData = new ReportCardModel();
	public static final String TAG = ApplicationSingleton.class.getSimpleName();
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
	private static ApplicationSingleton singleton;
	
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	LruBitmapCache mLruBitmapCache;
	private SharedPreferences mPref;
	private NetworkCallInterface networkCallInterface;

	public static ApplicationSingleton getInstance(){
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

	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
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

	public String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
		return type;
	}

	public void clearApplicationData() {

		File cacheDirectory = getCacheDir();
		File applicationDirectory = new File(cacheDirectory.getParent());
		if (applicationDirectory.exists()) {
			String[] fileNames = applicationDirectory.list();
			for (String fileName : fileNames) {
				if (!fileName.equals("lib")) {
					deleteFile(new File(applicationDirectory, fileName));
				}
			}
		}
	}

	public static boolean deleteFile(File file) {
		boolean deletedAll = true;
		if (file != null) {
			if (file.isDirectory()) {
				String[] children = file.list();
				for (int i = 0; i < children.length; i++) {
					deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
				}
			} else {
				deletedAll = file.delete();
			}
		}
		return deletedAll;
	}
	private void initializeInstance() {
		mPref = this.getApplicationContext().getSharedPreferences("classtune_material_pref_key", MODE_PRIVATE);
	}

	public void savePrefString(String key, String value){
		SharedPreferences.Editor editor = mPref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public String getPrefString(String key){
		return mPref.getString(key, "");
	}


	public void savePrefBoolean(String key, boolean value){
		SharedPreferences.Editor editor = mPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public boolean getPrefBoolean(String key){
		return mPref.getBoolean(key, false);
	}

	public void savePrefInteger(String key, int value){
		SharedPreferences.Editor editor = mPref.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public int getPrefInteger(String key){
		return mPref.getInt(key, 0);
	}


	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null;
	}

	public String printHashKey(Context pContext) {
		String hashKey = "";
		try {
			PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				hashKey = new String(Base64.encode(md.digest(), 0));
			}
		} catch (NoSuchAlgorithmException e) {
			Log.e("KEY_HASH", "printHashKey()", e);
		} catch (Exception e) {
			Log.e("KEY_HASH", "printHashKey()", e);
		}
		return  hashKey;
	}

	public boolean isUrlExists(String URLName){
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			//        HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con =
					(HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public String getFormattedDateString(String inputFormat, String outputFormat, String value){

		SimpleDateFormat inputPattern = new SimpleDateFormat(inputFormat);
		SimpleDateFormat outputPattern = new SimpleDateFormat(outputFormat);

		Date date = null;
		String str = null;

		try {
			date = inputPattern.parse(value);
			str = outputPattern.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public int pxToDp(int px) {
		return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}

	public int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public boolean containsKey(String key){
		return mPref.contains(key);
	}

	public void removeKey(String key){
		SharedPreferences.Editor editor = mPref.edit();
		editor.remove(key);
		editor.apply();
	}

	public String getDeviceID(){
		String android_id = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		return android_id;
	}

	public String convertJsonElementToString(JsonElement jsonElement){
		Gson gson = new Gson();
		JsonElement element = gson.fromJson (jsonElement.toString(), JsonElement.class);
		JsonObject jsonObj = element.getAsJsonObject();

		return jsonObj.toString();
	}

	public NetworkCallInterface getNetworkCallInterface(){
		Retrofit retrofit = ApiClient.getInstance(this);
		networkCallInterface = retrofit.create(NetworkCallInterface.class);

		return networkCallInterface;
	}


	public boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

   /* public List<Country> getCountryList(){
        Gson gson = new Gson();
        List<Country> countryList = gson.fromJson(getPrefString(IAppConstant.KEY_COUNTRY_LIST), new TypeToken<List<Country>>(){}.getType());
        return countryList;
    }

    public Client getAlgoliaClient(){
        Client client = new Client("EVFY5FKKL1", "0284bda458156122a6e1013aa11569f6");
        return client;
    }*/

	public String extractYTId(String ytUrl) {
		String vId = null;
		Pattern pattern = Pattern.compile(
				"^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(ytUrl);
		if (matcher.matches()){
			vId = matcher.group(1);
		}
		return vId;
	}

	public String getDecimalTwoDigitNumberFromDouble(Double value){
		DecimalFormat precision = new DecimalFormat("0.00");
		String str = precision.format(value);
		return str;
	}

}
