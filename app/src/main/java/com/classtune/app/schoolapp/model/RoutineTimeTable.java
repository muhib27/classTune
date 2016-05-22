package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class RoutineTimeTable {

	@SerializedName("batch_name")
	private String batchName;
	@SerializedName("course_name")
	private String className;
	@SerializedName("subject_code")
	private String subjectCode;
	@SerializedName("subject_name")
	private String subjectName;
	@SerializedName("subject_icon_name")
	private String subjectIconName;
	@SerializedName("subject_icon_path")
	private String subjectIconPath;
	@SerializedName("class_start_time")
	private String classStartTime;
	@SerializedName("class_end_time")
	private String classEndTime;
	@SerializedName("weekday_id")
	private String weekDayId;
	@SerializedName("weekday_text")
	private String weekDayText;
	@SerializedName("teacher_full_name")
	private String teacher_full_name;
	@SerializedName("teacher_short_code")
	private String teacherShortCode;
	@SerializedName("period_name")
	private String periodName;
	
	public String getTeacher_full_name() {
		return teacher_full_name;
	}
	public void setTeacher_full_name(String teacher_full_name) {
		this.teacher_full_name = teacher_full_name;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectIconName() {
		return subjectIconName;
	}
	public void setSubjectIconName(String subjectIconName) {
		this.subjectIconName = subjectIconName;
	}
	public String getSubjectIconPath() {
		return subjectIconPath;
	}
	public void setSubjectIconPath(String subjectIconPath) {
		this.subjectIconPath = subjectIconPath;
	}
	public String getClassStartTime() {
		return classStartTime;
	}
	public void setClassStartTime(String classStartTime) {
		this.classStartTime = classStartTime;
	}
	public String getClassEndTime() {
		return classEndTime;
	}
	public void setClassEndTime(String classEndTime) {
		this.classEndTime = classEndTime;
	}
	public String getWeekDayId() {
		return weekDayId;
	}
	public void setWeekDayId(String weekDayId) {
		this.weekDayId = weekDayId;
	}
	public String getWeekDayText() {
		return weekDayText;
	}
	public void setWeekDayText(String weekDayText) {
		this.weekDayText = weekDayText;
	}

	public String getTeacherShortCode() {
		return teacherShortCode;
	}

	public void setTeacherShortCode(String teacherShortCode) {
		this.teacherShortCode = teacherShortCode;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
}
