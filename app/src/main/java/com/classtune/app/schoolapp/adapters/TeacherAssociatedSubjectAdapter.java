package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.freeversion.TeacherSubjectAttendanceTakeActivity;
import com.classtune.app.schoolapp.model.Subject;
import com.classtune.app.schoolapp.utils.AppConstant;

import java.util.List;

public class TeacherAssociatedSubjectAdapter extends BaseAdapter {

	private List<Subject> items;
	private Context context;
	private LayoutInflater inflater;

	public TeacherAssociatedSubjectAdapter(Context context, List<Subject> items) {
		this.items = items;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int index, final View convertView, ViewGroup viewGroup) {
		
		View rowView = convertView;
		final ViewHolder holder;
		if (rowView == null) {
			holder = new ViewHolder();
			rowView = inflater.inflate(R.layout.row_teacher_associated_subject, null);
			holder.txtSubjectName=(TextView)rowView.findViewById(R.id.txtSubjectName);
			holder.btnTakeAttendance = (Button)rowView.findViewById(R.id.btnTakeAttendance);

			rowView.setTag(holder);
		}else{
			holder = (ViewHolder) rowView.getTag();
		}

		holder.txtSubjectName.setText(items.get(index).getName());

		holder.btnTakeAttendance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// go to subject attendance activity class
				Intent intent = new Intent(context, TeacherSubjectAttendanceTakeActivity.class);
				intent.putExtra(AppConstant.KEY_ASSOCIATED_SUBJECT_ID, items.get(index).getId());
				context.startActivity(intent);
			}
		});

		
		return rowView;
	}

	private static class ViewHolder {
		public TextView txtSubjectName;
		public Button btnTakeAttendance;
	}
	
} 