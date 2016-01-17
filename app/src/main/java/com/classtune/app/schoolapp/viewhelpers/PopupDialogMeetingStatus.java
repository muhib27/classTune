package com.classtune.app.schoolapp.viewhelpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;

public class PopupDialogMeetingStatus extends DialogFragment {

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
	}

	private String titleText, descriptionText;
	private TextView titleTextView, descriptionTextView;
	private ImageView popupIcon;
	
	
	private Button btnCancel, btnOk;
	
	private CheckBox checkBoxAccept, checkBoxDeny;
	
	private Context context;
	private int iconResId;
	
	
	private PopupOkButtonClickListener listener;

	public static PopupDialogMeetingStatus newInstance(int title) {
		PopupDialogMeetingStatus frag = new PopupDialogMeetingStatus();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout_meeting_status, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(titleText);
		descriptionTextView = (TextView) view
				.findViewById(R.id.popup_tv_description);
		descriptionTextView.setText(descriptionText);
		popupIcon = (ImageView) view.findViewById(R.id.popup_iv_header);
		popupIcon.setImageResource(iconResId);
		
		
		btnCancel = (Button)view.findViewById(R.id.btnCancel);
		btnOk = (Button)view.findViewById(R.id.btnOk);
		
		checkBoxAccept = (CheckBox)view.findViewById(R.id.checkBoxAccept);
		checkBoxDeny = (CheckBox)view.findViewById(R.id.checkBoxDeny);
		
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onOkButtonClicked("declined");
				getDialog().dismiss();
			}
		});
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onOkButtonClicked("accepted");
				getDialog().dismiss();
			}
		});
		
		
		
		
		checkBoxAccept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkBoxDeny.isChecked())
					checkBoxDeny.setChecked(false);
				
				if(checkBoxAccept.isChecked())
				{
					listener.onOkButtonClicked("accepted");
				}
				else if(checkBoxDeny.isChecked())
				{
					listener.onOkButtonClicked("declined");
				}
				
				getDialog().dismiss();
			}
		});
		
		checkBoxDeny.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkBoxAccept.isChecked())
					checkBoxAccept.setChecked(false);
				
				if(checkBoxAccept.isChecked())
				{
					listener.onOkButtonClicked("accepted");
				}
				else if(checkBoxDeny.isChecked())
				{
					listener.onOkButtonClicked("declined");
				}
				
				getDialog().dismiss();
			}
		});
		
		
		/*checkBoxAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(checkBoxDeny.isChecked())
					checkBoxDeny.setChecked(false);
			}
		});
		
		checkBoxDeny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(checkBoxAccept.isChecked())
					checkBoxAccept.setChecked(false);
			}
		});*/
		
		

		return view;
	}

	public void setData(String titleText, String description, int iconResId,
			Context context) {

		this.titleText = titleText;
		this.descriptionText = description;
		this.iconResId = iconResId;
		this.context = context;
	}

	public interface PickerItemSelectedListener {
		public void onPickerItemSelected(BaseType item);
	}
	
	
	public void setCallBack(PopupOkButtonClickListener listener)
	{
		this.listener = listener;
	}
	
	public interface PopupOkButtonClickListener
	{
		public void onOkButtonClicked(String status);
	}
	
	

}
