package com.classtune.app.freeversion;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Syllabus;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SingleSyllabus extends ChildContainerActivity {
	
	
	private UIHelper uiHelper;
	private String id;
	private Gson gson;
	private Syllabus data;
	
	private ImageView imgSubjectIcon;
	private TextView txtSubjectName;
	private TextView txtLastUpdated;
	private ExpandableTextView txtContent;
	
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
		setContentView(R.layout.activity_single_syllabus);
		
		gson = new Gson();
		
		uiHelper = new UIHelper(SingleSyllabus.this);
		
		if(getIntent().getExtras() != null)
			this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_SYLLABUS);
		
		initView();
		initApicall();
		
	}
	
	
	
	private void initView()
	{
		this.imgSubjectIcon = (ImageView)this.findViewById(R.id.imgSubjectIcon);
		this.txtSubjectName = (TextView)this.findViewById(R.id.txtSubjectName);
		this.txtLastUpdated = (TextView)this.findViewById(R.id.txtLastUpdated);
		this.txtContent = (ExpandableTextView)this.findViewById(R.id.txtContent);
	}
	
	private void initAction()
	{
		this.imgSubjectIcon.setImageResource(AppUtility.getImageResourceId(data.getSubject_icon(), this));
		this.txtSubjectName.setText(data.getSubject_name());
		this.txtLastUpdated.setText(data.getLast_updated());
		this.txtContent.setText(Html.fromHtml(data.getSyllabus_text(), null, new MyTagHandler()));
		
	}
	
	
	private void initApicall()
	{
		RequestParams params = new RequestParams();

		//app.showLog("adfsdfs", app.getUserSecret());

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("id", this.id);
		
		AppRestClient.post(URLHelper.URL_SINGLE_SYLLABUS, params, singleSyllabusHandler);
	}
	
	AsyncHttpResponseHandler singleSyllabusHandler = new AsyncHttpResponseHandler() {

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
				
				JsonObject objSyllabus = modelContainer.getData().get("syllabus").getAsJsonObject();
				data = gson.fromJson(objSyllabus.toString(), Syllabus.class);
				
				
				initAction();
				
			}
			
			else {

			}
			
			

		};
	};
	
	

}
