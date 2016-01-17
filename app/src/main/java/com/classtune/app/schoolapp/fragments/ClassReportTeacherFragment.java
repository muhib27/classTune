package com.classtune.app.schoolapp.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.schoolapp.BatchSelectionChangedBroadcastReceiver;
import com.classtune.app.schoolapp.BatchSelectionChangedBroadcastReceiver.onBatchIdChangeListener;
import com.classtune.app.R;
import com.classtune.app.schoolapp.StudentListActivity;
import com.classtune.app.schoolapp.fragments.DatePickerFragment.DatePickerOnSetDateListener;
import com.classtune.app.schoolapp.model.ClassReport;
import com.classtune.app.schoolapp.model.StudentAttendance;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ClickSpan;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;

public class ClassReportTeacherFragment extends Fragment implements
		OnClickListener,onBatchIdChangeListener {
	private View rootView;
	private TextView totalStudentCountTxt, presentStudentCountTxt,
			absentStudentCountTxt, lateStudentCountTxt, leaveStudentCountTxt;
	private TextView totalViewLink, presentViewLink, absentViewLink,
			lateViewLink, leaveViewLink;
	private ProgressBar pb;
	private TextView dateTxt;
	private LinearLayout dateSelectionLayout;
	private LinearLayout pbLayout;
	private BatchSelectionChangedBroadcastReceiver reciever=new BatchSelectionChangedBroadcastReceiver(this);
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().registerReceiver(reciever, new IntentFilter("com.classtune.app.schoolapp.batch"));
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(reciever);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("OnCreate", "Aise");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.e("OnActivityCreated", "Aise");
		if (TeachersAttendanceTabhostFragment.dateString.equalsIgnoreCase("")) {
			dateTxt.setText(AppUtility
					.getCurrentDate(AppUtility.DATE_FORMAT_APP));
		} else {
			dateTxt.setText(TeachersAttendanceTabhostFragment.dateString);
		}
		if(PaidVersionHomeFragment.selectedBatch!=null)
		getClassReport();
	}

	private void getClassReport() {
		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.BATCH_ID,
				PaidVersionHomeFragment.selectedBatch.getId());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		if (!TeachersAttendanceTabhostFragment.dateString.equalsIgnoreCase("")) {

			params.put("date", TeachersAttendanceTabhostFragment.dateString);
		}
		AppRestClient.post(URLHelper.URL_GET_BATCH_CLASS_REPORT, params, getClassReportHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.class_report_layout, container,
				false);
		totalStudentCountTxt = (TextView) rootView
				.findViewById(R.id.total_student_count);
		presentStudentCountTxt = (TextView) rootView
				.findViewById(R.id.present_student_count);
		absentStudentCountTxt = (TextView) rootView
				.findViewById(R.id.absent_student_count);
		lateStudentCountTxt = (TextView) rootView
				.findViewById(R.id.late_student_count);
		leaveStudentCountTxt = (TextView) rootView
				.findViewById(R.id.leave_student_count);
		dateTxt = (TextView) rootView.findViewById(R.id.tv_date);
		dateSelectionLayout = (LinearLayout) rootView
				.findViewById(R.id.choose_date_btn);
		dateSelectionLayout.setOnClickListener(this);
		pbLayout = (LinearLayout) rootView.findViewById(R.id.pb);
		
		totalViewLink=(TextView)rootView.findViewById(R.id.total_view_btn);
		presentViewLink=(TextView)rootView.findViewById(R.id.present_view_btn);
		absentViewLink=(TextView)rootView.findViewById(R.id.absent_view_btn);
		lateViewLink=(TextView)rootView.findViewById(R.id.late_view_btn);
		leaveViewLink=(TextView)rootView.findViewById(R.id.leave_view_btn);
		
		return rootView;
	}

	private void showDatepicker() {
		DatePickerFragment picker = new DatePickerFragment();
		picker.setCallbacks(datePickerCallback);
		picker.show(getFragmentManager(), "datePicker");
	}

	DatePickerOnSetDateListener datePickerCallback = new DatePickerOnSetDateListener() {

		@Override
		public void onDateSelected(int month, String monthName, int day,
				int year, String dateFormatServer, String dateFormatApp,
				Date date) {

			dateTxt.setText(dateFormatApp);
			TeachersAttendanceTabhostFragment.dateString=dateFormatServer;
			getClassReport();
		}
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.choose_date_btn:
			showDatepicker();
			break;

		default:
			break;
		}

	}
	
	private void updateUI(final ClassReport cReport)
	{
		if (TeachersAttendanceTabhostFragment.dateString.equalsIgnoreCase("")) {
			dateTxt.setText(AppUtility.getDateString(cReport.getCurrentDate(), AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));
		} else {
			dateTxt.setText(AppUtility.getDateString(TeachersAttendanceTabhostFragment.dateString, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));
		}
		totalStudentCountTxt.setText(cReport.getTotalStudentCount()+"");
		
		presentStudentCountTxt.setText(cReport.getPresentStudentCount()+"");
		lateStudentCountTxt.setText(cReport.getLateStudentCount()+"");
		absentStudentCountTxt.setText(cReport.getAbsentStudentCount()+"");
		leaveStudentCountTxt.setText(cReport.getLeaveStudentCount()+"");
		
		
		if(cReport.getPresentStudentCount()>0)
		clickify(presentViewLink, "View",new ClickSpan.OnClickListener()
	     {
	        @Override
	        public void onClick() {
	            // do something
	        	Intent intent=new Intent(getActivity(), StudentListActivity.class);
	        	intent.putParcelableArrayListExtra("students",(ArrayList<StudentAttendance>)(cReport.getStudent().getPresentStudents()));
	        	intent.putExtra("title", "Present Students");
	        	intent.putExtra("date", dateTxt.getText().toString());
	        	startActivity(intent);
	        }
	    });
		if(cReport.getLateStudentCount()>0)
		clickify(lateViewLink, "View",new ClickSpan.OnClickListener()
	     {
	        @Override
	        public void onClick() {
	            // do something
	        	Intent intent=new Intent(getActivity(), StudentListActivity.class);
	        	intent.putParcelableArrayListExtra("students",(ArrayList<StudentAttendance>)(cReport.getStudent().getLateStudents()));
	        	intent.putExtra("title", "Students late");
	        	intent.putExtra("date", dateTxt.getText().toString());
	        	startActivity(intent);
	        }
	    });
		if(cReport.getLeaveStudentCount()>0)
		clickify(leaveViewLink, "View",new ClickSpan.OnClickListener()
	     {
	        @Override
	        public void onClick() {
	            // do something
	        	Intent intent=new Intent(getActivity(), StudentListActivity.class);
	        	intent.putParcelableArrayListExtra("students",(ArrayList<StudentAttendance>)(cReport.getStudent().getLeaveStudents()));
	        	intent.putExtra("title", "Students on leave");
	        	intent.putExtra("date", dateTxt.getText().toString());
	        	startActivity(intent);
	        }
	    });
		if(cReport.getAbsentStudentCount()>0)
		clickify(absentViewLink, "View",new ClickSpan.OnClickListener()
	     {
	        @Override
	        public void onClick() {
	            // do something
	        	Intent intent=new Intent(getActivity(), StudentListActivity.class);
	        	intent.putParcelableArrayListExtra("students",(ArrayList<StudentAttendance>)(cReport.getStudent().getAbsentStudents()));
	        	intent.putExtra("title", "Students absent");
	        	intent.putExtra("date", dateTxt.getText().toString());
	        	startActivity(intent);
	        }
	    });
	}

	public static void clickify(
			TextView view,
			final String clickableText,
			final com.classtune.app.schoolapp.utils.ClickSpan.OnClickListener listener) {

		CharSequence text = view.getText();
		String string = text.toString();
		ClickSpan span = new ClickSpan(listener);

		int start = string.indexOf(clickableText);
		int end = start + clickableText.length();
		if (start == -1)
			return;

		if (text instanceof Spannable) {
			((Spannable) text).setSpan(span, start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			SpannableString s = SpannableString.valueOf(text);
			s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(s);
		}

		MovementMethod m = view.getMovementMethod();
		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			view.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

	AsyncHttpResponseHandler getClassReportHandler = new AsyncHttpResponseHandler() {
		public void onFailure(Throwable arg0, String arg1) {
			pbLayout.setVisibility(View.GONE);
			Log.e("error", arg1);
		};

		public void onStart() {
			pbLayout.setVisibility(View.VISIBLE);

		};

		public void onSuccess(int arg0, String responseString) {

			pbLayout.setVisibility(View.GONE);
			
			Wrapper wrapper=GsonParser.getInstance().parseServerResponse(responseString);
			if(wrapper.getStatus().getCode()==AppConstant.RESPONSE_CODE_SUCCESS)
			{
				ClassReport report=GsonParser.getInstance().parseClassReport(wrapper.getData().toString());
				updateUI(report);
			}

		};
	};
	

	@Override
	public void update(String batchId, String schoolId) {
		getClassReport();
	}

}