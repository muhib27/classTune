package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.StdAtt;

import java.util.ArrayList;
import java.util.List;

import static com.classtune.app.R.id.txtPresent;
import static com.classtune.app.R.id.txtStudentName;

public class TeacherSubjectAttendanceFullReportAdapter extends BaseAdapter implements Filterable {

	private LayoutInflater inflater;

	private Context context;
	private List<StdAtt> studentList;
	private List<StdAtt> filteredData = new ArrayList<>();
	private List<StdAtt> originalData = null;

	public TeacherSubjectAttendanceFullReportAdapter(Context context, List<StdAtt> studentList) {
		this.studentList = studentList;
		this.context = context;
		inflater = LayoutInflater.from(context);

		this.filteredData.clear();
		this.filteredData.addAll(this.studentList);

	}
	
	@Override
	public int getCount() {
		return filteredData.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredData.get(position);
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
			rowView = inflater.inflate(R.layout.row_teacher_subject_attendance_full_report, null);

			holder.txtStudentName =  (TextView) rowView.findViewById(txtStudentName);
			holder.txtRollNo = (TextView)  rowView.findViewById(R.id.txtRollNo);
			holder.txtPresent = (TextView)rowView.findViewById(txtPresent);
			holder.txtAbsent = (TextView)rowView.findViewById(R.id.txtAbsent);
			holder.txtLate = (TextView)rowView.findViewById(R.id.txtLate);
			holder.layoutRoll = (LinearLayout)rowView.findViewById(R.id.layoutRoll);

			rowView.setTag(holder);
		}else{
			holder = (ViewHolder) rowView.getTag();
		}


		final StdAtt stdAtt = filteredData.get(index);

		holder.txtStudentName.setText(stdAtt.getName());
		if(TextUtils.isEmpty(stdAtt.getRollNo())){
			holder.layoutRoll.setVisibility(View.GONE);
		}else{
			holder.layoutRoll.setVisibility(View.VISIBLE);
			holder.txtRollNo.setText(stdAtt.getRollNo());
		}

		holder.txtPresent.setText(String.valueOf(stdAtt.getPresent()));
		holder.txtAbsent.setText(String.valueOf(stdAtt.getAbsent()));
		holder.txtLate.setText(String.valueOf(stdAtt.getLate()));


		return rowView;
	}


	private static class ViewHolder {
		protected TextView txtStudentName;
		protected TextView txtRollNo;
		protected TextView txtPresent;
		protected TextView txtAbsent;
		protected TextView txtLate;
		protected LinearLayout layoutRoll;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {

				filteredData = (List<StdAtt>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<StdAtt> FilteredArrList = new ArrayList<StdAtt>();

				if (originalData == null) {
					originalData = new ArrayList<StdAtt>(filteredData);
				}


				if (constraint == null || constraint.length() == 0) {

					results.count = originalData.size();
					results.values = originalData;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < originalData.size(); i++) {
						String dataName = originalData.get(i).getName();
						if(!TextUtils.isEmpty(dataName)){
							if (dataName.toLowerCase().contains(constraint.toString().toLowerCase())) {
								FilteredArrList.add(originalData.get(i));
							}
						}


						String dataRoll = originalData.get(i).getRollNo();
						if(!TextUtils.isEmpty(dataRoll)){
							if (dataRoll.toLowerCase().contains(constraint.toString().toLowerCase())) {
								FilteredArrList.add(originalData.get(i));
							}
						}

					}

					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				return results;
			}

		};
	}

	
} 