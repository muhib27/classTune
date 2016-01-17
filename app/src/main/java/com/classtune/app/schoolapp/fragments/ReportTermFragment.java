/**
 * 
 */
package com.classtune.app.schoolapp.fragments;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.schoolapp.BatchSelectionChangedBroadcastReceiver;
import com.classtune.app.schoolapp.BatchSelectionChangedBroadcastReceiver.onBatchIdChangeListener;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.TermReportExamSubjectItem;
import com.classtune.app.schoolapp.model.TermReportItem;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;

import java.util.ArrayList;

/**
 * @author Amit
 * 
 */
public class ReportTermFragment extends Fragment implements
		onBatchIdChangeListener {

	private SchoolApp app;
	private UIHelper uiHelper;
	private View view;
	private ArrayList<TermReportItem> items;
	private LinearLayout layoutReport;
	private ArrayList<CustomButton> tabList;
	private BatchSelectionChangedBroadcastReceiver reciever = new BatchSelectionChangedBroadcastReceiver(
			this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		init();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().registerReceiver(reciever,
				new IntentFilter("com.classtune.app.schoolapp.batch"));
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(reciever);
	}

	private void init() {
		// TODO Auto-generated method stub
		uiHelper = new UIHelper(getActivity());
		app = (SchoolApp) getActivity().getApplicationContext();

		items = app.getReportCardData().getTermReportItemList();
		tabList = new ArrayList<CustomButton>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		view = inflater
				.inflate(R.layout.fragment_term_report, container, false);
		layoutReport = (LinearLayout) view.findViewById(R.id.layout_report);

		if (SchoolApp.getInstance().getReportCardData().getTermReportItemList()
				.size() > 0) {
			arrangeAndShowTabholders();
			tabList.get(0).setReportButtonSelected(true);
			tabList.get(0).performClick();
		}

		return view;
	}

	private void arrangeAndShowTabholders() {
		// TODO Auto-generated method stub

		/*LinearLayout tabHolder = (LinearLayout) view
				.findViewById(R.id.about_us_btn);*///tab_holder will be here

		for (int i = 0; i < items.size(); i++) {
			final int j = i;

			CustomButton button = new CustomButton(getActivity());

			button.setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.MATCH_PARENT, 1f));

			button.setTitleText(items.get(i).getExamName());

			button.setBackgroundResource(R.drawable.gray_btn);

			button.setClickable(true);

			button.setGravity(Gravity.CENTER);

			button.setTitleColor(getResources().getColor(R.color.gray_1));

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					layoutReport.removeAllViews();

					CustomButton b = (CustomButton) v;

					for (int k = 0; k < tabList.size(); k++) {
						tabList.get(k).setReportButtonSelected(false);
					}
					b.setReportButtonSelected(true);
					arrangeAndShowTermReportData(j);
				}
			});

			button.setButtonSelected(false);

			//tabHolder.addView(button);
			tabList.add(button);

			if (i < items.size() - 1) {
				LinearLayout divider = new LinearLayout(getActivity());
				divider.setLayoutParams(new LayoutParams(1,
						LayoutParams.MATCH_PARENT));
				divider.setBackgroundResource(R.color.gray_dark);

			//	tabHolder.addView(divider);
			}
		}
	}

	private void setValuesAndViews(int termNumber) {
		// TODO Auto-generated method stub
		Button gradeBtn = (Button) view.findViewById(R.id.btn_grade);
		gradeBtn.setText(items.get(termNumber).getGrade());

		Button positionBtn = (Button) view.findViewById(R.id.btn_position);
		positionBtn.setText("Position: " + items.get(termNumber).getPosition());

		Button totalBtn = (Button) view.findViewById(R.id.btn_total);
		totalBtn.setText("Total Students: "
				+ items.get(termNumber).getTotalStudent());

		Button gpaBtn = (Button) view.findViewById(R.id.btn_gpa);
		gpaBtn.setText(items.get(termNumber).getGpa());

	}

	class ViewHolder {
		LinearLayout layoutRow;

		TextView tvSubject;
		TextView tvGrade;
		TextView tvScore;
		TextView tvHighest;
		TextView tvTotal;
		TextView tvPercent;
	}

	private void arrangeAndShowTermReportData(int termNumber) {
		// TODO Auto-generated method stub

		setValuesAndViews(termNumber);

		ArrayList<TermReportExamSubjectItem> subjectReportList = items.get(
				termNumber).getExamSubjectList();

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		int size = items.get(termNumber).getExamSubjectList().size();

		for (int i = 0; i < size; i++) {
			View childView = mInflater.inflate(R.layout.row_term_report, null);

			ViewHolder holder = new ViewHolder();

			if (i == 0) {
				layoutReport.addView(childView);
				continue;
			}

			// *********************** Initialization ***********************
			holder.layoutRow = (LinearLayout) childView
					.findViewById(R.id.layout_row);
			holder.layoutRow.setBackgroundColor(getResources().getColor(
					R.color.white));

			holder.tvSubject = (TextView) childView
					.findViewById(R.id.tv_subject);

			holder.tvGrade = (TextView) childView.findViewById(R.id.tv_grade);
			holder.tvGrade
					.setTextColor(getResources().getColor(R.color.maroon));

			holder.tvScore = (TextView) childView.findViewById(R.id.tv_score);

			holder.tvHighest = (TextView) childView
					.findViewById(R.id.tv_highest);
			holder.tvHighest.setTextColor(getResources().getColor(
					R.color.maroon));

			holder.tvTotal = (TextView) childView.findViewById(R.id.tv_total);
			holder.tvTotal
					.setTextColor(getResources().getColor(R.color.gray_1));

			holder.tvPercent = (TextView) childView
					.findViewById(R.id.tv_percent);
			holder.tvPercent.setTextColor(getResources().getColor(
					R.color.gray_1));

			// *********************** Set values ***********************
			holder.tvSubject.setText(subjectReportList.get(i).getSubjctName());
			holder.tvGrade.setText(subjectReportList.get(i).getGrade());

			float m;

			if (subjectReportList.get(i).getMark().equalsIgnoreCase("-")) {
				holder.tvScore.setText(subjectReportList.get(i).getMark());
			} else {
				m = Float.parseFloat(subjectReportList.get(i).getMark());
				holder.tvScore.setText((int) m + "");
			}

			m = Float.parseFloat(subjectReportList.get(i).getMaxMark());
			holder.tvHighest.setText((int) m + "");

			m = Float.parseFloat(subjectReportList.get(i).getTotalMark());
			holder.tvTotal.setText((int) m + "");

			holder.tvPercent.setText(subjectReportList.get(i).getPercent()
					+ "%");

			layoutReport.addView(childView);
		}
	}

	@Override
	public void update(String batchId, String schoolId) {
		// TODO Auto-generated method stub

	}

}
