package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class AcademicCalendarDataItem {
	
	@SerializedName("event_id")
	private String eventId;
	@SerializedName("event_title")
	private String eventTitle;
	@SerializedName("event_start_date")
	private String eventDate;
	@SerializedName("event_description")
	private String eventDescription;
	
	
	@SerializedName("event_category_name")
	private String eventCategoryName;
	
	@SerializedName("event_end_date")
	private String eventEndDate;
	
	
	
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	
	public String getEventCategoryName() {
		return eventCategoryName;
	}
	public void setEventCategoryName(String eventCategoryName) {
		this.eventCategoryName = eventCategoryName;
	}
	public String getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

}
