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

public class PopupDialogAssessmentSummery extends DialogFragment {
	
	
	private UserHelper userHelper;
	
	

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

	private String titleText, descriptionText, questionText, answerText, explanationText;
	private TextView titleTextView, txtQuestion, txtAnswer, txtExplanation;
	private ImageView popupIcon;
	private ImageButton cancelBtn;
	private Context context;
	private int iconResId;
	private Button btnOk;
	
	

	public static PopupDialogAssessmentSummery newInstance(int title) {
		PopupDialogAssessmentSummery frag = new PopupDialogAssessmentSummery();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_assessment_summery, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(titleText);
		
		txtAnswer = (TextView)view.findViewById(R.id.txtAnswer);
		txtExplanation = (TextView)view.findViewById(R.id.txtExplanation);
		
		txtAnswer.setText(answerText);
		txtExplanation.setText(explanationText);
		
		txtQuestion = (TextView)view.findViewById(R.id.txtQuestion);
		txtQuestion.setText(questionText);
		
		btnOk = (Button)view.findViewById(R.id.btnOk);
		
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
		
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
			}
		});
		

		return view;
	}

	
	
	public void setData(String titleText, String questionText, String answerText, String explanationText, int iconResId,
			Context context) {

		this.titleText = titleText;
		this.questionText = questionText;
		this.answerText = answerText;
		this.explanationText = explanationText;
		this.iconResId = iconResId;
		this.context = context;
		
	}

	public interface PickerItemSelectedListener {
		public void onPickerItemSelected(BaseType item);
	}
	
	
	

}
