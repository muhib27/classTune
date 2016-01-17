package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class SchoolJoinAdditionalInfo {
	@SerializedName("batch")
	private String batch="";
	@SerializedName("user_section")
	private String userSection="";
	@SerializedName("employee_id")
	private String employeeId="";
	@SerializedName("roll_no")
	private String rollNo="";
	@SerializedName("student_id")
	private String studentId="";
	@SerializedName("contact_no")
	private String contactNo="";
	
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getUserSection() {
		return userSection;
	}
	public void setUserSection(String userSection) {
		this.userSection = userSection;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
	
}
