package com.classtune.app.schoolapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.SchoolEvent;
import com.classtune.app.schoolapp.viewhelpers.ExpandableTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArchievedEventListAdapter extends ArrayAdapter<SchoolEvent> {

	private final Context context;
	private List<SchoolEvent> items;
	private LayoutInflater vi;
	
	static class ViewHolder {
	    
		TextView titleTextView;
		ExpandableTextView descriptionTextView;
		TextView eventCatName;
		TextView txtTime, txtStartDate, txtEndDate;
	  }
	
	public ArchievedEventListAdapter(Context context, List<SchoolEvent> objects) {
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
	      rowView = vi.inflate(R.layout.row_archieved_events, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.event_title_text);
	      viewHolder.descriptionTextView=(ExpandableTextView)rowView.findViewById(R.id.event_details_text);
	      viewHolder.eventCatName = (TextView)rowView.findViewById(R.id.event_cat_name);
	      
	      viewHolder.txtTime = (TextView)rowView.findViewById(R.id.txtTime);
	      viewHolder.txtStartDate = (TextView)rowView.findViewById(R.id.txtStartDate);
	      viewHolder.txtEndDate = (TextView)rowView.findViewById(R.id.txtEndDate);
	      
	      rowView.setTag(viewHolder);
	    }

	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    SchoolEvent temp=items.get(position);
	    holder.titleTextView.setText(temp.getEventTitle());
	    holder.descriptionTextView.setText(temp.getEventDescription());
	    holder.eventCatName.setText(temp.getEventyCatName());
	    
	    
	    String startDate = temp.getEventStartDate();
	    String arrayStartDate[] = startDate.split("\\s+");
	    
	    String endDate = temp.getEventEndDate();
	    String arrayEndDate[] = endDate.split("\\s+");
	    
	    //holder.txtTime.setText(arrayStartDate[1] + "-"+ arrayEndDate[1]);
	 
	    
	    holder.txtTime.setText(get12HoursTime(arrayStartDate[1]) + "-"+ get12HoursTime(arrayEndDate[1]));
	    
	    
	    holder.txtStartDate.setText(arrayStartDate[0]);
	    holder.txtEndDate.setText(arrayEndDate[0]);
	    
	    
	    return rowView;
	  }
	
	
	
	private String get12HoursTime(String dateString)
	{
		String data = "";
		
		
		final String time = dateString;

		try {
		    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
		    final Date dateObj = sdf.parse(time);
		    data = new SimpleDateFormat("K:mm aa").format(dateObj);
		} catch (final ParseException e) {
		    e.printStackTrace();
		}
		
		return data;
		
	}
		
	
	
	
}

	


