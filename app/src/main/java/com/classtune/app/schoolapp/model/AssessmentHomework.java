package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class AssessmentHomework {
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("start_date")
	private String startDate;
	
	@SerializedName("end_date")
	private String endDate;
	
	@SerializedName("maximum_time")
	private String maximumTime;
	
	@SerializedName("pass_percentage")
	private String passPercentage;
	
	@SerializedName("timeover")
	private int timeover;
	
	@SerializedName("examGiven")
	private int examGiven;

	@SerializedName("not_started")
	private int notStarted;
	
	

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getMaximumTime() {
		return maximumTime;
	}

	public void setMaximumTime(String maximumTime) {
		this.maximumTime = maximumTime;
	}

	public String getPassPercentage() {
		return passPercentage;
	}

	public void setPassPercentage(String passPercentage) {
		this.passPercentage = passPercentage;
	}
	
	public int getTimeover() {
		return timeover;
	}

	public void setTimeover(int timeover) {
		this.timeover = timeover;
	}

	public int getExamGiven() {
		return examGiven;
	}

	public void setExamGiven(int examGiven) {
		this.examGiven = examGiven;
	}

	public int getNotStarted() {
		return notStarted;
	}

	public void setNotStarted(int notStarted) {
		this.notStarted = notStarted;
	}
}
