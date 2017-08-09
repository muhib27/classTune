/**
 * 
 */
package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.freeversion.SingleReportCardSubject;
import com.classtune.app.schoolapp.BatchSelectionChangedBroadcastReceiver;
import com.classtune.app.schoolapp.BatchSelectionChangedBroadcastReceiver.onBatchIdChangeListener;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.ClassTestItem;
import com.classtune.app.schoolapp.model.ClassTestReportItem;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.ReportCardModel;
import com.classtune.app.schoolapp.model.StudentAttendance;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.ExpandableTextView;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Amit
 *
 */
public class ReportClassTestFragment extends UserVisibleHintFragment implements onBatchIdChangeListener{

	private Batch selectedBatch;
	private StudentAttendance selectedStudent;
	public static IPickedStudentName pickedStudentNameListenerClassTest;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//fetchClassTestReport();
	}

	private ArrayList<ClassTestReportItem> items;
	private UIHelper uiHelper;
	private ApplicationSingleton app;
	private View view;
	private int currentType = 1;
	ArrayList<ViewHolder> holderList;
	private LinearLayout listLayout;
	UserHelper userHelper;
	private String batchId="";
	private String studentId="";
	private Context mContext;

	/*private LinearLayout layoutDataHolderProject;
	private LinearLayout layoutDataHolderClassTest;
	private TextView txtRemarksProject;
	private TextView txtRemarksClassTest;*/
	
	/*@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().registerReceiver(reciever, new IntentFilter("com.champs21.schoolapp.batch"));
		processItemCallErAge(ApplicationSingleton.getInstance().getReportCardData());
		processItems();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(reciever);
	}*/
	
	public static ReportClassTestFragment newInstance(String batchId, String studentId) {
		ReportClassTestFragment f = new ReportClassTestFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("batch_id", batchId);
		args.putString("student_id", studentId);
		f.setArguments(args);
		return f;
	}
	
	private BatchSelectionChangedBroadcastReceiver reciever=new BatchSelectionChangedBroadcastReceiver(this);
	private LinearLayout pbs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}


	private void init() {
		mContext = getActivity();
		uiHelper=new UIHelper(getActivity());
		app = (ApplicationSingleton) mContext.getApplicationContext();
		userHelper = new UserHelper(mContext);
		items = new ArrayList<ClassTestReportItem>();

		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.fragment_classtest_report, container, false);

		listLayout = (LinearLayout) view.findViewById(R.id.layout_list);
		pbs = (LinearLayout) view.findViewById(R.id.pb);

		return view;
	}

	class ViewHolder {

		// ***** Classtests *****
		LinearLayout layoutCT;

		TextView tvCTDate;
		ImageView imageCTSub;
		TextView tvCTSub;
		TextView tvCTName;

		TextView tvCTGrade;
		TextView tvCTMarks;
		TextView tvCTHighestMarks;
		TextView tvCTTotalMarks;
		TextView tvCTPercentage;


		// ***** Projects *****
		LinearLayout layoutProject;

		TextView tvProjectSub;
		ExpandableTextView tvProjectDescription;

		TextView tvProjectGrade;
		TextView tvProjectMarks;
		TextView tvProjectHighestMarks;
		TextView tvProjectTotalMarks;
		TextView tvProjectPercentage;


		// ***** Common *****
		LinearLayout layoutDynamicRow;
		LinearLayout layoutButtons;
		LinearLayout layoutRedline;
		LinearLayout layoutGap;

		CustomButton btnProject;
		CustomButton btnAllCT;

		//***** No Comment *****
		LinearLayout layoutDataHolderProject;
		LinearLayout layoutDataHolderClassTest;
		TextView txtRemarksProject;
		TextView txtRemarksClassTest;


	}


	private void fetchClassTestReport() {
		// TODO Auto-generated method stub
		HashMap<String,String> params = new HashMap<>();

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("no_exams", "1");
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
		}else if(userHelper.getUser().getType() == UserTypeEnum.TEACHER){
			params.put(RequestKeyHelper.BATCH_ID, selectedBatch.getId());
			params.put(RequestKeyHelper.STUDENT_ID, selectedStudent.getId());
		}
		//AppRestClient.post(URLHelper.URL_REPORT_CARD, params, reportCardHandler);
		reportCard(params);
	}

	private void reportCard(HashMap<String,String> params){

		pbs.setVisibility(View.VISIBLE);
		ApplicationSingleton.getInstance().getNetworkCallInterface().reportCard(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
							/*if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}*/
						pbs.setVisibility(View.GONE);

						//Toast.makeText(getActivity(), responseString, Toast.LENGTH_LONG).show();
						Log.e("##RESPONSE", "is: "+response.body());


						Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(response.body());

						if (wrapper.getStatus().getCode() == 200) {
							ReportCardModel reportCardData = GsonParser.getInstance().parseReports(wrapper.getData().toString());

							app.setReportCardData(reportCardData);


							processItemCallErAge(reportCardData);
							processItems();
						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.showMessage(getString(R.string.internet_error_text));
			/*if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}*/
						pbs.setVisibility(View.GONE);
					}
				}
		);
	}
	AsyncHttpResponseHandler reportCardHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			/*if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}*/
			pbs.setVisibility(View.GONE);
		};

		@Override
		public void onStart() {
			//uiHelper.showLoadingDialog("Please wait...");
			pbs.setVisibility(View.VISIBLE);

		};

		@Override
		public void onSuccess(int arg0, String responseString) {
			/*if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}*/
			pbs.setVisibility(View.GONE);

			//Toast.makeText(getActivity(), responseString, Toast.LENGTH_LONG).show();
			Log.e("##RESPONSE", "is: "+responseString);


			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(responseString);

			if (wrapper.getStatus().getCode() == 200) {
				ReportCardModel reportCardData = GsonParser.getInstance().parseReports(wrapper.getData().toString());
				
				app.setReportCardData(reportCardData);

				
				processItemCallErAge(reportCardData);
				processItems();
			}

		};
	};

	private void getBatch(HashMap<String, String> params){

		if(!uiHelper.isDialogActive())
			uiHelper.showLoadingDialog(getString(R.string.loading_text));
		else
			uiHelper.updateLoadingDialog(getString(R.string.loading_text));

		ApplicationSingleton.getInstance().getNetworkCallInterface().getBatch(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();
						Log.e("Response", ""+response.body());
						Wrapper wrapper=GsonParser.getInstance().parseServerResponse2(response.body());
						if(wrapper.getStatus().getCode()==AppConstant.RESPONSE_CODE_SUCCESS)
						{
							PaidVersionHomeFragment.isBatchLoaded=true;
							PaidVersionHomeFragment.batches.clear();
							String data=wrapper.getData().get("batches").toString();
							PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
							showPicker(PickerType.TEACHER_BATCH);
						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.dismissLoadingDialog();
					}
				}
		);
	}
	AsyncHttpResponseHandler getBatchEventsHandler=new AsyncHttpResponseHandler()
	{

		

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			//uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
		}

		@Override
		public void onStart() {
			super.onStart();
			
			if(!uiHelper.isDialogActive())
				uiHelper.showLoadingDialog(getString(R.string.loading_text));
			else
				uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			uiHelper.dismissLoadingDialog();
			Log.e("Response", responseString);
			Wrapper wrapper=GsonParser.getInstance().parseServerResponse(responseString);
			if(wrapper.getStatus().getCode()==AppConstant.RESPONSE_CODE_SUCCESS)
			{
				PaidVersionHomeFragment.isBatchLoaded=true;
				PaidVersionHomeFragment.batches.clear();
				String data=wrapper.getData().get("batches").toString();
				PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
				showPicker(PickerType.TEACHER_BATCH);
			}
			
		}
		
	};

	private void processItemCallErAge(ReportCardModel reportCardData)
	{
		items.clear();
		items.addAll(reportCardData.getClasstestReportItemList());

		

		holderList = new ArrayList<ViewHolder>();

		for (int i = 0; i < items.size(); i++) {
			holderList.add(new ViewHolder());
		}
	}
	
	
	LayoutInflater mInflater;


	protected void processItems() {
		// TODO Auto-generated method stub

		mInflater = LayoutInflater.from(mContext);

		for (int i = 0; i < items.size(); i++) {
			final int j = i;
			View view = mInflater.inflate(R.layout.row_classtest_report, null);

			ViewHolder holder = holderList.get(i);

			// %%%%%%%%%%%%%%%%%% Classtests %%%%%%%%%%%%%%%%%%
			holder.layoutCT = (LinearLayout) view.findViewById(R.id.layout_ct);

			holder.tvCTDate = (TextView) view.findViewById(R.id.tv_date_ct);
			holder.imageCTSub = (ImageView) view.findViewById(R.id.image_subject_ct);
			holder.tvCTSub = (TextView) view.findViewById(R.id.tv_subject_ct);
			holder.tvCTName = (TextView) view.findViewById(R.id.tv_name_ct);

			holder.tvCTGrade = (TextView) view.findViewById(R.id.tv_grade_ct);
			holder.tvCTMarks = (TextView) view.findViewById(R.id.tv_marks_ct);
			holder.tvCTHighestMarks = (TextView) view.findViewById(R.id.tv_highest_ct);
			holder.tvCTTotalMarks = (TextView) view.findViewById(R.id.tv_total_ct);
			holder.tvCTPercentage = (TextView) view.findViewById(R.id.tv_percent_ct);
			holder.layoutDataHolderClassTest = (LinearLayout)view.findViewById(R.id.layoutDataHolderClassTest);
			holder.txtRemarksClassTest = (TextView)view.findViewById(R.id.txtRemarksClassTest);


			// %%%%%%%%%%%%%%%%%% projects %%%%%%%%%%%%%%%%%%
			holder.layoutProject = (LinearLayout) view.findViewById(R.id.layout_project);

			holder.tvProjectDescription = (ExpandableTextView) view.findViewById(R.id.tv_description_project);
			holder.tvProjectGrade = (TextView) view.findViewById(R.id.tv_grade_project);
			holder.tvProjectHighestMarks = (TextView) view.findViewById(R.id.tv_highest_project);
			holder.tvProjectMarks = (TextView) view.findViewById(R.id.tv_marks_project);

			holder.tvProjectPercentage = (TextView) view.findViewById(R.id.tv_percent_project);
			holder.tvProjectSub = (TextView) view.findViewById(R.id.tv_subject_proect);
			holder.tvProjectTotalMarks = (TextView) view.findViewById(R.id.tv_total_project);

			holder.layoutDataHolderProject = (LinearLayout)view.findViewById(R.id.layoutDataHolderProject);
			holder.txtRemarksProject = (TextView)view.findViewById(R.id.txtRemarksProject);


			// %%%%%%%%%%%%%%%%%% Common %%%%%%%%%%%%%%%%%%
			holder.layoutDynamicRow = (LinearLayout) view.findViewById(R.id.layout_dynamic_row);
			holder.layoutButtons = (LinearLayout) view.findViewById(R.id.layout_buttons);
			holder.layoutRedline = (LinearLayout) view.findViewById(R.id.layout_redline);
			holder.layoutGap = (LinearLayout) view.findViewById(R.id.layout_gap);

			holder.btnProject = (CustomButton) view.findViewById(R.id.btn_project);

			holder.btnProject.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//						processDoneButton(i, (CustomButton) v);

					CustomButton btn = (CustomButton) v;

					ViewHolder parentHolder = holderList.get(j);

					ArrayList<ClassTestItem> projectList = items.get(j).getCtReportSubjectExam().getProjectList();

					if (!btn.isBtnSelected()) {

						if (projectList.size() > 0) {
							btn.setButtonBGSelected(true);
							parentHolder.btnAllCT.setButtonBGSelected(false);

							btn.setEnabled(false);

							parentHolder.layoutCT.setVisibility(View.GONE);
							parentHolder.layoutProject.setVisibility(View.GONE);
						}


						for (int k = 0; k < projectList.size(); k++) {

							View dynamicView = mInflater.inflate(R.layout.row_classtest_report, null);

							ViewHolder newHolder = new ViewHolder();

							newHolder.tvProjectDescription = (ExpandableTextView) dynamicView.findViewById(R.id.tv_description_project);
							newHolder.tvProjectGrade = (TextView) dynamicView.findViewById(R.id.tv_grade_project);
							newHolder.tvProjectHighestMarks = (TextView) dynamicView.findViewById(R.id.tv_highest_project);
							newHolder.tvProjectMarks = (TextView) dynamicView.findViewById(R.id.tv_marks_project);

							newHolder.tvProjectPercentage = (TextView) dynamicView.findViewById(R.id.tv_percent_project);
							newHolder.tvProjectSub = (TextView) dynamicView.findViewById(R.id.tv_subject_proect);
							newHolder.tvProjectTotalMarks = (TextView) dynamicView.findViewById(R.id.tv_total_project);

							newHolder.layoutCT = (LinearLayout) dynamicView.findViewById(R.id.layout_ct);
							newHolder.layoutCT.setVisibility(View.GONE);

							newHolder.layoutProject = (LinearLayout) dynamicView.findViewById(R.id.layout_project);
							newHolder.layoutProject.setVisibility(View.VISIBLE);

							newHolder.layoutButtons = (LinearLayout) dynamicView.findViewById(R.id.layout_buttons);
							newHolder.layoutButtons.setVisibility(View.GONE);

							newHolder.layoutRedline = (LinearLayout) dynamicView.findViewById(R.id.layout_redline);
							newHolder.layoutRedline.setVisibility(View.GONE);

							newHolder.layoutGap = (LinearLayout) dynamicView.findViewById(R.id.layout_gap);
							newHolder.layoutGap.setVisibility(View.GONE);

							newHolder.layoutDataHolderProject = (LinearLayout)dynamicView.findViewById(R.id.layoutDataHolderProject);
							newHolder.txtRemarksProject = (TextView)dynamicView.findViewById(R.id.txtRemarksProject);


							newHolder.tvProjectDescription.setText(projectList.get(k).getTopic());
							newHolder.tvProjectGrade.setText(projectList.get(k).getGrade());
							newHolder.tvProjectHighestMarks.setText(getDecimalFormatNumber(projectList.get(k).getMaxMark())); //projectList.get(k).getMaxMark()
							newHolder.tvProjectMarks.setText(getDecimalFormatNumber(projectList.get(k).getMark())); //projectList.get(k).getMark()

							Double p = Double.parseDouble(projectList.get(k).getPercentage());

							newHolder.tvProjectPercentage.setText(Math.rint(p) + "%");
							newHolder.tvProjectSub.setText(projectList.get(k).getExamName());
							newHolder.tvProjectTotalMarks.setText(getDecimalFormatNumber(projectList.get(k).getTotalMark()));//projectList.get(k).getTotalMark()

							if(items.get(j).getNoExams().equalsIgnoreCase("1"))
							{
								newHolder.layoutDataHolderProject.setVisibility(View.GONE);
								newHolder.txtRemarksProject.setVisibility(View.VISIBLE);
								newHolder.txtRemarksProject.setText(projectList.get(k).getRemarks());
								newHolder.tvCTDate.setVisibility(View.GONE);
							}
							else
							{
								newHolder.layoutDataHolderProject.setVisibility(View.VISIBLE);
								newHolder.txtRemarksProject.setVisibility(View.GONE);
								newHolder.tvCTDate.setVisibility(View.VISIBLE);
							}

							parentHolder.layoutDynamicRow.addView(dynamicView);

							//working on SingleReportCardSubject data
							dynamicView.setTag(items.get(j).getCtReportSubjectExam().getClasstestList());
							final int m = k;
							final String subjectName = items.get(j).getSubjectName();
							final String subjectIcon = items.get(j).getSubjectIcon();
							final String subjectId = items.get(j).getSubjectID();




							dynamicView.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									Intent intent = new Intent(getActivity(), SingleReportCardSubject.class);
									List<ClassTestItem> ctListInner = (List<ClassTestItem>) view.getTag();

									//Gson gson = new Gson();
									//String data = gson.toJson(ctListInner);

									intent.putExtra("inner_reportcard_item_subject_id", subjectId);
									intent.putExtra("inner_reportcard_item_position", m);
									intent.putExtra("inner_reportcard_item_exam_id", ctListInner.get(m).getExamId());
									intent.putExtra("inner_reportcard_item_exam_category_name", getString(R.string.java_reportclasstestfragment_class_test));
									intent.putExtra("inner_reportcard_item_exam_from_class_test", true);
									if(selectedStudent != null)
										intent.putExtra("inner_reportcard_item_student_id", selectedStudent.getId());


									startActivity(intent);
								}
							});


						}

					}


				}
			});

			holder.btnAllCT = (CustomButton) view.findViewById(R.id.btn_all_ct);

			holder.btnAllCT.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {


					CustomButton btn = (CustomButton) v;

					ViewHolder parentHolder = holderList.get(j);

					parentHolder.layoutDynamicRow.removeAllViews();

					parentHolder.layoutProject.setVisibility(View.GONE);
					parentHolder.layoutCT.setVisibility(View.VISIBLE);

					parentHolder.btnProject.setButtonBGSelected(false);
					parentHolder.btnProject.setEnabled(true);

					if (btn.isBtnSelected()) {
						btn.setButtonBGSelected(false);
						parentHolder.layoutDynamicRow.removeAllViews();

					} else {

						if (items.get(j).getCtReportSubjectExam().getClasstestList().size() > 1) {
							btn.setButtonBGSelected(true);
							parentHolder.btnProject.setButtonBGSelected(false);
						}

						// TODO Auto-generated method stub

						for (int k = 1; k < items.get(j).getCtReportSubjectExam().getClasstestList().size(); k++) {
							View dynamicView = mInflater.inflate(R.layout.row_classtest_report, null);

							ViewHolder newHolder = new ViewHolder();


							newHolder.tvCTDate = (TextView) dynamicView.findViewById(R.id.tv_date_ct);
							newHolder.imageCTSub = (ImageView) dynamicView.findViewById(R.id.image_subject_ct);
							newHolder.tvCTSub = (TextView) dynamicView.findViewById(R.id.tv_subject_ct);
							newHolder.tvCTName = (TextView) dynamicView.findViewById(R.id.tv_name_ct);

							newHolder.tvCTGrade = (TextView) dynamicView.findViewById(R.id.tv_grade_ct);
							newHolder.tvCTMarks = (TextView) dynamicView.findViewById(R.id.tv_marks_ct);
							newHolder.tvCTHighestMarks = (TextView) dynamicView.findViewById(R.id.tv_highest_ct);
							newHolder.tvCTTotalMarks = (TextView) dynamicView.findViewById(R.id.tv_total_ct);
							newHolder.tvCTPercentage = (TextView) dynamicView.findViewById(R.id.tv_percent_ct);

							newHolder.layoutCT = (LinearLayout) dynamicView.findViewById(R.id.layout_ct);
							newHolder.layoutCT.setVisibility(View.VISIBLE);

							newHolder.layoutProject = (LinearLayout) dynamicView.findViewById(R.id.layout_project);
							newHolder.layoutProject.setVisibility(View.GONE);

							newHolder.layoutButtons = (LinearLayout) dynamicView.findViewById(R.id.layout_buttons);
							newHolder.layoutButtons.setVisibility(View.GONE);

							newHolder.layoutRedline = (LinearLayout) dynamicView.findViewById(R.id.layout_redline);
							newHolder.layoutRedline.setVisibility(View.GONE);

							newHolder.layoutGap = (LinearLayout) dynamicView.findViewById(R.id.layout_gap);
							newHolder.layoutGap.setVisibility(View.GONE);

							newHolder.layoutDataHolderClassTest = (LinearLayout)dynamicView.findViewById(R.id.layoutDataHolderClassTest);
							newHolder.txtRemarksClassTest = (TextView)dynamicView.findViewById(R.id.txtRemarksClassTest);

							ArrayList<ClassTestItem> ctList = items.get(j).getCtReportSubjectExam().getClasstestList();

							newHolder.tvCTDate.setText(ctList.get(k).getExamDate());
							newHolder.tvCTSub.setVisibility(View.GONE);
							newHolder.imageCTSub.setVisibility(View.GONE);
							newHolder.tvCTName.setText(ctList.get(k).getExamName());

							newHolder.tvCTGrade.setText(ctList.get(k).getGrade());
							newHolder.tvCTHighestMarks.setText(getDecimalFormatNumber(ctList.get(k).getMaxMark()));//ctList.get(k).getMaxMark()
							newHolder.tvCTMarks.setText(getDecimalFormatNumber(ctList.get(k).getMark()));//ctList.get(k).getMark()
							newHolder.tvCTPercentage.setText(ctList.get(k).getPercentage() + "%");
							newHolder.tvCTTotalMarks.setText(getDecimalFormatNumber(ctList.get(k).getTotalMark()));//ctList.get(k).getTotalMark()

							//							Log.e("Position 2", i+"");


							if(items.get(j).getNoExams().equalsIgnoreCase("1"))
							{
								newHolder.layoutDataHolderClassTest.setVisibility(View.GONE);
								newHolder.txtRemarksClassTest.setVisibility(View.VISIBLE);
								newHolder.txtRemarksClassTest.setText(ctList.get(k).getRemarks());
								newHolder.tvCTDate.setVisibility(View.GONE);
							}
							else
							{
								newHolder.layoutDataHolderClassTest.setVisibility(View.VISIBLE);
								newHolder.txtRemarksClassTest.setVisibility(View.GONE);
								newHolder.tvCTDate.setVisibility(View.VISIBLE);
							}

							parentHolder.layoutDynamicRow.addView(dynamicView);

							//working on SingleReportCardSubject data
							dynamicView.setTag(items.get(j).getCtReportSubjectExam().getClasstestList());
							final int m = k;

							final String subjectName = items.get(j).getSubjectName();
							final String subjectIcon = items.get(j).getSubjectIcon();
							final String subjectId = items.get(j).getSubjectID();


							dynamicView.setOnClickListener(new View.OnClickListener(){
								@Override
								public void onClick(View view) {
									Intent intent = new Intent(getActivity(), SingleReportCardSubject.class);
									List<ClassTestItem>  ctListInner = (List<ClassTestItem>)view.getTag();

									//Gson gson = new Gson();
									//String data = gson.toJson(ctListInner);

									intent.putExtra("inner_reportcard_item_subject_id", subjectId);
									intent.putExtra("inner_reportcard_item_position", m);
									intent.putExtra("inner_reportcard_item_exam_id", ctListInner.get(m).getExamId());
									intent.putExtra("inner_reportcard_item_exam_category_name", getActivity().getString(R.string.java_reportclasstestfragment_class_test));
									intent.putExtra("inner_reportcard_item_exam_from_class_test", true);
									if(selectedStudent != null)
										intent.putExtra("inner_reportcard_item_student_id", selectedStudent.getId());

									startActivity(intent);
								}
							});

						}
					}


				}
			});

			ArrayList<ClassTestItem> ctList = items.get(i).getCtReportSubjectExam().getClasstestList();

			int pos = 0;
			holder.tvCTDate.setText(ctList.get(pos).getExamDate());
			holder.tvCTSub.setText(items.get(i).getSubjectName());
			holder.imageCTSub.setBackgroundResource(AppUtility.getImageResourceId(items.get(i).getSubjectIcon(), mContext));
			holder.tvCTName.setText(ctList.get(pos).getExamName());

			holder.tvCTGrade.setText(ctList.get(pos).getGrade());
			holder.tvCTHighestMarks.setText(getDecimalFormatNumber(ctList.get(pos).getMaxMark()));//ctList.get(pos).getMaxMark()

			holder.tvCTMarks.setText(getDecimalFormatNumber(ctList.get(pos).getMark()));//ctList.get(pos).getMark()
			holder.tvCTPercentage.setText(ctList.get(pos).getPercentage() + "%");
			holder.tvCTTotalMarks.setText(getDecimalFormatNumber(ctList.get(pos).getTotalMark()));//ctList.get(pos).getTotalMark()

			holder.layoutCT.setVisibility(View.VISIBLE);
			holder.layoutProject.setVisibility(View.GONE);

			//			ArrayList<ClassTestItem> projectList = items.get(i).getCtReportSubjectExam().getProjectList();
			//			arrangeProjectUI(projectList, holder);

			//working on SingleReportCardSubject data
			view.setTag(items.get(i).getCtReportSubjectExam().getClasstestList());
			final int m = pos;

			final String subjectName = items.get(j).getSubjectName();
			final String subjectIcon = items.get(j).getSubjectIcon();
			final String subjectId = items.get(j).getSubjectID();


			view.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getActivity(), SingleReportCardSubject.class);
					List<ClassTestItem>  ctListInner = (List<ClassTestItem>)view.getTag();

					//Gson gson = new Gson();
					//String data = gson.toJson(ctListInner);

					intent.putExtra("inner_reportcard_item_subject_id", subjectId);
					intent.putExtra("inner_reportcard_item_position", m);
					intent.putExtra("inner_reportcard_item_exam_id", ctListInner.get(m).getExamId());
					intent.putExtra("inner_reportcard_item_exam_category_name", getString(R.string.java_reportclasstestfragment_class_test));
					intent.putExtra("inner_reportcard_item_exam_from_class_test", true);
					if(selectedStudent != null)
						intent.putExtra("inner_reportcard_item_student_id", selectedStudent.getId());

					startActivity(intent);
				}
			});

			if(items.get(i).getNoExams().equalsIgnoreCase("1"))
			{
				holder.layoutDataHolderClassTest.setVisibility(View.GONE);
				holder.txtRemarksClassTest.setVisibility(View.VISIBLE);
				holder.txtRemarksClassTest.setText(ctList.get(pos).getRemarks());
				holder.tvCTDate.setVisibility(View.GONE);
			}
			else
			{
				holder.layoutDataHolderClassTest.setVisibility(View.VISIBLE);
				holder.txtRemarksClassTest.setVisibility(View.GONE);
				holder.tvCTDate.setVisibility(View.VISIBLE);
			}

			listLayout.addView(view);
		}
	}


	@Override
	public void update(String batchId, String schoolId) {
		// TODO Auto-generated method stub
		this.batchId = batchId;
		this.studentId = schoolId;
		uiHelper.showMessage(batchId + "  " + studentId);
		fetchClassTestReport();
	}

	public void showPicker(PickerType type) {

		Picker picker = Picker.newInstance(0);
		picker.setData(type, PaidVersionHomeFragment.batches, PickerCallback , getString(R.string.fragment_lessonplan_view_txt_select_batch));
		picker.show(getChildFragmentManager(), null);
	}
	
	public void showStudentPicker(PickerType type) {

		CustomPickerWithLoadData picker = CustomPickerWithLoadData.newInstance(0);
		HashMap<String, String> params = new HashMap<>();
		params.put(RequestKeyHelper.BATCH_ID,selectedBatch.getId());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		picker.setData(PickerType.TEACHER_STUDENT,params,URLHelper.URL_GET_STUDENTS_ATTENDANCE, PickerCallback , getString(R.string.java_parentreportcardfragment_select_student));
		picker.show(getChildFragmentManager(), null);
	}
	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case TEACHER_BATCH:
				selectedBatch=(Batch)item;
				showStudentPicker(PickerType.TEACHER_STUDENT);
				break;
			case TEACHER_STUDENT:
				selectedStudent = (StudentAttendance)item;
				/*Intent i = new Intent("com.champs21.schoolapp.batch");
                i.putExtra("batch_id", selectedBatch.getId());
                i.putExtra("student_id", selectedStudent.getId());
                getActivity().sendBroadcast(i);*/
				if(pickedStudentNameListenerClassTest != null) {
					pickedStudentNameListenerClassTest.onStudentPicked(selectedStudent.getStudentName());
				}

                fetchClassTestReport();
				break;
			default:
				break;
			}

		}
	};
	
	@Override
	protected void onVisible() {
		// TODO Auto-generated method stub

		if(AppUtility.isInternetConnected() == false){
			Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
		}
		
		if(userHelper.getUser().getType()==UserTypeEnum.TEACHER){
			if(!PaidVersionHomeFragment.isBatchLoaded)
			{
				HashMap<String,String> params=new HashMap<>();
				params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
				//AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
				getBatch(params);
			}else {
				showPicker(PickerType.TEACHER_BATCH);
			}
		}else {
			fetchClassTestReport();
		}
		
	}

	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub
		
	}


	private String getDecimalFormatNumber(float number)
	{
		String value = "";

		value = new DecimalFormat("#.##").format(number);

		return value;
	}

	private String getDecimalFormatNumber(String numberString)
	{
		if(numberString.equalsIgnoreCase("-")) {
			return "-";

		}

		else
		{
			float number = Float.parseFloat(numberString);
			String value = "";

			value = new DecimalFormat("#.##").format(number);

			return value;
		}

	}


	
}
