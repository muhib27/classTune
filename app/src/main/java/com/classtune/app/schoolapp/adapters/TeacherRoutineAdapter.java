package com.classtune.app.schoolapp.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.RoutineTimeTable;
import com.classtune.app.schoolapp.utils.AppUtility;

import java.util.List;

public class TeacherRoutineAdapter extends BaseAdapter {

	private static class ViewHolder {
		private TextView classNameText;
		private TextView subjectNameText;
		private TextView classTimeText;
		private ImageView icon;
	}
	
	private List<RoutineTimeTable> items;
	private Activity activity;
	
	public TeacherRoutineAdapter(Activity context, int textViewResourceId,
			List<RoutineTimeTable> items) {
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
		RoutineTimeTable item=items.get(index);
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			ViewHolder viewHolder = new ViewHolder();
			rowView = inflater.inflate(R.layout.row_teacher_routine_list, null);
			viewHolder.classNameText=(TextView)rowView.findViewById(R.id.tv_class_batch_name);
			viewHolder.subjectNameText=(TextView)rowView.findViewById(R.id.tv_subject_name);
			viewHolder.classTimeText=(TextView)rowView.findViewById(R.id.tv_time);
			viewHolder.icon=(ImageView)rowView.findViewById(R.id.icon);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		if(TextUtils.isEmpty(item.getClassName())){
			holder.classNameText.setText(item.getTeacher_full_name());
		}else 
		holder.classNameText.setText(item.getClassName()+" "+item.getBatchName());
		holder.subjectNameText.setText(item.getSubjectName());
		holder.classTimeText.setText(item.getClassStartTime()+"-"+item.getClassEndTime());
		holder.icon.setImageResource(AppUtility.getImageResourceId(item.getSubjectIconName(), activity));
		return rowView;
	}
	
} 