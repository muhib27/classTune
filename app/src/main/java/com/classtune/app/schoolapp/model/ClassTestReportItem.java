/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class ClassTestReportItem {
	@SerializedName("subject_name")
	private String subjectName;
	@SerializedName("subject_code")
	private String subjectCode;
	@SerializedName("subject_id")
	private String subjectID;
	@SerializedName("subject_icon")
	private String subjectIcon;
	@SerializedName("subject_exam")
	private CTReportSubjectExam ctReportSubjectExam;
	
	private boolean project = false;
	
	public void setCtReportSubjectExam(CTReportSubjectExam ctReportSubjectExam) {
		this.ctReportSubjectExam = ctReportSubjectExam;
	}
	public CTReportSubjectExam getCtReportSubjectExam() {
		return ctReportSubjectExam;
	}
	
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	
	public void setSubjectIcon(String subjectIcon) {
		this.subjectIcon = subjectIcon;
	}
	public String getSubjectIcon() {
		return subjectIcon;
	}
	
	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}
	public String getSubjectID() {
		return subjectID;
	}
	
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	
	public void setProject(boolean project) {
		this.project = project;
	}
	public boolean isProject() {
		return project;
	}
}
