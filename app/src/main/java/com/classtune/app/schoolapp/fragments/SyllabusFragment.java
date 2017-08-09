package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.PaidVersionHomeFragment;
import com.classtune.app.freeversion.SyllabusMidLayerActivity;
import com.classtune.app.schoolapp.model.BaseType;
import com.classtune.app.schoolapp.model.Batch;
import com.classtune.app.schoolapp.model.Picker;
import com.classtune.app.schoolapp.model.Picker.PickerItemSelectedListener;
import com.classtune.app.schoolapp.model.PickerType;
import com.classtune.app.schoolapp.model.Term;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.CustomTabButtonEllipsizeText;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyllabusFragment extends Fragment {
	
	private View view;
	private UIHelper uiHelper;
	private UserHelper userHelper;
	
	private CustomTabButtonEllipsizeText btnTermExam, btnClassTest, btnProject, btnYearly;
	
	private CustomTabButtonEllipsizeText currentButton;
	
	private List<CustomTabButtonEllipsizeText> listBtn;
	
	private HorizontalScrollView horizontalScrollView;
	
	private List<Term> listTerm;
	
	
	private ListView listViewTerms;
	private TermsAdapter adapter;
	
	private String selectedBatchId = "";
	private TextView selectedBatchTextView;
	private ImageButton tapLayout;
	private static final int REQ_YEARLY_ACTIVIVTY = 101;


	@Override
	public void onResume() {
		super.onResume();
		if(AppUtility.isInternetConnected() == false){
			Toast.makeText(getActivity(), R.string.internet_error_text, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		listTerm = new ArrayList<Term>();
		
		uiHelper = new UIHelper(getActivity());
		userHelper = new UserHelper(getActivity());
		
		listBtn = new ArrayList<CustomTabButtonEllipsizeText>();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if(userHelper.getUser().getType() == UserTypeEnum.TEACHER)
		{
			if(!PaidVersionHomeFragment.isBatchLoaded){
				HashMap<String,String> params=new HashMap<>();
				params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
				//AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
				getBatch(params);
			}else {
				if(PaidVersionHomeFragment.selectedBatch!=null){
					selectedBatchId = PaidVersionHomeFragment.selectedBatch.getId();
					initApiCall("3");
				}else {
					showBatchPicker(PickerType.TEACHER_BATCH);
				}
			}
			
		
		}else initApiCall("3");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_syllabus_new,
				container, false);
		
		initView(view);
		
		return view;
	}

	private int getWindowWidth()
	{
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		final int width = size.x;

		return width;
	}
	private void initView(View view)
	{
		
		listViewTerms = (ListView)view.findViewById(R.id.listViewTerms);
		adapter = new TermsAdapter();
		listViewTerms.setAdapter(adapter);
		
		
		horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollView);
		tapLayout = (ImageButton)view.findViewById(R.id.select_batch_btn);
		tapLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!PaidVersionHomeFragment.isBatchLoaded){
					HashMap<String,String> params=new HashMap<String, String>();
					params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
					//AppRestClient.post(URLHelper.URL_GET_TEACHER_BATCH, params, getBatchEventsHandler);
					getBatch(params);
				}else {
						showBatchPicker(PickerType.TEACHER_BATCH);
				}
			}
		});
		
		btnTermExam = (CustomTabButtonEllipsizeText)view.findViewById(R.id.btnTermExam);
		btnClassTest = (CustomTabButtonEllipsizeText)view.findViewById(R.id.btnClassTest);
		btnProject = (CustomTabButtonEllipsizeText)view.findViewById(R.id.btnProject);
		btnYearly = (CustomTabButtonEllipsizeText)view.findViewById(R.id.btnYearly);
		selectedBatchTextView = (TextView)view.findViewById(R.id.txtBatchName);
		if(PaidVersionHomeFragment.selectedBatch!=null){
			selectedBatchTextView.setText(PaidVersionHomeFragment.selectedBatch.getName());
		}
		if(userHelper.getUser().getType()!=UserTypeEnum.TEACHER){
			tapLayout.setVisibility(View.GONE);
			selectedBatchTextView.setVisibility(View.GONE);
		}
		listBtn.add(btnTermExam);
		listBtn.add(btnClassTest);
		listBtn.add(btnProject);
		listBtn.add(btnYearly);

		currentButton = btnTermExam;
		btnTermExam.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.syllabus_tap);
		
		//initApiCall("3");

		btnTermExam.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				//int scrollX = (v.getLeft() - (getWindowWidth() / 2)) + (v.getWidth() / 2);
				//horizontalScrollView.smoothScrollTo(scrollX, 0);

				currentButton = btnTermExam;
				btnTermExam.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.syllabus_tap);

				updateButtonUi();

				initApiCall("3");


			}
		});

		btnYearly.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				//int scrollX = (v.getLeft() - (getWindowWidth() / 2)) + (v.getWidth() / 2);
				//horizontalScrollView.smoothScrollTo(scrollX, 0);

				horizontalScrollView.postDelayed(new Runnable() {
					public void run() {
						horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
					}
				}, 100L);

				currentButton = btnYearly;
				btnYearly.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.yearly_tap);

				updateButtonUi();

				Intent intent = new Intent(getActivity(), SyllabusMidLayerActivity.class);
				intent.putExtra(AppConstant.ID_TERM, "");
				if(userHelper.getUser().getType() == UserTypeEnum.TEACHER)
				{
					intent.putExtra(AppConstant.ID_BATCH, selectedBatchId);
				}
				startActivityForResult(intent, REQ_YEARLY_ACTIVIVTY);
			}
		});
		


		btnClassTest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				//int scrollX = (v.getLeft() - (getWindowWidth() / 2)) + (v.getWidth() / 2);
			    //horizontalScrollView.smoothScrollTo(scrollX, 0);

				horizontalScrollView.postDelayed(new Runnable() {
					public void run() {
						horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
					}
				}, 100L);

				currentButton = btnClassTest;
				btnClassTest.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.syllabus_tap);

				updateButtonUi();

				initApiCall("1");
			}
		});


		btnProject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				//int scrollX = (v.getLeft() - (getWindowWidth() / 2)) + (v.getWidth() / 2);
				//horizontalScrollView.smoothScrollTo(scrollX, 0);


				currentButton = btnProject;
				btnProject.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.syllabus_tap);

				updateButtonUi();

				initApiCall("2");
			}
		});


		//btnTermExam.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.term_icon_tap);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == REQ_YEARLY_ACTIVIVTY){
			currentButton = btnTermExam;
			btnTermExam.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.syllabus_tap);
			btnYearly.setButtonSelected(false, getResources().getColor(R.color.black), R.drawable.yearly_normal);
			initApiCall("3");
		}
	}

	private void getBatch(HashMap<String, String> params){

		if(!uiHelper.isDialogActive())
			uiHelper.showLoadingDialog(getString(R.string.loading_text));
		else
			uiHelper.updateLoadingDialog(getString(R.string.loading_text));

		ApplicationSingleton.getInstance().getNetworkCallInterface().getBatch(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();
						Log.e("Response", "" +response.body());
						Wrapper wrapper=GsonParser.getInstance().parseServerResponse2(response.body());
						if(wrapper.getStatus().getCode()==AppConstant.RESPONSE_CODE_SUCCESS)
						{
							PaidVersionHomeFragment.isBatchLoaded=true;
							PaidVersionHomeFragment.batches.clear();
							String data=wrapper.getData().get("batches").toString();
							PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
							//showPicker(PickerType.TEACHER_BATCH);
							showBatchPicker(PickerType.TEACHER_BATCH);
						}

					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.dismissLoadingDialog();
					}
				}
		);

	}
	AsyncHttpResponseHandler getBatchEventsHandler=new AsyncHttpResponseHandler()
	{

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
				PaidVersionHomeFragment.isBatchLoaded=true;
				PaidVersionHomeFragment.batches.clear();
				String data=wrapper.getData().get("batches").toString();
				PaidVersionHomeFragment.batches.addAll(GsonParser.getInstance().parseBatchList(data));
				//showPicker(PickerType.TEACHER_BATCH);
				showBatchPicker(PickerType.TEACHER_BATCH);
			}

		}

	};


	private void updateButtonUi()
	{
		for(CustomTabButtonEllipsizeText btn : listBtn)
		{
			if(!btn.equals(currentButton))
			{

				if(btn == btnYearly)
				{
					btn.setButtonSelected(false, getResources().getColor(R.color.black), R.drawable.yearly_normal);
				}
				else
				{
					btn.setButtonSelected(false, getResources().getColor(R.color.black), R.drawable.syllabus_normal);
				}

			}
		}
	}


	private void initApiCall(String catId)
	{
		listTerm.clear();

		HashMap<String,String> params = new HashMap<>();


		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS)
		{
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo().getSchoolId());
			params.put(RequestKeyHelper.CATEGORY_ID, catId);
		}

		if(userHelper.getUser().getType() == UserTypeEnum.STUDENT)
		{

			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getPaidInfo().getBatchId());
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo().getSchoolId());
			params.put(RequestKeyHelper.CATEGORY_ID, catId);


		}

		if(userHelper.getUser().getType() == UserTypeEnum.TEACHER)
		{
			params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
			params.put(RequestKeyHelper.BATCH_ID, selectedBatchId);
			params.put(RequestKeyHelper.SCHOOL, userHelper.getUser().getPaidInfo().getSchoolId());
			params.put(RequestKeyHelper.CATEGORY_ID, catId);
		}

		//AppRestClient.post(URLHelper.URL_SYLLABUS_TERM, params, termHandler);
		syllabusTerm(params);
	}



	private void showBatchPicker(PickerType type) {

		Picker picker = Picker.newInstance(0);
		picker.setData(type, PaidVersionHomeFragment.batches, PickerCallback , getString(R.string.fragment_lessonplan_view_txt_select_batch));
		picker.show(getChildFragmentManager(), null);
	}

	PickerItemSelectedListener PickerCallback = new PickerItemSelectedListener() {

		@Override
		public void onPickerItemSelected(BaseType item) {

			switch (item.getType()) {
			case TEACHER_BATCH:
				Batch selectedBatch=(Batch)item;
				PaidVersionHomeFragment.selectedBatch=selectedBatch;
				Intent i = new Intent("com.classtune.app.schoolapp.batch");
                i.putExtra("batch_id", selectedBatch.getId());
                getActivity().sendBroadcast(i);


				selectedBatchId = selectedBatch.getId();
				selectedBatchTextView.setText(selectedBatch.getName());
				initApiCall("3");
				currentButton = btnTermExam;
				btnTermExam.setButtonSelected(true, getResources().getColor(R.color.black), R.drawable.syllabus_tap);
				updateButtonUi();
				break;
			default:
				break;
			}

		}
	};


	private void syllabusTerm(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().syllabusTerm(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();
						Wrapper modelContainer = GsonParser.getInstance()
								.parseServerResponse2(response.body());

						if (modelContainer.getStatus().getCode() == 200) {

							listTerm = GsonParser.getInstance().parseTerm(modelContainer.getData().getAsJsonArray("terms").toString());

							adapter.notifyDataSetChanged();


							if(listTerm.size() <= 0)
							{
								Toast.makeText(getActivity(), getString(R.string.fragment_archieved_events_txt_no_data_found), Toast.LENGTH_SHORT).show();
							}
						}

						else {

						}
					}

					@Override
					public void onFailure(Call<JsonElement> call, Throwable t) {
						uiHelper.showMessage(getString(R.string.internet_error_text));
						if (uiHelper.isDialogActive()) {
							uiHelper.dismissLoadingDialog();
						}
					}
				}
		);
	}
	AsyncHttpResponseHandler termHandler = new AsyncHttpResponseHandler() {

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

				listTerm = GsonParser.getInstance().parseTerm(modelContainer.getData().getAsJsonArray("terms").toString());

				adapter.notifyDataSetChanged();


				if(listTerm.size() <= 0)
				{
					Toast.makeText(getActivity(), getString(R.string.fragment_archieved_events_txt_no_data_found), Toast.LENGTH_SHORT).show();
				}
			}

			else {

			}



		};
	};


	private class TermsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listTerm.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listTerm.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
            if (convertView == null)
            {
            	holder = new ViewHolder();
            	convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_term_syllabus,null,false);

                holder.layoutRoot = (LinearLayout)convertView.findViewById(R.id.layoutRoot);
            	holder.txtDate = (TextView)convertView.findViewById(R.id.txtDate);
            	holder.txtExamName = (TextView)convertView.findViewById(R.id.txtExamName);
            	holder.layoutAction = (LinearLayout)convertView.findViewById(R.id.layoutAction);
            	holder.txtAction = (TextView)convertView.findViewById(R.id.txtAction);


            	convertView.setTag(holder);
            }

            else
            {
            	holder = (ViewHolder)convertView.getTag();
            }


            if(position%2 != 0)
                holder.layoutRoot.setBackgroundColor(getResources().getColor(R.color.bg_row_odd));
            else
                holder.layoutRoot.setBackgroundColor(Color.WHITE);




            holder.layoutAction.setTag(listTerm.get(position).getTermId());
			holder.layoutRoot.setTag(listTerm.get(position).getTermId());

            holder.txtExamName.setText(listTerm.get(position).getTermTitle());
            holder.txtAction.setText(getString(R.string.java_classreportteacherfragment_view));
            holder.txtDate.setText(listTerm.get(position).getExam_date());

            holder.layoutAction.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String termId = (String) ((LinearLayout) v).getTag();
					Intent intent = new Intent(getActivity(), SyllabusMidLayerActivity.class);
					intent.putExtra(AppConstant.ID_TERM, termId);
					if (userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
						intent.putExtra(AppConstant.ID_BATCH, selectedBatchId);
					}
					startActivity(intent);

				}
			});

			holder.layoutRoot.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					String termId = (String) ((LinearLayout) view).getTag();
					Intent intent = new Intent(getActivity(), SyllabusMidLayerActivity.class);
					intent.putExtra(AppConstant.ID_TERM, termId);
					if (userHelper.getUser().getType() == UserTypeEnum.TEACHER) {
						intent.putExtra(AppConstant.ID_BATCH, selectedBatchId);
					}
					startActivity(intent);
				}
			});
			
			return convertView;
		}
		
	}
	
	class ViewHolder{

        LinearLayout layoutRoot;
		TextView txtDate;
		TextView txtExamName;
		LinearLayout layoutAction;
		TextView txtAction;
        
    }

	
}
