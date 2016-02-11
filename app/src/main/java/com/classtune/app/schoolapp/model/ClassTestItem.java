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


	@SerializedName("remark")
	private String remarks;

	@SerializedName("max_mark_percent")
	private float maxMarkPercent;

	@SerializedName("avg_mark")
	private float averageMark;

	@SerializedName("avg_mark_percent")
	private float averageMarkPercent;

	@SerializedName("percentile")
	private float percentile;

	@SerializedName("subject_id")
	private String subjectId;

	@SerializedName("subject_name")
	private String subjectName;

	@SerializedName("subject_icon")
	private String subjectIcon;
	
	
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


	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public float getMaxMarkPercent() {
		return maxMarkPercent;
	}

	public void setMaxMarkPercent(float maxMarkPercent) {
		this.maxMarkPercent = maxMarkPercent;
	}

	public float getAverageMark() {
		return averageMark;
	}

	public void setAverageMark(float averageMark) {
		this.averageMark = averageMark;
	}

	public float getAverageMarkPercent() {
		return averageMarkPercent;
	}

	public void setAverageMarkPercent(float averageMarkPercent) {
		this.averageMarkPercent = averageMarkPercent;
	}

	public float getPercentile() {
		return percentile;
	}

	public void setPercentile(float percentile) {
		this.percentile = percentile;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectIcon() {
		return subjectIcon;
	}

	public void setSubjectIcon(String subjectIcon) {
		this.subjectIcon = subjectIcon;
	}
}
