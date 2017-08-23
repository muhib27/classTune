package com.classtune.app.freeversion;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.schoolapp.model.ExamRoutine;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleExamRoutine extends ChildContainerActivity {
	
	

	private UIHelper uiHelper;
	private ListView listViewExamData;
	private UserHelper userHelper;
	private List<ExamRoutine> listData;
	private EfficientAdapter mAdapter;
	private TextView examName;

    private RelativeLayout layoutMessage;
    private RelativeLayout layoutDataContainer;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.fragment_examroutine);
		initView();
		uiHelper = new UIHelper(this);
		listData = new ArrayList<ExamRoutine>();
		mAdapter = new EfficientAdapter(this);
		userHelper = new UserHelper(this, this);
		listViewExamData.setAdapter(mAdapter);
		examName = (TextView)findViewById(R.id.tv_report_exam_name);
		examName.setText(getIntent().getExtras().getString("term_name"));
		fetchExamRoutine();

        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getExtras().containsKey("total_unread_extras"))
            {
                String rid = getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                String rtype = getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");


                GcmIntentService.initApiCall(rid, rtype);
            }
        }
	}

	private void fetchExamRoutine() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		HashMap<String,String> params = new HashMap<>();
		// app.showLog("Secret before sending", app.getUserSecret());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put("exam_id", getIntent().getExtras().getString(AppConstant.ID_SINGLE_CALENDAR_EVENT));
		params.put("no_exams", "1");
		if(userHelper.getUser().getType()==UserTypeEnum.PARENTS){

            if(getIntent().getExtras()!=null)
            {
                if(getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                    params.put(RequestKeyHelper.STUDENT_ID, getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                    params.put(RequestKeyHelper.BATCH_ID, getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


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

            //params.put(RequestKeyHelper.SCHOOL,userHelper.getUser().getPaidInfo().getSchoolId());
			//params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			//params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
		}
		//AppRestClient.post(URLHelper.URL_ROUTINE_EXAM, params, examRoutineHandler);
		routineExam(params);
	}

	private void routineExam(HashMap<String,String> params){

		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));

		ApplicationSingleton.getInstance().getNetworkCallInterface().routineExam(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();

						if (response.body() != null ){
							Log.e("RESPONSE ROUTINE ", ""+response.body());
							// uiHelper.showMessage(responseString);
							Wrapper modelContainer = GsonParser.getInstance()
									.parseServerResponse2(response.body());
							if (modelContainer.getStatus().getCode() == 200) {

								layoutDataContainer.setVisibility(View.VISIBLE);
								layoutMessage.setVisibility(View.GONE);

								listData = GsonParser.getInstance().parseExam(
										modelContainer.getData()
												.getAsJsonArray("exam_time_table").toString());

								Log.e("ListData SIZE: ", listData.size() + "");
							}

							else if(modelContainer.getStatus().getCode() != 200 || modelContainer.getStatus().getCode() != 404)
							{
								layoutDataContainer.setVisibility(View.GONE);
								layoutMessage.setVisibility(View.VISIBLE);
							}
							mAdapter.notifyDataSetChanged();
							// Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
							// .getAllNotice().get(0).getNoticeTypeText());
						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.showMessage(getString(R.string.internet_error_text));
						uiHelper.dismissLoadingDialog();
					}
				}
		);
	}
	AsyncHttpResponseHandler examRoutineHandler = new AsyncHttpResponseHandler() {

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

		// u44tk4p199mvhgi8gf378ui510
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

                layoutDataContainer.setVisibility(View.VISIBLE);
                layoutMessage.setVisibility(View.GONE);

				listData = GsonParser.getInstance().parseExam(
						modelContainer.getData()
								.getAsJsonArray("exam_time_table").toString());

				Log.e("ListData SIZE: ", listData.size() + "");
			}

            else if(modelContainer.getStatus().getCode() != 200 || modelContainer.getStatus().getCode() != 404)
            {
                layoutDataContainer.setVisibility(View.GONE);
                layoutMessage.setVisibility(View.VISIBLE);
            }

            else {

			}
			mAdapter.notifyDataSetChanged();
			// Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
			// .getAllNotice().get(0).getNoticeTypeText());
		}
	};

	private void initView() {
		// TODO Auto-generated method stub
		listViewExamData = (ListView) findViewById(R.id.listViewExamData);

        layoutMessage = (RelativeLayout)this.findViewById(R.id.layoutMessage);
        layoutDataContainer = (RelativeLayout)this.findViewById(R.id.layoutDataContainer);

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
			return listData.size();
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
			ExamRoutine item = listData.get(position);

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.fragment_examroutine_singledata, null);



				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.tvSubject = (TextView) convertView
						.findViewById(R.id.txtSubject);
				holder.tvStartTime = (TextView) convertView
						.findViewById(R.id.txtStartTime);
				//holder.tvEndTime = (TextView) convertView.findViewById(R.id.txtEndTime);

				holder.tvDay = (TextView) convertView.findViewById(R.id.txtDay);

				holder.tvDate = (TextView) convertView
						.findViewById(R.id.txtDate);


                holder.layoutTitle = (LinearLayout)convertView.findViewById(R.id.layoutTitle);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
			
			Log.e("ITEM SUBJECT", item.getExam_subject_name());
			holder.tvSubject.setText(item.getExam_subject_name());

			/*reworked for no_exams
			holder.tvStartTime.setText(item.getExam_start_time() + " - "+item.getExam_end_time());
			*/

			//holder.tvEndTime.setText(item.getExam_end_time());

			/*reworked for no_exams
			holder.tvDate.setText(item.getExam_date());
			*/

			/*reworked for no_exams
			holder.tvDay.setText(item.getExam_day());
			*/


			if(item.getNoExams().equalsIgnoreCase("1"))
			{
				holder.tvDate.setText(R.string.java_singlexeamroutine_na);
				holder.tvStartTime.setText(R.string.java_singlexeamroutine_na);
				holder.tvDay.setVisibility(View.GONE);
			}
			else
			{
				holder.tvStartTime.setText(item.getExam_start_time() + " - "+item.getExam_end_time());
				holder.tvDate.setText(item.getExam_date());
				holder.tvDay.setText(item.getExam_day());
				holder.tvDay.setVisibility(View.VISIBLE);
			}


            if(position%2 != 0)
                holder.layoutTitle.setBackgroundColor(getResources().getColor(R.color.bg_row_odd));
            else
                holder.layoutTitle.setBackgroundColor(Color.WHITE);
			
			return convertView;
		}

		class ViewHolder {

            LinearLayout layoutTitle;
			TextView tvSubject;
			TextView tvStartTime;
			//TextView tvEndTime;
			TextView tvDay;
			TextView tvDate;
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
