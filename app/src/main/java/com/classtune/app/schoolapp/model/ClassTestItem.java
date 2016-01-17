/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class ClassTestItem {
	@SerializedName("exam_id")
	private String examId;
	@SerializedName("exam_name")
	private String examName;
	@SerializedName("exam_date")
	private String examDate;
	@SerializedName("your_grade")
	private String grade;
	@SerializedName("grade_point")
	private String gradePoint;
	@SerializedName("your_mark")
	private String mark;
	@SerializedName("your_percent")
	private String percentage;
	@SerializedName("topic")
	private String topic;
	@SerializedName("max_mark")
	private String maxMark;
	@SerializedName("category")
	private String category;
	@SerializedName("total_mark")
	private String totalMark;
	
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	
	public String getExamDate() {
		return examDate;
	}
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}
	
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public String getGradePoint() {
		return gradePoint;
	}
	public void setGradePoint(String gradePoint) {
		this.gradePoint = gradePoint;
	}
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public String getMaxMark() {
		return maxMark;
	}
	public void setMaxMark(String maxMark) {
		this.maxMark = maxMark;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}
}
