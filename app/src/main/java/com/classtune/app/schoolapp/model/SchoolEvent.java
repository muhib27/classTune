package com.classtune.app.schoolapp.model;

import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.google.gson.annotations.SerializedName;

public class SchoolEvent {

	public enum ackTypeEnum{
		
		NOT_GOING,JOIN_IN,NONE
	}
	
	public SchoolEvent() {
		super();
	}
	@SerializedName("event_id")
	private String eventId;
	
	@SerializedName("event_start_date")
	private String eventStartDate;
	
	@SerializedName("event_end_date")
	private String eventEndDate;
	
	@SerializedName("event_title")
	private String eventTitle;
	
	@SerializedName("event_category_id")
	private String eventCatId;
	
	@SerializedName("event_category_name")
	private String eventyCatName;
	
	@SerializedName("event_description")
	private String eventDescription;
	
	@SerializedName("event_common")
	private String eventCommon;
	
	@SerializedName("event_acknowledge")
	private int eventAck;

	@SerializedName("upcomming")
	private boolean isUpcoming;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("is_holiday")
	private String isHoliday;


	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public String getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getEventCatId() {
		return eventCatId;
	}
	public void setEventCatId(String eventCatId) {
		this.eventCatId = eventCatId;
	}
	public String getEventyCatName() {
		return eventyCatName;
	}
	public void setEventyCatName(String eventyCatName) {
		this.eventyCatName = eventyCatName;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getEventCommon() {
		return eventCommon;
	}
	public void setEventCommon(String eventCommon) {
		this.eventCommon = eventCommon;
	}
	public ackTypeEnum getEventAck() {
		switch (this.eventAck) {
			case 0:
				return ackTypeEnum.NOT_GOING;
			case 1:
				return ackTypeEnum.JOIN_IN;
			case 2:
				return ackTypeEnum.NONE;

			default:
				return ackTypeEnum.NONE;
			}
		
	}
	public void setEventAcks(int eventAcks) {
		this.eventAck = eventAcks;
	}

	public boolean isUpcoming() {
		return isUpcoming;
	}

	public void setIsUpcoming(boolean isUpcoming) {
		this.isUpcoming = isUpcoming;
	}

	public boolean isCurrentUserType()
	{
		return true;
	}
	
	public boolean isThisUser(String userProfileId)
	{
		if(userProfileId.equalsIgnoreCase(ApplicationSingleton.getInstance().getCurrentUser().getPaidInfo().getProfileId()))
			return true;
		else
			return false;
	}


	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setIsHoliday(String isHoliday){
		this.isHoliday = isHoliday;
	}

	public String getIsHoliday(){
		return isHoliday;
	}
}
