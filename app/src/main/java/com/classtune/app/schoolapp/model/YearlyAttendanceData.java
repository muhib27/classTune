package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class YearlyAttendanceData {

	@SerializedName("total")
	private int totalClass;
	@SerializedName("absent")
	private int absent;
	@SerializedName("late")
	private int late;
	@SerializedName("leave")
	private int leave;
	@SerializedName("current_date")
	private String uptoDate;
	
	public int getTotalClass() {
		return totalClass;
	}
	public void setTotalClass(int totalClass) {
		this.totalClass = totalClass;
	}
	public float getPresent() {
		return (float) ((float)totalClass-((float)absent+(float)leave+((float)late*0.5)));
	}
	public int getAbsent() {
		return absent;
	}
	public void setAbsent(int absent) {
		this.absent = absent;
	}
	public int getLate() {
		return late;
	}
	public void setLate(int late) {
		this.late = late;
	}
	public int getLeave() {
		return leave;
	}
	public void setLeave(int leave) {
		this.leave = leave;
	}
	public String getUptoDate() {
		return uptoDate;
	}
	public void setUptoDate(String uptoDate) {
		this.uptoDate = uptoDate;
	}
	
	
	
}
