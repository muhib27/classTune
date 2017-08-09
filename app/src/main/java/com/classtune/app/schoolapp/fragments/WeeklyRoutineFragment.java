package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.Period;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.WeekRoutine;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyRoutineFragment extends Fragment implements
		UserAuthListener {

	private UIHelper uiHelper;
	private UserHelper userHelper;
	private ListView mListView;
	private EfficientAdapter mAdapter;
	private ArrayList<WeekRoutine> weekList;
	private static final int[] dayResArray = { R.id.sunSubLay, R.id.monSubLay,
			R.id.tueSubLay, R.id.wedSubLay, R.id.thuSubLay, R.id.satSubLay };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new EfficientAdapter(getActivity());
		userHelper = new UserHelper(this, getActivity());
		weekList = new ArrayList<WeekRoutine>();
		fetchWeeklyRoutine();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		uiHelper = new UIHelper(this.getActivity());
		View view = inflater.inflate(R.layout.fragment_weekly_routine,
				container, false);
		// RoboGuice.getInjector(getActivity()).injectViewMembers(this);
		initView(view);
		mListView.setAdapter(mAdapter);
		return view;
	}

	private void fetchWeeklyRoutine() {
		// TODO Auto-generated method stub
		HashMap<String,String> params = new HashMap<>();
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
		params.put(RequestKeyHelper.DAILY, "0");
		//AppRestClient.post(URLHelper.URL_ROUTINE, params, routineHandler);
		routine(params);
	}

	private void routine(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().routine(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();
						Log.e("RESPONSE ROUTINE ", ""+response.body());
						// uiHelper.showMessage(responseString);
						Wrapper modelContainer = GsonParser.getInstance()
								.parseServerResponse2(response.body());
						if (modelContainer.getStatus().getCode() == 200) {
//				weekMap = GsonParser.getInstance().parseWeekPeriod(
//						modelContainer.getData().getAsJsonArray("time_table")
//								.toString());
							for(JsonElement obj: modelContainer.getData().getAsJsonArray("time_table")) {
								Set<Map.Entry<String,JsonElement>> entryset = obj.getAsJsonObject().entrySet();
								for(Map.Entry<String,JsonElement> entry:entryset){
									// User newUser=gson.fromJson(p.getAsJsonObject(entry.getKey()),User.class);
									Log.e("TIME: ", entry.getKey());
									String[]key = entry.getKey().split("-");
									String newKey = key[0]+"\n"+key[1];
									ArrayList<Period> allPeriod = GsonParser.getInstance().parseWeekPeriod(entry.getValue().getAsJsonArray().toString());
									weekList.add(new WeekRoutine(newKey, allPeriod));
								}
							}

						} else {

						}
						mAdapter.notifyDataSetChanged();
						// Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
						// .getAllNotice().get(0).getNoticeTypeText());
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.showMessage(getString(R.string.internet_error_text));
						uiHelper.dismissLoadingDialog();
					}
				}
		);
	}
	AsyncHttpResponseHandler routineHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			// uListener.onServerAuthenticationFailed(arg1);
			uiHelper.showMessage(getString(R.string.internet_error_text));
			uiHelper.dismissLoadingDialog();
		}

		@Override
		public void onStart() {
			super.onStart();
			// uListener.onServerAuthenticationStart();
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		}

		// 3gjnfr27hein04pfbe4ur7gbv7
		// 08e9344b9eb6b0fcc56717c5efa6e2d6e08e9e84ad9403ea45816188c5600f89
		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			uiHelper.dismissLoadingDialog();
			Log.e("RESPONSE ROUTINE ", responseString);
			// uiHelper.showMessage(responseString);
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);
			if (modelContainer.getStatus().getCode() == 200) {
//				weekMap = GsonParser.getInstance().parseWeekPeriod(
//						modelContainer.getData().getAsJsonArray("time_table")
//								.toString());
				for(JsonElement obj: modelContainer.getData().getAsJsonArray("time_table")) {
					Set<Map.Entry<String,JsonElement>> entryset = obj.getAsJsonObject().entrySet();
					for(Map.Entry<String,JsonElement> entry:entryset){
					   // User newUser=gson.fromJson(p.getAsJsonObject(entry.getKey()),User.class);
					    Log.e("TIME: ", entry.getKey());
					   String[]key = entry.getKey().split("-");
					   String newKey = key[0]+"\n"+key[1];
					   ArrayList<Period> allPeriod = GsonParser.getInstance().parseWeekPeriod(entry.getValue().getAsJsonArray().toString());
					   weekList.add(new WeekRoutine(newKey, allPeriod));
					}
				}

			} else {

			}
			mAdapter.notifyDataSetChanged();
			// Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
			// .getAllNotice().get(0).getNoticeTypeText());
		}
	};

	private void initView(View view) {
		// TODO Auto-generated method stub
		mListView = (ListView) view.findViewById(R.id.lvWeekly);
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
			// return syllabusMap.get(currentTabKey).size();
			return weekList.size();
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
			// dfsdf
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
			WeekRoutine weekRoutine = weekList.get(position);

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_weekly_routine, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_week_time);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTime.setText(weekRoutine.getTime());
			for(Period period: weekRoutine.getPeriodList()) {
				
				LinearLayout l = (LinearLayout) convertView.findViewById(dayResArray[Integer.parseInt(period.getWeekday_id())]);
				l.setVisibility(View.VISIBLE);
				TextView tv = (TextView)l.getChildAt(0);
				tv.setText(period.getSubject_code());
				
			}

			return convertView;
		}

		class ViewHolder {
			TextView tvTime;
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
