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

public class PopupDialogLessonPlanConfirmation extends DialogFragment {


	private UserHelper userHelper;
	private ActionCallback listener;

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
	private String titleText, descriptionText, actionButtonText;
	private TextView titleTextView, descriptionTextView;
	private ImageView popupIcon;
	private ImageButton cancelBtn;
	private Button btnAction, btnCancel;
	private Context context;
	private int iconResId;

	public static PopupDialogLessonPlanConfirmation newInstance(int title) {
		PopupDialogLessonPlanConfirmation frag = new PopupDialogLessonPlanConfirmation();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_lessonplan_confirmation, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(titleText);
		descriptionTextView = (TextView) view
				.findViewById(R.id.popup_tv_description);
		descriptionTextView.setText(descriptionText);
		popupIcon = (ImageView) view.findViewById(R.id.popup_iv_header);
		popupIcon.setImageResource(iconResId);


        btnAction = (Button) view.findViewById(R.id.btnAction);
        btnAction.setText(actionButtonText);
        btnCancel = (Button)view.findViewById(R.id.btnCancel);


        btnAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				listener.onActionCalled();


			}
		});

        btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				listener.onCancelCalled();
			}
		});
		
		
		cancelBtn = (ImageButton) view.findViewById(R.id.popup_btn_close);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDialog().dismiss();

			}
		});

		return view;
	}

	public void setData(String titleText, String actionButtonText, String description, int iconResId,
			Context context, ActionCallback listener) {

		this.titleText = titleText;
        this.actionButtonText = actionButtonText;
		this.descriptionText = description;
		this.iconResId = iconResId;
		this.context = context;
		this.listener = listener;
	}

	public interface PickerItemSelectedListener {
		public void onPickerItemSelected(BaseType item);
	}
	
	
	public interface ActionCallback
	{
		public void onActionCalled();
		public void onCancelCalled();

	}

}
