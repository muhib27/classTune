package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class School {

	@SerializedName("school_id")
	private String school_id;
	@SerializedName("status")
	private String status;
	@SerializedName("type")
	private String type;

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
