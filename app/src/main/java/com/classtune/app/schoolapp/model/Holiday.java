package com.classtune.app.schoolapp.model;

import com.classtune.app.schoolapp.adapters.CalendarAdapter.CalenderEventType;
import com.google.gson.annotations.SerializedName;

public class Holiday implements CalenderEvent{
	@SerializedName("title")
	private String title;
	@SerializedName("description")
	private String desc;
	@SerializedName("start_date")
	private String startDate;
	@SerializedName("end_date")
	private String endDate;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public CalenderEventType getType() {
		// TODO Auto-generated method stub
		return CalenderEventType.HOLIDAY;
	}
	
	
	
}
