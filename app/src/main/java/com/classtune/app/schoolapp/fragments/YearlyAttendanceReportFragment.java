package com.classtune.app.schoolapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.model.YearlyAttendanceData;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class YearlyAttendanceReportFragment extends Fragment implements UserAuthListener{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "OnActivityCreated");
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("current_series"))
				mSeries = (CategorySeries) savedInstanceState
				.getSerializable("current_series");
			if (savedInstanceState.containsKey("current_renderer"))
				mRenderer = (DefaultRenderer) savedInstanceState
				.getSerializable("current_renderer");
		}
	}

	private View view;
	/** Colors to be used for the pie slices. */
	private static int[] COLORS = new int[] { R.color.present,R.color.absent,R.color.late,R.color.leave};
	//private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;

	private UserHelper userHelper;
	private UIHelper uiHelper;
	private YearlyAttendanceData data;
	private static final String TAG="YearlyAttendanceFrangment";

	private TextView totalClassTextView,presentTextView,leaveTextView,absentTextView,lateTextView,percentTextView,tillMonthTextView,currentTextView;


	public static YearlyAttendanceReportFragment newInstance(String batch_id, String student_id) {
		YearlyAttendanceReportFragment f = new YearlyAttendanceReportFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("batch_id", batch_id);
		args.putString("student_id", student_id);
		f.setArguments(args);
		return f;
	}

	private void fetchYearlyAttendanceData()
	{
		RequestParams params=new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());

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

            //params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getSelectedChild().getSchoolId());
			//params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			//params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}
		if(userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo().getSchoolId());
			params.put(RequestKeyHelper.BATCH_ID, getArguments().getString("batch_id"));
			params.put(RequestKeyHelper.STUDENT_ID, getArguments().getString("student_id"));
		}

		Log.e("params attendance", params.toString());

		AppRestClient.post(URLHelper.URL_GET_ATTENDENCE_EVENTS, params, getYearlyAttendenceEventsHandler);

	}


	AsyncHttpResponseHandler getYearlyAttendenceEventsHandler=new AsyncHttpResponseHandler()
	{

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			//uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
			//Log.e("Error", arg1);
		}

		@Override
		public void onStart() {
			super.onStart();
			//initializeCountValues();
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
			if(wrapper.getStatus().getCode() == 200)
			{
				mSeries.clear();
				mRenderer.removeAllRenderers();
				data=GsonParser.getInstance().parseYearlyAttendanceData(wrapper.getData().toString());
				addValue(String.format("%.2f", calculatePercentage(data.getPresent(), data.getTotalClass())),getResources().getColor(R.color.present),getString(R.string.present_text));
				addValue(String.format("%.2f", calculatePercentage(data.getAbsent(), data.getTotalClass())),getResources().getColor(R.color.absent),getString(R.string.absent_text));
				addValue(String.format("%.2f", calculatePercentage(data.getLate(), data.getTotalClass())),getResources().getColor(R.color.late),getString(R.string.late_text));
				addValue(String.format("%.2f", calculatePercentage(data.getLeave(), data.getTotalClass())),getResources().getColor(R.color.leave),getString(R.string.leave_text));
				updateUI();
			}
			else if(wrapper.getStatus().getCode()==403)
			{
				userHelper.doLogIn();
			}
		}
	};


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("current_series", mSeries);
		outState.putSerializable("current_renderer", mRenderer);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "OnCreate");
		userHelper=new UserHelper(this, getActivity());
		uiHelper=new UIHelper(getActivity());

		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setZoomEnabled(false);
		mRenderer.setStartAngle(180);
		mRenderer.setPanEnabled(false);
		mRenderer.setDisplayValues(true);
		mRenderer.setScale((float) 1.2);

		DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
		float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);

		mRenderer.setLabelsTextSize(val);
		mRenderer.setLegendTextSize(val);
		mRenderer.setLabelsColor(Color.BLACK);
		//fetchYearlyAttendanceData();

	}



	private void addValue(String valueStr, int color, String key)
	{
		double value = 0;
		try {
			value = Double.parseDouble(valueStr);
		} catch (NumberFormatException e) {

			return;
		}
		if(value>0.00){
			mSeries.add(key, value);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(color);
			mRenderer.addSeriesRenderer(renderer);
			mChartView.repaint();
		}
		// mSeries.add("Series " + (mSeries.getItemCount() + 1), value);
		


	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Log.e(TAG, "OnCreateView");
		view = inflater.inflate(R.layout.yearly_attendance_layout, container,
				false);
		LinearLayout layout;
		totalClassTextView=(TextView)view.findViewById(R.id.total_class_value);
		presentTextView=(TextView)view.findViewById(R.id.present_value);
		absentTextView=(TextView)view.findViewById(R.id.absent_value);
		lateTextView=(TextView)view.findViewById(R.id.late_value);
		leaveTextView=(TextView)view.findViewById(R.id.leave_value);
		percentTextView=(TextView)view.findViewById(R.id.percent_value);
		currentTextView=(TextView)view.findViewById(R.id.current_date_text);
		tillMonthTextView=(TextView)view.findViewById(R.id.yearly_attendance_title);

		layout = (LinearLayout) view.findViewById(R.id.chart);
		/*if (mChartView == null) {*/

		mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);//this, , mRenderer);

		mRenderer.setClickEnabled(true);
		mRenderer.setShowLegend(false);
		mChartView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
				if (seriesSelection == null) {

				} else {
					for (int i = 0; i < mSeries.getItemCount(); i++) {
						mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
					}
					mChartView.repaint();

				}
			}
		});
		layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		/*} else {
		    	//mChartView.re
		    	layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
				          LayoutParams.FILL_PARENT));
		      mChartView.repaint();
		    }*/

		return view;
	}

	private void updateUI()
	{
		totalClassTextView.setText(data.getTotalClass()+"");
		presentTextView.setText(data.getPresent()+"");
		absentTextView.setText(data.getAbsent()+"");
		leaveTextView.setText(data.getLeave()+"");
		lateTextView.setText(data.getLate()+"");
		percentTextView.setText(String.format("%.2f", calculatePercentage(data.getPresent() + (float) data.getLeave(), data.getTotalClass()))+"%");
		currentTextView.setText(getDateString(data.getUptoDate()));
		String text = String.format(
				getResources().getString(R.string.yearly_attendance_report_title_text),
				getPreviousMonthdate(data.getUptoDate()));
		tillMonthTextView.setText(text);

	}

	private double calculatePercentage(float value,int totalValue)
	{

		return ((double)value/totalValue) * 100;
	}

	private String getDateString(String str)
	{
		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());

		try {
			parsed = df_input.parse(str);
			outputDate = df_output.format(parsed);

		} catch (ParseException e) {
			Log.e(TAG, "ParseException - dateFormat");
		}

		return outputDate;
	}

	private String getPreviousMonthdate(String str)
	{
		Calendar cal =  Calendar.getInstance();
		SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
		try {
			cal.setTime(df_input.parse(str));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.add(Calendar.MONTH ,-1);
		String previousMonthYear  = new SimpleDateFormat("MMM-yyyy").format(cal.getTime());
		return previousMonthYear;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, "OnResume");

		if(AppUtility.isInternetConnected() == false){
			Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
		}

		fetchYearlyAttendanceData();

	}

	@Override
	public void onAuthenticationStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthenticationSuccessful() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthenticationFailed(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}



}
