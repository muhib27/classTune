package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

public class PopupDialogHomeworkAssessmentResult extends DialogFragment {
	
	
	private UserHelper userHelper;
	private IOkButtonClick listener;
	

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
	
	private String nameText, subjectNameText, studentCountText, maxScoreText, minScoreText, totalMarkText, totalTimeTakenText, isPassedText, totalScoreText;
	
	
	private TextView txtName, txtSubjectName, txtStudentCount, txtMaxScore, txtMinScore, txtTotalMark, txtTotalTimeTaken, txtIsPassed, txtTotalScore;
	
	private Button btnOk;
	
	private boolean canPlayNow = false;
	
	

	public static PopupDialogHomeworkAssessmentResult newInstance(int title) {
		PopupDialogHomeworkAssessmentResult frag = new PopupDialogHomeworkAssessmentResult();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_homework_assessment_result, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(headerText);
		
		popupIcon = (ImageView) view.findViewById(R.id.popup_iv_header);
		popupIcon.setImageResource(iconResId);
		
		cancelBtn = (ImageButton) view.findViewById(R.id.popup_btn_close);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				
				//getActivity().finish();
			}
		});
		
		
		
		
		txtName = (TextView)view.findViewById(R.id.txtName);
		txtSubjectName = (TextView)view.findViewById(R.id.txtSubjectName);
		txtTotalMark = (TextView)view.findViewById(R.id.txtTotalMark);
		
		txtIsPassed = (TextView)view.findViewById(R.id.txtIsPassed);
		txtTotalScore = (TextView)view.findViewById(R.id.txtTotalScore);
		
		txtStudentCount = (TextView)view.findViewById(R.id.txtStudentCount);
		txtMaxScore = (TextView)view.findViewById(R.id.txtMaxScore);
		txtMinScore = (TextView)view.findViewById(R.id.txtMinScore);
		txtTotalTimeTaken = (TextView)view.findViewById(R.id.txtTotalTimeTaken);
		
		
		txtName.setText(nameText);
		txtSubjectName.setText(subjectNameText);
		txtStudentCount.setText(context.getString(R.string.java_popupdialoghomeworkassessmentresult_participated)+studentCountText);
		txtMaxScore.setText(context.getString(R.string.java_popupdialoghomeworkassessmentresult_highest_mark)+maxScoreText);
		txtMinScore.setText(context.getString(R.string.java_popupdialoghomeworkassessmentresult_lowest_mark)+minScoreText);
		txtTotalMark.setText(context.getString(R.string.java_popupdialoghomeworkassessmentresult_total_marks)+totalMarkText);
		txtTotalTimeTaken.setText(context.getString(R.string.java_popupdialoghomeworkassessmentresult_total_time_taken)+totalTimeTakenText);
		
		if(isPassedText.equalsIgnoreCase("1"))
			txtIsPassed.setText(R.string.java_popupdialoghomeworkassessmentresult_passed_yes);
		else
			txtIsPassed.setText(R.string.java_popupdialoghomeworkassessmentresult_passed_no);
		
		txtTotalScore.setText(context.getString(R.string.java_popupdialoghomeworkassessmentresult_total_score)+totalScoreText);
		
		
		
		
		
		
		btnOk = (Button)view.findViewById(R.id.btnOk);
				
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				listener.onOkButtonClick();
			}
		});
		
		
		
		

		return view;
	}

	public void setData(String headerText, String nameText, String subjectNameText, String studentCountText, String maxScoreText, String minScoreText, String totalMarkText, String totalTimeTakenText, String isPassedText, String totalScoreText, int iconResId,
			Context context, IOkButtonClick listener) {

		this.headerText = headerText;
		this.nameText = nameText;
		this.subjectNameText = subjectNameText;
		this.studentCountText = studentCountText;
		this.maxScoreText = maxScoreText;
		this.minScoreText = minScoreText;
		this.totalMarkText = totalMarkText;
		this.totalTimeTakenText = totalTimeTakenText;
		this.isPassedText = isPassedText;
		this.totalScoreText = totalScoreText;
		this.iconResId = iconResId;
		this.context = context;
		this.listener = listener;
		
		
	}

	public interface PickerItemSelectedListener {
		public void onPickerItemSelected(BaseType item);
	}
	
	
	public interface IOkButtonClick{
		
		public void onOkButtonClick();

	}
	
	

}
