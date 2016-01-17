package com.classtune.app.schoolapp.fragments;



import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.fragments.RollCallTeacherFragment.ViewHolder;
import com.classtune.app.schoolapp.model.StudentAttendance;


public class LeaveApplicationDialogFragment extends DialogFragment implements OnClickListener {

	
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		nameText.setText(student.getStudentName());
		startDate.setText(student.getLeaveStartDate());
		endDate.setText(student.getLeaveEndDate());
		reasonText.setText(student.getReason());
		if(flag)
		{
			acceptBtn.setVisibility(View.GONE);
			denyBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private TextView startDate,endDate,reasonText,nameText;
	private Button acceptBtn,denyBtn;
	
	
	private LeaveApplicationStatusListener listener;
	private StudentAttendance student;
	private ViewHolder holder;
	private RelativeLayout parentView;
	private boolean flag;
	
	public LeaveApplicationDialogFragment() {

	}

	public static LeaveApplicationDialogFragment newInstance(int title) {
		LeaveApplicationDialogFragment frag = new LeaveApplicationDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		final View view = inflater.inflate(R.layout.leave_application_diolog_layout, container, false);
		acceptBtn=(Button)view.findViewById(R.id.dialog_btn_accept);
		acceptBtn.setOnClickListener(this);
		denyBtn=(Button)view.findViewById(R.id.dialog_btn_deny);
		denyBtn.setOnClickListener(this);
		nameText=(TextView)view.findViewById(R.id.name_text);
		startDate=(TextView)view.findViewById(R.id.start_date_text);
		endDate=(TextView)view.findViewById(R.id.end_date_text);
		reasonText=(TextView)view.findViewById(R.id.reason_text);
		return view;
	}

	
	public void setData(LeaveApplicationStatusListener lis,StudentAttendance s,ViewHolder holder,RelativeLayout rowView,Boolean flag)
	{
		this.listener=lis;
		this.student=s;
		this.holder=holder;
		this.parentView=rowView;
		this.flag=flag;
	}
	
	public interface LeaveApplicationStatusListener
	{
		public void onAccept(StudentAttendance s, ViewHolder holder, RelativeLayout rowView);
		public void onDeny(StudentAttendance s, ViewHolder holder, RelativeLayout rowView);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_btn_accept:
			student.setStatus(3);
			listener.onAccept(student, holder, parentView);
			getDialog().dismiss();
			break;
		case R.id.dialog_btn_deny:
			student.setStatus(0);
			listener.onDeny(student, holder, parentView);
			getDialog().dismiss();
			break;
		default:
			break;
		}
	}
	
	
}
