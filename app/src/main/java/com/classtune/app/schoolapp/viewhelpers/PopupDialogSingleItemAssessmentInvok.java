package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.utils.UserHelper;

public class PopupDialogSingleItemAssessmentInvok extends DialogFragment {
	
	
	private UserHelper userHelper;
	private IStartButtonClick listener;
	

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		// getDataFromDb();
		titleTextView.setText(headerText);
		/*getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);*/

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// items=new ArrayList<BaseType>();
		
		this.userHelper = new UserHelper(getActivity());
		
		this.setCancelable(false);
	}

	private String headerText, descriptionText;
	private TextView titleTextView;
	private ImageView popupIcon;
	private ImageButton cancelBtn;
	private Context context;
	private int iconResId;
	
	private String titleText, noOfQuestion, totalScore, totalTime, highestScore, totalPlayed;
	
	
	private TextView txtTitle, txtNoOfQuestion, txtTotalScore, txtTotalTime, txtHighestScore, txtTotalPlayed;
	
	private Button btnStart, btnLeaderBoard;
	
	private boolean canPlayNow = false;
	
	

	public static PopupDialogSingleItemAssessmentInvok newInstance(int title) {
		PopupDialogSingleItemAssessmentInvok frag = new PopupDialogSingleItemAssessmentInvok();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_assessment_singleitem_invok, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(headerText);
		
		popupIcon = (ImageView) view.findViewById(R.id.popup_iv_header);
		popupIcon.setImageResource(iconResId);
		
		txtTitle = (TextView)view.findViewById(R.id.txtTitle);
		txtNoOfQuestion = (TextView)view.findViewById(R.id.txtNoOfQuestion);
		txtTotalScore = (TextView)view.findViewById(R.id.txtTotalScore);
		txtTotalTime = (TextView)view.findViewById(R.id.txtTotalTime);
		txtHighestScore = (TextView)view.findViewById(R.id.txtHighestScore);
		txtTotalPlayed = (TextView)view.findViewById(R.id.txtTotalPlayed);
		
		txtTitle.setText(titleText);
		txtNoOfQuestion.setText(context.getString(R.string.java_popupaialogsingleitemassessmentnnvok_total_questions)+noOfQuestion);
		txtTotalScore.setText(context.getString(R.string.java_popupaialogsingleitemassessmentnnvok_total_score)+totalScore);
		txtTotalTime.setText(context.getString(R.string.java_popupaialogsingleitemassessmentnnvok_max_quiz_time)+totalTime);
		txtHighestScore.setText(context.getString(R.string.java_popupaialogsingleitemassessmentnnvok_highest_score)+highestScore);
		txtTotalPlayed.setText(context.getString(R.string.java_popupaialogsingleitemassessmentnnvok_total_players)+totalPlayed);
		
		
		
		
		btnStart = (Button)view.findViewById(R.id.btnStart);
				
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();

				listener.onStartButtonClick();
			}
		});


		btnLeaderBoard = (Button)view.findViewById(R.id.btnLeaderBoard);

		btnLeaderBoard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onLeaderBoardButtonClick();
			}
		});
		
		cancelBtn = (ImageButton) view.findViewById(R.id.popup_btn_close);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				
				//getActivity().finish();
			}
		});
		
		
		if(canPlayNow == false)
		{
			btnStart.setVisibility(View.GONE);
			
			txtNoOfQuestion.setVisibility(View.GONE);
			txtTotalScore.setVisibility(View.GONE);
			txtTotalTime.setVisibility(View.GONE);
			txtHighestScore.setVisibility(View.GONE);
			txtTotalPlayed.setVisibility(View.GONE);
		}
		else
		{
			btnStart.setVisibility(View.VISIBLE);
			
			txtNoOfQuestion.setVisibility(View.VISIBLE);
			txtTotalScore.setVisibility(View.VISIBLE);
			txtTotalTime.setVisibility(View.VISIBLE);
			txtHighestScore.setVisibility(View.VISIBLE);
			txtTotalPlayed.setVisibility(View.VISIBLE);
		}
		

		return view;
	}

	public void setData(boolean canPlayNow, String headerText, String titleText, String noOfQuestion, String totalScore, String highestScore, String totalTime, String totalPlayed, int iconResId,
			Context context, IStartButtonClick listener) {

		this.canPlayNow = canPlayNow;
		this.headerText = headerText;
		this.titleText = titleText;
		this.noOfQuestion = noOfQuestion;
		this.totalScore = totalScore;
		this.highestScore = highestScore;
		this.totalPlayed = totalPlayed;
		this.iconResId = iconResId;
		this.context = context;
		this.listener = listener;
		
		/*int seconds = (int) (Integer.parseInt(totalTime) / 1000) % 60 ;
		int minutes = (int) ((Integer.parseInt(totalTime) / (1000*60)) % 60);
		int hours   = (int) ((Integer.parseInt(totalTime) / (1000*60*60)) % 24);*/
		
		this.totalTime = convertSecondsToHMS(totalTime);
		
	}

	public interface PickerItemSelectedListener {
		public void onPickerItemSelected(BaseType item);
	}
	
	
	public interface IStartButtonClick{
		
		public void onStartButtonClick();
		public void onLeaderBoardButtonClick();
		
	}
	
	private String convertSecondsToHMS(String seconds)
	{
		String result = "";
		if(!TextUtils.isEmpty(seconds))
		{
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
		}
				
		return result;
	}

}
