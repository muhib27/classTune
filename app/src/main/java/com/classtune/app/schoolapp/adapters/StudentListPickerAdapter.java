package com.classtune.app.schoolapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.StudentAttendance;

import java.util.List;

public class StudentListPickerAdapter extends BaseAdapter {

	private static class ViewHolder {
		public TextView nameText;
		public TextView rollNoText;
		public Button viewProfileBtn;
	}
	
	private List<StudentAttendance> items;
	private Activity activity;
	
	public StudentListPickerAdapter(Activity context, int textViewResourceId,
			List<StudentAttendance> items) {
		this.items = items;
		this.activity = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup viewGroup) {
		
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.student_row_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.nameText=(TextView)rowView.findViewById(R.id.label);
			viewHolder.rollNoText=(TextView)rowView.findViewById(R.id.roll_no_text);
			viewHolder.viewProfileBtn = (Button)rowView.findViewById(R.id.btn_view_student_profile);
			viewHolder.viewProfileBtn.setVisibility(View.GONE);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		StudentAttendance item=items.get(index);
		
		
		holder.nameText.setText(item.getStudentName());
		holder.rollNoText.setText(item.getRollNo());
			
		
		return rowView;
	}
	
} 