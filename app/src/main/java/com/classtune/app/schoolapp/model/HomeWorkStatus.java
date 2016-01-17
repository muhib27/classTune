package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class HomeWorkStatus {
	
	@SerializedName("student_name")
	private String student_name;
	
	@SerializedName("student_roll")
	private String student_roll;
	
	@SerializedName("home_work_status")
	private String home_work_status;
	
	

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getStudent_roll() {
		return student_roll;
	}

	public void setStudent_roll(String student_roll) {
		this.student_roll = student_roll;
	}

	public String getHome_work_status() {
		return home_work_status;
	}

	public void setHome_work_status(String home_work_status) {
		this.home_work_status = home_work_status;
	}

}
