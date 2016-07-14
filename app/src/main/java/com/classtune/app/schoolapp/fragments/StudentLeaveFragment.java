package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.StudentAttendance;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentLeaveFragment extends UserVisibleHintFragment{

	
	
	
	
	private View rootView;
	private ListView studentListView;
	private LinearLayout pbLayout;
	private ArrayList<StudentAttendance> arraylist = new ArrayList<StudentAttendance>();
	private StudentLeaveListAdapter adapter;
	private UIHelper uiHelper;
	
	private TextView txtDate;

	private TextView txtMessage;


	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//fetchData();
		
	}

	private void fetchData() {

		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		AppRestClient.post(URLHelper.URL_GET_STUDENT_LEAVE_LIST, params,
				getStudentHandler);

	}

	AsyncHttpResponseHandler getStudentHandler = new AsyncHttpResponseHandler() {
		public void onFailure(Throwable arg0, String arg1) {
			pbLayout.setVisibility(View.GONE);
			Log.e("error", arg1);
		};

		public void onStart() {
			pbLayout.setVisibility(View.VISIBLE);
			arraylist.clear();
			if(adapter!=null)
			adapter.notifyDataSetChanged();
		};

		public void onSuccess(int arg0, String responseString) {
			arraylist.clear();

			pbLayout.setVisibility(View.GONE);
			Log.e("Menu", responseString);
			Wrapper wrapper = GsonParser.getInstance().parseServerResponse(
					responseString);
			arraylist.addAll(GsonParser.getInstance().parseStudentList(
					(wrapper.getData().get("leaves")).toString()));
			adapter = new StudentLeaveListAdapter(getActivity(), arraylist);
			studentListView.setAdapter(adapter);


			if(arraylist.size() <=0 )
			{
				txtMessage.setVisibility(View.VISIBLE);
			}
			else
			{
				txtMessage.setVisibility(View.GONE);
			}



		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UIHelper(getActivity());
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_leave_approval_layout, container,
				false);
		studentListView = (ListView) rootView.findViewById(R.id.listview);
		pbLayout = (LinearLayout) rootView.findViewById(R.id.pb);
		// Capture Text in EditText
		txtDate = (TextView)rootView.findViewById(R.id.date_text);
		Date cDate = new Date();
		String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
		txtDate.setText(AppUtility.getDateString(fDate, AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));


		txtMessage = (TextView)rootView.findViewById(R.id.txtMessage);
		
		return rootView;
	}
	
	
	private Batch selectedBatch;
	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case TEACHER_BATCH:
				selectedBatch=(Batch)item;
				fetchData();
				break;
			
			default:
				break;
			}

		}
	};
	@Override
	protected void onVisible() {

		if(AppUtility.isInternetConnected() == false){
			Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
		}

		fetchData();
	}

	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub
		
	}
	
	
	public class StudentLeaveListAdapter extends BaseAdapter {

		// Declare Variables
		Context mContext;
		LayoutInflater inflater;
		private List<StudentAttendance> studentlist = null;
		private ArrayList<StudentAttendance> arraylist;

		public StudentLeaveListAdapter(Context context, List<StudentAttendance> studentlist) {
			mContext = context;
			this.studentlist = studentlist;
			inflater = LayoutInflater.from(mContext);
			this.arraylist = new ArrayList<StudentAttendance>();
			this.arraylist.addAll(studentlist);
		}

		public class ViewHolder {
			TextView namebatch,applydate,startend;
			ImageButton accept,decline;
		}

		@Override
		public int getCount() {
			return studentlist.size();
		}

		@Override
		public StudentAttendance getItem(int position) {
			return studentlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View view, ViewGroup parent) {
			final ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = inflater.inflate(R.layout.item_leave, null);
				// Locate the TextViews in listview_item.xml
				holder.namebatch = (TextView) view.findViewById(R.id.tv_name_and_batch);
				holder.applydate = (TextView) view.findViewById(R.id.tv_apply_date);
				holder.startend = (TextView) view.findViewById(R.id.tv_start_date_and_end_date);
				holder.accept = (ImageButton) view.findViewById(R.id.btn_approve_leave);
				holder.decline = (ImageButton) view.findViewById(R.id.btn_decline_leave);
				
				
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			// Set the results into TextViews
			//holder.population.setText(worldpopulationlist.get(position).getPopulation());
			
			holder.decline.setTag(""+position);
			holder.accept.setTag(""+position);
			holder.namebatch.setText(studentlist.get(position).getStudentName()+mContext.getString(R.string.java_studentleavefragment_batch)+studentlist.get(position).getBatch());
			holder.applydate.setText(mContext.getString(R.string.java_studentleavefragment_apply_date)+studentlist.get(position).getCreateDate());
			// Listen for ListView Item Click
			holder.startend.setText(mContext.getString(R.string.java_studentleavefragment_duration)+studentlist.get(position).getLeaveStartDate()+mContext.getString(R.string.java_studentleavefragment_to)+studentlist.get(position).getLeaveEndDate());
			
			
			holder.accept.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = Integer.parseInt(v.getTag().toString());
					
					initApiCallApprove(studentlist.get(pos).getId(), String.valueOf(studentlist.get(pos).getLeaveId()), "1");
				}
			});
			holder.decline = (ImageButton)view.findViewById(R.id.btn_decline_leave);
			holder.decline.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = Integer.parseInt(v.getTag().toString());
					
					
					initApiCallApprove(studentlist.get(pos).getId(), String.valueOf(studentlist.get(pos).getLeaveId()), "0");
				}
			});
			

			return view;
		}

		

	}
	
	
	private void initApiCallApprove(String studentId, String leaveId, String approveCode)
	{
		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.STUDENT_ID, studentId);
		params.put("leave_id", leaveId);
		params.put("status", approveCode);
		
		AppRestClient.post(URLHelper.URL_APPROVE_LEAVE, params,
				approveHandler);
	}
	
	AsyncHttpResponseHandler approveHandler = new AsyncHttpResponseHandler() {

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
				
				fetchData();
				Toast.makeText(getActivity(), R.string.java_studentleavefragment_successfully_done, Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
			}
			
			else
			{
				
			}
		}
		
	};
	

}
