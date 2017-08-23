package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.StudentAttendance;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLeaveFragment extends  UserVisibleHintFragment{


	
	private View rootView;
	private ListView studentListView;
	private LinearLayout pbLayout;
	private ArrayList<StudentAttendance> arraylist = new ArrayList<StudentAttendance>();
	private StudentLeaveListAdapter adapter;
	
	
	private UserHelper userHelper;
	
	private TextView txtDate;

	private TextView txtMessage;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//fetchData();
		userHelper = new UserHelper(getActivity());
	}

	private void fetchData() {

		HashMap<String,String> params = new HashMap<>();
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
		{
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());


            if(getActivity().getIntent().getExtras()!=null)
            {
                if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                    params.put(RequestKeyHelper.STUDENT_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));

                    GcmIntentService.initApiCall(rid, rtype);
                }
                else
                {
                    params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());

                }
            }
            else
            {
                params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());

            }

			//params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
			//AppRestClient.post(URLHelper.URL_GET_PARENT_LEAVE_LIST, params, getStudentHandler);
			techerLeaveList(params);
		}
		else
		{
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			//AppRestClient.post(URLHelper.URL_GET_TEACHER_LEAVE_LIST, params, getStudentHandler);
			techerLeaveList(params);
		}
		
		
		

	}

	private void techerLeaveList(HashMap<String,String> params){
		pbLayout.setVisibility(View.VISIBLE);
		arraylist.clear();
		if(adapter!=null)
			adapter.notifyDataSetChanged();
		ApplicationSingleton.getInstance().getNetworkCallInterface().teacherLeaveList(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						arraylist.clear();
						pbLayout.setVisibility(View.GONE);
						if (response.body() != null){
							Log.e("Menu", ""+ response.body());
							Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
									response.body());
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
						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						pbLayout.setVisibility(View.GONE);
					}
				}
		);
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
			TextView namebatch,applydate,startend,status, txtLeaveSubject;
			
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
				view = inflater.inflate(R.layout.item_leave_teacher, null);
				// Locate the TextViews in listview_item.xml
				holder.namebatch = (TextView) view.findViewById(R.id.tv_name_and_batch);
				holder.applydate = (TextView) view.findViewById(R.id.tv_apply_date);
				holder.startend = (TextView) view.findViewById(R.id.tv_start_date_and_end_date);
				holder.status = (TextView)view.findViewById(R.id.tv_leave_status);
				holder.txtLeaveSubject = (TextView)view.findViewById(R.id.txtLeaveSubject);
				
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			// Set the results into TextViews
			//holder.population.setText(worldpopulationlist.get(position).getPopulation());
			if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
			{
				holder.txtLeaveSubject.setVisibility(View.VISIBLE);
				holder.namebatch.setVisibility(View.GONE);
			}
			else
			{
				holder.txtLeaveSubject.setVisibility(View.GONE);
				holder.namebatch.setVisibility(View.VISIBLE);
			}
			
			
			
			holder.txtLeaveSubject.setText(studentlist.get(position).getLeave_subject());
			
			holder.namebatch.setText(studentlist.get(position).getLeave_type());
			holder.applydate.setText(mContext.getString(R.string.java_myleavefragment_apply_date)+studentlist.get(position).getCreateDate());
			// Listen for ListView Item Click
			holder.startend.setText(mContext.getString(R.string.java_myleavefragment_duration)+studentlist.get(position).getLeaveStartDate()+mContext.getString(R.string.java_myleavefragment_to)+studentlist.get(position).getLeaveEndDate());
			switch (studentlist.get(position).getTeacherLeaveStatus()) {
			case 0:
				holder.status.setText(R.string.java_myleavefragment_declined);
				holder.status.setTextColor(getResources().getColor(R.color.white));
				holder.status.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
				break;
			case 1:
				holder.status.setTextColor(getResources().getColor(R.color.white));
				holder.status.setText(R.string.java_myleavefragment_accepted);
				holder.status.setBackgroundColor(getActivity().getResources().getColor(R.color.accepted_color));
				break;
			case 2:
				holder.status.setTextColor(getResources().getColor(R.color.black));
				holder.status.setText(R.string.java_myleavefragment_pending);
				holder.status.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_2));
				break;
			default:
				break;
			}
			return view;
		}

		

	}

}
