package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class ExamRoutine {

	
	@SerializedName("exam_subject_id")
	private String exam_subject_id;
	@SerializedName("exam_subject_name")
	private String exam_subject_name;
	@SerializedName("exam_start_time")
	private String exam_start_time;
	@SerializedName("exam_end_time")
	private String exam_end_time;
	@SerializedName("exam_date")
	private String exam_date;
	@SerializedName("exam_day")
	private String exam_day;
	@SerializedName("name")
	private String name;
	@SerializedName("id")
	private String id;
	@SerializedName("no_exams")
	private String noExams;
	
	public String getName() {
		return name;
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

	public String getExam_day() {
		return exam_day;
	}

	public void setExam_day(String exam_day) {
		this.exam_day = exam_day;
	}

	public String getExam_subject_id() {
		return exam_subject_id;
	}

	public void setExam_subject_id(String exam_subject_id) {
		this.exam_subject_id = exam_subject_id;
	}

	public String getExam_subject_name() {
		return exam_subject_name;
	}

	public void setExam_subject_name(String exam_subject_name) {
		this.exam_subject_name = exam_subject_name;
	}

	public String getExam_start_time() {
		return exam_start_time;
	}

	public void setExam_start_time(String exam_start_time) {
		this.exam_start_time = exam_start_time;
	}

	public String getExam_end_time() {
		return exam_end_time;
	}

	public void setExam_end_time(String exam_end_time) {
		this.exam_end_time = exam_end_time;
	}

	public String getExam_date() {
		return exam_date;
	}

	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}

	public String getNoExams() {
		return noExams;
	}

	public void setNoExams(String noExams) {
		this.noExams = noExams;
	}
}
