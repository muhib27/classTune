package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchoolEventWrapper {

	private List<SchoolEvent> events;
	private String total;
	@SerializedName("has_next")
	private boolean hasnext;
	
	public List<SchoolEvent> getEvents() {
		return events;
	}
	public void setEvents(List<SchoolEvent> events) {
		this.events = events;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public boolean isHasnext() {
		return hasnext;
	}
	public void setHasnext(boolean hasnext) {
		this.hasnext = hasnext;
	}
	
	
}
