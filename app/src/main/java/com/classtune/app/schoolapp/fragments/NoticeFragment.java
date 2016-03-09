package com.classtune.app.schoolapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.ModelContainer;
import com.classtune.app.schoolapp.model.Notice;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.ReminderHelper;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.CustomButton;
import com.classtune.app.schoolapp.viewhelpers.CustomTabButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

public class NoticeFragment extends UserVisibleHintFragment implements OnClickListener,UserAuthListener {

	
	UIHelper uiHelper;
	String currentTabKey = "1";
	HashMap<String, ArrayList<Notice>> noticeMap;
	EfficientAdapter adapter;
	ListView listviewNotice;
	CustomTabButton current;
	UserHelper userHelper;

	// int currentNormalImageId;
	// int currentSelectedImageId;
	CustomTabButton generalBtn, announcementBtn, circulerBtn;
	private LinearLayout pbs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		noticeMap = new HashMap<String, ArrayList<Notice>>(3);
		userHelper=new UserHelper(this, getActivity());
		
		adapter = new EfficientAdapter(getActivity());
		createArrayList();
		//fetchNotice();
	}

	private void createArrayList() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= 3; i++) {
			String pos = "" + i;
			noticeMap.put(pos, new ArrayList<Notice>());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		uiHelper = new UIHelper(this.getActivity());
		View view = inflater
				.inflate(R.layout.fragment_notice, container, false);
		// RoboGuice.getInjector(getActivity()).injectViewMembers(this);
		initView(view);
		pbs = (LinearLayout)view.findViewById(R.id.pb);
		listviewNotice = (ListView) view.findViewById(R.id.notice_listview);
		listviewNotice.setAdapter(adapter);

		return view;
	}

	AsyncHttpResponseHandler ackBtnHandler = new AsyncHttpResponseHandler() {
		public void onFailure(Throwable arg0, String arg1) {
			Log.e("button", "failed");
			uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();
		};

		public void onStart() {
			Log.e("button", "onstart");
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		};

		public void onSuccess(int arg0, String response) {
			Log.e("response", response);
			Log.e("button", "success");
			uiHelper.dismissLoadingDialog();

			ModelContainer modelContainer = GsonParser.getInstance().parseGson(
					response);

			// arrangeHomeworkData(modelContainer);

			// adapter.notifyDataSetChanged();

			// Log.e("status code", modelContainer.getStatus().getCode() + "");
			if (modelContainer.getData().getNotice_ack()
					.getAcknowledge_status().equals("1")) {
				clickedAckBtn.setImage(R.drawable.done_tap);
				clickedAckBtn.setTitleColor(NoticeFragment.this.getActivity()
						.getResources().getColor(R.color.maroon));
				clickedAckBtn.setTitleText(getActivity().getString(R.string.java_noticefragment_acknowledge));
				clickedAckBtn.setEnabled(false);
			}

		};
	};

	private void initView(View view) {

		HashMap<String, Integer> btnGeneralTag = new HashMap<String, Integer>();

		generalBtn = (CustomTabButton) view
				.findViewById(R.id.btn_notice_general);
		generalBtn.setOnClickListener(this);

		btnGeneralTag.put(AppConstant.NORMAL, R.drawable.eye_gray);
		btnGeneralTag.put(AppConstant.SELECTED, R.drawable.eye_black);

		generalBtn.setTag(btnGeneralTag);

		circulerBtn = (CustomTabButton) view
				.findViewById(R.id.btn_notice_circular);
		circulerBtn.setOnClickListener(this);

		HashMap<String, Integer> btnCircularTag = new HashMap<String, Integer>();
		btnCircularTag.put(AppConstant.NORMAL, R.drawable.circular_gray);
		btnCircularTag.put(AppConstant.SELECTED, R.drawable.circular_black);

		circulerBtn.setTag(btnCircularTag);

		announcementBtn = (CustomTabButton) view
				.findViewById(R.id.btn_notice_announcement);
		announcementBtn.setOnClickListener(this);

		HashMap<String, Integer> btnAnnouncementTag = new HashMap<String, Integer>();
		btnAnnouncementTag
				.put(AppConstant.NORMAL, R.drawable.annaouncment_gray);
		btnAnnouncementTag.put(AppConstant.SELECTED,
				R.drawable.annaouncment_black);

		announcementBtn.setTag(btnAnnouncementTag);

		generalBtn.setButtonSelected(true,
				getResources().getColor(R.color.black), R.drawable.eye_black);
		current = generalBtn;

	}

	AsyncHttpResponseHandler NoticeHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			// uListener.onServerAuthenticationFailed(arg1);
			/*uiHelper.showMessage(arg1);
			uiHelper.dismissLoadingDialog();*/
			pbs.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			super.onStart();
			// uListener.onServerAuthenticationStart();
