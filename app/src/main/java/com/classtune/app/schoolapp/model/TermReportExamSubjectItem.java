/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class TermReportExamSubjectItem {
	@SerializedName("subject_name")
	private String subjctName;
	@SerializedName("subject_code")
	private String subjctCode;
	@SerializedName("subject_id")
	private String subjctId;
	@SerializedName("subject_icon")
	private String subjctIcon;
	@SerializedName("your_grade")
	private String grade;
	@SerializedName("your_mark")
	private String mark;
	@SerializedName("your_percent")
	private String percent;
	@SerializedName("credit_points")
	private String creditPoints;
	@SerializedName("remarks")
	private String remarks;
	@SerializedName("max_mark")
	private String maxMark;
	@SerializedName("total_mark")
	private String totalMark;
	
	
	public String getSubjctName() {
		return subjctName;
	}
	public void setSubjctName(String subjctName) {
		this.subjctName = subjctName;
	}
	
	public String getSubjctCode() {
		return subjctCode;
	}
	public void setSubjctCode(String subjctCode) {
		this.subjctCode = subjctCode;
	}
	
	public String getSubjctId() {
		return subjctId;
	}
	public void setSubjctId(String subjctId) {
		this.subjctId = subjctId;
	}
	
	public String getSubjctIcon() {
		return subjctIcon;
	}
	public void setSubjctIcon(String subjctIcon) {
		this.subjctIcon = subjctIcon;
	}
	
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	
	public String getCreditPoints() {
		return creditPoints;
	}
	public void setCreditPoints(String creditPoints) {
		this.creditPoints = creditPoints;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getMaxMark() {
		return maxMark;
	}
	public void setMaxMark(String maxMark) {
		this.maxMark = maxMark;
	}
	
	public String getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}
}
