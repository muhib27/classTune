package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Student {
	
	@SerializedName("name")
	private String studentName;
	@SerializedName("roll")
	private String rollNo;
	@SerializedName("admission_no")
	private String admissionNo;
	@SerializedName("date_of_birth")
	private String dob;
	@SerializedName("gender")
	private String gender;
	@SerializedName("class")
	private String studentClass;
	@SerializedName("batch")
	private String batchName;
	@SerializedName("user_image")
	private String imageUrl;
	@SerializedName("guradian")
	private String gaurdian;
	
	@SerializedName("contact")
	private String contact;
	
	
	@SerializedName("phone")
	private String phone;
	
	
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getAdmissionNo() {
		return admissionNo;
	}
	public void setAdmissionNo(String admissionNo) {
		this.admissionNo = admissionNo;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getGaurdian() {
		return gaurdian;
	}
	public void setGaurdian(String gaurdian) {
		this.gaurdian = gaurdian;
	}
	
	
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