//			uiHelper.showLoadingDialog("Please wait...");
			pbs.setVisibility(View.VISIBLE);
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
//			uiHelper.dismissLoadingDialog();
			pbs.setVisibility(View.GONE);
			//Toast.makeText(getActivity(), responseString, Toast.LENGTH_LONG).show();
			Log.e("NOTICE RESPONSE ", responseString);
			// uiHelper.showMessage(responseString);
			ModelContainer modelContainer = GsonParser.getInstance().parseGson(
					responseString);
			arrangeNoticeData(modelContainer);
			adapter.notifyDataSetChanged();
//			Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
//					.getAllNotice().get(0).getNoticeTypeText());
		}

	};
	private CustomButton clickedAckBtn;
	private CustomButton clickedReminderBtn;

	public void fetchNotice() {
		
		RequestParams params = new RequestParams();
		
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		
	
		if (userHelper.getUser().getType() == UserTypeEnum.STUDENT||userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo().getSchoolId());
		}
		
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getSelectedChild().getSchoolId());
		}
		
		//params.put(RequestKeyHelper.TO_DATE, "2014-06-23");

		AppRestClient.post(URLHelper.URL_NOTICE, params, NoticeHandler);

	}
	
	public void arrangeNoticeData(ModelContainer container) {
		for(int i=1;i<=3;i++){
			String pos = "" + i;
			noticeMap.get(pos).clear();
		}
		if(container.getStatus().getCode()!=404){
			for (Notice notice : container.getData().getAllNotice()) {
				noticeMap.get(notice.getNoticeTypeId()).add(notice);
			}
		}
		
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
			return noticeMap.get(currentTabKey).size();
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
			Notice notice;
			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_notice_list, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.txt_notice_header);
				/*holder.tvContent = (ExpandableTextView) convertView
						.findViewById(R.id.txt_notice_content);*/
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.txt_notice_date);
				/*holder.acknowledgeBtn = (CustomButton) convertView
						.findViewById(R.id.btn_notice_acknowledge);*/
				holder.reminderBtn = (CustomButton) convertView
						.findViewById(R.id.btn_notice_reminder);

				//holder.acknowledgeBtn.setOnClickListener(NoticeFragment.this);
				holder.reminderBtn.setOnClickListener(NoticeFragment.this);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			notice = noticeMap.get(currentTabKey).get(position);
			// Bind the data efficiently with the holder.
			holder.tvTitle.setText(" "
					+ noticeMap.get(currentTabKey).get(position)
							.getNoticeTitle());

			/*holder.tvContent.setText(" "
					+ Html.fromHtml(noticeMap.get(currentTabKey).get(position)
							.getNoticeContent(), null, new MyTagHandler()));*/
			holder.tvDate.setText(" "
					+ noticeMap.get(currentTabKey).get(position)
							.getPublishedAt());
			/*holder.acknowledgeBtn.setTag(noticeMap.get(currentTabKey)
					.get(position).getNoticeId());*/
			holder.reminderBtn.setTag(noticeMap.get(currentTabKey)
					.get(position));
			/*if (notice.getAllAck().size() != 0) {
				if (notice.getAllAck().get(0).getAcknowledge_status()
						.equals("1")) {

					setButtonState(holder.acknowledgeBtn, R.drawable.done_tap, false, "Acknowledged");
					
				} else if (notice.getAllAck().get(0).getAcknowledge_status()
						.equals("0")) {
					setButtonState(holder.acknowledgeBtn, R.drawable.done_normal, true, "Acknowledge");
				}
			} else {
				setButtonState(holder.acknowledgeBtn, R.drawable.done_normal, false, "Acknowledge");
				holder.acknowledgeBtn.setTitleColor(NoticeFragment.this
						.getActivity().getResources().getColor(R.color.gray_1));
			}*/
			Log.e("NOTICE PUBLISH", notice.getPublishedAt() + "");
			if (ReminderHelper.getInstance().reminder_map.containsKey(notice.getPublishedAt())){
				setButtonState(holder.reminderBtn, R.drawable.btn_reminder_tap, false, getString(R.string.btn_reminder));
				
			}else {
				setButtonState(holder.reminderBtn, R.drawable.btn_reminder_normal, true, getString(R.string.btn_reminder));
			}
			return convertView;
		}

		class ViewHolder {
			TextView tvTitle;
			//ExpandableTextView tvContent;
			TextView tvDate;
			//CustomButton acknowledgeBtn;
			CustomButton reminderBtn;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_notice_general:
			setTabSelected(v, generalBtn, "1");
			break;
		case R.id.btn_notice_circular:
			setTabSelected(v, circulerBtn, "2");
			break;
		case R.id.btn_notice_announcement:
			setTabSelected(v, announcementBtn, "3");
			break;
		/*case R.id.btn_notice_acknowledge:
			requestAcknowledge(v.getTag().toString(), (CustomButton) v);
			break;*/
		case R.id.btn_notice_reminder:
			CustomButton reminderBtn = (CustomButton) v;
			Notice rmNotice = (Notice) reminderBtn.getTag();
			reminderBtn.setImage(R.drawable.btn_reminder_tap);
			reminderBtn.setTitleColor(NoticeFragment.this.getActivity()
					.getResources().getColor(R.color.maroon));
			reminderBtn.setEnabled(false);
			String content = ""+ Html.fromHtml(rmNotice.getNoticeContent());
			ReminderHelper.getInstance().setReminder(rmNotice.getPublishedAt(),
					rmNotice.getNoticeTitle(), content,
					rmNotice.getPublishedAt(), getActivity());
			break;
		default:
			break;
		}
	}

	@SuppressLint("ResourceAsColor")
	private void setButtonState(CustomButton btn, int imgResId, boolean enable , String btnText) {
		btn.setImage(imgResId);
		btn.setTitleText(btnText);
		btn.setEnabled(enable);
		if(enable) {
			setBtnTitleColor(btn, R.color.gray_1); 
		} else {
			setBtnTitleColor(btn, R.color.maroon); 
		}
	}
	
	private void setBtnTitleColor(CustomButton btn, int colorId) {
		btn.setTitleColor(getActivity().getResources().getColor(colorId));
	}
	
	private void requestAcknowledge(String tag, CustomButton btn) {
		// TODO Auto-generated method stub
		this.clickedAckBtn = btn;

		RequestParams params = new RequestParams();

		

		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.NOTICE_ID, btn.getTag().toString());

		AppRestClient.post(URLHelper.URL_NOTICE_ACKNOWLEDGE, params,
				ackBtnHandler);
	}

	@SuppressWarnings("unchecked")
	public void setTabSelected(View v, CustomTabButton currentCustom,
			String noticeTypeId) {
		CustomTabButton btn = (CustomTabButton) v;
		HashMap<String, Integer> btnTag = (HashMap<String, Integer>) current
				.getTag();
		current.setButtonSelected(false,
				getResources().getColor(R.color.black),
				btnTag.get(AppConstant.NORMAL));
		HashMap<String, Integer> clickedBtnTag = (HashMap<String, Integer>) btn
				.getTag();
		btn.setButtonSelected(true, getResources().getColor(R.color.black),
				clickedBtnTag.get(AppConstant.SELECTED));
		current = currentCustom;
		currentTabKey = noticeTypeId;
		adapter.notifyDataSetChanged();
	}
	// i.e input like: 2014-06-04 12:00:09

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
	protected void onVisible() {
		fetchNotice();
	}

	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}

}
