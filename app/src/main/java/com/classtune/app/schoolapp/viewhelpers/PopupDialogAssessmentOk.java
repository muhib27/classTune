package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.content.Intent;
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

import com.classtune.app.schoolapp.LoginActivity;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.utils.UserHelper;

public class PopupDialogAssessmentOk extends DialogFragment {
	
	
	private UserHelper userHelper;
	private OkCallback listener;

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
		this.setCancelable(false);
		this.userHelper = new UserHelper(getActivity());
	}

	private boolean isLoggedIn;
	private boolean isShowRetryButton;
	private String titleText, descriptionText, scoreText;
	private TextView titleTextView, descriptionTextView, txtScoreShow;
	private ImageView popupIcon;
	private ImageButton cancelBtn;
	private Button btnLogin, btnRetry, btnResultSummery;
	private Context context;
	private int iconResId;

	public static PopupDialogAssessmentOk newInstance(int title) {
		PopupDialogAssessmentOk frag = new PopupDialogAssessmentOk();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_assessment_ok, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(titleText);
		descriptionTextView = (TextView) view
				.findViewById(R.id.popup_tv_description);
		descriptionTextView.setText(descriptionText);
		popupIcon = (ImageView) view.findViewById(R.id.popup_iv_header);
		popupIcon.setImageResource(iconResId);
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnRetry = (Button) view.findViewById(R.id.btnRetry);
		btnResultSummery = (Button)view.findViewById(R.id.btnResultSummery);
		
		txtScoreShow = (TextView)view.findViewById(R.id.txtScoreShow);
		txtScoreShow.setText(scoreText);
				
		/*if(isLoggedIn==false)
		{
			
		}
		else
		{
			btnLogin.setText("Save Score");
		}
		*/
		
		if(isShowRetryButton == true)
		{
			btnRetry.setVisibility(View.VISIBLE);
		}
		else
		{
			btnRetry.setVisibility(View.GONE);
		}
		
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				listener.onLoginCalled();
				
				if(isLoggedIn == false)
				{
					Intent intent = new Intent(context, LoginActivity.class);
					intent.putExtra("assessment_score", "ok");
					getActivity().startActivityForResult(intent, 10);
				}
				
				
			}
		});
		
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				listener.onRetryCalled();
				
				
			}
		});
		
		btnResultSummery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onSummeryCalled();
			}
		});
		
		
		cancelBtn = (ImageButton) view.findViewById(R.id.popup_btn_close);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				getActivity().finish();
			}
		});

		return view;
	}

	public void setData(boolean isLoggedIn, boolean isShowRetryButton, String titleText, String scoreText, String description, int iconResId,
			Context context, OkCallback listener) {

		this.isLoggedIn = isLoggedIn;
		this.isShowRetryButton = isShowRetryButton;
		this.titleText = titleText;
		this.scoreText = scoreText;
		this.descriptionText = description;
		this.iconResId = iconResId;
		this.context = context;
		this.listener = listener;
	}

	public interface PickerItemSelectedListener {
		public void onPickerItemSelected(BaseType item);
	}
	
	
	public interface OkCallback
	{
		public void onLoginCalled();
		public void onRetryCalled();
		public void onSummeryCalled();
	}

}
