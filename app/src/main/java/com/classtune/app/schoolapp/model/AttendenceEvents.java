package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendenceEvents {
	@SerializedName("absent")
	private List<Absent> absents;
	@SerializedName("late")
	private List<Late> lates;
	@SerializedName("holiday")
	private List<Holiday> holidays;
	@SerializedName("leave")
	private List<Leave> leaves;
	@SerializedName("total")
	private int totalClass;
	@SerializedName("current_msg")
	private String current_msg;
	@SerializedName("current_date")
	private String current_date;
	
	public String getCurrent_msg() {
		return current_msg;
	}
	public void setCurrent_msg(String current_msg) {
		this.current_msg = current_msg;
	}
	public String getCurrent_date() {
		return current_date;
	}
	public void setCurrent_date(String current_date) {
		this.current_date = current_date;
	}
	public int getTotalClass() {
		return totalClass;
	}
	public void setTotalClass(int totalClass) {
		this.totalClass = totalClass;
	}
	public List<Absent> getAbsents() {
		return absents;
	}
	public void setAbsents(List<Absent> absents) {
		this.absents = absents;
	}
	public List<Late> getLates() {
		return lates;
	}
	public void setLates(List<Late> lates) {
		this.lates = lates;
	}
	public List<Holiday> getHolidays() {
		return holidays;
	}
	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}
	public List<Leave> getLeaves() {
		return leaves;
	}
	public void setLeaves(List<Leave> leaves) {
		this.leaves = leaves;
	}
	
	
}
