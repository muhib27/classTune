package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class TeacherHomework {

	@SerializedName("subjects")
	private String subjects;
	
	@SerializedName("batch")
	private String batch;
	
	@SerializedName("attachment_file_name")
	private String attachment_file_name;

	@SerializedName("course")
	private String course;

	public String getSection() {
		return section;
	}

	@SerializedName("section")
	private String section;
	
	@SerializedName("subjects_id")
	private String subjects_id;
	
	@SerializedName("subjects_icon")
	private String subjects_icon;
	
	@SerializedName("assign_date")
	private String assign_date;
	
	@SerializedName("duedate")
	private String duedate;
	
	@SerializedName("name")
	private String homework_name;
	
	@SerializedName("content")
	private String content;
	
	@SerializedName("type")
	private String homework_type;
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("done")
	private String done;

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getAttachment_file_name() {
		return attachment_file_name;
	}

	public void setAttachment_file_name(String attachment_file_name) {
		this.attachment_file_name = attachment_file_name;
	}
	
	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getSubjects_id() {
		return subjects_id;
	}

	public void setSubjects_id(String subjects_id) {
		this.subjects_id = subjects_id;
	}

	public String getSubjects_icon() {
		return subjects_icon;
	}

	public void setSubjects_icon(String subjects_icon) {
		this.subjects_icon = subjects_icon;
	}
	
	public String getAssign_date() {
		return assign_date;
	}

	public void setAssign_date(String assign_date) {
		this.assign_date = assign_date;
	}

	public String getDuedate() {
		return duedate;
	}
	

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public String getHomework_name() {
		return homework_name;
	}

	public void setHomework_name(String homework_name) {
		this.homework_name = homework_name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHomework_type() {
		return homework_type;
	}

	public void setHomework_type(String homework_type) {
		this.homework_type = homework_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDone() {
		return done;
	}

	public void setDone(String done) {
		this.done = done;
	}

}
