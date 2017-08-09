package com.classtune.app.schoolapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.ChildContainerActivity;
import com.classtune.app.schoolapp.model.Student;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentInfoActivity extends ChildContainerActivity {

	private String studentId;
	private LinearLayout pbLayout;
	private Context con;
	private TextView studentName,studentClass, roll, guardianName, gender, batchName, admissionNumber, txtContactNumber, txtAddress;
	private ImageView profileImage;
	private ProgressBar progress;
	private int feedKeyValue = 0;
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_info);
		
		if(getIntent().getExtras() != null) {
			studentId = getIntent().getExtras().getString("student_id");
			feedKeyValue = getIntent().getExtras().getInt("key_from_feed", 0);
		}
		
		fetchStudentInfo();
		
		init();
	}
	
	private void init() {
		con=StudentInfoActivity.this;
		studentId=getIntent().getStringExtra("student_id");
		studentName = (TextView)findViewById(R.id.tv_profile_student_name);
		studentClass = (TextView)findViewById(R.id.tv_profile_student_class);
		roll = (TextView)findViewById(R.id.tv_profile_roll);
		guardianName = (TextView)findViewById(R.id.tv_profile_guardian_name);
		gender = (TextView)findViewById(R.id.tv_profile_gender);
		batchName = (TextView)findViewById(R.id.tv_profile_batch_name);
		admissionNumber = (TextView)findViewById(R.id.tv_profile_admission_no);
		profileImage = (ImageView)findViewById(R.id.iv_profile_picture);
		progress = (ProgressBar)findViewById(R.id.progress);
		pbLayout = (LinearLayout)findViewById(R.id.pb);
		
		
		txtContactNumber = (TextView)findViewById(R.id.txtContactNumber);
		txtAddress = (TextView)findViewById(R.id.txtAddress);
	}

	/*@Override
	protected void onResume() {
		// TODO Auto-generated  stub
		super.onResume();
		fetchStudentInfo();
	}*/
	
	private void fetchStudentInfo()
	{
		HashMap<String, String> params=new HashMap<>();

		if(feedKeyValue == 1)
		{
			if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
			{
				params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
				params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());

			}
			else if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.STUDENT)
			{
				params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			}
			//AppRestClient.post(URLHelper.URL_GET_INFO, params, getStudentInfoHandler);
			getStudentInfo(params);
		}
		else
		{
			params.put(RequestKeyHelper.STUDENT_ID, studentId);
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			//AppRestClient.post(URLHelper.URL_GET_STUDENT_INFO, params, getStudentInfoHandler);
			getStudentInfo(params);
		}

	}
	
	private void updateUI(Student student)
	{
		studentName.setText(student.getStudentName());
		studentClass.setText(student.getStudentClass());
		guardianName.setText(student.getGaurdian());
		roll.setText(student.getRollNo());
		//gender.setText(student.getGender());
		batchName.setText(student.getBatchName());
		admissionNumber.setText(student.getAdmissionNo());
		ImageLoader.getInstance().displayImage(student.getImageUrl(),
			     profileImage, AppUtility.getOptionForUserImage(),
			     new ImageLoadingListener() {

			      @Override
			      public void onLoadingStarted(String imageUri, View view) {
			        progress.setVisibility(View.VISIBLE);

			      }

			      @Override
			      public void onLoadingFailed(String imageUri, View view,
			        FailReason failReason) {
			       // spinner.setVisibility(View.GONE);
			    	  progress.setVisibility(View.GONE);
			      }

			      @Override
			      public void onLoadingComplete(String imageUri,
			        View view, Bitmap loadedImage) {
			    	  progress.setVisibility(View.GONE);
			      }

			      @Override
			      public void onLoadingCancelled(String imageUri,
			        View view) {

			      }
			     });
		
		
		txtContactNumber.setText(student.getPhone());
		txtAddress.setText(student.getContact());
		
	}

	private void getStudentInfo(HashMap<String, String> params){
		pbLayout.setVisibility(View.VISIBLE);
		ApplicationSingleton.getInstance().getNetworkCallInterface().getStudentInfo(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						pbLayout.setVisibility(View.GONE);
						Log.e("Response****", ""+response.body());

						Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
								response.body());
						if (wrapper.getStatus().getCode() == 200) {
							Student student=GsonParser.getInstance().parseStudentInfo(wrapper.getData().get("student").toString());
							updateUI(student);

						} else {

						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						pbLayout.setVisibility(View.GONE);
					}
				}
		);
	}
	AsyncHttpResponseHandler getStudentInfoHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			pbLayout.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			super.onStart();
			pbLayout.setVisibility(View.VISIBLE);
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			pbLayout.setVisibility(View.GONE);
			Log.e("Response****", responseString);

			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			if (wrapper.getStatus().getCode() == 200) {
				Student student=GsonParser.getInstance().parseStudentInfo(wrapper.getData().get("student").toString());
				updateUI(student);
				
			} else {
				
			}

		}

	};

}
