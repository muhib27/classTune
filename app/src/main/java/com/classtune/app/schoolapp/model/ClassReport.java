package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassReport {
	
	@SerializedName("total")
	private int totalStudentCount;
	@SerializedName("present")
	private int presentStudentCount;
	@SerializedName("absent")
	private int absentStudentCount;
	@SerializedName("late")
	private int lateStudentCount;
	@SerializedName("leave")
	private int leaveStudentCount;
	@SerializedName("current_date")
	private String currentDate;
	@SerializedName("student")
	private Student student;
	
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public int getTotalStudentCount() {
		return totalStudentCount;
	}
	public void setTotalStudentCount(int totalStudentCount) {
		this.totalStudentCount = totalStudentCount;
	}
	public int getPresentStudentCount() {
		return presentStudentCount;
	}
	public void setPresentStudentCount(int presentStudentCount) {
		this.presentStudentCount = presentStudentCount;
	}
	public int getAbsentStudentCount() {
		return absentStudentCount;
	}
	public void setAbsentStudentCount(int absentStudentCount) {
		this.absentStudentCount = absentStudentCount;
	}
	public int getLateStudentCount() {
		return lateStudentCount;
	}
	public void setLateStudentCount(int lateStudentCount) {
		this.lateStudentCount = lateStudentCount;
	}
	public int getLeaveStudentCount() {
		return leaveStudentCount;
	}
	public void setLeaveStudentCount(int leaveStudentCount) {
		this.leaveStudentCount = leaveStudentCount;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	
	public class Student
	{
		@SerializedName("present")
		private List<StudentAttendance> presentStudents;
		@SerializedName("absent")
		private List<StudentAttendance> absentStudents;
		@SerializedName("late")
		private List<StudentAttendance> lateStudents;
		@SerializedName("leave")
		private List<StudentAttendance> leaveStudents;
		public List<StudentAttendance> getPresentStudents() {
			return presentStudents;
		}
		public void setPresentStudents(List<StudentAttendance> presentStudents) {
			this.presentStudents = presentStudents;
		}
		public List<StudentAttendance> getAbsentStudents() {
			return absentStudents;
		}
		public void setAbsentStudents(List<StudentAttendance> absentStudents) {
			this.absentStudents = absentStudents;
		}
		public List<StudentAttendance> getLateStudents() {
			return lateStudents;
		}
		public void setLateStudents(List<StudentAttendance> lateStudents) {
			this.lateStudents = lateStudents;
		}
		public List<StudentAttendance> getLeaveStudents() {
			return leaveStudents;
		}
		public void setLeaveStudents(List<StudentAttendance> leaveStudents) {
			this.leaveStudents = leaveStudents;
		}
		
	}
	
}
