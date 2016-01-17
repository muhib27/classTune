package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class AssessmentHistory {
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("mark")
	private String mark;
	
	@SerializedName("created_date")
	private String createdDate;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("topic")
	private String topic;
	
	@SerializedName("total")
	private int total;
	
	@SerializedName("pid")
	private String pid;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
