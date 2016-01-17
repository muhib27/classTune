package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class MeetingStatus {
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("batch")
	private String batch;

	@SerializedName("id")
	private String id;
	
	@SerializedName("date")
	private String date;
	
	@SerializedName("status")
	private String status;
	
	@SerializedName("timeover")
	private int timeOver;

	@SerializedName("show_for")
	private int showFor;
	
	@SerializedName("type")
	private int type;
	
	@SerializedName("description")
	private String description;

	


	public String getName() {
		return name;
	}

	
	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getTimeOver() {
		return timeOver;
	}

	public void setTimeOver(int timeOver) {
		this.timeOver = timeOver;
	}
	

	public int getShowFor() {
		return showFor;
	}


	public void setShowFor(int showFor) {
		this.showFor = showFor;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

}
