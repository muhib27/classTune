package com.classtune.app.freeversion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.TermReportExamSubjectItem;
import com.classtune.app.schoolapp.model.TermReportItem;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

public class SingleItemTermReportActivity extends ChildContainerActivity{
	private SchoolApp app;
	private UIHelper uiHelper;
	private LinearLayout layoutReport;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_term_report);
		init();
		fetchDataFromServer();
	}

	
	private void fetchDataFromServer() {
		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.SCHOOL_ID, getIntent().getExtras().getString("id"));
		params.put("no_exams", "1");


		if(userHelper.getUser().getType()==UserTypeEnum.PARENTS){
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}else if(userHelper.getUser().getType() == UserTypeEnum.TEACHER){
			params.put(RequestKeyHelper.BATCH_ID, getIntent().getExtras().getString("batch_id"));
			params.put(RequestKeyHelper.STUDENT_ID, getIntent().getExtras().getString("student_id"));
		}
		
		AppRestClient.post(URLHelper.URL_GET_SINGLE_TERM_REPORT, params, reportCardHandler);
	}

	AsyncHttpResponseHandler reportCardHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
			//pbs.setVisibility(View.GONE);
		};

		@Override
		public void onStart() {
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
			//pbs.setVisibility(View.VISIBLE);

		};

		@Override
		public void onSuccess(int arg0, String responseString) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
			//pbs.setVisibility(View.GONE);

			//Toast.makeText(getActivity(), responseString, Toast.LENGTH_LONG).show();
			Log.e("SINGLE_TERM_RESPONSE", responseString);
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(responseString);

			if (wrapper.getStatus().getCode() == 200) {
				TermReportItem reportCardData = GsonParser.getInstance().parseTermReport(wrapper.getData().getAsJsonObject("report").toString());
				arrangeAndShowTermReportData(reportCardData);
			}


		};
	};
	private TextView examName;

	private void init() {
		// TODO Auto-generated method stub
		uiHelper = new UIHelper(this);
		app = (SchoolApp) this.getApplicationContext();
		layoutReport = (LinearLayout) findViewById(R.id.layout_report);
		examName = (TextView) findViewById(R.id.tv_report_exam_name);
		examName.setText(getIntent().getExtras().getString("term_name"));
		
	}

	
	/*private void arrangeAndShowTabholders() {
		// TODO Auto-generated method stub

		LinearLayout tabHolder = (LinearLayout) view
				.findViewById(R.id.tab_holder);

		for (int i = 0; i < items.size(); i++) {
			final int j = i;

			CustomButton button = new CustomButton(this);

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

			tabHolder.addView(button);
			tabList.add(button);

			if (i < items.size() - 1) {
				LinearLayout divider = new LinearLayout(getActivity());
				divider.setLayoutParams(new LayoutParams(1,
						LayoutParams.MATCH_PARENT));
				divider.setBackgroundResource(R.color.gray_dark);

				tabHolder.addView(divider);
			}
		}
	}*/

	private void setValuesAndViews(TermReportItem term) {
		// TODO Auto-generated method stub
		Button gradeBtn = (Button) findViewById(R.id.btn_grade);
		gradeBtn.setText(term.getGrade());

		Button positionBtn = (Button) findViewById(R.id.btn_position);
		positionBtn.setText(getString(R.string.java_reporttermfragment_position)+ term.getPosition());

		Button totalBtn = (Button) findViewById(R.id.btn_total);
		totalBtn.setText(getString(R.string.java_reporttermfragment_total_students)
				+ term.getTotalStudent());

		Button gpaBtn = (Button) findViewById(R.id.btn_gpa);
		if(term.getGpa().equalsIgnoreCase("-"))
		{
			gpaBtn.setText(term.getGpa());
		}
		else
		{
			gpaBtn.setText(getString(R.string.java_singleitemtermreportactivity_gpa)+"\n"+term.getGpa());
		}


		
	}

	class ViewHolder {
		LinearLayout layoutRow;

		TextView tvSubject;
		TextView tvGrade;
		TextView tvScore;
		TextView tvHighest;
		TextView tvTotal;
		TextView txtView;
	}

	private void arrangeAndShowTermReportData(TermReportItem term) {
		// TODO Auto-generated method stub

		setValuesAndViews(term);

		ArrayList<TermReportExamSubjectItem> subjectReportList = term.getExamSubjectList();

		LayoutInflater mInflater = LayoutInflater.from(this);

		int size = term.getExamSubjectList().size();

		for (int i = 0; i < size; i++) {
			View childView = mInflater.inflate(R.layout.row_term_report, null);
            View headerView = mInflater.inflate(R.layout.row_term_report,null);
			ViewHolder holder = new ViewHolder();

			if (i == 0) {
				layoutReport.addView(headerView);
				//continue;
			}

			/*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, (int) AppUtility.getDeviceIndependentDpFromPixel(this, 25), 0, (int) AppUtility.getDeviceIndependentDpFromPixel(this, 25));
			layoutReport.setLayoutParams(params);*/



			// *********************** Initialization ***********************
			holder.layoutRow = (LinearLayout) childView
					.findViewById(R.id.layout_row);
			holder.layoutRow.setBackgroundColor(getResources().getColor(
					R.color.white));

			holder.tvSubject = (TextView) childView
					.findViewById(R.id.tv_subject);

			holder.tvGrade = (TextView) childView.findViewById(R.id.tv_grade);
			holder.tvGrade
					.setTextColor(getResources().getColor(R.color.classtune_green_color));

			holder.tvScore = (TextView) childView.findViewById(R.id.tv_score);

			holder.tvHighest = (TextView) childView
					.findViewById(R.id.tv_highest);
			holder.tvHighest.setTextColor(getResources().getColor(
					R.color.classtune_green_color));

			holder.tvTotal = (TextView) childView.findViewById(R.id.tv_total);
			holder.tvTotal
					.setTextColor(getResources().getColor(R.color.gray_1));

			holder.txtView = (TextView)childView.findViewById(R.id.txtView);
			holder.txtView.setText(getString(R.string.java_classreportteacherfragment_view));

			/*holder.tvPercent = (TextView) childView
					.findViewById(R.id.tv_percent);
			holder.tvPercent.setTextColor(getResources().getColor(
					R.color.gray_1));*/

			// *********************** Set values ***********************
			holder.tvSubject.setText(subjectReportList.get(i).getSubjctName());

			if(subjectReportList.get(i).getNoExams().equalsIgnoreCase("1"))
			{
				holder.tvGrade.setText(getString(R.string.java_singlexeamroutine_na));
				holder.tvScore.setText(getString(R.string.java_singlexeamroutine_na));
				holder.tvHighest.setText(getString(R.string.java_singlexeamroutine_na));
				holder.tvTotal.setText(getString(R.string.java_singlexeamroutine_na));
			}

			else
			{
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
			}






			//percentile

			/*float percentage = 0;
			percentage = subjectReportList.get(i).getPercentile();
			float percentile = (float)Math.round(percentage);
			float percent = (float)Math.round(percentile / 5);
			int finalPercentile = (int)percent * 5;
			holder.tvPercent.setText("Top "+String.valueOf(finalPercentile)+" %");*/

			//percentile finish

			layoutReport.addView(childView);


			//working on SingleReportCardSubject data
			childView.setTag(subjectReportList.get(i));
			final int k = i;

			childView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					Intent intent = new Intent(SingleItemTermReportActivity.this, SingleReportCardSubject.class);
					TermReportExamSubjectItem ctListInner = (TermReportExamSubjectItem)view.getTag();

					intent.putExtra("inner_reportcard_item_subject_id", ctListInner.getSubjctId());
					intent.putExtra("inner_reportcard_item_position", k);
					intent.putExtra("inner_reportcard_item_exam_group_id", SingleItemTermReportActivity.this.getIntent().getExtras().getString("id"));
					intent.putExtra("inner_reportcard_item_exam_category_name", getString(R.string.java_singleItemtermreportactivity_term_report));
					intent.putExtra("inner_reportcard_item_exam_from_class_test", false);

					intent.putExtra("inner_reportcard_item_student_id", SingleItemTermReportActivity.this.getIntent().getExtras().getString("student_id"));

					startActivity(intent);
				}
			});

		}
	}


}
