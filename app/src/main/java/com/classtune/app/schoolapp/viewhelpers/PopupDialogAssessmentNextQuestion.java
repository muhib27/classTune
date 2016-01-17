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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.utils.UserHelper;

public class PopupDialogAssessmentNextQuestion extends DialogFragment {
	
	
	private UserHelper userHelper;
	
	private IButtonclick listener;

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		// getDataFromDb();
		titleTextView.setText(titleText);
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

	private String titleText, descriptionText, answerText, questionText;
	private TextView titleTextView, descriptionTextView;
	private ImageView popupIcon;
	private ImageButton cancelBtn;
	private Context context;
	private int iconResId;
	private String explanationText;
	
	private Button btnExplanation, btnNext;
	
	private LinearLayout layoutExplanation;
	private TextView txtExplanation, txtAnswer, txtQuestion;
	private boolean isToggle = false;

	public static PopupDialogAssessmentNextQuestion newInstance(int title) {
		PopupDialogAssessmentNextQuestion frag = new PopupDialogAssessmentNextQuestion();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_assessment_next_question, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(titleText);
		descriptionTextView = (TextView) view
				.findViewById(R.id.popup_tv_description);
		descriptionTextView.setText(descriptionText);
		popupIcon = (ImageView) view.findViewById(R.id.popup_iv_header);
		popupIcon.setImageResource(iconResId);
		
		layoutExplanation = (LinearLayout)view.findViewById(R.id.layoutExplanation);
		txtExplanation  = (TextView)view.findViewById(R.id.txtExplanation);
		txtExplanation.setText(explanationText);
		
		txtAnswer = (TextView)view.findViewById(R.id.txtAnswer);
		txtAnswer.setText(answerText);
		
		txtQuestion = (TextView)view.findViewById(R.id.txtQuestion);
		txtQuestion.setText(questionText);
		
		cancelBtn = (ImageButton) view.findViewById(R.id.popup_btn_close);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				
				getActivity().finish();
			}
		});
		
		cancelBtn.setVisibility(View.INVISIBLE);
		
		
		btnExplanation = (Button)view.findViewById(R.id.btnExplanation);
		btnExplanation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onExplanationButtonClick();

				isToggle = !isToggle;
				if(isToggle)
				{
					layoutExplanation.setVisibility(View.VISIBLE);
					btnExplanation.setBackgroundResource(R.drawable.btn_red_back2);

					//txtExplanation.setText(explanationText);
				}
				else
				{
					layoutExplanation.setVisibility(View.GONE);
					btnExplanation.setBackgroundResource(R.drawable.btn_red_back1);
				}
			}
		});


		btnNext = (Button)view.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				listener.onNextButtonClick();
			}
		});
		
		
		

		return view;
	}

	public void setData(String titleText, String description, int iconResId,
			Context context, String answerText, String questionText, String explanationText, IButtonclick listener) {

		this.titleText = titleText;
		this.descriptionText = description;
		this.iconResId = iconResId;
		this.context = context;
		this.answerText = answerText;
		this.questionText = questionText;
		this.explanationText = explanationText;
		this.listener = listener;
		
	}

	public interface PickerItemSelectedListener {
		public void onPickerItemSelected(BaseType item);
	}
	
	
	public interface IButtonclick{
		
		public void onExplanationButtonClick();
		public void onNextButtonClick();
		
	}
	

}
