package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Syllabus {

	@SerializedName("id")
	private String id;
	@SerializedName("subject_id")
	private String subject_id;
	@SerializedName("subject_name")
	private String subject_name;
	@SerializedName("subject_icon_name")
	private String subject_icon;
	@SerializedName("syllabus_text")
	private String syllabus_text;
	@SerializedName("last_updated")
	private String last_updated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getSubject_icon() {
		return subject_icon;
	}

	public void setSubject_icon(String subject_icon) {
		this.subject_icon = subject_icon;
	}

	public String getSyllabus_text() {
		return syllabus_text;
	}

	public void setSyllabus_text(String syllabus_text) {
		this.syllabus_text = syllabus_text;
	}

	public String getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(String last_updated) {
		this.last_updated = last_updated;
	}

}
