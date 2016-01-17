package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Comment {

	@SerializedName("id")
	private String id;
	@SerializedName("title")
	private String title;
	@SerializedName("details")
	private String details;
	@SerializedName("created_date")
	private String created_date;
	@SerializedName("username")
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
}
