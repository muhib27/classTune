package com.classtune.app.schoolapp.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.SchoolEvent;
import com.classtune.app.schoolapp.model.SchoolEvent.ackTypeEnum;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.ReminderHelper;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButtonTest;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpcomingEventListAdapter extends ArrayAdapter<SchoolEvent> {

	private final Context context;



    private List<SchoolEvent> items;
	private LayoutInflater vi;
	private UIHelper uiHelper;
	private SchoolEvent selectedEvent;
	
	static class ViewHolder {
	    
		TextView titleTextView;
		TextView descriptionTextView;
		CustomButtonTest joinInBtn;
		CustomButtonTest remainderBtn;
		//CustomButtonTest notGoingBtn;
		TextView eventCatName;
		TextView txtTime, txtStartDate, txtEndDate;
		
	  }
	
	public UpcomingEventListAdapter(Context context, List<SchoolEvent> objects,UIHelper uiHelper) {
		super(context,  0, objects);
		this.context=context;
		this.items=objects;
		this.uiHelper=uiHelper;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(final int position, final View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		View rowView = convertView;
	    
		if (rowView == null) {
	      /*LayoutInflater inflater = context.getLayoutInflater();*/
	      rowView = LayoutInflater.from(context).inflate(R.layout.row_upcoming_events, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.event_title_text);
	      viewHolder.descriptionTextView=(TextView)rowView.findViewById(R.id.event_details_text);
	      viewHolder.joinInBtn=(CustomButtonTest)rowView.findViewById(R.id.btn_join_in);
	      //viewHolder.notGoingBtn=(CustomButtonTest)rowView.findViewById(R.id.btn_not_going);
	      viewHolder.remainderBtn=(CustomButtonTest)rowView.findViewById(R.id.btn_reminder);
	      viewHolder.eventCatName = (TextView)rowView.findViewById(R.id.event_cat_name);
	      
	      viewHolder.txtTime = (TextView)rowView.findViewById(R.id.txtTime);
	      viewHolder.txtStartDate = (TextView)rowView.findViewById(R.id.txtStartDate);
	      viewHolder.txtEndDate = (TextView)rowView.findViewById(R.id.txtEndDate);

	      
	      rowView.setTag(viewHolder);
	    }
	    
	    

	    

	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    
	    holder.remainderBtn.setTag(items.get(position));
	    
	    SchoolEvent temp=items.get(position);
	    holder.titleTextView.setText(temp.getEventTitle());
	    holder.descriptionTextView.setText(temp.getEventDescription());
	    switch (temp.getEventAck()) {
		case JOIN_IN:
			holder.joinInBtn.setButtonSelected(true);
			//holder.notGoingBtn.setButtonSelected(false);
			break;
		case NOT_GOING:
			//holder.notGoingBtn.setButtonSelected(true);
			holder.joinInBtn.setButtonSelected(false);
			break;
		case NONE:
			//holder.notGoingBtn.setButtonSelected(false);
			holder.joinInBtn.setButtonSelected(false);
			break;

		default:
			break;
		}
	    
	    holder.joinInBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!holder.joinInBtn.getSelectedState())
				{
					selectedEvent=items.get(position);
					if(AppUtility.isInternetConnected()){
						setButtonState(holder.joinInBtn, R.drawable.done_tap, false, context.getString(R.string.java_singleeventactivity_joined));
						notifyServerAboutAck(ackTypeEnum.JOIN_IN.ordinal(),items.get(position).getEventId());
					}
					else
						uiHelper.showMessage(context.getResources().getString(R.string.internet_error_text));
				}
				
			}
		});
	    /*holder.notGoingBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!holder.notGoingBtn.getSelectedState())
				{
					selectedEvent=items.get(position);
					if(AppUtility.isInternetConnected())
						notifyServerAboutAck(ackTypeEnum.NOT_GOING.ordinal(),items.get(position).getEventId());
					else
						uiHelper.showMessage(context.getResources().getString(R.string.internet_error_text));
				}
			}
		});*/
	    holder.remainderBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//holder.remainderBtn.toggleButtonState();
				CustomButtonTest reminderBtn = (CustomButtonTest) v;
				
				SchoolEvent rmEvent = (SchoolEvent) reminderBtn.getTag();
				reminderBtn.setImage(R.drawable.btn_reminder_tap);
				reminderBtn.setTitleColor(context.getResources().getColor(R.color.classtune_green_color));
				reminderBtn.setEnabled(false);
				String content = ""+ Html.fromHtml(rmEvent.getEventDescription());
				//ReminderHelper.getInstance().setReminder(rmEvent.getEventStartDate(), rmEvent.getEventTitle(), content, rmEvent.getEventStartDate(), context);

                AppUtility.listenerDatePickerCancel = new AppUtility.IDatePickerCancel() {
                    @Override
                    public void onCancelCalled() {

                        Log.e("CCCCC", "cancel called");
                        holder.remainderBtn.setImage(R.drawable.btn_reminder_normal);
                        holder.remainderBtn.setTitleColor(context.getResources().getColor(R.color.gray_1));
                        holder.remainderBtn.setEnabled(true);
                    }
                };

                AppUtility.showDateTimePicker(rmEvent.getEventStartDate(), rmEvent.getEventTitle(), content, context);




            }
		});
	    
	    
	    holder.eventCatName.setText(temp.getEventyCatName());

        //Log.e("JOIN_TYPE", "outer is: "+ackTypeEnum.JOIN_IN.ordinal());
	    
	    /*if(ackTypeEnum.JOIN_IN.ordinal() == 1)
	    {
	    	setButtonState(holder.joinInBtn, R.drawable.done_tap, false, "Joined");
	    	holder.joinInBtn.setEnabled(false);

            Log.e("JOIN_TYPE", "inner is: "+ackTypeEnum.JOIN_IN.ordinal());
	    }*/


        Log.e("JOIN_TYPE", "outer is: " + temp.getEventAck().toString());

        if(temp.getEventAck().equals(ackTypeEnum.JOIN_IN))
        {
            setButtonState(holder.joinInBtn, R.drawable.done_tap, false, context.getString(R.string.java_singleeventactivity_joined));
            holder.joinInBtn.setEnabled(false);

            Log.e("JOIN_TYPE", "inner is: " + temp.getEventAck().JOIN_IN.ordinal());
        }
	    
	    if (ReminderHelper.getInstance().reminder_map.containsKey(temp.getEventStartDate())){
			setButtonState(holder.remainderBtn, R.drawable.btn_reminder_tap, false, context.getString(R.string.btn_reminder));
			
		}else {
			setButtonState(holder.remainderBtn, R.drawable.btn_reminder_normal, true, context.getString(R.string.btn_reminder));
		}
	    
	    
	    String startDate = items.get(position).getEventStartDate();
	    String arrayStartDate[] = startDate.split("\\s+");
	    
	    String endDate = items.get(position).getEventEndDate();
	    String arrayEndDate[] = endDate.split("\\s+");
	    
	    //viewHolder.txtTime.setText(arrayStartDate[1] + "-"+ arrayEndDate[1]);
	    
	    holder.txtTime.setText(get12HoursTime(arrayStartDate[1]) + "-"+ get12HoursTime(arrayEndDate[1]));
	    
	    
	    holder.txtStartDate.setText(arrayStartDate[0]);
	    holder.txtEndDate.setText(arrayEndDate[0]);


		/*rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, SingleEventActivity.class);
				intent.putExtra(AppConstant.ID_SINGLE_EVENT, items.get(position).getEventId());
				context.startActivity(intent);
			}
		});*/

		if(items.get(position).getIsHoliday().equalsIgnoreCase("1") && !temp.getEventAck().equals(ackTypeEnum.JOIN_IN)){
			setButtonState(holder.joinInBtn, R.drawable.done_normal, true, context.getString(R.string.java_singleeventactivity_joined));
			holder.joinInBtn.setEnabled(false);
		}
	    
	    return rowView;
	  }



	
	@SuppressLint("ResourceAsColor")
	private void setButtonState(CustomButtonTest btn, int imgResId, boolean enable , String btnText) {
		
		btn.setImage(imgResId);
		btn.setTitleText(btnText);
		btn.setEnabled(enable);
		if(enable) {
			setBtnTitleColor(btn, R.color.gray_1); 
		} else {
			setBtnTitleColor(btn, R.color.classtune_green_color);
		}
	}
	private void setBtnTitleColor(CustomButtonTest btn, int colorId) {
		btn.setTitleColor(context.getResources().getColor(colorId));
	}
	
	
	private boolean shouldToggle(CustomButtonTest button1,CustomButtonTest button2)
	{
		if(button1.getSelectedState()==button2.getSelectedState())
			return false;
		else
			return true;
	}
	
	
	
	private void notifyServerAboutAck(int ackType,String id)
	{
		RequestParams params=new RequestParams();
		params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
		params.put(RequestKeyHelper.EVENT_ID, id);
		params.put(RequestKeyHelper.STATUS, String.valueOf(ackType));
		
		AppRestClient.post(URLHelper.URL_POST_ACK_EVENT, params, postAckHandler);
	}
	
	AsyncHttpResponseHandler postAckHandler=new AsyncHttpResponseHandler()
	{
		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);
			
		}

		@Override
		public void onStart() {
			super.onStart();
			if(!uiHelper.isDialogActive())
				uiHelper.showLoadingDialog(context.getResources().getString(R.string.loading_text));
			else
				uiHelper.updateLoadingDialog(context.getResources().getString(R.string.loading_text));
			
		}

		@Override
		public void onSuccess(int arg0, String responseString) {
			super.onSuccess(arg0, responseString);
			uiHelper.dismissLoadingDialog();
			Wrapper wrapper=GsonParser.getInstance().parseServerResponse(responseString);
			if(wrapper.getStatus().getCode()==200)
			{
				selectedEvent.setEventAcks(wrapper.getData().get("event_ack").getAsInt());
				notifyDataSetChanged();
			}
			else
			{
				
			}
			Log.e("Events", responseString);
			
			
		}
		
	};
	
	
	
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

	


