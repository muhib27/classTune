package com.classtune.app.freeversion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classtune.app.R;
import com.classtune.app.schoolapp.model.SchoolEvent;
import com.classtune.app.schoolapp.model.Wrapper;
import com.classtune.app.schoolapp.networking.AppRestClient;
import com.classtune.app.schoolapp.utils.AppConstant;
import com.classtune.app.schoolapp.utils.AppUtility;
import com.classtune.app.schoolapp.utils.GsonParser;
import com.classtune.app.schoolapp.utils.ReminderHelper;
import com.classtune.app.schoolapp.utils.RequestKeyHelper;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.classtune.app.schoolapp.utils.UserHelper;
import com.classtune.app.schoolapp.viewhelpers.CustomButtonTest;
import com.classtune.app.schoolapp.viewhelpers.CustomRhombusIcon;
import com.classtune.app.schoolapp.viewhelpers.ExpandableTextView;
import com.classtune.app.schoolapp.viewhelpers.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleEventActivity extends ChildContainerActivity {

    private UIHelper uiHelper;
    private String id;
    private Gson gson;
    private Context context;

    private SchoolEvent data;
    private LayoutInflater vi;
    private SchoolEvent selectedEvent;


    TextView titleTextView;
    ExpandableTextView descriptionTextView;
    CustomButtonTest joinInBtn;
    CustomButtonTest remainderBtn;
    //CustomButtonTest notGoingBtn;
    TextView eventCatName;
    TextView txtTime, txtStartDate, txtEndDate;

    private LinearLayout bottomLayer;
    private CustomRhombusIcon categoryIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        context = SingleEventActivity.this;

        gson = new Gson();

        uiHelper = new UIHelper(SingleEventActivity.this);

        if(getIntent().getExtras() != null)
            this.id = getIntent().getExtras().getString(AppConstant.ID_SINGLE_EVENT);

        initView();
        initApiCall();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        homeBtn.setVisibility(View.VISIBLE);
        logo.setVisibility(View.GONE);
    }

    private void initView()
    {
        titleTextView = (TextView)this.findViewById(R.id.event_title_text);
        descriptionTextView=(ExpandableTextView)this.findViewById(R.id.event_details_text);
        joinInBtn=(CustomButtonTest)this.findViewById(R.id.btn_join_in);
        //viewHolder.notGoingBtn=(CustomButtonTest)rowView.findViewById(R.id.btn_not_going);
        remainderBtn=(CustomButtonTest)this.findViewById(R.id.btn_reminder);
        eventCatName = (TextView)this.findViewById(R.id.event_cat_name);

        txtTime = (TextView)this.findViewById(R.id.txtTime);
        txtStartDate = (TextView)this.findViewById(R.id.txtStartDate);
        txtEndDate = (TextView)this.findViewById(R.id.txtEndDate);

        bottomLayer = (LinearLayout)this.findViewById(R.id.bottomLayer);
        categoryIcon = (CustomRhombusIcon)this.findViewById(R.id.categoryIcon);
    }

    private void initApiCall()
    {

        RequestParams params=new RequestParams();
        params.put(RequestKeyHelper.USER_SECRET, UserHelper.getUserSecret());
        params.put("id", id);


        AppRestClient.post(URLHelper.URL_GET_SINGLE_EVENT, params, singleEventHandler);
    }

    AsyncHttpResponseHandler singleEventHandler = new AsyncHttpResponseHandler()
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
            Wrapper wrapper= GsonParser.getInstance().parseServerResponse(responseString);
            if(wrapper.getStatus().getCode()==200)
            {
                JsonObject objHomework = wrapper.getData().get("events").getAsJsonObject();
                data = gson.fromJson(objHomework.toString(), SchoolEvent.class);

                initAction();

            }
            else
            {

            }
            Log.e("Events", responseString);


        }

    };



    private void initAction()
    {

        if(data.isUpcoming())
        {
            categoryIcon.setIconImage(R.drawable.event_row_icon);
            bottomLayer.setVisibility(View.VISIBLE);
        }
        else
        {
            categoryIcon.setIconImage(R.drawable.event_row_gray_icon);
            bottomLayer.setVisibility(View.GONE);
        }


        remainderBtn.setTag(data);

        SchoolEvent temp=data;
        titleTextView.setText(temp.getEventTitle());
        descriptionTextView.setText(temp.getEventDescription());
        switch (temp.getEventAck()) {
            case JOIN_IN:
                joinInBtn.setButtonSelected(true);
                //holder.notGoingBtn.setButtonSelected(false);
                break;
            case NOT_GOING:
                //holder.notGoingBtn.setButtonSelected(true);
                joinInBtn.setButtonSelected(false);
                break;
            case NONE:
                //holder.notGoingBtn.setButtonSelected(false);
                joinInBtn.setButtonSelected(false);
                break;

            default:
                break;
        }

        joinInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!joinInBtn.getSelectedState())
                {
                    selectedEvent=data;
                    if(AppUtility.isInternetConnected()){
                        setButtonState(joinInBtn, R.drawable.done_tap, false, "Joined");
                        notifyServerAboutAck(SchoolEvent.ackTypeEnum.JOIN_IN.ordinal(),data.getEventId());
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
        remainderBtn.setOnClickListener(new View.OnClickListener() {

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
                        remainderBtn.setImage(R.drawable.btn_reminder_normal);
                        remainderBtn.setTitleColor(context.getResources().getColor(R.color.gray_1));
                        remainderBtn.setEnabled(true);
                    }
                };

                AppUtility.showDateTimePicker(rmEvent.getEventStartDate(), rmEvent.getEventTitle(), content, context);




            }
        });


        eventCatName.setText(temp.getEventyCatName());

        //Log.e("JOIN_TYPE", "outer is: "+ackTypeEnum.JOIN_IN.ordinal());

	    /*if(ackTypeEnum.JOIN_IN.ordinal() == 1)
	    {
	    	setButtonState(holder.joinInBtn, R.drawable.done_tap, false, "Joined");
	    	holder.joinInBtn.setEnabled(false);

            Log.e("JOIN_TYPE", "inner is: "+ackTypeEnum.JOIN_IN.ordinal());
	    }*/


        Log.e("JOIN_TYPE", "outer is: " + temp.getEventAck().toString());

        if(temp.getEventAck().equals(SchoolEvent.ackTypeEnum.JOIN_IN))
        {
            setButtonState(joinInBtn, R.drawable.done_tap, false, "Joined");
            joinInBtn.setEnabled(false);

            Log.e("JOIN_TYPE", "inner is: " + temp.getEventAck().JOIN_IN.ordinal());
        }

        if (ReminderHelper.getInstance().reminder_map.containsKey(temp.getEventStartDate())){
            setButtonState(remainderBtn, R.drawable.btn_reminder_tap, false, "Reminder");

        }else {
            setButtonState(remainderBtn, R.drawable.btn_reminder_normal, true, "Reminder");
        }


        String startDate = data.getEventStartDate();
        String arrayStartDate[] = startDate.split("\\s+");

        String endDate = data.getEventEndDate();
        String arrayEndDate[] = endDate.split("\\s+");

        //viewHolder.txtTime.setText(arrayStartDate[1] + "-"+ arrayEndDate[1]);

        txtTime.setText(get12HoursTime(arrayStartDate[1]) + "-"+ get12HoursTime(arrayEndDate[1]));


        txtStartDate.setText(arrayStartDate[0]);
        txtEndDate.setText(arrayEndDate[0]);


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
            Wrapper wrapper= GsonParser.getInstance().parseServerResponse(responseString);
            if(wrapper.getStatus().getCode()==200)
            {
                selectedEvent.setEventAcks(wrapper.getData().get("event_ack").getAsInt());

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

