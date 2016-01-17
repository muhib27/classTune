package com.classtune.app.schoolapp.fragments;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.freeversion.SingleSyllabus;
import com.classtune.app.R;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.Syllabus;
import com.classtune.app.schoolapp.model.Term;
import com.classtune.app.schoolapp.model.UserAuthListener;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.CustomTabButton;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

public class SyllabusFragment_old extends UserVisibleHintFragment implements
		OnClickListener, UserAuthListener, OnItemClickListener {

	private UIHelper uiHelper;
	private UserHelper userHelper;
	private ListView listviewSyllabus;
	private EfficientAdapter adapter;
	private HashMap<String, ArrayList<Term>> termMap;
	// private HashMap<String, V>
	private boolean isDataLoaded = false;
	private ArrayList<Syllabus> syllabusList;
	private String currentTermId = "";
	private LinearLayout scrollLinearLayout;
	CustomTabButton current;
	Batch selectedBatch;
	private LinearLayout pbs;
	private ArrayList<Term> terms;
	private int initialPosition = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		syllabusList = new ArrayList<Syllabus>();
		termMap = new HashMap<String, ArrayList<Term>>();
		adapter = new EfficientAdapter(getActivity());
		userHelper = new UserHelper(this, getActivity());

	}

	private void fetchTerm() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		// app.showLog("Secret before sending", app.getUserSecret());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		if (userHelper.getUser().getType() == UserTypeEnum.STUDENT
				|| userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser()
					.getPaidInfo().getSchoolId());
		}
		if (userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
			params.put(RequestKeyHelper.BATCH_ID, selectedBatch.getId());
		}

		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) {
			params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser()
					.getSelectedChild().getProfileId());
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser()
					.getSelectedChild().getBatchId());
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser()
					.getSelectedChild().getSchoolId());
		}

		AppRestClient.post(URLHelper.URL_SYLLABUS_TERM, params, TermHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		uiHelper = new UIHelper(this.getActivity());
		View view = inflater.inflate(R.layout.fragment_syllabus, container,
				false);
		// RoboGuice.getInjector(getActivity()).injectViewMembers(this);
		initView(view);
		listviewSyllabus.setAdapter(adapter);
		listviewSyllabus.setOnItemClickListener(this);
		// listviewSyllabus.setAdapter(adapter);

		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		listviewSyllabus = (ListView) view.findViewById(R.id.syllabus_listview);
		scrollLinearLayout = (LinearLayout) view
				.findViewById(R.id.syllabusTabLayout);
		pbs = (LinearLayout) view.findViewById(R.id.pb);

		
		
		
	}

	AsyncHttpResponseHandler TermHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			// uListener.onServerAuthenticationFailed(arg1);
			/*
			 * uiHelper.showMessage(arg1); uiHelper.dismissLoadingDialog();
			 */
			pbs.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			super.onStart();
			// uListener.onServerAuthenticationStart();
			// uiHelper.showLoadingDialog("Please wait...");
			pbs.setVisibility(View.VISIBLE);
		}

		// loh265ob1qppsisnnvj8tbk6j1
		// 08e9344b9eb6b0fcc56717c5efa6e2d6e08e9e84ad9403ea45816188c5600f89
		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			// uiHelper.dismissLoadingDialog();
			pbs.setVisibility(View.GONE);
			// Log.e("RESPONSE SYLLABUS ", responseString);
			/*
			 * Toast.makeText(getActivity(), responseString, Toast.LENGTH_LONG)
			 * .show();
			 */
			// uiHelper.showMessage(responseString);
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);
			if (modelContainer.getStatus().getCode() == 200) {
				terms = GsonParser.getInstance().parseTerm(
						modelContainer.getData().getAsJsonArray("terms")
								.toString());
				fetchTerm(terms.get(0).getTermId());
				createTermButtons(terms);
				CustomTabButton btn = (CustomTabButton) scrollLinearLayout
						.getChildAt(0);
				current = btn;
				setTabSelected(btn);
				isDataLoaded = true;
			} else {

			}
			// adapter.notifyDataSetChanged();
			// Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
			// .getAllNotice().get(0).getNoticeTypeText());
		}
	};

	public void onViewCreated(View view, Bundle savedInstanceState) {

		if (isDataLoaded) {
			createTermButtons(terms);
			setTabSelected((CustomTabButton) scrollLinearLayout
					.getChildAt(initialPosition));
		}

	};

	private void createTermButtons(ArrayList<Term> terms) {
		// TODO Auto-generated method stub
		scrollLinearLayout.removeAllViews();
		int i = 0;
		for (Term term : terms) {
			LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			parms.setMargins(3, 0, 0, 0);
			parms.weight = 1;
			CustomTabButton btn = new CustomTabButton(getActivity());
			btn.setClickable(true);
			btn.setGravity(Gravity.CENTER);
			btn.setBackgroundResource(R.drawable.tab_general_btn);
			btn.setTag("" + (i++));
			btn.setTitleText(term.getTermTitle());
			btn.setLayoutParams(parms);
			btn.setImage(R.drawable.term_icon_normal);
			btn.setOnClickListener(this);
			scrollLinearLayout.addView(btn);
			Log.e("TERMID", term.getTermId());
		}
	}

	AsyncHttpResponseHandler singleTermHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			// uListener.onServerAuthenticationFailed(arg1);
			/*
			 * uiHelper.showMessage(arg1); uiHelper.dismissLoadingDialog();
			 */
			pbs.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			super.onStart();
			// uListener.onServerAuthenticationStart();
			// uiHelper.showLoadingDialog("Please wait...");
			pbs.setVisibility(View.VISIBLE);
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			// uiHelper.dismissLoadingDialog();
			pbs.setVisibility(View.GONE);
			Log.e("SINGLE TERM RESPONSE", responseString);
			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);
			if (modelContainer.getStatus().getCode() == 200) {
				syllabusList = GsonParser.getInstance().parseTermSyllabus(
						modelContainer.getData().getAsJsonArray("syllabus")
								.toString());
				adapter.notifyDataSetChanged();
			} else {
				syllabusList.clear();adapter.notifyDataSetChanged();
			}
			// adapter.notifyDataSetChanged();
			// Log.e("GSON NOTICE TYPE TEXT:", modelContainer.getData()
			// .getAllNotice().get(0).getNoticeTypeText());
		}
	};

	private void fetchTerm(String termId) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		// app.showLog("Secret before sending", app.getUserSecret());
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo()
				.getSchoolId());
		params.put(RequestKeyHelper.TERM_ID, termId);

		if (userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
			params.put(RequestKeyHelper.BATCH_ID, selectedBatch.getId());
		}

		AppRestClient.post(URLHelper.URL_SYLLABUS, params, singleTermHandler);
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
			return syllabusList.size();
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
			return syllabusList.get(position);
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
			Syllabus item = syllabusList.get(position);

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_syllabus_list,
						null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.txt_syllabus_header);
				/*
				 * holder.tvContent = (ExpandableTextView) convertView
				 * .findViewById(R.id.txt_syllabus_content);
				 */
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.txt_syllabus_date);
				holder.subjectIcon = (ImageView) convertView
						.findViewById(R.id.iv_syllabus_subject_icon);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvTitle.setText(item.getSubject_name());
			holder.subjectIcon.setImageResource(AppUtility.getImageResourceId(
					item.getSubject_icon(), getActivity()));
			/*
			 * holder.tvContent.setText(" " +
			 * Html.fromHtml(item.getSyllabus_text(), null, new
			 * MyTagHandler()));
			 */
			holder.tvDate.setText(item.getLast_updated());
			return convertView;
		}

		class ViewHolder {
			TextView tvTitle;
			// ExpandableTextView tvContent;
			TextView tvDate;
			ImageView subjectIcon;
		}
	}

	@SuppressWarnings("unchecked")
	public void setTabSelected(View v) {
		CustomTabButton btn = (CustomTabButton) v;

		current.setButtonSelected(false,
				getResources().getColor(R.color.black),
				R.drawable.term_icon_normal);

		btn.setButtonSelected(true, getResources().getColor(R.color.black),
				R.drawable.term_icon_tap);
		current = btn;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CustomTabButton btn = (CustomTabButton) v;
		int pos = Integer.parseInt(v.getTag().toString());
		Term t = terms.get(pos);
		fetchTerm(t.getTermId());
		setTabSelected(btn);
		initialPosition = pos;
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
	public void onItemClick(AdapterView<?> arg0, View child, int position,
			long arg3) {
		/*String htmlContent = syllabusList.get(position).getSyllabus_text();
		Intent webIntent = new Intent(getActivity(), SyllabusActivity.class);
		webIntent.putExtra("html_data", htmlContent);
		startActivity(webIntent);*/
		Syllabus data = (Syllabus)adapter.getItem(position);
		
		Intent intent = new Intent(getActivity(), SingleSyllabus.class);
		intent.putExtra(AppConstant.ID_SINGLE_SYLLABUS, data.getId());
		startActivity(intent);
	}

	@Override
	protected void onVisible() {
		if (!isDataLoaded) {
			if (userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
				if (PaidVersionHomeFragment.isBatchLoaded) {
					showPicker(PickerType.TEACHER_BATCH);
				}
			} else
				fetchTerm();

		}
	}

	public void showPicker(PickerType type) {

		Picker picker = Picker.newInstance(0);
		picker.setData(type, PaidVersionHomeFragment.batches, PickerCallback,
				"Select Batch");
		picker.show(getChildFragmentManager(), null);
	}

	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case TEACHER_BATCH:
				selectedBatch = (Batch) item;
				fetchTerm();
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onInvisible() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPaswordChanged() {
		// TODO Auto-generated method stub
		
	}
}
