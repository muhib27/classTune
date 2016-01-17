package com.classtune.app.schoolapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StudentAttendance implements Parcelable, BaseType {

	public enum AttendanceStatus {
		ABSENT, PRESENT, LATE, ON_LEAVE, APPLIED_LEAVE
	}

	@SerializedName("student_id")
	private String id;
	@SerializedName("roll_no")
	private String rollNo;
	@SerializedName("student_name")
	private String studentName;
	@SerializedName("status")
	private int status;
	@SerializedName("reason")
	private String reason;
	@SerializedName("leave_id")
	private int leaveId;
	@SerializedName("leave_start_date")
	private String leaveStartDate;
	@SerializedName("leave_end_date")
	private String leaveEndDate;
	@SerializedName("created_at")
	private String createDate;
	@SerializedName("approved")
	private String approved;
	@SerializedName("batch")
	private String batch;
	@SerializedName("leave_type")
	private String leave_type;
	
	@SerializedName("leave_subject")
	private String leave_subject;
	
	

	public String getLeave_type() {
		return leave_type;
	}

	public void setLeave_type(String leave_type) {
		this.leave_type = leave_type;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public static Parcelable.Creator<StudentAttendance> getCreator() {
		return CREATOR;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public AttendanceStatus getStatus() {

		return AttendanceStatus.values()[status];
	}

	public int getTeacherLeaveStatus(){
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}

	public String getLeaveStartDate() {
		return leaveStartDate;
	}

	public void setLeaveStartDate(String leaveStartDate) {
		this.leaveStartDate = leaveStartDate;
	}

	public String getLeaveEndDate() {
		return leaveEndDate;
	}

	public void setLeaveEndDate(String leaveEndDate) {
		this.leaveEndDate = leaveEndDate;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(id);
		out.writeString(studentName);
		out.writeString(rollNo);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<StudentAttendance> CREATOR = new Parcelable.Creator<StudentAttendance>() {
		public StudentAttendance createFromParcel(Parcel in) {
			return new StudentAttendance(in);
		}

		public StudentAttendance[] newArray(int size) {
			return new StudentAttendance[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private StudentAttendance(Parcel in) {
		id = in.readString();
		studentName = in.readString();
		rollNo = in.readString();

	}

	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.TEACHER_STUDENT;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return studentName;
	}
	
	public String getLeave_subject() {
		return leave_subject;
	}

	public void setLeave_subject(String leave_subject) {
		this.leave_subject = leave_subject;
	}

}
