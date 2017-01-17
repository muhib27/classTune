package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class TeacherHomeworkData {
	
	@SerializedName("subjects")
	private String subjects;

	@SerializedName("batch")
	private String batch;
	
	@SerializedName("attachment_file_name")
	private String attachment_file_name;
	
	@SerializedName("course")
	private String course;

	@SerializedName("section")
	private String section;

	public String getSection() {
		return section;
	}

	@SerializedName("subjects_id")
	private String subjects_id;
	
	@SerializedName("subjects_icon")
	private String subjects_icon;
	
	@SerializedName("assign_date")
	private String assign_date;
	
	@SerializedName("duedate")
	private String duedate;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("content")
	private String content;
	
	@SerializedName("type")
	private String type;
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("done")
	private String done;

	@SerializedName("is_editable")
	private boolean isEditable;


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
