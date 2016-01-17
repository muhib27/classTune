package com.classtune.app.freeversion;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.schoolapp.NotifyServiceReceiver;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.AssessmentQuestion;
import com.classtune.app.schoolapp.model.AssessmentQuestion.Option;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.CountDownTimerPausable;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.SharedPreferencesHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogAssessmentExitHomework;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogAssessmentNextQuestionHomework;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogAssessmentOk;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogAssessmentScore;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AssesmentHomeworkActivity extends ChildContainerActivity implements View.OnClickListener{
	
	private static final int ANSWER_TIME = 1000;
	private static final int REFRESH_TIME = 1500;
	
	
	private LinearLayout layoutChoice1;
	private LinearLayout layoutChoice2;
	private LinearLayout layoutChoice3;
	private LinearLayout layoutChoice4;
	
	private TextView txtChoice1;
	private TextView txtChoice2;
	private TextView txtChoice3;
	private TextView txtChoice4;
	
	private ImageView imgViewChoice1;
	private ImageView imgViewChoice2;
	private ImageView imgViewChoice3;
	private ImageView imgViewChoice4;
	
	private LinearLayout layoutSelected;
	
	private LinearLayout layoutLowerChoicePanel;
	
	
	private LinearLayout layoutSelectedLinear;
	
	private List<LinearLayout> listLayout;
	private List<View> listLayoutLinear;
	
	private int selectedPosition = 0;
	
	private UIHelper uiHelper;
	private UserHelper userHelper;
	
	private JsonObject objectPost;
	private String assesmentId;
	
	
	private TextView txtTitle;
	private TextView txtTopic;
	private TextView txtTime;
	//private TextView txtPlayCount;
	private TextView txtQuestion;
	
	private List<AssessmentQuestion> listAssessmentQuestion = new ArrayList<AssessmentQuestion>();
	private List<PopulateSummeryHomeworkData> listAssessmentQuestionSummery = new ArrayList<PopulateSummeryHomeworkData>();
	
	
	private int currentPosition = 0;
	
	private float score = 0;
	
	private float totalScore = 0;
	
	private TextView txtTimer;
	
	private CountDownTimerPausable countDownTimer = null;
	private boolean timerComplete = false;
	
	private boolean isLinearView = false;
	
	private LinearLayout layoutBoxDataHolder;
	private LinearLayout layoutLinearDataHolder;
	
	long tStart = 0; //= System.currentTimeMillis();
	long tEnd = 0; //= System.currentTimeMillis();
	long tDelta = 0;//tEnd - tStart;
	
	private int totalQuestionNumber = 0;
	
	private String postId = "";
	private String title = "";
	
	
	private FrameLayout layoutFrameDataHolder;
	
	private String currentDate = "";
	private int passPercentage = 0;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_assesment_homework);
		
		
		//String rootObj2 = getIntent().getExtras().getString(AppConstant.POST_FRAG_OBJECT2);  
		//objectPost = new Gson().fromJson(rootObj2, JsonObject.class);
		
		
		//postId = objectPost.get("id").getAsString();
		
		
		assesmentId = getIntent().getExtras().getString("ASSESSMENT_HOMEWORK_ID");  
		
		if(!TextUtils.isEmpty(assesmentId))
		{
			initApiCall();
		}
			
		
		
		userHelper = new UserHelper(this);
		uiHelper = new UIHelper(this);
		
		initView();
		initAction();
		
		
		//Log.e("USER_ID_ASS", "is: "+userHelper.getUser().getUserId());
		
		//sendNotification(AssesmentActivity.this.title+" quiz has beed activated now for you!", postId);
		
		//startNotification(this);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	   
	}
	
	private void initView()
	{
		
		this.listLayout = new ArrayList<LinearLayout>();
		this.listLayoutLinear = new ArrayList<View>();
		
		this.layoutChoice1 = (LinearLayout)this.findViewById(R.id.layoutChoice1);
		this.layoutChoice2 = (LinearLayout)this.findViewById(R.id.layoutChoice2);
		this.layoutChoice3 = (LinearLayout)this.findViewById(R.id.layoutChoice3);
		this.layoutChoice4 = (LinearLayout)this.findViewById(R.id.layoutChoice4);
		
		this.txtChoice1 = (TextView)this.findViewById(R.id.txtChoice1);
		this.txtChoice2 = (TextView)this.findViewById(R.id.txtChoice2);
		this.txtChoice3 = (TextView)this.findViewById(R.id.txtChoice3);
		this.txtChoice4 = (TextView)this.findViewById(R.id.txtChoice4);
		
		this.imgViewChoice1 = (ImageView)this.findViewById(R.id.imgViewChoice1);
		this.imgViewChoice2 = (ImageView)this.findViewById(R.id.imgViewChoice2);
		this.imgViewChoice3 = (ImageView)this.findViewById(R.id.imgViewChoice3);
		this.imgViewChoice4 = (ImageView)this.findViewById(R.id.imgViewChoice4);
		
		this.listLayout.add(this.layoutChoice1);
		this.listLayout.add(this.layoutChoice2);
		this.listLayout.add(this.layoutChoice3);
		this.listLayout.add(this.layoutChoice4);
		
		this.txtTitle = (TextView)this.findViewById(R.id.txtTitle);
		this.txtTitle.setSelected(true);
		this.txtTopic = (TextView)this.findViewById(R.id.txtTopic);
		this.txtTime = (TextView)this.findViewById(R.id.txtTime);
		//this.txtPlayCount = (TextView)this.findViewById(R.id.txtPlayCount);
		this.txtQuestion = (TextView)this.findViewById(R.id.txtQuestion);
		
		this.layoutLowerChoicePanel = (LinearLayout)this.findViewById(R.id.layoutLowerChoicePanel);
		
		this.txtTimer = (TextView)this.findViewById(R.id.txtTimer);
		//this.txtTimer.setText(String.valueOf(getTime(AppConstant.TOTAL_ASSESSMENT_TIME)));
		
		this.layoutBoxDataHolder = (LinearLayout)this.findViewById(R.id.layoutBoxDataHolder);
		this.layoutLinearDataHolder = (LinearLayout)this.findViewById(R.id.layoutLinearDataHolder);
		
		this.layoutFrameDataHolder = (FrameLayout)this.findViewById(R.id.layoutFrameDataHolder);
	}
	
	
	private void initTimer(long time)
	{
		if(this.countDownTimer == null)
		{
			this.countDownTimer = new CountDownTimerPausable(time, 1) {
		        public void onTick(long millisUntilFinished) {
		            //int seconds = (int) ((millisUntilFinished % 1000));

		            //Log.v("COUNT_DOWN", "timer: "+seconds);
		            //txtTimer.setText(String.valueOf(seconds)+" secomnds "+String.valueOf(millisUntilFinished/100)+ " ms");
		            
		            //txtTimer.setText(String.valueOf(millisUntilFinished/1000)+"."+String.valueOf((int)millisUntilFinished%10));
		            //int seconds = (int) (totalTime / 1000) % 60 ;
		            txtTimer.setText(convertSecondsToHMS(String.valueOf(millisUntilFinished / 1000)));
		        	
		            
		           // Log.v("PLAY_TIME_SINGLEDATA", "marks: "+AppConstant.PLAY_TIME);
		            
		        }

		        public void onFinish() {
		        	txtTimer.setText("00:00");
		        	
		        	timerComplete = true;
		        	countDownTimer = null;
		        	
		        	if(currentPosition == listAssessmentQuestion.size()-1)
		        	{

			        	if(!userHelper.isLoggedIn())
						{
							//showCustomDialogScore("SCORE", R.drawable.assessment_icon_popup, "Your score is "+getScore()+"/"+totalScore +" login to save your score");
							showCustomDialogOk(false, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
						}
						else
						{
							//R.drawable.assessment_icon_popup
							showCustomDialogOk(true, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
							
						}
		        	}
		        	else
		        	{
		        		currentPosition++;
		        		
		        		enableDisableClickListener(false);
		        		
		        		if(currentPosition < listAssessmentQuestion.size())
		        		{
		        			if(isValid)
								showCustomDialogNextQuestion("QUIZ", R.drawable.assessment_icon_popup, "Congratulations! \nYou got it right.",  listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getAnswer(), listAssessmentQuestion.get(currentPosition-1).getQuestion(), listAssessmentQuestion.get(currentPosition-1).getExplanation());
							else
							{
								if(currentPosition <= listAssessmentQuestion.size())
								{
									listAssessmentQuestionSummery.add(new PopulateSummeryHomeworkData(listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getId(), listAssessmentQuestion.get(currentPosition-1).getQuestion(), isValid, 
											listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getAnswer(), 
											listAssessmentQuestion.get(currentPosition-1).getExplanation()));
								}
								
								showCustomDialogNextQuestion("QUIZ", R.drawable.assessment_icon_popup, "Time Out!",  listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getAnswer(), listAssessmentQuestion.get(currentPosition-1).getQuestion(), listAssessmentQuestion.get(currentPosition-1).getExplanation());
		        			
			        			
							}
		        		}
		        		
		        	}
		        	
		        	
		        }
		    };

		    this.countDownTimer.start();
		}
	}
	
	private void initAction()
	{
		
		layoutChoice1.setOnClickListener(this);
		layoutChoice2.setOnClickListener(this);
		layoutChoice3.setOnClickListener(this);
		layoutChoice4.setOnClickListener(this);
		
		
		
	}
	
	
	private void enableDisableClickListener(boolean yes)
	{
		if(yes)
		{
			layoutChoice1.setOnClickListener(null);
			layoutChoice2.setOnClickListener(null);
			layoutChoice3.setOnClickListener(null);
			layoutChoice4.setOnClickListener(null);
		}
		else
		{
			layoutChoice1.setOnClickListener(this);
			layoutChoice2.setOnClickListener(this);
			layoutChoice3.setOnClickListener(this);
			layoutChoice4.setOnClickListener(this);
		}
				
		
	}
	
	
	private void enableDisableClickListenerLinearView(List<View> list, boolean yes)
	{
		if(yes)
		{
			for(int i = 0;i<list.size();i++)
			{
				list.get(i).setOnClickListener(null);
			}
		}
		
	}
	
	
	
	//new yellow #f9f277
	//old yellow #fff200
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		currentPosition++;
		if(currentPosition < listAssessmentQuestion.size()){
			//populateDataQuestionAfterFirstTime(listAssessmentQuestion);
			
		}
		else{
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					if(!userHelper.isLoggedIn())
					{
						//showCustomDialogScore("SCORE", R.drawable.assessment_icon_popup, "Your score is "+getScore()+"/"+totalScore +" login to save your score");
						showCustomDialogOk(false, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
					}
					else
					{
						//R.drawable.assessment_icon_popup
						showCustomDialogOk(true, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
						
					}
				}
			}, REFRESH_TIME);
			
			
			
		}
		
		
		
		switch (view.getId()) {
		case R.id.layoutChoice1:
			//layoutChoice1.setBackgroundResource(R.drawable.shape_answer_stroke_yellow);
			layoutChoice1.setBackgroundColor(Color.parseColor("#f9f277"));
			layoutSelected = layoutChoice1;
			selectedPosition = 0;
			break;
			
		case R.id.layoutChoice2	:
			
			//layoutChoice2.setBackgroundResource(R.drawable.shape_answer_stroke_yellow);
			layoutChoice2.setBackgroundColor(Color.parseColor("#f9f277"));
			layoutSelected = layoutChoice2;
			selectedPosition = 1;
			break;
			
		case R.id.layoutChoice3	:
			//layoutChoice3.setBackgroundResource(R.drawable.shape_answer_stroke_yellow);
			layoutChoice3.setBackgroundColor(Color.parseColor("#f9f277"));
			layoutSelected = layoutChoice3;
			selectedPosition = 2;
			break;
			
		case R.id.layoutChoice4	:
			//layoutChoice4.setBackgroundResource(R.drawable.shape_answer_stroke_yellow);
			layoutChoice4.setBackgroundColor(Color.parseColor("#f9f277"));
			layoutSelected = layoutChoice4;
			selectedPosition = 3;
			break;

		default:
			break;
		}
		
		
		Log.e("IS_VALID", "is: " + isValidChoosen());
		Log.e("MARKS", "is: " + getScore());
		
		
		Log.e("ANSWER_SELECTED", "pos: " + selectedPosition);
		
		for(LinearLayout layout : listLayout)
		{
			if(layout != layoutSelected)
			{
				layout.setBackgroundResource(0);
			}
		}
		
		
		super.onClick(view);
	}
	
	
	private void initApiCall() {


		RequestParams params = new RequestParams();
		params.put("id", assesmentId);
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		

		AppRestClient.post(URLHelper.URL_HOMEWORK_ASSESSMENT, params,
				assessmentHandler);
	}
	
	private void initApiCallAddmark() {

		

		RequestParams params = new RequestParams();
		
		params.put("id", assesmentId);
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("start_time", currentDate);
		params.put("total_score", String.valueOf(getScore()));
		params.put("total_time", String.valueOf(getEllaspsedSeconds()));
		
		
		
		
		if(listAssessmentQuestionSummery.size() < listAssessmentQuestion.size())
		{
			for(int i=0; i<listAssessmentQuestionSummery.size();i++)
			{
				params.put("answer_question["+ String.valueOf(i)+"]", listAssessmentQuestion.get(i).getId());
			}
		}
		else
		{
			for(int i=0;i<listAssessmentQuestion.size();i++)
			{
				//Log.e("checking", ""+"answer_question["+String.valueOf(i)+"]"+": "+listAssessmentQuestion.get(i).getId());
				params.put("answer_question["+ String.valueOf(i)+"]", listAssessmentQuestion.get(i).getId());
			}
		}
		
		for(int i=0;i<listAssessmentQuestionSummery.size();i++)
		{
			
			//Log.e("checking", ""+"answer_option["+String.valueOf(i)+"]"+": "+listAssessmentQuestionSummery.get(i).getId());
			params.put("answer_option["+ String.valueOf(i)+"]", listAssessmentQuestionSummery.get(i).getId());
			
			if(listAssessmentQuestionSummery.get(i).isRightAnswer())
			{
				//Log.e("checking", ""+"answer_correct["+String.valueOf(i)+"]"+": "+"1");
				params.put("answer_correct["+ String.valueOf(i)+"]", "1");
			}
			else
			{
				//Log.e("checking", ""+"answer_correct["+String.valueOf(i)+"]"+": "+"0");
				params.put("answer_correct["+ String.valueOf(i)+"]", "0");
			}
			
		}
	
		
		int percentage = (int) ((getScore()*100)/totalScore);
		
		
		if(percentage >= passPercentage)
		{
			params.put("is_passed", "1");
		}
		else
		{
			params.put("is_passed", "0");
		}
		
		
		AppRestClient.post(URLHelper.URL_HOMEWORK_ASSESSMENT_ADDMARK, params,
				assessmentAddMarkHandler);
	}
	
	
	private void initApiCallPlayCount() {


		RequestParams params = new RequestParams();
		params.put("assessment_id", assesmentId);
		
		

		AppRestClient.post(URLHelper.URL_ASSESSMENT_UPDATE_PLAY, params,
				assessmentUpdatePlay);
	}
	
	
	private AsyncHttpResponseHandler assessmentHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
		};

		@Override
		public void onStart() {
			uiHelper.showLoadingDialog("Please wait...");
		};

		@Override
		public void onSuccess(String responseString) {
			//Log.e("ASSESSMENT", "data: "+responseString);
			
			
			
			tStart = System.currentTimeMillis();

			uiHelper.dismissLoadingDialog();

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {
				
				currentDate = modelContainer.getData().get("current_date").getAsString();
				
				
				
				JsonObject assessment = modelContainer.getData().get("assesment").getAsJsonObject();
				
				passPercentage = assessment.get("pass_percentage").getAsInt();
				
				
				String title = assessment.get("title").getAsString();
				
				
				JsonArray arrayquestion = assessment.get("question").getAsJsonArray();
				
				List<AssessmentQuestion> data = parseQuestion(arrayquestion.toString());
				listAssessmentQuestion = data;
				
				//Log.e("QQQ", "is: "+data.get(0).getListQuestion().get(0).getAnswer());
				AssesmentHomeworkActivity.this.title = title;
				
				
				populateDataQuestion(listAssessmentQuestion);
				
				for(int i=0;i<listAssessmentQuestion.size();i++)
				{
					
					float sc = Float.parseFloat(listAssessmentQuestion.get(i).getMark());
					totalScore = totalScore+sc;
				}
				
				populateData(title);
				
				initTimer(Long.parseLong(listAssessmentQuestion.get(0).getTime()) * 1000);
				
				
				totalQuestionNumber = listAssessmentQuestion.size();
				
			}
			
			if (modelContainer.getStatus().getCode() == 404) 
			{
				Toast.makeText(AssesmentHomeworkActivity.this, "You have already completed this quiz!", Toast.LENGTH_SHORT).show();
				AssesmentHomeworkActivity.this.finish();
			}
			

			else {
				
				

			}

		};

	};
	
	private void populateData(String title)
	{
		this.txtTitle.setText(title);
		
	}
	
	
	
	private AsyncHttpResponseHandler assessmentAddMarkHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
		};

		@Override
		public void onStart() {
			uiHelper.showLoadingDialog("Please wait...");
		};

		@Override
		public void onSuccess(String responseString) {
			Log.e("ASSESSMENT_ADDMARKS", "data: " + responseString);

			uiHelper.dismissLoadingDialog();

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) 
			{
				
				Toast.makeText(AssesmentHomeworkActivity.this, "Marks added successfully", Toast.LENGTH_SHORT).show();
				
				AssesmentHomeworkActivity.this.finish();
			}

			else {

			}
				
			/*if (modelContainer.getStatus().getCode() == 404) 
			{
				Toast.makeText(AssesmentHomeworkActivity.this, "You are already in leader board!", Toast.LENGTH_LONG).show();
				
				AssesmentHomeworkActivity.this.finish();
			}
			else if(modelContainer.getStatus().getCode() == 200)
			{
				Toast.makeText(AssesmentHomeworkActivity.this, "Marks added successfully", Toast.LENGTH_SHORT).show();
				//sendNotification(AssesmentActivity.this.title+" quiz has beed activated now for you!", postId);
				//startNotification(AssesmentActivity.this);
				AssesmentHomeworkActivity.this.finish();
			}*/
				
			
			//sendNotification(AssesmentActivity.this.title+" quiz has beed activated now for you!", postId);
			

		};

	};
	
	private AsyncHttpResponseHandler assessmentUpdatePlay = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			//uiHelper.showMessage(arg1);
			//uiHelper.dismissLoadingDialog();
		};

		@Override
		public void onStart() {
			//uiHelper.showLoadingDialog("Please wait...");
		};

		@Override
		public void onSuccess(String responseString) {
			Log.e("ASSESSMENT_ADDMARKS", "data: " + responseString);

			//uiHelper.dismissLoadingDialog();

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {
				
				
			}

			else {

			}

		};

	};
	
	
	
	public List<AssessmentQuestion> parseQuestion(String object) {

		List<AssessmentQuestion> tags = new ArrayList<AssessmentQuestion>();
		Type listType = new TypeToken<List<AssessmentQuestion>>() {
		}.getType();
		tags = (List<AssessmentQuestion>) new Gson().fromJson(object, listType);
		return tags;
	}
	
	
	
	
	private void populateDataQuestion(List<AssessmentQuestion> listAssessmentQuestion)
	{
		layoutLinearDataHolder.removeAllViews();
		listLayoutLinear.clear();
		
		if(listAssessmentQuestion.get(currentPosition).getStyle().equals("1"))
		{
			isLinearView = true;
		}
		else //if(listAssessmentQuestion.get(currentPosition).getStyle().equals("2"))
		{
			isLinearView = false;
		}
		
		
		/*for(int i=0;i<listAssessmentQuestion.get(currentPosition).getListQuestion().size();i++)
		{
			if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage()))
			{
				isLinearView = true;
			}
			else
			{
				isLinearView = false;
			}
		}*/
		
		
		if(isLinearView == false)
		{
			layoutBoxDataHolder.setVisibility(View.GONE);
			layoutLinearDataHolder.setVisibility(View.VISIBLE);
			
			txtQuestion.setText(listAssessmentQuestion.get(currentPosition).getQuestion());
			
			for(int i=0;i<listAssessmentQuestion.get(currentPosition).getListQuestion().size();i++)
			{
				//if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage()))
				//{
					
					View child = getLayoutInflater().inflate(R.layout.row_assessment_linear_view, null);
					TextView txtNumPos = (TextView)child.findViewById(R.id.txtNumPos);
					txtNumPos.setText(String.valueOf(i + 1));
					child.setTag(i);
					listLayoutLinear.add(child);
					
					ImageView img = (ImageView)child.findViewById(R.id.imgLinView);
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage()))
					{
						img.setVisibility(View.VISIBLE);
						SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage(), img);
					}
					else
						img.setVisibility(View.GONE);
					//layoutLinearDataHolder.addView(img);
					
					TextView tv = (TextView)child.findViewById(R.id.txtViewLin);
					tv.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswer());
					//layoutLinearDataHolder.addView(tv);
					child.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.e("CHILD_TAG", "tag: " + (Integer) v.getTag());
							
							selectedPosition = (Integer)v.getTag();
							
							layoutSelectedLinear = (LinearLayout) v;
							layoutSelectedLinear.setBackgroundColor(Color.parseColor("#f9f277"));
							
							currentPosition++;
							if(currentPosition < AssesmentHomeworkActivity.this.listAssessmentQuestion.size()){
								//populateDataQuestionAfterFirstTime(listAssessmentQuestion);
								
							}
							else{
								Handler handler = new Handler();
								handler.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										
										if(!userHelper.isLoggedIn())
										{
											//showCustomDialogScore("SCORE", R.drawable.assessment_icon_popup, "Your score is "+getScore()+"/"+totalScore +" login to save your score");
											showCustomDialogOk(false, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
										}
										else
										{
											//R.drawable.assessment_icon_popup
											showCustomDialogOk(true, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
											
										}
									}
								}, REFRESH_TIME);
								
								
								
							}
							
							enableDisableClickListenerLinearView(listLayoutLinear, true);
							isValidChoosen();
							
							
						}
					});
					
					//listLayoutLinear.add(child);
					layoutLinearDataHolder.addView(child);
				//}
				
			}
		}
		else
		{
			layoutBoxDataHolder.setVisibility(View.VISIBLE);
			layoutLinearDataHolder.setVisibility(View.GONE);
			
			
			if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() >=4 )
			{
				layoutLowerChoicePanel.setVisibility(View.VISIBLE);
			}
			else
			{
				layoutLowerChoicePanel.setVisibility(View.GONE);
			}

			
			txtQuestion.setText(listAssessmentQuestion.get(currentPosition).getQuestion());
			
			txtChoice1.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswer());
			txtChoice2.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswer());
			
			if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
			{
				txtChoice3.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswer());
				txtChoice4.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswer());
			}
			
			if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswerImage()))
			{
				imgViewChoice1.setVisibility(View.VISIBLE);
				SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage(), imgViewChoice1);
				/*ImageLoader.getInstance().displayImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswerImage(), imgViewChoice1, AppUtility.getOption(), new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						k
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});*/
			}
			else
			{
				imgViewChoice1.setVisibility(View.GONE);
			}
				
			
			if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage()))
			{
				imgViewChoice2.setVisibility(View.VISIBLE);
				SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage(), imgViewChoice2);
			}
			else
			{
				imgViewChoice2.setVisibility(View.GONE);
			}
			
			
			if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
			{
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswerImage()))
				{
					imgViewChoice3.setVisibility(View.VISIBLE);
					SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswerImage(), imgViewChoice3);
				}
				else
				{
					imgViewChoice3.setVisibility(View.GONE);
				}
				
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswerImage()))
				{
					imgViewChoice4.setVisibility(View.VISIBLE);
					SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswerImage(), imgViewChoice4);
				}
				else
				{
					imgViewChoice4.setVisibility(View.GONE);
				}
			}
			
			
			if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswer()))
			{
				txtChoice1.setVisibility(View.VISIBLE);
				
			}
			else
			{
				txtChoice1.setVisibility(View.GONE);
				
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				imgViewChoice1.setLayoutParams(layoutParams);
			}
			
			if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswer()))
			{
				txtChoice2.setVisibility(View.VISIBLE);
			}
			else
			{
				txtChoice2.setVisibility(View.GONE);
				
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				imgViewChoice2.setLayoutParams(layoutParams);
			}
			
			if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
			{
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswer()))
				{
					txtChoice3.setVisibility(View.VISIBLE);
				}
				else
				{
					txtChoice3.setVisibility(View.GONE);
					
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					imgViewChoice3.setLayoutParams(layoutParams);
				}
				
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswer()))
				{
					txtChoice4.setVisibility(View.VISIBLE);
				}
				else
				{
					txtChoice4.setVisibility(View.GONE);
					
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					imgViewChoice4.setLayoutParams(layoutParams);
				}
				
			}
			
			
		}
		
		
		
		
	}
	
	
	private void populateDataQuestionAfterFirstTime(final List<AssessmentQuestion> listAssessmentQuestion)
	{
		
		//enableDisableClickListener(true);
		
		
		
		layoutLinearDataHolder.removeAllViews();
		listLayoutLinear.clear();
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				enableDisableClickListener(false);
				enableDisableClickListenerLinearView(listLayoutLinear, false);
				layoutFrameDataHolder.setVisibility(View.VISIBLE);
				txtQuestion.setVisibility(View.VISIBLE);
				
				
				
				
				if(listAssessmentQuestion.get(currentPosition).getStyle().equals("1"))
				{
					isLinearView = true;
				}
				else //if(listAssessmentQuestion.get(currentPosition).getStyle().equals("2"))
				{
					isLinearView = false;
				}
				
				/*for(int i=0;i<listAssessmentQuestion.get(currentPosition).getListQuestion().size();i++)
				{
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage()))
					{
						isLinearView = true;
					}
					else
					{
						isLinearView = false;
					}
				}*/
				
				
				if(isLinearView == false)
				{
					layoutBoxDataHolder.setVisibility(View.GONE);
					layoutLinearDataHolder.setVisibility(View.VISIBLE);
					
					
					txtQuestion.setText(listAssessmentQuestion.get(currentPosition).getQuestion());
					
					for(int i=0;i<listAssessmentQuestion.get(currentPosition).getListQuestion().size();i++)
					{
						//if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage()))
						//{
							
							View child = getLayoutInflater().inflate(R.layout.row_assessment_linear_view, null);
							TextView txtNumPos = (TextView)child.findViewById(R.id.txtNumPos);
							txtNumPos.setText(String.valueOf(i + 1));
							child.setTag(i);
							listLayoutLinear.add(child);
							
							ImageView img = (ImageView)child.findViewById(R.id.imgLinView);
							if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage()))
							{
								img.setVisibility(View.VISIBLE);
								SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage(), img);
							}
							else
								img.setVisibility(View.GONE);
							//layoutLinearDataHolder.addView(img);
							
							TextView tv = (TextView)child.findViewById(R.id.txtViewLin);
							tv.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswer());
							//layoutLinearDataHolder.addView(tv);
							child.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Log.e("CHILD_TAG", "tag: " + (Integer) v.getTag());
									
									selectedPosition = (Integer)v.getTag();
									
									layoutSelectedLinear = (LinearLayout) v;
									layoutSelectedLinear.setBackgroundColor(Color.parseColor("#f9f277"));
									
									currentPosition++;
									if(currentPosition < AssesmentHomeworkActivity.this.listAssessmentQuestion.size()){
										//populateDataQuestionAfterFirstTime(listAssessmentQuestion);
										
									}
									else{
										Handler handler = new Handler();
										handler.postDelayed(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												
												if(!userHelper.isLoggedIn())
												{
													//showCustomDialogScore("SCORE", R.drawable.assessment_icon_popup, "Your score is "+getScore()+"/"+totalScore +" login to save your score");
													showCustomDialogOk(false, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
												}
												else
												{
													//R.drawable.assessment_icon_popup
													showCustomDialogOk(true, false, "Your score is", String.valueOf(getScore() + "/" + String.valueOf(totalScore)), "", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
													
												}
											}
										}, REFRESH_TIME);
										
										
										
									}
									
									enableDisableClickListenerLinearView(listLayoutLinear, true);
									isValidChoosen();
								}
							});
							
							
							//listLayoutLinear.add(child);
							layoutLinearDataHolder.addView(child);
						//}
						
					}
				}
				else
				{
					layoutBoxDataHolder.setVisibility(View.VISIBLE);
					layoutLinearDataHolder.setVisibility(View.GONE);
					
					
					if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() >=4 )
					{
						layoutLowerChoicePanel.setVisibility(View.VISIBLE);
					}
					else
					{
						layoutLowerChoicePanel.setVisibility(View.GONE);
					}

					
					txtQuestion.setText(listAssessmentQuestion.get(currentPosition).getQuestion());
					
					txtChoice1.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswer());
					txtChoice2.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswer());
					
					if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
					{
						txtChoice3.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswer());
						txtChoice4.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswer());
					}
					
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswerImage()))
					{
						imgViewChoice1.setVisibility(View.VISIBLE);
						SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage(), imgViewChoice1);
						/*ImageLoader.getInstance().displayImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswerImage(), imgViewChoice1, AppUtility.getOption(), new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								// TODO Auto-generated method stub
								k
							}
							
							@Override
							public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								// TODO Auto-generated method stub
								
							}
						});*/
					}
					else
					{
						imgViewChoice1.setVisibility(View.GONE);
					}
						
					
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage()))
					{
						imgViewChoice2.setVisibility(View.VISIBLE);
						SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage(), imgViewChoice2);
					}
					else
					{
						imgViewChoice2.setVisibility(View.GONE);
					}
					
					
					if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
					{
						if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswerImage()))
						{
							imgViewChoice3.setVisibility(View.VISIBLE);
							SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswerImage(), imgViewChoice3);
						}
						else
						{
							imgViewChoice3.setVisibility(View.GONE);
						}
						
						if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswerImage()))
						{
							imgViewChoice4.setVisibility(View.VISIBLE);
							SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswerImage(), imgViewChoice4);
						}
						else
						{
							imgViewChoice4.setVisibility(View.GONE);
						}
					}
					
					
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswer()))
					{
						txtChoice1.setVisibility(View.VISIBLE);
						
					}
					else
					{
						txtChoice1.setVisibility(View.GONE);
						
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						imgViewChoice1.setLayoutParams(layoutParams);
					}
					
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswer()))
					{
						txtChoice2.setVisibility(View.VISIBLE);
					}
					else
					{
						txtChoice2.setVisibility(View.GONE);
						
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						imgViewChoice2.setLayoutParams(layoutParams);
					}
					
					if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
					{
						if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswer()))
						{
							txtChoice3.setVisibility(View.VISIBLE);
						}
						else
						{
							txtChoice3.setVisibility(View.GONE);
							
							LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
							imgViewChoice3.setLayoutParams(layoutParams);
						}
						
						if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswer()))
						{
							txtChoice4.setVisibility(View.VISIBLE);
						}
						else
						{
							txtChoice4.setVisibility(View.GONE);
							
							LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
							imgViewChoice4.setLayoutParams(layoutParams);
						}
						
					}
					
					
				}

				/*if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() >=4 )
				{
					layoutLowerChoicePanel.setVisibility(View.VISIBLE);
				}
				else
				{
					layoutLowerChoicePanel.setVisibility(View.GONE);
				}

				
				txtQuestion.setText(listAssessmentQuestion.get(currentPosition).getQuestion());
				
				txtChoice1.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswer());
				txtChoice2.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswer());
				
				if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
				{
					txtChoice3.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswer());
					txtChoice4.setText(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswer());
				}
				
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswerImage()))
				{
					imgViewChoice1.setVisibility(View.VISIBLE);
					SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswerImage(), imgViewChoice1);
				}
				else
				{
					imgViewChoice1.setVisibility(View.GONE);
				}
					
				
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage()))
				{
					imgViewChoice2.setVisibility(View.VISIBLE);
					SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswerImage(), imgViewChoice2);
				}
				else
				{
					imgViewChoice2.setVisibility(View.GONE);
				}
				
				
				if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
				{
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswerImage()))
					{
						imgViewChoice3.setVisibility(View.VISIBLE);
						SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswerImage(), imgViewChoice3);
					}
					else
					{
						imgViewChoice3.setVisibility(View.GONE);
					}
					
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswerImage()))
					{
						imgViewChoice4.setVisibility(View.VISIBLE);
						SchoolApp.getInstance().displayUniversalImage(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswerImage(), imgViewChoice4);
					}
					else
					{
						imgViewChoice4.setVisibility(View.GONE);
					}
				}
				
				
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(0).getAnswer()))
				{
					txtChoice1.setVisibility(View.VISIBLE);
					
				}
				else
				{
					txtChoice1.setVisibility(View.GONE);
					
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					imgViewChoice1.setLayoutParams(layoutParams);
				}
				
				if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(1).getAnswer()))
				{
					txtChoice2.setVisibility(View.VISIBLE);
				}
				else
				{
					txtChoice2.setVisibility(View.GONE);
					
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					imgViewChoice2.setLayoutParams(layoutParams);
				}
				
				if(listAssessmentQuestion.get(currentPosition).getListQuestion().size() > 2)
				{
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(2).getAnswer()))
					{
						txtChoice3.setVisibility(View.VISIBLE);
					}
					else
					{
						txtChoice3.setVisibility(View.GONE);
						
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						imgViewChoice3.setLayoutParams(layoutParams);
					}
					
					if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(3).getAnswer()))
					{
						txtChoice4.setVisibility(View.VISIBLE);
					}
					else
					{
						txtChoice4.setVisibility(View.GONE);
						
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						imgViewChoice4.setLayoutParams(layoutParams);
					}
					
				}*/
			
			
				//enableDisableClickListener(false);
				
				
			}
		}, REFRESH_TIME);
		
		
		
	}


	private void measureStyle()
	{
		if(listAssessmentQuestion.get(currentPosition).getStyle().equals("1"))
		{
			isLinearView = false;
		}
		else if(listAssessmentQuestion.get(currentPosition).getStyle().equals("2"))
		{
			isLinearView = true;
		}
		
		
		for(int i=0;i<listAssessmentQuestion.get(currentPosition).getListQuestion().size();i++)
		{
			if(!TextUtils.isEmpty(listAssessmentQuestion.get(currentPosition).getListQuestion().get(i).getAnswerImage()))
			{
				isLinearView = true;
			}
			else
			{
				isLinearView = false;
			}
		}
	}
	
	
	private int rightAnswerPos = 0;
	private boolean isValid = false;
	
	
	
	// new green #c5f2c5
	//new red f85961
	
	//old green #00ff00
	//old red #d2252d
	
	private boolean isValidChoosen()
	{
		enableDisableClickListener(true);
		//boolean isValid = false;
		//int rightAnswerPos = 0;
		Log.e("SIZE", "ll: " + listLayoutLinear.size());
		
		if(currentPosition-1 < listAssessmentQuestion.size())
		{
			List<Option> listQuestion = listAssessmentQuestion.get(currentPosition-1).getListQuestion();
			
			
			for(int i=0;i<listQuestion.size();i++)
			{
				if(listQuestion.get(i).getCorrect().equalsIgnoreCase("1"))
				{
					rightAnswerPos = i;
				}
			}
			
			Log.e("RIGHT_ANS_POS", "is: " + rightAnswerPos);
			
			if(selectedPosition == rightAnswerPos)
				isValid = true;
			else 
				isValid = false;
			
			if(isValid)
			{
				score = score + Float.parseFloat(listAssessmentQuestion.get(currentPosition - 1).getMark());
			}
			
			
		}
		
		
		Handler handler = new Handler();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(countDownTimer!= null && !countDownTimer.isPaused())
					countDownTimer.pause();
				
				switch (rightAnswerPos) {
				case 0:
					//layoutChoice1.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					layoutChoice1.setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;
					
				case 1:
					//layoutChoice2.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					layoutChoice2.setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;
					
				case 2:
					//layoutChoice3.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					layoutChoice3.setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;
					
				case 3:
					//layoutChoice4.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					layoutChoice4.setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;

				default:
					break;
				}
				
				if(isValid == false)
				{
					switch (selectedPosition) {
					case 0:
						//layoutChoice1.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						layoutChoice1.setBackgroundColor(Color.parseColor("#f85961"));
						break;
						
					case 1:
						//layoutChoice2.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						layoutChoice2.setBackgroundColor(Color.parseColor("#f85961"));
						break;
						
					case 2:
						//layoutChoice3.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						layoutChoice3.setBackgroundColor(Color.parseColor("#f85961"));
						break;
						
					case 3:
						//layoutChoice4.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						layoutChoice4.setBackgroundColor(Color.parseColor("#f85961"));
						break;

					default:
						break;
					}
				}

				
				
				
				
				switch (rightAnswerPos) {
				case 0:
					//layoutChoice1.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					if(listLayoutLinear.size() > 0 && listLayoutLinear.get(0) != null)
						listLayoutLinear.get(0).setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;
					
				case 1:
					//layoutChoice2.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					if(listLayoutLinear.size() > 0 && listLayoutLinear.get(1) != null)
						listLayoutLinear.get(1).setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;
					
				case 2:
					//layoutChoice3.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					if(listLayoutLinear.size() > 0 && listLayoutLinear.get(2) != null)
						listLayoutLinear.get(2).setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;
					
				case 3:
					//layoutChoice4.setBackgroundResource(R.drawable.shape_answer_stroke_green);
					if(listLayoutLinear.size() > 0 && listLayoutLinear.get(3) != null)
						listLayoutLinear.get(3).setBackgroundColor(Color.parseColor("#c5f2c5"));
					break;

				default:
					break;
				}
				
				if(isValid == false)
				{
					switch (selectedPosition) {
					case 0:
						//layoutChoice1.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						if(listLayoutLinear.size() > 0 && listLayoutLinear.get(0) != null)
							listLayoutLinear.get(0).setBackgroundColor(Color.parseColor("#f85961"));
						break;
						
					case 1:
						//layoutChoice2.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						if(listLayoutLinear.size() > 0 && listLayoutLinear.get(1) != null)
							listLayoutLinear.get(1).setBackgroundColor(Color.parseColor("#f85961"));
						break;
						
					case 2:
						//layoutChoice3.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						if(listLayoutLinear.size() > 0 && listLayoutLinear.get(2) != null)
							listLayoutLinear.get(2).setBackgroundColor(Color.parseColor("#f85961"));
						break;
						
					case 3:
						//layoutChoice4.setBackgroundResource(R.drawable.shape_answer_stroke_red);
						if(listLayoutLinear.size() > 0 && listLayoutLinear.get(3) != null)
							listLayoutLinear.get(3).setBackgroundColor(Color.parseColor("#f85961"));
						break;

					default:
						break;
					}
				}
				
				
			}
		}, ANSWER_TIME);
		
		
		
		
		
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if(countDownTimer!= null && countDownTimer.isPaused())
					countDownTimer.start();
				
				
				for(LinearLayout layout : listLayout)
				{
					
					layout.setBackgroundResource(0);
					
				}
				
				if(isLinearView)
				{
					if(listLayoutLinear.size() > 0)
					{
						for(View v : listLayoutLinear)
						{
							v.setBackgroundResource(0);						
						}
					}
				}
				
				if(currentPosition < listAssessmentQuestion.size())
				{
					
					enableDisableClickListener(false);
					
					if(isValid)
						showCustomDialogNextQuestion("QUIZ", R.drawable.assessment_icon_popup, "Congratulations! \nYou got it right.", listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getAnswer(), listAssessmentQuestion.get(currentPosition-1).getQuestion(), listAssessmentQuestion.get(currentPosition-1).getExplanation());
					else
						showCustomDialogNextQuestion("QUIZ", R.drawable.assessment_icon_popup, "Opps! Wrong answer.",  listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getAnswer(), listAssessmentQuestion.get(currentPosition-1).getQuestion(), listAssessmentQuestion.get(currentPosition-1).getExplanation());
				}
				
				isValid = false;
				isLinearView = false;
				
			}
		}, REFRESH_TIME);
		
		
		if(currentPosition <= listAssessmentQuestion.size())
		{
			listAssessmentQuestionSummery.add(new PopulateSummeryHomeworkData(listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getId(), listAssessmentQuestion.get(currentPosition-1).getQuestion(), isValid, 
					listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getAnswer(), 
					listAssessmentQuestion.get(currentPosition-1).getExplanation()));
		}
		
		
		
		return isValid;
	}
	
	
	private float getScore()
	{
		return this.score;
	}
	
	private void showCustomDialogScore(String headerText, int imgResId,
			String descriptionText) {

			
			PopupDialogAssessmentScore picker = PopupDialogAssessmentScore.newInstance(0);
			picker.setData(headerText,descriptionText,imgResId, AssesmentHomeworkActivity.this);
			picker.show(getSupportFragmentManager(), null);
	}
	
	
	
	private void showCustomDialogNextQuestion(String headerText, int imgResId,
			String descriptionText, String answerText, String questionText, String explanationText) {
		
			

			if(countDownTimer != null && !countDownTimer.isPaused())
			{
				countDownTimer.pause();
			}
			
			
			PopupDialogAssessmentNextQuestionHomework picker = PopupDialogAssessmentNextQuestionHomework.newInstance(0);
			picker.setData(headerText,descriptionText,imgResId, AssesmentHomeworkActivity.this, answerText, questionText, explanationText, new PopupDialogAssessmentNextQuestionHomework.IButtonclick() {
				
				@Override
				public void onNextButtonClick() {
					// TODO Auto-generated method stub
					enableDisableClickListener(true);
					enableDisableClickListenerLinearView(listLayoutLinear, true);
					layoutFrameDataHolder.setVisibility(View.GONE);
					txtQuestion.setVisibility(View.GONE);
					
					
					
					populateDataQuestionAfterFirstTime(listAssessmentQuestion);
					
					//countDownTimer.start();
					//if(countDownTimer != null)
					//{
						//countDownTimer.cancel();
						//countDownTimer = null;
						
						countDownTimer = null;
						if(currentPosition < listAssessmentQuestion.size())
							initTimer(Long.parseLong(listAssessmentQuestion.get(currentPosition).getTime()) * 1000);
					//}
						
						
				}
				
				@Override
				public void onExplanationButtonClick() {
					// TODO Auto-generated method stub
					
				}
			});
			picker.show(getSupportFragmentManager(), null);
	}
	
	
	private void showCustomDialogOk(final boolean isLoggedIn, boolean isShowRetryButton, String titleText, String scoreText, String description, int iconResId,
			Context context) {

			if(countDownTimer != null && !countDownTimer.isPaused())
				countDownTimer.pause();
			
			
			
			if(listAssessmentQuestionSummery.size() == listAssessmentQuestion.size()-1)
			{
				listAssessmentQuestionSummery.add(new PopulateSummeryHomeworkData(listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getId(), listAssessmentQuestion.get(currentPosition-1).getQuestion(), isValid, 
						listAssessmentQuestion.get(currentPosition-1).getListQuestion().get(rightAnswerPos).getAnswer(), 
						listAssessmentQuestion.get(currentPosition-1).getExplanation()));
			}
		
		
			setResult(Activity.RESULT_OK);
			PopupDialogAssessmentOk picker = PopupDialogAssessmentOk.newInstance(0);
			picker.setData(isLoggedIn, isShowRetryButton, titleText, scoreText, description, iconResId, AssesmentHomeworkActivity.this, new PopupDialogAssessmentOk.OkCallback() {

				@Override
				public void onLoginCalled() {
					// TODO Auto-generated method stub
					if(isLoggedIn)
					{
						initApiCallAddmark();
					}
				}

				@Override
				public void onRetryCalled() {
					// TODO Auto-generated method stub
					reloadActivity();
				}

				@Override
				public void onSummeryCalled() {
					// TODO Auto-generated method stub
					/*Log.e("SUMMERY", "is: "+listAssessmentQuestionSummery.get(0).getQuestion());
					Log.e("SUMMERY", "is: "+listAssessmentQuestionSummery.get(0).isRightAnswer());
					Log.e("SUMMERY", "is: "+listAssessmentQuestionSummery.get(0).getExplanation());
					Log.e("SUMMERY", "is: "+listAssessmentQuestionSummery.get(0).getAnswer());*/
					
					Gson gson = new Gson();
					Intent intent = new Intent(AssesmentHomeworkActivity.this, AssessmentSummeryActivity.class);
					intent.putExtra("assessment_summery", gson.toJson(listAssessmentQuestionSummery));
					
					startActivity(intent);
					
				}
				
				
			});
			picker.show(getSupportFragmentManager(), null);
	}
	
	
	private void showCustomialogExit(String titleText, String description, int iconResId, Context context)
	{
		
		if(countDownTimer != null && !countDownTimer.isPaused())
		{
			countDownTimer.pause();
		}
		
		
		PopupDialogAssessmentExitHomework picker = PopupDialogAssessmentExitHomework.newInstance(0);
		picker.setData(titleText, description, iconResId, context, new PopupDialogAssessmentExitHomework.IButtonclick(){

			@Override
			public void onExitButtonClick() {
				// TODO Auto-generated method stub
				initApiCallAddmark();
			}

			@Override
			public void onCancelButtonClick() {
				// TODO Auto-generated method stub
				if(countDownTimer != null && countDownTimer.isPaused())
				{
					countDownTimer.start();
				}
				
				
				
			}});
		picker.show(getSupportFragmentManager(), null);
	}
	
	
	/*public void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		
		Log.e("REQ_CODE", "req code: "+requestCode);
        if (requestCode == 10) 
        {
        	
        	Log.e("USER_ID_ASS_REQ", "is: "+userHelper.getUser().getUserId());
        	
        	
        	initApiCallAddmark();
        	
        	
        }
    }*/
	
	
	private String getTime(long totalTime)
	{
		String time = "";
		int seconds = (int) (totalTime / 1000) % 60 ;
		int minutes = (int) ((totalTime / (1000*60)) % 60);
		int hours   = (int) ((totalTime / (1000*60*60)) % 24);
		
		time = String.valueOf(minutes)+":"+ String.valueOf(seconds);
		
		return time;
	}
	
	private void reloadActivity()
	{
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		if(this.countDownTimer != null)
		{
			this.countDownTimer.cancel();
			this.countDownTimer = null;
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        // do something on back.
	    	
	    	showCustomialogExit("QUIZ", "Your score will be automatically saved. Do you want to exit your progress now?", R.drawable.assessment_icon_popup, AssesmentHomeworkActivity.this);
	    	
	    	return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	
	private String convertSecondsToHMS(String seconds)
	{
		String result = "";
		
		int second = Integer.parseInt(seconds);
		
		int hr = (int)(second/3600);
		int rem = (int)(second%3600);
		int mn = rem/60;
		int sec = rem%60;
		//String hrStr = (hr<10 ? "0" : "")+hr;
		String mnStr = (mn<10 ? "0" : "")+mn;
		String secStr = (sec<10 ? "0" : "")+sec;
		
		//result = hrStr+":"+mnStr+":"+secStr;
		result = mnStr+":"+secStr;
				
		return result;
	}
	
	
	private int getEllaspsedSeconds()
	{
		tEnd = System.currentTimeMillis();
		tDelta = tEnd - tStart;
		int elapsedSeconds = (int) (tDelta / 1000);
		
		return elapsedSeconds;
	}
	
	
	public class PopulateSummeryHomeworkData{
		
		
		
		private String id = "";
		private String question = "";
		private boolean isRightAnswer = false;
		private String answer = "";
		private String explanation = "";
		
		
		public PopulateSummeryHomeworkData()
		{
			
		}
		
		public PopulateSummeryHomeworkData(String id, String question, boolean isRightAnswer, String answer, String explanation)
		{
			this.id = id;
			this.question = question;
			this.isRightAnswer = isRightAnswer;
			this.answer = answer;
			this.explanation = explanation;
		}
		
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		public String getQuestion() {
			return question;
		}
		public void setQuestion(String question) {
			this.question = question;
		}
		public boolean isRightAnswer() {
			return isRightAnswer;
		}
		public void setRightAnswer(boolean isRightAnswer) {
			this.isRightAnswer = isRightAnswer;
		}
		public String getAnswer() {
			return answer;
		}
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		public String getExplanation() {
			return explanation;
		}
		public void setExplanation(String explanation) {
			this.explanation = explanation;
		}
		
	}
	
	
	
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	
	
	/*private void createNotification(String msg, String postId)
	{
		Calendar cal = Calendar.getInstance();
		
		Intent activate = new Intent(this, SingleItemShowActivity.class);
		AlarmManager alarms ;
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, activate, 0);
		alarms = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+5000, alarmIntent);
	}*/
	
	private PendingIntent pendingIntent;
	private void startNotification(Context context)
	{
		
		 /*Intent serviceIntent = new Intent(NotifyService.class.getName());
		 serviceIntent.putExtra("POST_ID", postId);*/
		
		
		/* Intent myIntent = new Intent(context , NotifyService.class);     
		 AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
		 PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
*/
		 /*Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());*/
	        //calendar.add(Calendar.SECOND, 10);

		 //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+5*1000 , pendingIntent);
        
		 Intent myIntent = new Intent(context, NotifyServiceReceiver.class);
		 SharedPreferencesHelper.getInstance().setString("not_post_id", postId);
	     pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
	     AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    // alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
	    // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 5 , pendingIntent);
	     
	     Calendar calendar = Calendar.getInstance();
		 calendar.set(Calendar.HOUR_OF_DAY, 12);
		 calendar.set(Calendar.MINUTE, 00);
		 calendar.set(Calendar.SECOND, 00);

		 alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);
		 //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 30*1000 , pendingIntent);
		 
        
	}
	
	
	
	/*private void sendNotification(String msg,String postId) {
	        mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);

	        Intent intent=new Intent(this, SingleItemShowActivity.class);
	        intent.putExtra(AppConstant.ITEM_ID, postId);
	        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	        	    Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					intent, 0);
	        
	        
	        AlarmManager alarms ;
	        //Calendar cal = Calendar.getInstance();
	        
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.add(Calendar.SECOND, 10);

	        
	        alarms = (AlarmManager) getSystemService(ALARM_SERVICE);
	        alarms.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , contentIntent);

	        
	        
	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.icon)
	        .setContentTitle("Champs21")
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        .setContentText(msg);

	        mBuilder.setContentIntent(contentIntent);
	        //mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
	        mBuilder.setAutoCancel(true);
	        mNotificationManager.notify(0, mBuilder.build());
	    }*/

}
