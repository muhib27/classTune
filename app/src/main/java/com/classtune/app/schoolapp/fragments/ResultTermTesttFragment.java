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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.freeversion.SingleItemTermReportActivity;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.R;
import com.classtune.app.schoolapp.adapters.ExamRoutineListAdapter;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.ExamRoutine;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.StudentAttendance;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ResultTermTesttFragment extends UserVisibleHintFragment implements
		UserAuthListener {
	
	private Batch selectedBatch;
	private StudentAttendance selectedStudent;
	private View view;
	private ListView examList;
	private UserHelper userHelper;
	private UIHelper uiHelper;
	private List<ExamRoutine> items;
	private Context con;
	private ExamRoutineListAdapter adapter;
	private TextView gridTitleText;
	private LinearLayout pbs;
	private static final String TAG = "Exam Routine";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("HAAHHAHA", "Oncreate!!!");
		init();
	}
	
	public static ResultTermTesttFragment newInstance(String batchId, String studentId) {
		ResultTermTesttFragment f = new ResultTermTesttFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("batch_id", batchId);
		args.putString("student_id", studentId);
		f.setArguments(args);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_academic_calendar_exam,
				container, false);
		((LinearLayout) view.findViewById(R.id.top_panel_for_others))
				.setVisibility(View.GONE);
		examList = (ListView) view.findViewById(R.id.exam_listview);
		gridTitleText = (TextView) view.findViewById(R.id.grid_title_textview);
		gridTitleText.setText("Term Test");
		pbs = (LinearLayout) view.findViewById(R.id.pb);
		setUpList();
		//loadDataInToList();
		return view;
	}

	private void loadDataInToList() {

		if (AppUtility.isInternetConnected()) {
			fetchDataFromServer();
		} else
			uiHelper.showMessage(con.getString(R.string.internet_error_text));

	}

	private void fetchDataFromServer() {

		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.CATEGORY_ID, "3");
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {


            if(getActivity().getIntent().getExtras()!=null)
            {
                if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                    params.put(RequestKeyHelper.STUDENT_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                    params.put(RequestKeyHelper.BATCH_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


                    GcmIntentService.initApiCall(rid, rtype);
                }
                else
                {
                    params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                    params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
                }
            }
            else
            {
                params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
                params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
            }


            //params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			//params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());

			/*Log.e("SCHOOL_BATCH_ID_STUDENT_ID", userHelper.getUser()
					.getPaidInfo().getSchoolId()
					+ " "
					+ userHelper.getUser().getPaidInfo().getBatchId()
					+ " "
					+ userHelper.getUser().getSelectedChild().getProfileId());*/
		}else if(userHelper.getUser().getType() == UserTypeEnum.TEACHER){
			params.put(RequestKeyHelper.BATCH_ID, selectedBatch.getId());
			params.put(RequestKeyHelper.STUDENT_ID, selectedStudent.getId());
		}

		/*
		 * params.put(RequestKeyHelper.PAGE_NUMBER,"1");
		 * params.put(RequestKeyHelper.PAGE_SIZE, "500");
		 * params.put(RequestKeyHelper.ORIGIN, "1");
		 */

		// params.put(RequestKeyHelper.BATCH_ID,
		// userHelper.getUser().getPaidInfo().getBatchId());

		// params.put("category", pageSize+"");

		AppRestClient.post(URLHelper.URL_GET_RESULT_REPORT,
				params, getAcademicEventsHandler);

	}

	AsyncHttpResponseHandler getAcademicEventsHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			pbs.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			super.onStart();
			/*
			 * if(uiHelper.isDialogActive())
			 * uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			 * else
			 * uiHelper.showLoadingDialog(getString(R.string.loading_text));
			 */
			pbs.setVisibility(View.VISIBLE);

		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			// uiHelper.dismissLoadingDialog();
			pbs.setVisibility(View.GONE);
			Log.e("report term response", responseString);
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SUCCESS) {
				items.clear();
				items.addAll(GsonParser.getInstance()
						.parseExamRoutine(
								wrapper.getData().getAsJsonArray("all_exam")
										.toString()));
				adapter.notifyDataSetChanged();
			} else if (wrapper.getStatus().getCode() == AppConstant.RESPONSE_CODE_SESSION_EXPIRED) {
				// userHelper.doLogIn();
			}
			//Log.e("Events", responseString);

			initListActionClick();

		}

	};

	private void initListActionClick() {
		examList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ExamRoutine data = (ExamRoutine) adapter.getItem(position);
				Intent intent = new Intent(getActivity(),
						SingleItemTermReportActivity.class);
				intent.putExtra("id",
						data.getId());
				if(userHelper.getUser().getType()==UserTypeEnum.TEACHER){
					intent.putExtra("batch_id", selectedBatch.getId());
					intent.putExtra("student_id", selectedStudent.getId());
				}
				intent.putExtra("term_name", data.getName());
				startActivity(intent);
			}
		});
	}

	private void setUpList() {
		adapter = new ExamRoutineListAdapter(con, items);
		examList.setAdapter(adapter);
	}

	private void init() {
		con = getActivity();
		items = new ArrayList<ExamRoutine>();
		uiHelper = new UIHelper(getActivity());
		userHelper = new UserHelper(this, con);

	}

	@Override
	public void onAuthenticationStart() {

		if (uiHelper.isDialogActive())
			uiHelper.updateLoadingDialog(getString(R.string.authenticating_text));
		else
			uiHelper.showLoadingDialog(getString(R.string.authenticating_text));

	}

	@Override
	public void onAuthenticationSuccessful() {
		fetchDataFromServer();
	}

	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub

	}

	public void showPicker(PickerType type) {

		Picker picker = Picker.newInstance(0);
		picker.setData(type, PaidVersionHomeFragment.batches, PickerCallback , "Select Batch");
		picker.show(getChildFragmentManager(), null);
	}
	
	public void showStudentPicker(PickerType type) {

		CustomPickerWithLoadData picker = CustomPickerWithLoadData.newInstance(0);
		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.BATCH_ID,selectedBatch.getId());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		picker.setData(PickerType.TEACHER_STUDENT,params,URLHelper.URL_GET_STUDENTS_ATTENDANCE, PickerCallback , "Select Student");
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
                //fetchDataFromServer();
				loadDataInToList();
				break;
			default:
				break;
			}

		}
	};
	
	@Override
	protected void onVisible() {
		// TODO Auto-generated method stub
		
		if(userHelper.getUser().getType()==UserTypeEnum.TEACHER){
			if(!PaidVersionHomeFragment.isBatchLoaded)
			{
				RequestParams params=new RequestParams();
				params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
				AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
			}else {
				showPicker(PickerType.TEACHER_BATCH);
			}
		}else {
			loadDataInToList();
		}
		
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
	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub

	}

}