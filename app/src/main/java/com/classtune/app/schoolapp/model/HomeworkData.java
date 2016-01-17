/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class HomeworkData {
	@SerializedName("id")
	private String id;
	
	@SerializedName("teacher_id")
	private String teacherId;
	
	@SerializedName("teacher_name")
	private String teacherName;
	
	@SerializedName("subjects_id")
	private String subjectId;
	
	@SerializedName("subjects")
	private String subject;
	
	@SerializedName("assign_date")
	private String assign_date;
	
	@SerializedName("duedate")
	private String dueDate;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("is_done")
	private String isDone;
	
	@SerializedName("type")
	private String type;
	
	@SerializedName("content")
	private String content;
	
	@SerializedName("subjects_icon")
	private String subject_icon_name;
	
	@SerializedName("time_over")
	private int timeOver;
	
	@SerializedName("attachment_file_name")
	private String attachmentFileName;
	
	
	
	public String getSubject_icon_name() {
		return subject_icon_name;
	}
	public void setSubject_icon_name(String subject_icon_name) {
		this.subject_icon_name = subject_icon_name;
	}
	

	public String getAssign_date() {
		return assign_date;
	}
	public void setAssign_date(String assign_date) {
		this.assign_date = assign_date;
	}
	
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	public void setIsDone(String isDone) {
		this.isDone = isDone;
	}
	public String getIsDone() {
		return isDone;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubject() {
		return subject;
	}
	
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	

	public int getTimeOver() {
		return timeOver;
	}
	public void setTimeOver(int timeOver) {
		this.timeOver = timeOver;
	}
	
	public String getAttachmentFileName() {
		return attachmentFileName;
	}
	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}
}
