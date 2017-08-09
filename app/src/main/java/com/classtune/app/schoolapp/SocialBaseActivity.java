package com.classtune.app.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.FreeVersionPost;
import com.classtune.app.schoolapp.model.User;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogShare;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogShare.IShareButtonclick;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.activity.RoboFragmentActivity;


public abstract class SocialBaseActivity extends RoboFragmentActivity implements
		 View.OnClickListener,UserAuthListener,IShareButtonclick{

	

	@Override
	public void onSocialButtonClick() {
		sharePostAll(post);
	}

	@Override
	public void onMyschoolButtonClick() {
		sharePostToMySchool();
	}

	private void sharePostToMySchool()
	{
		HashMap<String,String> params=new HashMap<>();
		params.put(RequestKeyHelper.USER_ID, UserHelper.getUserFreeId());
		params.put("id", post.getId());
		//AppRestClient.post(URLHelper.URL_FREE_VERSION_SHARE_TO_MY_SCHOOL, params, postToSchoolHandler);
		freeVersionShareMySchool(params);
		
	}
	public void parentSetResult(){
		setResult(RESULT_OK,new Intent());
	}

	private void freeVersionShareMySchool(HashMap<String,String> params){
		uiHelper.showLoadingDialog("Posting to my school...");
		ApplicationSingleton.getInstance().getNetworkCallInterface().freeVersionShareMySchool(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

						Log.e("Response****", ""+response.body());

						if(uiHelper.isDialogActive())
						{
							uiHelper.dismissLoadingDialog();
						}
						Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
								response.body());
						if (wrapper.getStatus().getCode() == 200) {
							uiHelper.showMessage("Post is successfully sent.");
						} else {

						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						if(uiHelper.isDialogActive())
						{
							uiHelper.dismissLoadingDialog();
						}
					}
				}
		);
	}
	private AsyncHttpResponseHandler postToSchoolHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			Log.e("Response****", arg1);
			if(uiHelper.isDialogActive())
			{
				uiHelper.dismissLoadingDialog();
			}

		}

		@Override
		public void onStart() {
			super.onStart();
			uiHelper.showLoadingDialog("Posting to my school...");
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);

			Log.e("Response****", responseString);

			if(uiHelper.isDialogActive())
			{
				uiHelper.dismissLoadingDialog();
			}
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			if (wrapper.getStatus().getCode() == 200) {
				uiHelper.showMessage("Post is successfully sent.");
			} else {
				
			}

		}

	};
	
	private FreeVersionPost post;
	private boolean shareFlag=false;
	
	private static final String FACEOOK_TAG="Facebook";
	
	/**
	 * Code used to identify the login request to the {@link PlusClientFragment}
	 * .
	 */
	public static final int REQUEST_CODE_PLUS_CLIENT_FRAGMENT = 0;

	/** Delegate responsible for handling Google sign-in. */
	//protected PlusClientFragment mPlus;

	/** Profile as returned by the PhotoHunt service. */
	protected User mPhotoUser;

	

	/** Image cache which manages asynchronous loading and caching of images. */
	protected ImageLoader mImageLoader;

	/** Person as returned by Google Play Services. */
	protected Person mPlusPerson;

	

	/**
	 * Stores the pending click listener which should be executed if the user
	 * successfully authenticates. {@link #mPendingClick} is populated if a user
	 * performs an action which requires authentication but has not yet
	 * successfully authenticated.
	 */
	protected View.OnClickListener mPendingClick;

	/**
	 * Stores the {@link View} which corresponds to the pending click listener
	 * and is supplied as an argument when the action is eventually resolved.
	 */
	protected View mPendingView;

	/**
	 * Stores the @link com.google.android.gms.common.SignInButton} for use in
	 * the action bar.
	 */
	protected SignInButton mSignInButton;
	
	public UIHelper uiHelper;
	public UserHelper userHelper;
	
	private boolean shouldRegisterToServer=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the PlusClientFragment which will initiate authentication if
		// required.
		// AuthUtil.SCOPES describe the permissions that we are requesting of
		// the user to access
		// their information and write to their moments vault.
		// AuthUtil.VISIBLE_ACTIVITIES describe the types of moment which we can
		// read from or write
		// to the user's vault.
		/*mPlus = PlusClientFragment.getPlusClientFragment(this, AppConstant.SCOPES,
				AppConstant.VISIBLE_ACTIVITIES);*/
		uiHelper=new UIHelper(SocialBaseActivity.this);
		userHelper=new UserHelper(this, SocialBaseActivity.this);
		/*mSignInButton = (SignInButton) getLayoutInflater().inflate(
				R.layout.sign_in_button, null);
		mSignInButton.setOnClickListener(this);*/
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		

		// Delegate onActivityResult handling to PlusClientFragment to resolve
		// authentication
		// failures, eg. if the user must select an account or grant our
		// application permission to
		// access the information we have requested in AuthUtil.SCOPES and
		// AuthUtil.VISIBLE_ACTIVITIES
		boolean isProcessed = false;
		if (requestCode != 110 && requestCode != 111 && requestCode != 112) {
			isProcessed=true;
			/*mPlus.handleOnActivityResult(requestCode, responseCode, intent);*/

		}
		
		if(!isProcessed)
		{
			///SchoolAllFragment
			super.onActivityResult(requestCode, responseCode, intent);
		}
	}
	
	
	public void sharePost1(FreeVersionPost post)
	{
		this.post=post;
		doFaceBookLogin(true);
		//Log.e("test", post.getTitle());
		
	}
	
	public void showSharePicker(FreeVersionPost post)
	{
		this.post=post;
		PopupDialogShare picker = PopupDialogShare.newInstance(0);
		picker.setData(this);
		picker.show(getSupportFragmentManager(), null);
	}
	
	public void sharePostUniversal(FreeVersionPost post){
		if(post.getCan_share()==1){
			showSharePicker(post);
		}else{
			sharePostAll(post);
		}
	}
	
	public void sharePostAll(FreeVersionPost postt){
		this.post = postt;
		Intent intent=new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide what to do with it.
		intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
		intent.putExtra(Intent.EXTRA_TEXT, post.getShareLink().getLink());
		startActivity(Intent.createChooser(intent, "CHAMPS21"));

	}
	
	public void publishFeed()
	{	

	};
	
	@Override
	protected void onResume() {
		super.onResume();

	}

	public void doAppLogin(User user)
	{
		userHelper.doLogIn(user);
	}
	
	public void doAppRegister(User user)
	{
		//userHelper.doRegister(user);
	}
	
	public void doFaceBookLogin(boolean flag)
	{
		Log.e("Login", "doFaceBookLogin");
		shareFlag=flag;
	}
	

	
			
	
	@Override
	public void onStop() {
		super.onStop();

		// Reset any asynchronous tasks we have running.
		resetTaskState();
	}

	/*private void createUserObjectToRegister(LogInTypeEnum type,User user)
	{
		switch (type) {
		case GOOGLE_PLUS:
			user.setType(LogInTypeEnum.GOOGLE_PLUS.value);
			break;
		case FACEBOOK:
			user.setType(LogInTypeEnum.FACEBOOK.value);
			break;
		default:
			break;
		}
		userHelper.doRegister(user);
	}*/
	
	
	
	
	
	
	/**
	 * Invoked when the {@link PlusClientFragment} delegate has successfully
	 * authenticated the user.
	 * 
	 * @param plusClient
	 *            The connected PlusClient which gives us access to the Google+
	 *            APIs.
	 */
	

	
	
	

	/**
	 * Invoked when the PhotoHunt profile has been successfully retrieved for an
	 * authenticated user.
	 * 
	 * @param profile
	 */
	public void setAuthenticatedProfile(User profile) {
		mPhotoUser = profile;
	}

	/**
	 * Update the user interface to reflect the current application state. This
	 * function is called whenever this Activity's state has been modified.
	 * 
	 * {@link BaseActivity} calls this method when user authentication succeeds
	 * or fails.
	 */
	public void update() {
		supportInvalidateOptionsMenu();
	}

	/**
	 * Execute actions which are pending; eg. because they were waiting for the
	 * user to authenticate.
	 */
	protected void executePendingActions() {
		// On successful authentication we resolve any pending actions
		if (mPendingClick != null) {
			mPendingClick.onClick(mPendingView);
			mPendingClick = null;
			mPendingView = null;
		}
	}

	

	@Override
	public void onClick(View view) {
		/*if (view.getId() == R.id.google_login_btn) {
			//mPlus.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
		}*/
	}

	public void doGooglePlusLogin()
	{
		shouldRegisterToServer=true;
		//mPlus.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
		
	}
	
	
	/**
	 * Provides a guard to ensure that a user is authenticated.
	 */
	protected boolean requireSignIn() {
		/*if (!mPlus.isAuthenticated()) {
			mPlus.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
			return false;
		} else {
			return true;
		}*/
		return false;
	}

	/**
	 * @return true if the user is currently authenticated through Google
	 *         sign-in 
	 */
	public boolean isAuthenticated() {
		return false;//mPlus.isAuthenticated() && mPhotoUser != null;
	}

	/**
	 * @return true if the user is currently being authenticated through Google
	 *         sign-in or if the the user's PhotoHunt profile is being fetched.
	 */
	public boolean isAuthenticating() {
		/*return mPlus.isConnecting() || mPlus.isAuthenticated()
		&& mPhotoUser == null;*/
		return false;
	}

	/**
	 * Resets the state of asynchronous tasks used by this activity.
	 */
	protected void resetTaskState() {
		
	}
	


}
