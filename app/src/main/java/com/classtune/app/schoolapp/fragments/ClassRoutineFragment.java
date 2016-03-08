package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Period;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
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

import java.util.ArrayList;

public class ClassRoutineFragment extends Fragment implements UserAuthListener{

	private UIHelper uiHelper;
	private UserHelper userHelper;
	private GridView mGridView;
	private ArrayList<Period> periodList;
	private EfficientAdapter mAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		periodList = new ArrayList<Period>();
		mAdapter = new EfficientAdapter(getActivity());
		userHelper = new UserHelper(this, getActivity());
		fetchRoutine();
	}

	private void fetchRoutine() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		// app.showLog("Secret before sending", app.getUserSecret());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		if (userHelper.getUser().getType() == UserTypeEnum.STUDENT) {
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo().getSchoolId());
		}


		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getSelectedChild().getSchoolId());
		}
		//params.put(RequestKeyHelper.DATE, AppUtility.getCurrentDate(AppUtility.DATE_FORMAT_SERVER));
		params.put(RequestKeyHelper.DAILY, "1");
		AppRestClient.post(URLHelper.URL_ROUTINE, params, routineHandler);
	}

	AsyncHttpResponseHandler routineHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			// uListener.onServerAuthenticationFailed(arg1);
			uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
		}

		@Override
		public void onStart() {
			super.onStart(); 
			// uListener.onServerAuthenticationStart();
			uiHelper.showLoadingDialog(getActivity().getString(R.string.java_accountsettingsactivity_please_wait));
		}
		//3gjnfr27hein04pfbe4ur7gbv7
		//08e9344b9eb6b0fcc56717c5efa6e2d6e08e9e84ad9403ea45816188c5600f89
		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			uiHelper.dismissLoadingDialog();
			Log.e("RESPONSE ROUTINE ", responseString);
			// uiHelper.showMessage(responseString);
			Wrapper modelContainer = GsonParser.getInstance().parseServerResponse(
					responseString);
			if(modelContainer.getStatus().getCode()==200) {
				periodList = GsonParser.getInstance().parsePeriod(modelContainer.getData().getAsJsonArray("time_table").toString());

			} else {

			}
			mAdapter.notifyDataSetChanged();
			//			Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
			//					.getAllNotice().get(0).getNoticeTypeText());
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		uiHelper = new UIHelper(this.getActivity());
		View view = inflater
				.inflate(R.layout.fragment_class_routine, container, false);
		// RoboGuice.getInjector(getActivity()).injectViewMembers(this);
		initView(view);
		mGridView.setAdapter(mAdapter);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mGridView = (GridView)view.findViewById(R.id.gv_class_routine);
	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			//return syllabusMap.get(currentTabKey).size();
			return periodList.size();
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;
			Period item = periodList.get(position);

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.cell_grid, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_period_position);
				holder.tvduration = (TextView) convertView
						.findViewById(R.id.tv_period_duration);
				holder.tvSubjectName = (TextView) convertView
						.findViewById(R.id.tv_period_subject_name);
				holder.subjectIcon = (ImageView)convertView.findViewById(R.id.img_subject_icon);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(periodList.size()>0){
				holder.tvSubjectName.setText(item.getSubject_name());
				holder.tvduration.setText(item.getClass_start_time()+getActivity().getString(R.string.java_classroutinefragment_to)+item.getClass_end_time());
				holder.tvTitle.setText(getIntWithSuffix(position + 1) + getActivity().getString(R.string.java_classroutinefragment_period));
				holder.subjectIcon.setImageResource(AppUtility.getImageResourceId(item.getSubject_icon_name(), getActivity()));
			}
			
			return convertView;
		}
		
		public String getIntWithSuffix(int num){
			switch (num) {
			case 1:
				
				return num+getActivity().getString(R.string.java_classroutinefragment_st);
			case 2:
				
				return num+getActivity().getString(R.string.java_classroutinefragment_nd);
			case 3:
				
				return num+getActivity().getString(R.string.java_classroutinefragment_rd);

			default:
				return num+getActivity().getString(R.string.java_classroutinefragment_th);
			}
			
		}
		class ViewHolder {
			TextView tvTitle;
			TextView tvduration;
			TextView tvSubjectName;
			ImageView subjectIcon;
		}
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
