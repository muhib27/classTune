package com.classtune.app.schoolapp.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.classtune.app.R;

public class UserTypeSelectionDialog extends DialogFragment implements OnClickListener {

	public interface UserTypeListener{
		
		public void onTypeSelected(int ordinal);
	}
	
	RadioGroup rgUserType;
	
	public static UserTypeSelectionDialog newInstance(){
		UserTypeSelectionDialog dialogFragment = new UserTypeSelectionDialog();
	    return dialogFragment;

	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.user_type_selection_dialog_layout, container);
	    rgUserType=(RadioGroup)view.findViewById(R.id.radioGroup_usertype);
	    view.findViewById(R.id.radio_student).setOnClickListener(this);
	    view.findViewById(R.id.radio_other).setOnClickListener(this);
	    view.findViewById(R.id.radio_parent).setOnClickListener(this);
	    view.findViewById(R.id.radio_teacher).setOnClickListener(this);
	    view.findViewById(R.id.ok_btn).setOnClickListener(this);
	    this.getDialog().setTitle(R.string.java_usertypeselectiondialog_select_user_type);
	    return view;
	}
	
	@Override
	public void onClick(View v) {
		int selectedId = rgUserType.getCheckedRadioButtonId();
		int ordinal=2;
		switch (selectedId) {
		case R.id.radio_student:
			ordinal = 2;
			break;

		case R.id.radio_parent:
			ordinal = 4;
			break;

		case R.id.radio_teacher:
			ordinal = 3;
			break;

		case R.id.radio_other:
			ordinal = 1;
			break;

		default:
			break;
		}
		
		if(v.getId()==R.id.ok_btn)
		{
			
		 UserTypeListener activity = (UserTypeListener) getActivity();
		 activity.onTypeSelected(ordinal);
		 this.dismiss();
		}
		//getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent().putExtra("TYPE", ordinal));
	}

}
