package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Period {

	@SerializedName("teacher_first_name")
	private String teacher_first_name;
	@SerializedName("teacher_full_name")
	private String teacher_full_name;
	@SerializedName("subject_code")
	private String subject_code;
	@SerializedName("subject_name")
	private String subject_name;
	@SerializedName("class_start_time")
	private String class_start_time;
	@SerializedName("class_end_time")
	private String class_end_time;
	@SerializedName("weekday_id")
	private String weekday_id;
	@SerializedName("weekday_text")
	private String weekday_text;
	@SerializedName("subject_icon_name")
	private String subject_icon_name;
	@SerializedName("subject_icon_path")
	private String subject_icon_path;

	public String getSubject_icon_name() {
		return subject_icon_name;
	}

	public void setSubject_icon_name(String subject_icon_name) {
		this.subject_icon_name = subject_icon_name;
	}

	public String getSubject_icon_path() {
		return subject_icon_path;
	}

	public void setSubject_icon_path(String subject_icon_path) {
		this.subject_icon_path = subject_icon_path;
	}

	public String getTeacher_first_name() {
		return teacher_first_name;
	}

	public void setTeacher_first_name(String teacher_first_name) {
		this.teacher_first_name = teacher_first_name;
	}

	public String getTeacher_full_name() {
		return teacher_full_name;
	}

	public void setTeacher_full_name(String teacher_full_name) {
		this.teacher_full_name = teacher_full_name;
	}

	public String getSubject_code() {
		return subject_code;
	}

	public void setSubject_code(String subject_code) {
		this.subject_code = subject_code;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getClass_start_time() {
		return class_start_time;
	}

	public void setClass_start_time(String class_start_time) {
		this.class_start_time = class_start_time;
	}

	public String getClass_end_time() {
		return class_end_time;
	}

	public void setClass_end_time(String class_end_time) {
		this.class_end_time = class_end_time;
	}

	public String getWeekday_id() {
		return weekday_id;
	}

	public void setWeekday_id(String weekday_id) {
		this.weekday_id = weekday_id;
	}

	public String getWeekday_text() {
		return weekday_text;
	}

	public void setWeekday_text(String weekday_text) {
		this.weekday_text = weekday_text;
	}

}
