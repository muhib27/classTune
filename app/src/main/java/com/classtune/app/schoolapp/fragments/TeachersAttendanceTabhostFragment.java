package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.SchoolApp;
import com.classtune.app.schoolapp.viewhelpers.MyFragmentTabHost;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class TeachersAttendanceTabhostFragment extends Fragment implements OnClickListener{

	private UIHelper uiHelper;
	private TextView batchNameText;
	private ImageButton selectBatchBtn;
	public static String dateString="";
	
	private TextView txtDate;
	private  HorizontalScrollView horizontalScrollView;




	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dateString="";
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		uiHelper=new UIHelper(getActivity());
		super.onCreate(savedInstanceState);
		/*if(!PaidVersionHomeFragment.isBatchLoaded)
		{
			RequestParams params=new RequestParams();
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
		}else showPicker(PickerType.TEACHER_BATCH);*/
		
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
	
	public void showPicker(PickerType type) {

		Picker picker = Picker.newInstance(0);
		picker.setData(type, PaidVersionHomeFragment.batches, PickerCallback, getString(R.string.fragment_lessonplan_view_txt_select_batch));
		picker.show(getChildFragmentManager(), null);
	}

	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case TEACHER_BATCH:
				Batch selectedBatch=(Batch)item;
				PaidVersionHomeFragment.selectedBatch=selectedBatch;
				batchNameText.setText(selectedBatch.getName());
				/*Intent i = new Intent("com.champs21.schoolapp.batch");
                i.putExtra("batch_id", selectedBatch.getId());
                getActivity().sendBroadcast(i);*/

				SchoolApp.getInstance().globalBroadcastThroughApp(AppConstant.KEY_BATCH_FROM_TEACHERATTENDANCE_TAB, new Gson().toJson(selectedBatch));


				break;
			default:
				break;
			}
		}
	};




	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private MyFragmentTabHost mTabHostAttendanceTeacher;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;

	private View rootView;

	/**
		 *
		 */
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	/**
		 *
		 *
		 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 *
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	public void updateSelectedBatch(){
		if(PaidVersionHomeFragment.selectedBatch!=null){
			batchNameText.setText(PaidVersionHomeFragment.selectedBatch.getName());
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.teacher_attendance_tabhost_layout,
				container, false);
		mTabHostAttendanceTeacher = (MyFragmentTabHost) rootView
				.findViewById(R.id.tabhost_attendance_teacher);
		batchNameText=(TextView)rootView.findViewById(R.id.batch_text);
		if(PaidVersionHomeFragment.selectedBatch!=null){
			batchNameText.setText(PaidVersionHomeFragment.selectedBatch.getName());
		}
		selectBatchBtn=(ImageButton)rootView.findViewById(R.id.select_batch_btn);
		selectBatchBtn.setOnClickListener(this);

		Date cDate = new Date();
		String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
		txtDate = (TextView)rootView.findViewById(R.id.date_text);
		txtDate.setText(AppUtility.getDateString(fDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));

		horizontalScrollView = (HorizontalScrollView)rootView.findViewById(R.id.horizontalScrollView);

		initialiseTabHost(savedInstanceState);



		return rootView;
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHostAttendanceTeacher.getCurrentTabTag()); // save
																					// the
																					// tab
																					// selected
		super.onSaveInstanceState(outState);
	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHostAttendanceTeacher.setup(getActivity(),
				getChildFragmentManager(),
				R.id.realtabcontent_attendance_teacher);
		TabInfo tabInfo = null;
		MyFragmentTabHost.TabSpec spec = mTabHostAttendanceTeacher
				.newTabSpec(AppConstant.TAB_ROLLCALL_TEACHER);
		spec.setIndicator(getIndicatorView(getString(R.string.title_roll_call_tab)));
		addTab(this.mTabHostAttendanceTeacher, spec, (tabInfo = new TabInfo(
				AppConstant.TAB_ROLLCALL_TEACHER,
				RollCallTeacherFragment.class, args)));

		spec = mTabHostAttendanceTeacher
				.newTabSpec(AppConstant.TAB_CLASS_REPORT_TEACHER);
		spec.setIndicator(getIndicatorView(getString(R.string.title_class_report_tab)));
		addTab(this.mTabHostAttendanceTeacher, spec, (tabInfo = new TabInfo(
				AppConstant.TAB_CLASS_REPORT_TEACHER,
				ClassReportTeacherFragment.class, args)));

		spec = mTabHostAttendanceTeacher
				.newTabSpec(AppConstant.TAB_STUDENT_REPORT_TEACHER);
		spec.setIndicator(getIndicatorView(getString(R.string.title_student_report_tab)));
		addTab(this.mTabHostAttendanceTeacher, spec, (tabInfo = new TabInfo(
				AppConstant.TAB_STUDENT_REPORT_TEACHER,
				StudentReportTeacherFragment.class, args)));

		spec = mTabHostAttendanceTeacher
				.newTabSpec(AppConstant.TAB_TEACHER_SUBJECT_ATTENDANCE_TAKE_ATTENDANCE);
		spec.setIndicator(getIndicatorView(getString(R.string.subject_attentance)));
		addTab(this.mTabHostAttendanceTeacher, spec, (tabInfo = new TabInfo(
				AppConstant.TAB_TEACHER_SUBJECT_ATTENDANCE_TAKE_ATTENDANCE,
				TeacherSubjectAttendanceTakeAttendanceFragment.class, args)));


		spec = mTabHostAttendanceTeacher
				.newTabSpec(AppConstant.TAB_TEACHER_SUBJECT_ATTENDANCE_REPORT);
		spec.setIndicator(getIndicatorView(getString(R.string.title_student_report_tab)));
		addTab(this.mTabHostAttendanceTeacher, spec, (tabInfo = new TabInfo(
				AppConstant.TAB_TEACHER_SUBJECT_ATTENDANCE_REPORT,
				TeacherSubjectAttendanceReport.class, args)));


		Log.e("SURRENT_TAB", "is: "+mTabHostAttendanceTeacher.getCurrentTab());

		// Default to first tab
		// this.onTabChanged(AppConstant.TAB_ROLLCALL_TEACHER);
		//
		// mTabHostAttendanceTeacher.setOnTabChangedListener(this);

		mTabHostAttendanceTeacher.setOnTabChangedListener(new TabHost.OnTabChangeListener(){

			@Override
			public void onTabChanged(String s) {

				DisplayMetrics displaymetrics = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int width = displaymetrics.widthPixels;

				final TabWidget tabWidget = mTabHostAttendanceTeacher.getTabWidget();
				final int screenWidth = width;
				final int leftX = tabWidget.getChildAt(mTabHostAttendanceTeacher.getCurrentTab()).getLeft();
				int newX = 0;

				newX = leftX + (tabWidget.getChildAt(mTabHostAttendanceTeacher.getCurrentTab()).getWidth() / 2) - (screenWidth / 2);
				if (newX < 0) {
					newX = 0;
				}
				horizontalScrollView.smoothScrollTo(newX, 0);

			}
		});


	}

	private View getIndicatorView(String text) {
		View tabIndicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab_indicator_attendance,
				this.mTabHostAttendanceTeacher.getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(text);
		return tabIndicator;
	}

	/**
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private void addTab(MyFragmentTabHost tabHost,
			MyFragmentTabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new TabFactory(getActivity()));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = getChildFragmentManager().findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			getChildFragmentManager().executePendingTransactions();
		}

		tabHost.addTab(tabSpec, tabInfo.clss, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_batch_btn:
			showPicker(PickerType.TEACHER_BATCH);
			break;

		default:
			break;
		}

	}


}
