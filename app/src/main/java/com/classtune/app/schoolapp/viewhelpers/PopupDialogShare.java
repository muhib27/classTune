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
import com.classtune.app.schoolapp.utils.UserHelper;

public class PopupDialogShare extends DialogFragment {
	
	
	private UserHelper userHelper;
	
	private IShareButtonclick listener;

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		// getDataFromDb();
		//titleTextView.setText(titleText);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// items=new ArrayList<BaseType>();
		
		this.userHelper = new UserHelper(getActivity());
		
		this.setCancelable(false);
	}

	private String titleText, descriptionText;
	private TextView titleTextView, descriptionTextView;
	private ImageView popupIcon;
	private ImageButton cancelBtn;
	private Context context;
	private int iconResId;
	private String explanationText;
	
	private Button btnExplanation, btnNext;
	
	private LinearLayout layoutExplanation;
	private TextView txtExplanation;
	private boolean isToggle = false;

	public static PopupDialogShare newInstance(int title) {
		PopupDialogShare frag = new PopupDialogShare();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_share, container,
				false);
		
		cancelBtn = (ImageButton) view.findViewById(R.id.popup_btn_close);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				
			}
		});
		
		cancelBtn.setVisibility(View.VISIBLE);
		
		
		Button btnAll = (Button)view.findViewById(R.id.btn_all_share);
		btnAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				listener.onSocialButtonClick();

			}
		});


		Button btnSchool = (Button)view.findViewById(R.id.btn_champs);
		btnSchool.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				listener.onMyschoolButtonClick();
			}
		});
		
		
		

		return view;
	}

	public void setData(IShareButtonclick listener) {

		
		this.listener = listener;
		
	}

	
	public interface IShareButtonclick{
		
		public void onSocialButtonClick();
		public void onMyschoolButtonClick();
		
	}
	

}
