package com.classtune.app.freeversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.HomeworkData;
import com.classtune.app.schoolapp.model.TeacherHomeworkData;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.MyTagHandler;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.ExpandableTextView;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SingleTeacherDraftHomeworkActivity extends ChildContainerActivity {
	
	private UIHelper uiHelper;
	private String id;
	private TextView tvLesson;
	//private WebView webViewContent;
	
	private ExpandableTextView txtContent;
	private TextView tvSubject;
	private TextView tvDate;
	private ImageView ivSubjectIcon;
	
	private LinearLayout bottmlay;
	
	private TeacherHomeworkData data;
	private Gson gson;
	
	private ImageButton btnDownload;
	private LinearLayout layoutDownloadHolder;

	private Button btnPublish;
	private Button btnEdit;

	private static final int REQUEST_EDIT_HOMEWORK = 55;

	private boolean isEditable = false;
	
	
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
		setContentView(R.layout.activity_single_teacher_draft_homework);
		
		gson = new Gson();
		
		uiHelper = new UIHelper(SingleTeacherDraftHomeworkActivity.this);
		
		if(getIntent().getExtras() != null)
			this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_HOMEWORK);
		
		initView();
		initApicall();
		
		
	}
	
	
	private void initView()
	{
		//this.webViewContent = (WebView)this.findViewById(R.id.webViewContent);
		this.txtContent = (ExpandableTextView)this.findViewById(R.id.txtContent);
		this.tvLesson = (TextView) this.findViewById(R.id.tv_homework_content);
		this.tvSubject = (TextView) this.findViewById(R.id.tv_teacher_feed_subject_name);
		this.tvDate = (TextView) this.findViewById(R.id.tv_teacher_homewrok_feed_date);
		
		this.ivSubjectIcon = (ImageView) this.findViewById(R.id.imgViewCategoryMenuIcon);
		this.bottmlay = (LinearLayout)this.findViewById(R.id.bottmlay);
		this.btnDownload = (ImageButton)this.findViewById(R.id.btnDownload);
		this.layoutDownloadHolder = (LinearLayout)this.findViewById(R.id.layoutDownloadHolder);

		this.btnPublish = (Button)this.findViewById(R.id.btnPublish);
		this.btnEdit = (Button)this.findViewById(R.id.btnEdit);
	}
	
	
	
	private void initAction()
	{
		this.tvLesson.setText(data.getName());
		this.txtContent.setText(Html.fromHtml(data.getContent(), null, new MyTagHandler()));
		
		

		this.tvSubject.setText(data.getSubjects());
		
		String[] parts = data.getDuedate().split(" ");
		String part1 = parts[0];
		this.tvDate.setText(part1);
		
	
		
		this.ivSubjectIcon.setImageResource(AppUtility.getImageResourceId(data.getSubjects_icon(), this));
		

		
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


		this.btnPublish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				initApicallPublish();

			}
		});


		/*if(isEditable == true)
		{
			btnEdit.setVisibility(View.VISIBLE);
		}
		else
		{
			btnEdit.setVisibility(View.GONE);
		}*/

		this.btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(isEditable == true)
				{
					Intent intent = new Intent(SingleTeacherDraftHomeworkActivity.this, SingleTeacherEditHomeworkActivity.class);
					intent.putExtra(AppConstant.ID_SINGLE_HOMEWORK, data.getId());
					startActivityForResult(intent, REQUEST_EDIT_HOMEWORK);
				}
				else
				{
					Toast.makeText(SingleTeacherDraftHomeworkActivity.this, R.string.java_singleteacherdrafthomeworkactivity_edit_from_website, Toast.LENGTH_SHORT).show();
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
		RequestParams params = new RequestParams();

		//app.showLog("adfsdfs", app.getUserSecret());

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("id", this.id);
		
		
		
		AppRestClient.post(URLHelper.URL_SINGLE_TEACHER_HOMEWORK, params, singleTeacherHomeWorkHandler);
	}
	
	AsyncHttpResponseHandler singleTeacherHomeWorkHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
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

				isEditable = data.getIsEditable();

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

	private void initApicallPublish()
	{
		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("id", this.id);



		AppRestClient.post(URLHelper.URL_SINGLE_TEACHER_PUBLISH_HOMEWORK, params, singleTeacherPublishHomeWorkHandler);
	}

	AsyncHttpResponseHandler singleTeacherPublishHomeWorkHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
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


				Toast.makeText(SingleTeacherDraftHomeworkActivity.this, R.string.java_singleteacherdrafthomeworkactivity_successfully_published, Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();

			}

			else {

			}



		};
	};


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
