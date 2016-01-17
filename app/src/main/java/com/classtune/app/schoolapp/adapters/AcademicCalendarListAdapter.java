package com.classtune.app.schoolapp.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.AcademicCalendarDataItem;
import com.classtune.app.schoolapp.utils.AppUtility;

import java.util.List;

public class AcademicCalendarListAdapter extends ArrayAdapter<AcademicCalendarDataItem> {

	private final Context context;
	private List<AcademicCalendarDataItem> items;
	private LayoutInflater vi;
	
	static class ViewHolder {
	    
		TextView dateTextView;
		TextView descriptionTextView;
        LinearLayout dateBg,examBg,viewBg;
	  }
	
	public AcademicCalendarListAdapter(Context context, List<AcademicCalendarDataItem> objects) {
		super(context,  0, objects);
		this.context=context;
		this.items=objects;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View rowView = convertView;

	    if (rowView == null) {
	      /*LayoutInflater inflater = context.getLayoutInflater();*/
	      rowView = vi.inflate(R.layout.row_academic_calendar_exam_list, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.dateTextView = (TextView) rowView.findViewById(R.id.date_text);
          viewHolder.dateBg = (LinearLayout) rowView.findViewById(R.id.date_bg_academic);
            viewHolder.examBg = (LinearLayout) rowView.findViewById(R.id.exam_bg_academic);
            viewHolder.viewBg = (LinearLayout) rowView.findViewById(R.id.view_bg_academic);
            viewHolder.descriptionTextView=(TextView)rowView.findViewById(R.id.exam_text);
	      rowView.setTag(viewHolder);
	    }


	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    AcademicCalendarDataItem temp=items.get(position);
	    holder.dateTextView.setText(AppUtility.getDateString(temp.getEventDate(),AppUtility.DATE_FORMAT_APP,AppUtility.DATE_FORMAT_SERVER));
	    holder.descriptionTextView.setText(temp.getEventTitle());
        holder.dateBg.setBackgroundColor((position%2!=0)?context.getResources().getColor(R.color.bg_row_odd):context.getResources().getColor(R.color.white));
        holder.examBg.setBackgroundColor((position%2!=0)?context.getResources().getColor(R.color.bg_row_odd):context.getResources().getColor(R.color.white));
        holder.viewBg.setBackgroundColor((position%2!=0)?context.getResources().getColor(R.color.bg_row_odd):context.getResources().getColor(R.color.white));
	    
	    return rowView;
	  }
	
	
	@Override
	public AcademicCalendarDataItem getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}
	
	
	
	
	
}

	


