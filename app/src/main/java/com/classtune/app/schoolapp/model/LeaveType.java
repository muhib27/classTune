package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class LeaveType implements BaseType{

	@SerializedName("type")
	private String leaveType;
	
	@SerializedName("id")
	private String id;
	
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.LEAVE;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return leaveType;
	}
	
	
}
