package com.classtune.app.schoolapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.AnyFragmentLoadActivity;
import com.classtune.app.freeversion.ChildContainerActivity;
import com.classtune.app.freeversion.SingleEventActivity;
import com.classtune.app.freeversion.SingleExamRoutine;
import com.classtune.app.freeversion.SingleHomeworkActivity;
import com.classtune.app.freeversion.SingleMeetingRequestActivity;
import com.classtune.app.freeversion.SingleNoticeActivity;
import com.classtune.app.schoolapp.model.NotificationReminder;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.SharedPreferencesHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

public class NotificationActivity extends ChildContainerActivity {
	
	
	boolean hasNext = false;
	private PullToRefreshListView mListView;
	private ProgressBar mSpinner;
	private NotificationAdapter mAdapter;
	private Context mContext;
	private int pageNumber = 1;
	private int pageSize = 10;
	private boolean isRefreshing = false;
	private boolean loading = false;
	private boolean stopLoadingData = false;
	private static final int REQUEST_REMINDER = 85;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		initviews();
		setUpList();
		fetchNotification();

        if(getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().containsKey("total_unread_extras"))
            {
                String rid = getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                String rtype = getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");
				GcmIntentService.initApiCall(rid, rtype);
            }
        }
	}

	private void fetchNotification() {
		RequestParams params = new RequestParams();
		params.put("user_secret", UserHelper.getUserSecret());
		params.put(RequestKeyHelper.PAGE_NUMBER, pageNumber + "");
		params.put(RequestKeyHelper.PAGE_SIZE, pageSize + "");
		AppRestClient.post(URLHelper.URL_NOTIFICATION, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() { if (pageNumber == 1) {
					mSpinner.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFailure(Throwable arg0, String responseString) {
				//super.onFailure(arg0, responseString);
				mSpinner.setVisibility(View.GONE);
				Toast.makeText(NotificationActivity.this, R.string.internet_error_text, Toast.LENGTH_LONG).show();
				//Log.e("Response FAIL", responseString.toString());
			}

			@Override
			public void onSuccess(int arg0, String responseString) {
				//Toast.makeText(NotificationActivity.this, responseString, Toast.LENGTH_LONG).show();
				mSpinner.setVisibility(View.GONE);
				/*
				 * if (fitnessAdapter.getPageNumber() == 1) {
				 * fitnessAdapter.getList().clear(); // setupPoppyView(); }
				 */
				Log.e("Response NOTIFICATION", responseString.toString());
				// app.showLog("Response", responseString);
				Wrapper modelContainer = GsonParser.getInstance().parseServerResponse(
						responseString);

				if (modelContainer.getStatus().getCode() == 200) {

					hasNext = modelContainer.getData().get("has_next").getAsBoolean();

					if (pageNumber == 1)
						mAdapter.clearList();

					if (!hasNext) {
						// fitnessAdapter.setStopLoadingData(true);
						stopLoadingData = true;
					}

					// fitnessAdapter.getList().addAll();
					ArrayList<NotificationReminder> allpost = GsonParser.getInstance()
							.parseNotification(
									modelContainer.getData().getAsJsonArray("reminder")
											.toString());
					
					mAdapter.addData(allpost);
					Log.e("NOTIFICATION LIST SIZE", mAdapter.list.size() + "");
					mAdapter.notifyDataSetChanged();
					
					// if (pageNumber != 0 || isRefreshing) {
					mListView.onRefreshComplete();
					loading = false;
					// }
				}
			}
		});
		
	}

	private void initviews() {
		mListView = (PullToRefreshListView) findViewById(R.id.list_notification);
		mSpinner = (ProgressBar)findViewById(R.id.progressBar);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				NotificationReminder data = (NotificationReminder)mAdapter.getItem(position-1);
				invokeClasses(data.getRtype(), data);

				data.setIsAlreadyRead(true);

				//View clickedView = mAdapter.getView(position-1, null, null);
				
				LinearLayout lay = (LinearLayout)view.findViewById(R.id.layoutRootView);
				lay.setBackgroundColor(Color.WHITE);
				
			}
		});
		
	}
	
	private void setUpList() {

		initializePageing();
		mListView.setMode(Mode.PULL_FROM_END);
		// Set a listener to be invoked when the list should be refreshed.
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(NotificationActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				Mode m = mListView.getCurrentMode();
				if (m == Mode.PULL_FROM_START) {
					stopLoadingData = false;
					isRefreshing = true;
					pageNumber = 1;
					loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId, "", true);*/
				} else if (!stopLoadingData) {
					pageNumber++;
					loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId, "", false);*/
					fetchNotification();
				} else {
					new NoDataTask().execute();
				}
			}
		});

		mAdapter = new NotificationAdapter(this,
				new ArrayList<NotificationReminder>());
		mAdapter.clearList();
		mListView.setAdapter(mAdapter);
	}
	public class NotificationAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<NotificationReminder> list;
		
		public NotificationAdapter(Context context, ArrayList<NotificationReminder> list) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.list = list;
		}
		public void clearList() {
			// TODO Auto-generated method stub
			this.list.clear();
		}
		private void addData(ArrayList<NotificationReminder> nlist){
			this.list.addAll(nlist);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public class ViewHolder{
			
			TextView txtTitle;
			TextView txtBody;
			ImageView imgViewIcon;
			LinearLayout layoutRootView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(
						R.layout.row_notification, parent, false);

				holder.txtTitle = (TextView)convertView.findViewById(R.id.tv_ntitle);
				holder.txtBody = (TextView)convertView.findViewById(R.id.tv_ndescription);
				holder.imgViewIcon = (ImageView)convertView.findViewById(R.id.iv_nicon);
				holder.layoutRootView = (LinearLayout)convertView.findViewById(R.id.layoutRootView);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.txtTitle.setText(list.get(position).getSubject());
			holder.txtBody.setText(list.get(position).getBody());
			setImgViewIcon(holder.imgViewIcon, list.get(position).getRtype());
			
			if(list.get(position).getIs_read().equalsIgnoreCase("0"))
			{
				holder.layoutRootView.setBackgroundColor(Color.parseColor("#edeeef"));
			}
			else
			{
				holder.layoutRootView.setBackgroundColor(Color.WHITE);
			}

			if(list.get(position).isAlreadyRead())
			{
				holder.layoutRootView.setBackgroundColor(Color.WHITE);
			}
			
			
			return convertView;
		}
		
	}
	private void initializePageing() {
		pageNumber = 1;
		isRefreshing = false;
		loading = false;
		stopLoadingData = false;
	}
	private class NoDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	
	
	
	private void setImgViewIcon(ImageView imgView, String rType)
	{
		int type = Integer.parseInt(rType);
		
		switch (type) {
		case 1:
			imgView.setImageResource(R.drawable.notice_event);
			break;
			
		case 2:
			imgView.setImageResource(R.drawable.notice_exam_schadule);
			break;
			
		case 3:
			imgView.setImageResource(R.drawable.notice_exam_report);
			break;
			
		case 4:
			imgView.setImageResource(R.drawable.notice_homework);
			break;
			
		case 5:
			imgView.setImageResource(R.drawable.notice_notice);
			break;
			
		case 6:
			imgView.setImageResource(R.drawable.notice_attendance);
			break;
			
		case 7:
			imgView.setImageResource(R.drawable.notice_leave);
			break;
			
		case 8:
			imgView.setImageResource(R.drawable.notice_leave);
			break;
			
		case 9:
			imgView.setImageResource(R.drawable.notice_leave);
			break;
			
		case 10:
			imgView.setImageResource(R.drawable.notice_leave);
			break;
			
		case 11:
			imgView.setImageResource(R.drawable.notice_meeting_request);
			break;

		case 12:
			imgView.setImageResource(R.drawable.notice_meeting_request);
			break;
			
		case 13:
			imgView.setImageResource(R.drawable.notice_meeting_request);
			break;
			
		case 14:
			imgView.setImageResource(R.drawable.notice_meeting_request);
			break;
			
		default:
			imgView.setImageResource(R.drawable.notice_default);
			
			break;
		}
		
		
	}
	
	
	
	private void invokeClasses(String rType, NotificationReminder data)
	{
		int type = Integer.parseInt(rType);
		Intent intent;
		switch (type) {
		case 1:
			
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ParentEventFragment");*/

			intent = new Intent(this, SingleEventActivity.class);
			intent.putExtra(AppConstant.ID_SINGLE_EVENT, data.getRid());


            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 2:
			
			intent = new Intent(this, SingleExamRoutine.class);
			intent.putExtra(AppConstant.ID_SINGLE_CALENDAR_EVENT, data.getRid());

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 3:
			
			intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ParentReportCardFragment");

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
				intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
			
		case 4:
			intent = new Intent(this, SingleHomeworkActivity.class);
			intent.putExtra(AppConstant.ID_SINGLE_HOMEWORK, data.getRid());


            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }


			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
			
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 5:
			intent = new Intent(this, SingleNoticeActivity.class);
			intent.putExtra(AppConstant.ID_SINGLE_NOTICE, data.getRid());

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }


			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 6:
			intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ParentAttendenceFragment");


            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }


			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 7:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivity(intent);*/
			
			break;
			
		case 8:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivityForResult(intent, REQUEST_REMINDER);*/
			
			intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "MyLeaveFragment");

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }


			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 9:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivityForResult(intent, REQUEST_REMINDER);*/

			intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "StudentLeaveFragment");

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 10:
			/*intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "ApplyForLeaveFragment");
			startActivityForResult(intent, REQUEST_REMINDER);*/
			
			intent = new Intent(this, AnyFragmentLoadActivity.class);
			intent.putExtra("class_name", "MyLeaveFragment");

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 11:
			intent = new Intent(this, SingleMeetingRequestActivity.class);
			intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, data.getRid());

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);
			
			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 12:
			intent = new Intent(this, SingleMeetingRequestActivity.class);
			intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, data.getRid());

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);

			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;
			
		case 13:
			intent = new Intent(this, SingleMeetingRequestActivity.class);
			intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, data.getRid());

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);

			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;

		case 14:
			intent = new Intent(this, SingleMeetingRequestActivity.class);
			intent.putExtra(AppConstant.ID_SINGLE_MEETING_REQUEST, data.getRid());

            if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
            {
                Bundle extra = new Bundle();
                extra.putString("student_id", data.getStudentId());
                extra.putString("batch_id", data.getBatchId());
                intent.putExtra("total_unread_extras", extra);
            }

			startActivityForResult(intent, REQUEST_REMINDER);

			
			if(data.getIs_read().equalsIgnoreCase("0"))
				initApiCall(data.getRid(), rType);
			
			break;

            case 15:
                intent = new Intent(this, AnyFragmentLoadActivity.class);
                intent.putExtra("class_name", "QuizFragment");

                if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
                {
                    Bundle extra = new Bundle();
                    extra.putString("student_id", data.getStudentId());
                    extra.putString("batch_id", data.getBatchId());
                    intent.putExtra("total_unread_extras", extra);
                }

                startActivityForResult(intent, REQUEST_REMINDER);

                if(data.getIs_read().equalsIgnoreCase("0"))
                    initApiCall(data.getRid(), rType);

                break;

			//send case 20 to 'Task' feature for now its going to SchoolFeedFragment as a dummy
			case 20:
				intent = new Intent(this, AnyFragmentLoadActivity.class);
				intent.putExtra("class_name", "SchoolFeedFragment");

				if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
				{
					Bundle extra = new Bundle();
					extra.putString("student_id", data.getStudentId());
					extra.putString("batch_id", data.getBatchId());
					intent.putExtra("total_unread_extras", extra);
				}

				startActivityForResult(intent, REQUEST_REMINDER);

				if(data.getIs_read().equalsIgnoreCase("0"))
					initApiCall(data.getRid(), rType);

				break;

			case 21:
				intent = new Intent(this, AnyFragmentLoadActivity.class);
				intent.putExtra("class_name", "SchoolFeedFragment");

				if(userHelper.getUser().getType() == UserHelper.UserTypeEnum.PARENTS)
				{
					Bundle extra = new Bundle();
					extra.putString("student_id", data.getStudentId());
					extra.putString("batch_id", data.getBatchId());
					intent.putExtra("total_unread_extras", extra);
				}

				startActivityForResult(intent, REQUEST_REMINDER);

				if(data.getIs_read().equalsIgnoreCase("0"))
					initApiCall(data.getRid(), rType);

				break;

		default:
			break;
		}
	}
	
	
	
	
	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, responseCode, intent);
		
		if(requestCode == REQUEST_REMINDER)
		{
			//mAdapter.notifyDataSetChanged();
		}
		
	}
	
	
	private void initApiCall(String rId, String rTtype)
	{

		RequestParams params = new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());


        if(rId != null)
        {
            params.put("rid", rId);
        }

        if(rTtype != null)
		{
			params.put("rtype", rTtype);
		}

		
		AppRestClient.post(URLHelper.URL_EVENT_REMINDER, params, reminderHandler);
	
	}
	

	AsyncHttpResponseHandler reminderHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		@Override
		public void onStart() {
			
				uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
			

		};

		@Override
		public void onSuccess(int arg0, String responseString) {
			

			uiHelper.dismissLoadingDialog();


			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {
				
				//fetchNotification();

                modelContainer.getData().get("unread_total").getAsString();

                SharedPreferencesHelper.getInstance().setString("total_unread", modelContainer.getData().get("unread_total").getAsString());

                userHelper.saveTotalUnreadNotification( modelContainer.getData().get("unread_total").getAsString());

                listenerActivity.onNotificationCountChangedFromActivity(Integer.parseInt(modelContainer.getData().get("unread_total").getAsString()));
				
			}
			
			else {

			}
		};
	};


    public static INotificationCountChangedFromActivity listenerActivity;

    public interface INotificationCountChangedFromActivity{

        public void onNotificationCountChangedFromActivity(int count);

    }



}
