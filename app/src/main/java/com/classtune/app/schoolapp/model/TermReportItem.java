/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Amit
 *
 */
public class TermReportItem {
	@SerializedName("exam_id")
	private String examId;
	@SerializedName("exam_name")
	private String examName;
	@SerializedName("acknowledge")
	private boolean acknowledge;
	@SerializedName("exam_subjects")
	private ArrayList<TermReportExamSubjectItem> examSubjectList;
	@SerializedName("GPA")
	private String gpa;
	@SerializedName("total_mark")
	private String totalMark;
	@SerializedName("total_grade_point")
	private String totalGradePoint;
	@SerializedName("total_student")
	private String totalStudent;
	@SerializedName("Your_position")
	private String position;
	@SerializedName("grade")
	private String grade;
	
	
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
	
	public boolean isAcknowledge() {
		return acknowledge;
	}
	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}
	
	public ArrayList<TermReportExamSubjectItem> getExamSubjectList() {
		return examSubjectList;
	}
	public void setExamSubjectList(
			ArrayList<TermReportExamSubjectItem> examSubjectList) {
		this.examSubjectList = examSubjectList;
	}
	
	public String getGpa() {
		return gpa;
	}
	public void setGpa(String gpa) {
		this.gpa = gpa;
	}
	
	public String getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}
	
	public String getTotalGradePoint() {
		return totalGradePoint;
	}
	public void setTotalGradePoint(String totalGradePoint) {
		this.totalGradePoint = totalGradePoint;
	}
	
	public String getTotalStudent() {
		return totalStudent;
	}
	public void setTotalStudent(String totalStudent) {
		this.totalStudent = totalStudent;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGrade() {
		return grade;
	}
}
