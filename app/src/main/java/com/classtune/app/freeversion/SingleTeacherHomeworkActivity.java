package com.classtune.app.freeversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.HomeworkData;
import com.classtune.app.schoolapp.model.TeacherHomeworkData;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleTeacherHomeworkActivity extends ChildContainerActivity {
	
	private UIHelper uiHelper;
	private String id;
	private TextView tvLesson;
	private TextView tvShift;
	private TextView tvCourse;
	private TextView tvSection;
	private TextView tvDueDate;
	private TextView tvAssignDate;
	private LinearLayout mLinearLayout;
	//private WebView webViewContent;

	private TextView tvSubject;
	private CustomButton btnDone;
	private ImageView ivSubjectIcon;
	
	private LinearLayout bottmlay;
	
	private TeacherHomeworkData data;
	private Gson gson;
	
	private Button btnDownload;
	private LinearLayout layoutDownloadHolder;

	private LinearLayout layoutHorizontalBar;
	private Button btnEdit;
	private static final int REQUEST_EDIT_HOMEWORK = 56;
	private WebView webView;
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_teacher_homework);
		
		gson = new Gson();
		
		uiHelper = new UIHelper(SingleTeacherHomeworkActivity.this);
		
		if(getIntent().getExtras() != null)
			this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_HOMEWORK);
		
		initView();
		initApicall();
		
		
	}
	
	
	private void initView()
	{
		//this.webViewContent = (WebView)this.findViewById(R.id.webViewContent);
		this.webView = (WebView)this.findViewById(R.id.webView);
		this.tvLesson = (TextView) this.findViewById(R.id.tv_homework_content);
		this.tvSubject = (TextView) this.findViewById(R.id.tv_teacher_feed_subject_name);
		this.tvShift = (TextView)this.findViewById(R.id.tv_teacher_homewrok_feed_shift);
		this.tvCourse = (TextView)this.findViewById(R.id.tv_teacher_homework_feed_course);
		this.tvSection = (TextView)this.findViewById(R.id.tv_teacher_homework_feed_section);
		this.tvDueDate = (TextView)this.findViewById(R.id.txtDueDate);
		this.tvAssignDate = (TextView)this.findViewById(R.id.txtAssignDate);
		this.mLinearLayout = (LinearLayout)this.findViewById(R.id.single_teacher_homework_detail_visibility);
		
		this.btnDone = (CustomButton) this.findViewById(R.id.btn_done);
		this.ivSubjectIcon = (ImageView) this.findViewById(R.id.imgViewCategoryMenuIcon);
		this.bottmlay = (LinearLayout)this.findViewById(R.id.bottmlay);
		this.btnDownload = (Button)this.findViewById(R.id.btnDownload);
		this.layoutDownloadHolder = (LinearLayout)this.findViewById(R.id.layoutDownloadHolder);

		this.layoutHorizontalBar = (LinearLayout)this.findViewById(R.id.layoutHorizontalBar);
		this.btnEdit = (Button)this.findViewById(R.id.btnEdit);

	}
	
	
	
	private void initAction()
	{
		mLinearLayout.setVisibility(View.VISIBLE);
		this.tvLesson.setText(data.getName());
		this.webView.loadData(data.getContent(), "text/html; charset=utf-8", "UTF-8");
		
		
		btnDone.setTitleText(getString(R.string.java_singleteacherhomeworkactivity_done_by)+data.getDone());
		btnDone.setTextSize(16);

		this.tvSubject.setText(data.getSubjects());
		this.tvShift.setText(data.getBatch());
		this.tvCourse.setText(data.getCourse());
		this.tvSection.setText(data.getSection());
		this.tvDueDate.setText(data.getDuedate());
		this.tvAssignDate.setText(data.getAssign_date());
	
		
		this.ivSubjectIcon.setImageResource(AppUtility.getImageResourceId(data.getSubjects_icon(), this));
		
		
		
		
		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(SingleTeacherHomeworkActivity.this, TeacherHomeworkDoneActivity.class);
				intent.putExtra(AppConstant.ID_TEACHER_HOMEWORK_DONE, data.getId());
				startActivity(intent);
			}
		});
		
		
		
		
		if(!TextUtils.isEmpty(data.getAttachment_file_name()))
		{
			layoutDownloadHolder.setVisibility(View.VISIBLE);
		}
		else
		{
			layoutDownloadHolder.setVisibility(View.GONE);
		}
		
		
		
		btnDownload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//http://api.champs21.com/api/freeuser/downloadattachment?id=47


				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://api.champs21.com/api/freeuser/downloadattachment?id=" + data.getId())));
			}
		});


		/*if(data.getIsEditable() == true)
		{
			btnEdit.setVisibility(View.VISIBLE);
			layoutHorizontalBar.setVisibility(View.VISIBLE);
		}
		else
		{
			btnEdit.setVisibility(View.GONE);
			layoutHorizontalBar.setVisibility(View.GONE);
		}*/

		this.btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(data.getIsEditable() == true)
				{
					Intent intent = new Intent(SingleTeacherHomeworkActivity.this, SingleTeacherEditHomeworkActivity.class);
					intent.putExtra(AppConstant.ID_SINGLE_HOMEWORK, data.getId());
					startActivityForResult(intent, REQUEST_EDIT_HOMEWORK);
				}
				else
				{
					Toast.makeText(SingleTeacherHomeworkActivity.this, getString(R.string.java_singleteacherdrafthomeworkactivity_edit_from_website), Toast.LENGTH_SHORT).show();
				}


			}
		});



		
		
	}
	
	
	
	
	
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	private void showWebViewContent(String text, WebView webView) {
		
		final String mimeType = "text/html";
		final String encoding = "UTF-8";

		webView.loadDataWithBaseURL("", text, mimeType, encoding, null);
		WebSettings webViewSettings = webView.getSettings();
		webViewSettings.setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());

				
	}
	
	
	
	private List<HomeworkData> parseHomeworkData(String object) {

		List<HomeworkData> tags = new ArrayList<HomeworkData>();
		Type listType = new TypeToken<List<HomeworkData>>() {
		}.getType();
		tags = (List<HomeworkData>) new Gson().fromJson(object, listType);
		return tags;
	}
	
	
	private void initApicall()
	{
		HashMap<String,String> params = new HashMap<>();

		//app.showLog("adfsdfs", app.getUserSecret());

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("id", this.id);
		
		
		
		//AppRestClient.post(URLHelper.URL_SINGLE_TEACHER_HOMEWORK, params, singleTeacherHomeWorkHandler);
		singleTeacherHomework(params);
	}
	private void singleTeacherHomework(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().singleTeacherHomework(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();


						Wrapper modelContainer = GsonParser.getInstance()
								.parseServerResponse2(response.body());

						if (modelContainer.getStatus().getCode() == 200) {

							JsonObject objHomework = modelContainer.getData().get("homework").getAsJsonObject();
							data = gson.fromJson(objHomework.toString(), TeacherHomeworkData.class);

							Log.e("HHH", "data: " + data.getName());

							initAction();

						}

						else {

						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.showMessage(getString(R.string.internet_error_text));
						if (uiHelper.isDialogActive()) {
							uiHelper.dismissLoadingDialog();
						}
					}
				}
		);
	}
	AsyncHttpResponseHandler singleTeacherHomeWorkHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		@Override
		public void onStart() {
			
				uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
			

		};

		@Override
		public void onSuccess(int arg0, String responseString) {
			

			uiHelper.dismissLoadingDialog();


			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {
				
				JsonObject objHomework = modelContainer.getData().get("homework").getAsJsonObject();
				data = gson.fromJson(objHomework.toString(), TeacherHomeworkData.class);
				
				Log.e("HHH", "data: " + data.getName());
				
				initAction();
				
			}
			
			else {

			}
			
			

		};
	};
	
	/*@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		webViewContent.destroy();
	};*/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == RESULT_OK)
		{

			if(requestCode == REQUEST_EDIT_HOMEWORK)
			{
				initApicall();
			}
		}


	}

	@Override
	public void onBackPressed() {

		setResult(RESULT_OK);
		finish();
		super.onBackPressed();
	}

}
