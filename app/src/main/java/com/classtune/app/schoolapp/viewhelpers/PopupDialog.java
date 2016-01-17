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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.classtune.app.schoolapp.LoginActivity;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;

public class PopupDialog extends DialogFragment {

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
	private ImageButton cancelBtn, loginBtn;
	private Context context;
	private int iconResId;

	public static PopupDialog newInstance(int title) {
		PopupDialog frag = new PopupDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.popup_layout, container,
				false);
		titleTextView = (TextView) view.findViewById(R.id.popup_tv_header);
		titleTextView.setText(titleText);
		descriptionTextView = (TextView) view
				.findViewById(R.id.popup_tv_description);
		descriptionTextView.setText(descriptionText);
		popupIcon = (ImageView) view.findViewById(R.id.popup_iv_header);
		popupIcon.setImageResource(iconResId);
		loginBtn = (ImageButton) view.findViewById(R.id.popup_btn_login);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
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

}
