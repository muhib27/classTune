package com.classtune.app.schoolapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.classtune.app.freeversion.SingleTeacherHomeworkActivity;
import com.classtune.app.freeversion.TeacherHomeworkDoneActivity;
import com.classtune.app.R;
import com.classtune.app.schoolapp.fragments.TeacherHomeWorkFragment.IFilterClicked;
import com.classtune.app.schoolapp.fragments.TeacherHomeWorkFragment.IFilterInsideClicked;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.HomeWorkSubject;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.TeacherHomework;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.CustomDateTimePicker;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonArray;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TeacherHomeWorkFeedFragment extends Fragment implements UserAuthListener, IFilterClicked, IFilterInsideClicked{
	
	UIHelper uiHelper;
	UserHelper userHelper;
	private PullToRefreshListView listGoodread;
	private GoodReadAdapter adapter;
	private ArrayList<TeacherHomework> allGooadReadPost = new ArrayList<TeacherHomework>();
	private ProgressBar spinner;
	
	
	boolean hasNext = false;
	private int pageNumber = 1;
	private int pageSize = 10;
	private boolean isRefreshing = false;
	private boolean loading = false;
	private boolean stopLoadingData = false;
	
	private String selectedSubjectId;
	private String selectedDate;
	private List<BaseType> homeWorkSubject;

	private TextView txtMessage;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UIHelper(getActivity());
		adapter = new GoodReadAdapter(getActivity());
		userHelper = new UserHelper(this, getActivity());
		allGooadReadPost.clear();
		Log.e("SIZE OF ALLGOODREADPOST:", "" + allGooadReadPost.size());
		
		homeWorkSubject = new ArrayList<BaseType>();
		
		TeacherHomeWorkFragment.lsitener = this;
		TeacherHomeWorkFragment.lsitenerInside = this;


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homework_feed, container, false);
		listGoodread = (PullToRefreshListView) view
				.findViewById(R.id.listView_category);
		spinner = (ProgressBar) view.findViewById(R.id.loading);
		listGoodread.setAdapter(adapter);
		//adapter.notifyDataSetChanged();
		setUpList();
		loadDataInToList();
		
		initListAction();

		txtMessage = (TextView)view.findViewById(R.id.txtMessage);

		
		return view;
	}
	
	
	private void initListAction()
	{
		
		listGoodread.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				TeacherHomework  data = (TeacherHomework)adapter.getItem(position-1);
				
				Intent intent = new Intent(getActivity(), SingleTeacherHomeworkActivity.class);
				intent.putExtra(AppConstant.ID_SINGLE_HOMEWORK, data.getId());
				startActivity(intent);
				
				Log.e("DATA_CLICKED", "is: " + data.getId());
				
				
				
			}
		});
		
	}
	
	
	private void loadDataInToList() {
		if (AppUtility.isInternetConnected()) {
			processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,"");
		} else
			uiHelper.showMessage(getActivity().getString(
					R.string.internet_error_text));
	}
	
	private void setUpList() {

		initializePageing();
		listGoodread.setMode(Mode.PULL_FROM_END);
		// Set a listener to be invoked when the list should be refreshed.
		listGoodread.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				Mode m = listGoodread.getCurrentMode();
				if (m == Mode.PULL_FROM_START) {
					stopLoadingData = false;
					isRefreshing = true;
					pageNumber = 1;
					loading = true;
					processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							"");
				} else if (!stopLoadingData) {
					pageNumber++;
					loading = true;
					processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							"");
				} else {
					new NoDataTask().execute();
				}
			}
		});

		adapter = new GoodReadAdapter(getActivity());
		listGoodread.setAdapter(adapter);
	}
	
	private void processFetchPost(String url, String categoryIndex) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		
		if (UserHelper.isLoggedIn()) {
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		}

		params.put(RequestKeyHelper.PAGE_NUMBER, pageNumber + "");
		params.put(RequestKeyHelper.PAGE_SIZE, pageSize + "");
		

		// Log.e("Params", params.toString());

			
		AppRestClient.post(URLHelper.URL_TEACHER_HOMEWORK_FEED, params,
					fitnessHandler);
		
	}
	
	private void processFetchPost(String url, String categoryIndex, boolean isFilterApply) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		
		if (UserHelper.isLoggedIn()) {
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		}

		params.put(RequestKeyHelper.PAGE_NUMBER, pageNumber + "");
		params.put(RequestKeyHelper.PAGE_SIZE, pageSize + "");
		
		if(isFilterApply == true)
		{
			params.put("subject_id", selectedSubjectId);
		}

		// Log.e("Params", params.toString());

			
		AppRestClient.post(URLHelper.URL_TEACHER_HOMEWORK_FEED, params,
					fitnessHandler);
		
	}
	
	
	private void processFetchPostDate(String url, String categoryIndex, boolean isFilterApply) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		
		if (UserHelper.isLoggedIn()) {
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		}

		params.put(RequestKeyHelper.PAGE_NUMBER, pageNumber + "");
		params.put(RequestKeyHelper.PAGE_SIZE, pageSize + "");
		
		if(isFilterApply == true)
		{
			params.put("duedate", selectedDate);
		}

		// Log.e("Params", params.toString());

			
		AppRestClient.post(URLHelper.URL_TEACHER_HOMEWORK_FEED, params,
					fitnessHandler);
		
	}
	
	
	
	
	AsyncHttpResponseHandler fitnessHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		@Override
		public void onStart() {
			if (pageNumber == 0 && !isRefreshing) {
				if (!uiHelper.isDialogActive())
					uiHelper.showLoadingDialog(getString(R.string.loading_text));
				else
					uiHelper.updateLoadingDialog(getString(R.string.loading_text));
			}
			if (pageNumber == 1) {
				spinner.setVisibility(View.VISIBLE);
			}
		};

		@Override
		public void onSuccess(int arg0, String responseString) {

			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
			/*
			 * if (fitnessAdapter.getPageNumber() == 1) {
			 * fitnessAdapter.getList().clear(); // setupPoppyView(); }
			 */
			Log.e("Response CATEGORY", responseString);
			// app.showLog("Response", responseString);
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);

			if (modelContainer.getStatus().getCode() == 200) {

				hasNext = modelContainer.getData().get("has_next")
						.getAsBoolean();

				if (pageNumber == 1)
					allGooadReadPost.clear();
				spinner.setVisibility(View.GONE);
				if (!hasNext) {
					// fitnessAdapter.setStopLoadingData(true);
					stopLoadingData = true;
				}

				// fitnessAdapter.getList().addAll();
				ArrayList<TeacherHomework> allpost = GsonParser.getInstance()
						.parseTeacherHomework(
								modelContainer.getData().getAsJsonArray("homework")
										.toString());

				

				Log.e("pagenumber: " + pageNumber, "  size of list: " + allpost.size());
				for (int i = 0; i < allpost.size(); i++) {

					allGooadReadPost.add(allpost.get(i));
				}
				adapter.notifyDataSetChanged();

				if (pageNumber != 0 || isRefreshing) {
					listGoodread.onRefreshComplete();
					loading = false;
				}


				if(allGooadReadPost.size() <= 0)
				{
					txtMessage.setVisibility(View.VISIBLE);
				}
				else
				{
					txtMessage.setVisibility(View.GONE);
				}
				
				
			}

		}

		
	};
	
	
	private void showSubjectPicker() {
		Picker picker = Picker.newInstance(0);
		picker.setData(PickerType.HOMEWORK_SUBJECT, homeWorkSubject, PickerCallback,
				"Choose your subject");
		picker.show(getChildFragmentManager(), null);
	}
	
	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case HOMEWORK_SUBJECT:
				HomeWorkSubject hs = (HomeWorkSubject) item;
				selectedSubjectId = hs.getId();
				
					
				adapter.clearList();
				
				processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,"", true);
				
				
				
				break;
			default:
				break;
			}

		}
	};
	
	
	private void initializePageing() {
		pageNumber = 1;
		isRefreshing = false;
		loading = false;
		stopLoadingData = false;
	}
	public static SchoolFeedFragment newInstance(int schoolId) {
		SchoolFeedFragment f = new SchoolFeedFragment();
		Bundle args = new Bundle();
		args.putInt("school_id", schoolId);
		f.setArguments(args);
		return f;
	}

	private class NoDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			listGoodread.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	
	public class GoodReadAdapter extends BaseAdapter {

		private Context context;
		
		public GoodReadAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return allGooadReadPost.size();
		}

		@Override
		public Object getItem(int position) {
			return allGooadReadPost.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void clearList()
		{
			allGooadReadPost.clear();
		}
		
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final int i = position;

			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = LayoutInflater.from(this.context).inflate(
						R.layout.item_teacher_homework_feed, parent,
						false);
				holder.subjectName = (TextView) convertView.findViewById(R.id.tv_teacher_feed_subject_name);
				holder.date = (TextView) convertView.findViewById(R.id.tv_teacher_homewrok_feed_date);
				//holder.classname = (TextView) convertView.findViewById(R.id.tv_teacher_homework_feed_class);
				holder.section = (TextView) convertView.findViewById(R.id.tv_teavher_homework_feed_section);
				holder.doneBtn = (CustomButton) convertView.findViewById(R.id.btn_done);
				holder.homeworkContent = (TextView) convertView.findViewById(R.id.tv_homework_content);
				holder.txtAssignDate = (TextView)convertView.findViewById(R.id.txtAssignDate);
				holder.txtAttachment = (TextView)convertView.findViewById(R.id.txtAttachment);
				
				holder.ivSubjectIcon = (ImageView)convertView.findViewById(R.id.imgViewCategoryMenuIcon);
				
				//holder.reminderBtn = (CustomButton) convertView.findViewById(R.id.btn_reminder);
				
				/*holder.reminderBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(v.getTag().toString());
						AppUtility.showDateTimePicker(AppConstant.KEY_HOMEWORK+allGooadReadPost.get(pos).getId(), allGooadReadPost.get(pos).getSubjects()+ ": " + AppConstant.NOTIFICATION_HOMEWORK, allGooadReadPost.get(pos).getHomework_name(), getActivity());
					}
				});*/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.homeworkContent.setFocusable(false);
			holder.homeworkContent.setFocusableInTouchMode(false);
			
			
			if (allGooadReadPost.size() > 0) {
				TeacherHomework hwork = allGooadReadPost.get(position);
				holder.subjectName.setText(hwork.getSubjects());
				holder.date.setText(AppUtility.getDateString(hwork.getDuedate(), AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));
				//holder.classname.setText(hwork.getCourse());
				holder.section.setText(hwork.getBatch());
				//holder.homeworkContent.setText(Html.fromHtml(hwork.getContent(),null,new MyTagHandler()));
				holder.homeworkContent.setText(allGooadReadPost.get(position).getHomework_name());
				/*holder.doneBtn.setTag(""+position);
				holder.reminderBtn.setTag(""+position);*/
				
				holder.doneBtn.setTag(allGooadReadPost.get(position).getId());
				
				holder.doneBtn.setTitleText("Done by "+allGooadReadPost.get(position).getDone());
				holder.doneBtn.setTextSize(16);
				
				holder.txtAssignDate.setText(AppUtility.getDateString(hwork.getAssign_date(), AppUtility.DATE_FORMAT_APP, AppUtility.DATE_FORMAT_SERVER));
				
				holder.ivSubjectIcon.setImageResource(AppUtility.getImageResourceId(allGooadReadPost.get(position).getSubjects_icon(), context));
				
			} else {
				Log.e("FREE_HOME_API", "array is empty!");
			}
			
			
			if(!TextUtils.isEmpty(allGooadReadPost.get(position).getAttachment_file_name()))
			{
				holder.txtAttachment.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.txtAttachment.setVisibility(View.GONE);
			}
			
			
			if(!allGooadReadPost.get(position).getDone().equals("0"))
			{
				holder.doneBtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String id = (String) ((CustomButton)v).getTag();
						
						Intent intent = new Intent(getActivity(), TeacherHomeworkDoneActivity.class);
						intent.putExtra(AppConstant.ID_TEACHER_HOMEWORK_DONE, id);
						startActivity(intent);
					}
				});
			}
			
			

			return convertView;
		}
	}

	class ViewHolder {
		
		TextView subjectName, date,classname, section, txtAssignDate, txtAttachment;
		TextView homeworkContent;
		CustomButton doneBtn;
		ImageView ivSubjectIcon;

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

	
	
	
	private void initApiCallSubject()
	{
		RequestParams params = new RequestParams();

		//app.showLog("adfsdfs", app.getUserSecret());

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		
		AppRestClient.post(URLHelper.URL_HOMEWORK_SUBJECT_TEACHER, params, subjectHandler);
		
		
	}
	
	
	AsyncHttpResponseHandler subjectHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(arg1);
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}
		};

		@Override
		public void onStart() {
			
				uiHelper.showLoadingDialog("Please wait...");
			

		};

		@Override
		public void onSuccess(int arg0, String responseString) {
			if (uiHelper.isDialogActive()) {
				uiHelper.dismissLoadingDialog();
			}

			homeWorkSubject.clear();
			
			Log.e("Response", responseString);
			//app.showLog("Response", responseString);
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);
			if (modelContainer.getStatus().getCode() == 200) {

				JsonArray array = modelContainer.getData().get("subjects").getAsJsonArray();
				
				List<HomeWorkSubject> list = new ArrayList<HomeWorkSubject>();
				for(int i=0;i<array.size();i++)
				{
					
					list.add(new HomeWorkSubject(array.get(i).getAsJsonObject().get("name").getAsString(), array.get(i).getAsJsonObject().get("id").getAsString()));
					
					
				}
				
				homeWorkSubject.addAll(list);

				showSubjectPicker();
			}

			else {

			}
		};
	};



	@Override
	public void onFilterSubjectClicked() {
		// TODO Auto-generated method stub
		initApiCallSubject();
	}

	@Override
	public void onFilterDateClicked() {
		// TODO Auto-generated method stub
		showDateTimePicker(getActivity());
	}
	
	private void showDateTimePicker(final Context context) {
		CustomDateTimePicker custom = new CustomDateTimePicker(context,
				new CustomDateTimePicker.ICustomDateTimeListener() {

					@Override
					public void onCancel() {

					}

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {
						// TODO Auto-generated method stub

						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd");

						String dateStr = format.format(dateSelected);

						selectedDate = dateStr;
						Log.e("Date Selected", selectedDate);
						
						adapter.clearList();
						processFetchPostDate(URLHelper.URL_FREE_VERSION_CATEGORY,"", true);
						
						
						
					}
				});
		/**
		 * Pass Directly current time format it will return AM and PM if you set
		 * false
		 */
		custom.set24HourFormat(false);
		/**
		 * Pass Directly current data and time to show when it pop up
		 */
		custom.setDate(Calendar.getInstance());
		custom.showDialog();
	}

	@Override
	public void onFilterClicked(boolean isClicked) {
		// TODO Auto-generated method stub
		if(!isClicked)
		{
			processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,"");
		}
		
		
	}


	//kjhglkjhghglkjghlkjbglkjgblkjhglkjhlkjhlkjh
	
}
