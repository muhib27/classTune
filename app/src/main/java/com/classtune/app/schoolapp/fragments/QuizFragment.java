package com.classtune.app.schoolapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.AssesmentHomeworkActivity;
import com.classtune.app.schoolapp.GcmIntentService;
import com.classtune.app.schoolapp.model.AssessmentHomework;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogHomeworkAssessmentResult;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuizFragment extends Fragment {
	
	private View view;
	private UIHelper uiHelper;
	private UserHelper userHelper;
	
	
	private boolean hasNextAssessment = false;
	private int pageNumber = 1;
	private boolean isRefreshing = false;
	private boolean loading = false;
	private boolean stopLoadingData = false;
	
	private List<AssessmentHomework> listAssessmentHomework;
	private PullToRefreshListView listViewAssessment;
	
	
	private AssessmentAdapter assessmentAdapter;
	
	private TextView txtNoData;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		uiHelper = new UIHelper(getActivity());
		userHelper = new UserHelper(getActivity());
		
		listAssessmentHomework = new ArrayList<AssessmentHomework>();
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_quiz, container, false);
		
		initView(view);
		initApiCallAssessment(pageNumber);
		
		return view;
		
		
		
	}
	
	
	
	private void initView(View view)
	{
		listViewAssessment = (PullToRefreshListView)view.findViewById(R.id.listViewAssessment);
		txtNoData = (TextView)view.findViewById(R.id.txtNoData);
		
		setUpList();
	}
	
	
	private void initializePageing() {
		pageNumber = 1;
		isRefreshing = false;
		loading = false;
		stopLoadingData = false;
	}
	
	private void setUpList() {

		initializePageing();
		listViewAssessment.setMode(Mode.PULL_FROM_END);
		// Set a listener to be invoked when the list should be refreshed.
		listViewAssessment.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				Mode m = listViewAssessment.getCurrentMode();
				if (m == Mode.PULL_FROM_START) {
					stopLoadingData = false;
					isRefreshing = true;
					pageNumber = 1;
					loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
					initApiCallAssessment(pageNumber);
				} else if (!stopLoadingData) {
					pageNumber++;
					loading = true;
					/*processFetchPost(URLHelper.URL_FREE_VERSION_CATEGORY,
							currentCategoryId);*/
					initApiCallAssessment(pageNumber);
					
				} else {
					new NoDataTaskAssessment().execute();
				}
			}
		});

		
		
		this.assessmentAdapter = new AssessmentAdapter();
		this.assessmentAdapter.clearList();
		this.listViewAssessment.setAdapter(assessmentAdapter);
	}
	
	private class NoDataTaskAssessment extends AsyncTask<Void, Void, String[]> {

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

			assessmentAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			listViewAssessment.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	
	private void initApiCallAssessment(int pageNumber)
	{
		HashMap<String,String> params = new HashMap<>();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
		{
            if(getActivity().getIntent().getExtras()!=null)
            {
                if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                    params.put(RequestKeyHelper.STUDENT_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                    params.put(RequestKeyHelper.BATCH_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


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


			//params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
			//params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
		}
		
		
		params.put("page_size", "10");
		params.put("page_number", String.valueOf(pageNumber));
		params.put("not_started", "1");
		

		//AppRestClient.post(URLHelper.URL_HOMEWORK_ASSESSMENT_LIST, params, assessmentHomeworkHandler);
		homeworkAccessmentList(params);
	}
	private void homeworkAccessmentList(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().homeworkAccessmentList(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();

						if (response.body() != null){
							Wrapper modelContainer = GsonParser.getInstance()
									.parseServerResponse2(response.body());

							hasNextAssessment = modelContainer.getData().get("has_next").getAsBoolean();


							if (pageNumber == 1)
							{
								assessmentAdapter.clearList();
							}

							if (!hasNextAssessment)
							{
								stopLoadingData = true;
							}



							if (modelContainer.getStatus().getCode() == 200) {
								JsonArray arraHomework = modelContainer.getData().get("homework").getAsJsonArray();

								//listAssessmentHomework = parseAssessmentList(arraHomework.toString());


								for (int i = 0; i < parseAssessmentList(arraHomework.toString())
										.size(); i++) {
									listAssessmentHomework.add(parseAssessmentList(arraHomework.toString()).get(i));
								}


								if (pageNumber != 0 || isRefreshing)
								{
									listViewAssessment.onRefreshComplete();
									loading = false;
								}

								assessmentAdapter.notifyDataSetChanged();

							}

							if(listAssessmentHomework.size() <= 0)
							{
								txtNoData.setVisibility(View.VISIBLE);
								//listViewAssessment.setVisibility(View.GONE);
							}
							else
							{
								txtNoData.setVisibility(View.GONE);
								//listViewAssessment.setVisibility(View.VISIBLE);
							}
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
	private AsyncHttpResponseHandler assessmentHomeworkHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			uiHelper.dismissLoadingDialog();
		};

		@Override
		public void onStart() {
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		};

		@Override
		public void onSuccess(String responseString) {
			

			uiHelper.dismissLoadingDialog();

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);
			
			hasNextAssessment = modelContainer.getData().get("has_next").getAsBoolean();
			
			
			if (pageNumber == 1)
			{
				assessmentAdapter.clearList();
			}
				
			if (!hasNextAssessment) 
			{
				stopLoadingData = true;
			}
			
			

			if (modelContainer.getStatus().getCode() == 200) {
				JsonArray arraHomework = modelContainer.getData().get("homework").getAsJsonArray();
				
				//listAssessmentHomework = parseAssessmentList(arraHomework.toString());
				
				
				for (int i = 0; i < parseAssessmentList(arraHomework.toString())
						.size(); i++) {
					listAssessmentHomework.add(parseAssessmentList(arraHomework.toString()).get(i));
				}
				
				
				if (pageNumber != 0 || isRefreshing) 
				{
					listViewAssessment.onRefreshComplete();
					loading = false;
				}
				
				assessmentAdapter.notifyDataSetChanged();
				
			}
			
			else {

			}
			
			
			if(listAssessmentHomework.size() <= 0)
			{
				txtNoData.setVisibility(View.VISIBLE);
				//listViewAssessment.setVisibility(View.GONE);
			}
			else
			{
				txtNoData.setVisibility(View.GONE);
				//listViewAssessment.setVisibility(View.VISIBLE);
			}

		};

	};
	
	
	private void initApiCallAssessmentResult(String id)
	{
		HashMap<String,String> params = new HashMap<>();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		
		params.put("id", id);
		
		if (userHelper.getUser().getType() == UserTypeEnum.PARENTS) 
		{
            if(getActivity().getIntent().getExtras()!=null)
            {
                if(getActivity().getIntent().getExtras().containsKey("total_unread_extras"))
                {
                    String rid = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rid");
                    String rtype = getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("rtype");

                    params.put(RequestKeyHelper.STUDENT_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("student_id"));
                    params.put(RequestKeyHelper.BATCH_ID, getActivity().getIntent().getExtras().getBundle("total_unread_extras").getString("batch_id"));


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


			//params.put(RequestKeyHelper.STUDENT_ID, userHelper.getUser().getSelectedChild().getProfileId());
			//params.put(RequestKeyHelper.BATCH_ID, userHelper.getUser().getSelectedChild().getBatchId());
		}
		

		//AppRestClient.post(URLHelper.URL_HOMEWORK_ASSESSMENT_RESULT, params, assessmentHomeworkResultHandler);
		homeworkAccessmentResult(params);
	}
	
	private void homeworkAccessmentResult(HashMap<String,String> params){
		uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		ApplicationSingleton.getInstance().getNetworkCallInterface().homeworkAccessmentResult(params).enqueue(
				new Callback<JsonElement>() {
					@Override
					public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
						uiHelper.dismissLoadingDialog();

						if (response.body() != null){
							Wrapper modelContainer = GsonParser.getInstance()
									.parseServerResponse2(response.body());


							if (modelContainer.getStatus().getCode() == 200) {

								JsonObject obj = modelContainer.getData().get("assesment").getAsJsonObject();

								String nameText = obj.get("name").getAsString();
								String subjectText = obj.get("subject_name").getAsString();
								String totalStudent = obj.get("total_student").getAsString();
								String totaltotalParticipated = obj.get("total_participated").getAsString();
								String maxScore = obj.get("max_score").getAsString();
								String minScore = obj.get("min_score").getAsString();
								String totalTimeTaken = obj.get("total_time_taken").getAsString();

								String totalMarkText = obj.get("total_mark").getAsString();
								String isPassedText = obj.get("is_passed").getAsString();
								String totalScoreText = obj.get("total_score").getAsString();


								String studentCountText = totaltotalParticipated+"/"+totalStudent;


								showCustomDialogHomeworkAssessmentOk(getString(R.string.fragment_quiz_header), nameText, subjectText, studentCountText, maxScore, minScore, totalMarkText, totalTimeTaken, isPassedText, totalScoreText, R.drawable.assessment_icon_popup, getActivity());



							}
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
	private AsyncHttpResponseHandler assessmentHomeworkResultHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			uiHelper.showMessage(getString(R.string.internet_error_text));
			uiHelper.dismissLoadingDialog();
		};

		@Override
		public void onStart() {
			uiHelper.showLoadingDialog(getString(R.string.java_accountsettingsactivity_please_wait));
		};

		@Override
		public void onSuccess(String responseString) {
			

			uiHelper.dismissLoadingDialog();

			Wrapper modelContainer = GsonParser.getInstance()
					.parseServerResponse(responseString);
			

			if (modelContainer.getStatus().getCode() == 200) {
				
				JsonObject obj = modelContainer.getData().get("assesment").getAsJsonObject();
				
				String nameText = obj.get("name").getAsString();
				String subjectText = obj.get("subject_name").getAsString();
				String totalStudent = obj.get("total_student").getAsString();
				String totaltotalParticipated = obj.get("total_participated").getAsString();
				String maxScore = obj.get("max_score").getAsString();
				String minScore = obj.get("min_score").getAsString();
				String totalTimeTaken = obj.get("total_time_taken").getAsString();
				
				String totalMarkText = obj.get("total_mark").getAsString();
				String isPassedText = obj.get("is_passed").getAsString();
				String totalScoreText = obj.get("total_score").getAsString();
				
				
				String studentCountText = totaltotalParticipated+"/"+totalStudent;
				
				
				showCustomDialogHomeworkAssessmentOk(getString(R.string.fragment_quiz_header), nameText, subjectText, studentCountText, maxScore, minScore, totalMarkText, totalTimeTaken, isPassedText, totalScoreText, R.drawable.assessment_icon_popup, getActivity());
				
				
				
			}
			
			else {

			}
			
			

		};

	};


	public List<AssessmentHomework> parseAssessmentList(String object) {

		List<AssessmentHomework> tags = new ArrayList<AssessmentHomework>();
		Type listType = new TypeToken<List<AssessmentHomework>>() {
		}.getType();
		tags = (List<AssessmentHomework>) new Gson().fromJson(object, listType);
		return tags;
	}
	
	private class AssessmentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAssessmentHomework.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listAssessmentHomework.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public void clearList() {
			listAssessmentHomework.clear();
		}
		
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolderAssessment holder;
			if (convertView == null) {
				holder = new ViewHolderAssessment();

				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_assessment_homework, parent, false);

				holder.layoutRoot = (LinearLayout)convertView.findViewById(R.id.layoutRoot);

				holder.txtPosition = (TextView)convertView.findViewById(R.id.txtPosition);
				holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
				holder.txtStartDate = (TextView)convertView.findViewById(R.id.txtStartDate);
				holder.txtEndDate = (TextView)convertView.findViewById(R.id.txtEndDate);
				holder.txtMaximumTime = (TextView)convertView.findViewById(R.id.txtMaximumTime);
				holder.txtPassPercentage = (TextView)convertView.findViewById(R.id.txtPassPercentage);
				holder.btnPlay = (Button)convertView.findViewById(R.id.btnPlay);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolderAssessment)convertView.getTag();
			}
			
			holder.txtPosition.setText(String.valueOf(position + 1)+". ");
			
			holder.txtName.setText(listAssessmentHomework.get(position).getName());
			holder.txtStartDate.setText(getString(R.string.java_homeworkfragment_start_date)+listAssessmentHomework.get(position).getStartDate());
			holder.txtEndDate.setText(getString(R.string.java_homeworkfragment_due_date)+listAssessmentHomework.get(position).getEndDate());
			holder.txtMaximumTime.setText(getString(R.string.java_homeworkfragment_max_time)+listAssessmentHomework.get(position).getMaximumTime());
			holder.txtPassPercentage.setText(getString(R.string.java_homeworkfragment_pass_percentage)+listAssessmentHomework.get(position).getPassPercentage());
			holder.btnPlay.setTag(position);
			holder.layoutRoot.setTag(position);

			listAssessmentHomework.get(position).setIsClickable(true);
			holder.btnPlay.setEnabled(listAssessmentHomework.get(position).isClickable());
			holder.layoutRoot.setEnabled(listAssessmentHomework.get(position).isClickable());

			if(listAssessmentHomework.get(position).getNotStarted() == 1)
			{
				holder.btnPlay.setText(R.string.java_quizfragment_yet_to_start);

				listAssessmentHomework.get(position).setIsClickable(false);
				holder.btnPlay.setEnabled(listAssessmentHomework.get(position).isClickable());
				holder.layoutRoot.setEnabled(listAssessmentHomework.get(position).isClickable());
			}
			else
			{
				if(listAssessmentHomework.get(position).getTimeover() == 0 && listAssessmentHomework.get(position).getExamGiven() == 0)
				{
					holder.btnPlay.setText(getString(R.string.java_homeworkfragment_play));

					if (userHelper.getUser().getType() == UserTypeEnum.PARENTS)
					{
						holder.btnPlay.setText(R.string.java_quizfragment_not_played);
					}

					holder.btnPlay.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Button btn = ((Button)v);

							if (userHelper.getUser().getType() != UserTypeEnum.PARENTS)
							{
								listAssessmentHomework.get((Integer) btn.getTag()).setIsClickable(true);
								btn.setEnabled(listAssessmentHomework.get((Integer) btn.getTag()).isClickable());

								Intent intent = new Intent(getActivity(), AssesmentHomeworkActivity.class);
								intent.putExtra("ASSESSMENT_HOMEWORK_ID", listAssessmentHomework.get((Integer) btn.getTag()).getId());
								startActivity(intent);


							}


						}
					});


					holder.layoutRoot.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							LinearLayout btn = ((LinearLayout) v);

							if (userHelper.getUser().getType() != UserTypeEnum.PARENTS) {
								listAssessmentHomework.get((Integer) btn.getTag()).setIsClickable(true);
								btn.setEnabled(listAssessmentHomework.get((Integer) btn.getTag()).isClickable());

								Intent intent = new Intent(getActivity(), AssesmentHomeworkActivity.class);
								intent.putExtra("ASSESSMENT_HOMEWORK_ID", listAssessmentHomework.get((Integer) btn.getTag()).getId());
								startActivity(intent);


							}


						}
					});

				}

				if(listAssessmentHomework.get(position).getExamGiven() == 1) {
					holder.btnPlay.setText(R.string.java_quizfragment_result);

					holder.btnPlay.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.e("CCC", "clicked from result");
							Button btn = ((Button) v);

							listAssessmentHomework.get((Integer) btn.getTag()).setIsClickable(true);
							btn.setEnabled(listAssessmentHomework.get((Integer) btn.getTag()).isClickable());

							initApiCallAssessmentResult(listAssessmentHomework.get((Integer) btn.getTag()).getId());


						}
					});


					holder.layoutRoot.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.e("CCC", "clicked from result");
							LinearLayout btn = ((LinearLayout) v);

							listAssessmentHomework.get((Integer) btn.getTag()).setIsClickable(true);
							btn.setEnabled(listAssessmentHomework.get((Integer) btn.getTag()).isClickable());

							initApiCallAssessmentResult(listAssessmentHomework.get((Integer) btn.getTag()).getId());


						}
					});
				}
				if(listAssessmentHomework.get(position).getTimeover() == 1 && listAssessmentHomework.get(position).getExamGiven() == 0)
				{
					holder.btnPlay.setText(R.string.java_quizfragment_time_over);

					listAssessmentHomework.get(position).setIsClickable(false);
					holder.btnPlay.setEnabled(listAssessmentHomework.get(position).isClickable());
					holder.layoutRoot.setEnabled(listAssessmentHomework.get(position).isClickable());
				}

			}


			
			return convertView;
		}
		
		class ViewHolderAssessment {

			LinearLayout layoutRoot;

			TextView txtPosition;
			TextView txtName;
			TextView txtStartDate;
			TextView txtEndDate;
			TextView txtMaximumTime;
			TextView txtPassPercentage;
			Button btnPlay;
		}
	}
	
	
	private void showCustomDialogHomeworkAssessmentOk(String headerText, String nameText, String subjectNameText, String studentCountText, String maxScoreText, String minScoreText, String totalMarkText, String totalTimeTakenText, String isPassedText, String totalScoreText, int iconResId,
			Context context) {

			PopupDialogHomeworkAssessmentResult picker = PopupDialogHomeworkAssessmentResult.newInstance(0);
			//picker.setData(headerText, nameText, totalMarkText, isPassedText, totalScoreText, iconResId, context, new PopupDialogHomeworkAssessmentResult.IOkButtonClick(){

			picker.setData(headerText, nameText, subjectNameText, studentCountText, maxScoreText, minScoreText, totalMarkText, totalTimeTakenText, isPassedText, totalScoreText, iconResId, context, new PopupDialogHomeworkAssessmentResult.IOkButtonClick(){
			
				@Override
				public void onOkButtonClick() {
					// TODO Auto-generated method stub
					
				}});
			
			
			
			picker.show(getActivity().getSupportFragmentManager(), null);
	}

}
