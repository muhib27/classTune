package com.classtune.app.schoolapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.StudentAssociated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.classtune.app.R.id.layoutAbsent;
import static com.classtune.app.R.id.layoutLate;
import static com.classtune.app.R.id.layoutPresent;

public class TeacherTakeSubjectAttendanceAdapter extends BaseAdapter implements Filterable {

	private LayoutInflater inflater;

	private Context context;
	private List<StudentAssociated> studentList;
	private List<StudentAssociated> filteredData = new ArrayList<>();
	private List<StudentAssociated> originalData = null;
	private boolean isClickable = false;
	private Set<String> listStudentStatusNew;
	private boolean isAllPresent = false;
	private List<String> mList;
	private boolean isUpdate = false;

	public TeacherTakeSubjectAttendanceAdapter(Context context, List<StudentAssociated> studentList) {
		this.studentList = studentList;
		this.context = context;
		inflater = LayoutInflater.from(context);

		this.filteredData.clear();
		this.filteredData.addAll(this.studentList);

		this.listStudentStatusNew = new HashSet<>();
		this.mList = new ArrayList<>();
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


	public void setClickable(boolean isClickable){
		this.isClickable = isClickable;
	}
	public boolean getClickable(){
		return this.isClickable;
	}

	public List<String> getListStudentDataId() {

		List<String> mSet = new ArrayList<>();
		for(String str : listStudentStatusNew){
			str = str.substring(0, str.length()-1);
			mSet.add(str);
		}
		return mSet;
	}


	public List<String> getListStudentStatusNew() {


		mList.clear();

		Log.e("HASH_SET_SIZE", "size: "+listStudentStatusNew.size());
		for(String str : listStudentStatusNew){
			String a = str.substring(str.length() - 1);
			Log.e("HASH_SET", str);

			if(a.equals("A"))
				mList.add("0");
			else if(a.equals("L"))
				mList.add("1");
		}

		return mList;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean update) {
		isUpdate = update;
	}

	public boolean isAllPresent() {
		return isAllPresent;
	}

	public void setAllPresent(boolean allPresent) {
		isAllPresent = allPresent;
	}

	@Override
	public View getView(final int index, final View convertView, ViewGroup viewGroup) {

		View rowView = convertView;
		final ViewHolder holder;
		if (rowView == null) {
			holder = new ViewHolder();
			rowView = inflater.inflate(R.layout.row_teacher_take_subject_attendance, null);

			holder.txtStudentName =  (TextView) rowView.findViewById(R.id.txtStudentName);
			holder.txtRollNo = (TextView)  rowView.findViewById(R.id.txtRollNo);
			holder.layoutPresent = (LinearLayout) rowView.findViewById(layoutPresent);
			holder.layoutAbsent = (LinearLayout) rowView.findViewById(layoutAbsent);
			holder.layoutAbsent = (LinearLayout) rowView.findViewById(layoutAbsent);
			holder.layoutLate = (LinearLayout) rowView.findViewById(layoutLate);
			holder.txtPresent = (TextView)rowView.findViewById(R.id.txtPresent);
			holder.txtAbsent = (TextView)rowView.findViewById(R.id.txtAbsent);
			holder.txtLate = (TextView)rowView.findViewById(R.id.txtLate);
			holder.layoutRoll = (LinearLayout)rowView.findViewById(R.id.layoutRoll);

			holder.lisCheckBox = new ArrayList<>();

			holder.lisCheckBox.add(holder.layoutPresent);
			holder.lisCheckBox.add(holder.layoutAbsent);
			holder.lisCheckBox.add(holder.layoutLate);

			rowView.setTag(holder);
		}else{
			holder = (ViewHolder) rowView.getTag();
		}


		final StudentAssociated student = filteredData.get(index);
		holder.txtStudentName.setText(student.getStudentName());
		holder.txtRollNo.setText(student.getRollNo());
		if(!TextUtils.isEmpty(student.getRollNo())){
			holder.layoutRoll.setVisibility(View.VISIBLE);
		}else{
			holder.layoutRoll.setVisibility(View.GONE);
		}

		if(isClickable == false){
			holder.layoutPresent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					((LinearLayout)view).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
					Toast.makeText(context, R.string.message_register_room, Toast.LENGTH_SHORT).show();
				}
			});
			holder.layoutAbsent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					((LinearLayout)view).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
					Toast.makeText(context, R.string.message_register_room, Toast.LENGTH_SHORT).show();
				}
			});
			holder.layoutLate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					((LinearLayout)view).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
					Toast.makeText(context, R.string.message_register_room, Toast.LENGTH_SHORT).show();
				}
			});


		}else{

			holder.layoutPresent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					validateCheckBoxSelected(holder.lisCheckBox, (LinearLayout)view, R.color.leave);
					student.setClickedPresent(true);
					student.setClickedAbsent(false);
					student.setClickedLate(false);

					holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.white));
					holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
					holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));

					if(listStudentStatusNew.contains(student.getStudentId()+"A")){
						listStudentStatusNew.remove(student.getStudentId()+"A");
					}
					if(listStudentStatusNew.contains(student.getStudentId()+"L")){
						listStudentStatusNew.remove(student.getStudentId()+"L");
					}


				}
			});
			holder.layoutAbsent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					validateCheckBoxSelected(holder.lisCheckBox, (LinearLayout)view, R.color.absent);
					student.setClickedPresent(false);
					student.setClickedAbsent(true);
					student.setClickedLate(false);

					holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.black));
					holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.white));
					holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));


					if(listStudentStatusNew.contains(student.getStudentId()+"L")){
						listStudentStatusNew.remove(student.getStudentId()+"L");
					}
					listStudentStatusNew.add(student.getStudentId()+"A");

				}
			});
			holder.layoutLate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					validateCheckBoxSelected(holder.lisCheckBox, (LinearLayout)view, R.color.late);
					student.setClickedPresent(false);
					student.setClickedAbsent(false);
					student.setClickedLate(true);

					holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.black));
					holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
					holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.white));

					if(listStudentStatusNew.contains(student.getStudentId()+"A")){
						listStudentStatusNew.remove(student.getStudentId()+"A");
					}
					listStudentStatusNew.add(student.getStudentId()+"L");

				}
			});
		}



		if(isAllPresent){
			holder.layoutPresent.setBackgroundColor(ContextCompat.getColor(context, R.color.leave));
			holder.layoutAbsent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
			holder.layoutLate.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

			holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.white));
			holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
			holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));

		}else{
			if(student.getAtt() == 0){
				holder.layoutPresent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
				holder.layoutAbsent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
				holder.layoutLate.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

				holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.black));
				holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
				holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));

			}else if(student.getAtt() == 1){
				holder.layoutPresent.setBackgroundColor(ContextCompat.getColor(context, R.color.leave));
				holder.layoutAbsent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
				holder.layoutLate.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

				holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.white));
				holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
				holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));

			}else if(student.getAtt() == 2){
				holder.layoutPresent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
				holder.layoutAbsent.setBackgroundColor(ContextCompat.getColor(context, R.color.absent));
				holder.layoutLate.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

				holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.black));
				holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.white));
				holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));

				listStudentStatusNew.add(student.getStudentId()+"A");
			}else if(student.getAtt() == 3){
				holder.layoutPresent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
				holder.layoutAbsent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
				holder.layoutLate.setBackgroundColor(ContextCompat.getColor(context, R.color.late));

				holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.black));
				holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
				holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.white));

				listStudentStatusNew.add(student.getStudentId()+"L");
			}
		}


		if(student.isClickedPresent()){
			validateCheckBoxSelected(holder.lisCheckBox, holder.layoutPresent, R.color.leave);

			holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.white));
			holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
			holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));

			if(listStudentStatusNew.contains(student.getStudentId()+"A")){
				listStudentStatusNew.remove(student.getStudentId()+"A");
			}
			if(listStudentStatusNew.contains(student.getStudentId()+"L")){
				listStudentStatusNew.remove(student.getStudentId()+"L");
			}
		}else if(student.isClickedAbsent()){
			validateCheckBoxSelected(holder.lisCheckBox, holder.layoutAbsent, R.color.absent);

			holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.black));
			holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.white));
			holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.black));

			if(listStudentStatusNew.contains(student.getStudentId()+"L")){
				listStudentStatusNew.remove(student.getStudentId()+"L");
			}
			listStudentStatusNew.add(student.getStudentId()+"A");
		}else if(student.isClickedLate()){
			validateCheckBoxSelected(holder.lisCheckBox, holder.layoutLate, R.color.late);

			holder.txtPresent.setTextColor(ContextCompat.getColor(context, R.color.black));
			holder.txtAbsent.setTextColor(ContextCompat.getColor(context, R.color.black));
			holder.txtLate.setTextColor(ContextCompat.getColor(context, R.color.white));

			if(listStudentStatusNew.contains(student.getStudentId()+"A")){
				listStudentStatusNew.remove(student.getStudentId()+"A");
			}
			listStudentStatusNew.add(student.getStudentId()+"L");
		}


		return rowView;
	}

	private void validateCheckBoxSelected(final List<LinearLayout> lisCheckBox, final LinearLayout appCompatCheckBox, int colorCode){

		appCompatCheckBox.setBackgroundColor(ContextCompat.getColor(context, colorCode));
		for(int i=0; i<lisCheckBox.size();i++) {

			if(!lisCheckBox.get(i).equals(appCompatCheckBox)){
				lisCheckBox.get(i).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
			}

		}
	}

	private static class ViewHolder {
		protected TextView txtStudentName;
		protected TextView txtRollNo;
		protected LinearLayout layoutPresent;
		protected LinearLayout layoutAbsent;
		protected LinearLayout layoutLate;
		protected TextView txtPresent;
		protected TextView txtAbsent;
		protected TextView txtLate;
		protected LinearLayout layoutRoll;
		protected List<LinearLayout> lisCheckBox = new ArrayList<>();
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {

				filteredData = (List<StudentAssociated>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<StudentAssociated> FilteredArrList = new ArrayList<StudentAssociated>();

				if (originalData == null) {
					originalData = new ArrayList<StudentAssociated>(filteredData);
				}


				if (constraint == null || constraint.length() == 0) {

					results.count = originalData.size();
					results.values = originalData;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < originalData.size(); i++) {
						String dataName = originalData.get(i).getStudentName();
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