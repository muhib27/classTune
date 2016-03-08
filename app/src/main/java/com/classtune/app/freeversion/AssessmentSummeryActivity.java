package com.classtune.app.freeversion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.freeversion.AssesmentActivity.PopulateSummeryData;
import com.classtune.app.schoolapp.viewhelpers.PopupDialogAssessmentSummery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AssessmentSummeryActivity extends ChildContainerActivity {
	
	private List<PopulateSummeryData> listAssessmentQuestionSummery = new ArrayList<PopulateSummeryData>();
	
	private ListView listSummery;
	private AssessmentSummeryAdapter adapter;
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_assesment_summery);
		
		
		listAssessmentQuestionSummery.clear();
		
		String strObj = getIntent().getStringExtra("assessment_summery");
		
		
		Type listType = new TypeToken<List<PopulateSummeryData>>() {
		}.getType();
		listAssessmentQuestionSummery = (List<PopulateSummeryData>) new Gson().fromJson(strObj, listType);
		
		Log.e("DATS_SUMMERY", "is: " + listAssessmentQuestionSummery.get(0).getAnswer());
		
		initView();
		initAction();
		
	}
	
	
	private void initView()
	{
		this.listSummery = (ListView)this.findViewById(R.id.listSummery);
	}
	
	private void initAction()
	{
		this.adapter = new AssessmentSummeryAdapter();
		this.listSummery.setAdapter(this.adapter);
		this.adapter.notifyDataSetChanged();
		
		if(listAssessmentQuestionSummery.size() <= 0)
		{
			Toast.makeText(AssessmentSummeryActivity.this, R.string.java_assessmentsummeryactivity_no_summery_available, Toast.LENGTH_SHORT).show();
		}
	}
	
	
	//R.drawable.assessment_icon_popup
	private void showCustomDialogScore(String headerText, String questionText, String answerText, String explanationText, int imgResId) {

			
			PopupDialogAssessmentSummery picker = PopupDialogAssessmentSummery.newInstance(0);
			picker.setData(headerText, questionText, answerText, explanationText,imgResId, AssessmentSummeryActivity.this);
			picker.show(getSupportFragmentManager(), null);
	}
	
	
	
	public class AssessmentSummeryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAssessmentQuestionSummery.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listAssessmentQuestionSummery.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void clearList() {
			listAssessmentQuestionSummery.clear();
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = LayoutInflater.from(AssessmentSummeryActivity.this).inflate(R.layout.row_assessment_summery, parent, false);

				holder.txtPosition = (TextView)convertView.findViewById(R.id.txtPosition);
				holder.txtQuestion = (TextView)convertView.findViewById(R.id.txtQuestion);
				holder.imgStatus = (ImageView)convertView.findViewById(R.id.imgStatus);
				holder.layoutExplanation = (LinearLayout)convertView.findViewById(R.id.layoutExplanation);
				
				
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.txtPosition.setText(String.valueOf(position + 1)+". ");
			holder.txtQuestion.setText(listAssessmentQuestionSummery.get(position).getQuestion());
			holder.layoutExplanation.setTag(position);
			
			if(listAssessmentQuestionSummery.get(position).isRightAnswer())
				holder.imgStatus.setImageResource(R.drawable.assessment_tick);
			else
				holder.imgStatus.setImageResource(R.drawable.assessment_cross);
			
			
			
			
			holder.layoutExplanation.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// show answer and explanation popup here
					
					showCustomDialogScore(getString(R.string.java_assessmentsummeryactivity_summery_title), listAssessmentQuestionSummery.get((Integer) ((LinearLayout)v).getTag()).getQuestion(), listAssessmentQuestionSummery.get((Integer) ((LinearLayout)v).getTag()).getAnswer(), listAssessmentQuestionSummery.get((Integer) ((LinearLayout)v).getTag()).getExplanation(),
							R.drawable.assessment_icon_popup);
				}
			});
			
			
			
			return convertView;
		}

	}

	
	class ViewHolder {
		
		TextView txtPosition;
		TextView txtQuestion;
		ImageView imgStatus;
		LinearLayout layoutExplanation;
		
	}

}
