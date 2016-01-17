package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.StudentInfoActivity;
import com.classtune.app.schoolapp.model.StudentAttendance;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentListAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<StudentAttendance> studentlist = null;
	private ArrayList<StudentAttendance> arraylist;

	public StudentListAdapter(Context context, List<StudentAttendance> studentlist) {
		mContext = context;
		this.studentlist = studentlist;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<StudentAttendance>();
		this.arraylist.addAll(studentlist);
	}

	public class ViewHolder {
		TextView rollNumber;
		TextView studentName;
		Button viewProfile;
	}

	@Override
	public int getCount() {
		return studentlist.size();
	}

	@Override
	public StudentAttendance getItem(int position) {
		return studentlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.student_row_item, null);
			// Locate the TextViews in listview_item.xml
			holder.rollNumber = (TextView) view.findViewById(R.id.roll_no_text);
			holder.studentName = (TextView) view.findViewById(R.id.label);
			holder.viewProfile = (Button) view.findViewById(R.id.btn_view_student_profile);
			holder.viewProfile.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = Integer.parseInt(v.getTag().toString());
					Intent studentIntent = new Intent(mContext,StudentInfoActivity.class);
					studentIntent.putExtra("student_id", studentlist.get(pos).getId());
					mContext.startActivity(studentIntent);
				}
			});
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.rollNumber.setText(studentlist.get(position).getRollNo());
		holder.studentName.setText(studentlist.get(position).getStudentName());
		holder.viewProfile.setTag(""+position);
		//holder.population.setText(worldpopulationlist.get(position).getPopulation());
		
		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, SingleItemView.class);
				// Pass all data rank
				intent.putExtra("rank",(worldpopulationlist.get(position).getRank()));
				// Pass all data country
				intent.putExtra("country",(worldpopulationlist.get(position).getCountry()));
				// Pass all data population
				intent.putExtra("population",(worldpopulationlist.get(position).getPopulation()));
				// Pass all data flag
				// Start SingleItemView Class
				mContext.startActivity(intent);*/
			}
		});

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		studentlist.clear();
		if (charText.length() == 0) {
			studentlist.addAll(arraylist);
		} 
		else 
		{
			for (StudentAttendance st : arraylist) 
			{
				if(st.getRollNo() != null)
				{
					if (st.getStudentName().toLowerCase(Locale.getDefault()).contains(charText)||st.getRollNo().toLowerCase(Locale.getDefault()).contains(charText))
					{
						studentlist.add(st);
					}
				}
				else
				{
					if (st.getStudentName().toLowerCase(Locale.getDefault()).contains(charText))
					{
						studentlist.add(st);
					}
				}

			}
		}
		notifyDataSetChanged();
	}

}
