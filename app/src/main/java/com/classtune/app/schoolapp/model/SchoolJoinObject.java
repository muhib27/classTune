package com.classtune.app.schoolapp.model;

import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;

public class SchoolJoinObject {

	private UserTypeEnum type;
	private String gradeIDs="";
	private SchoolJoinAdditionalInfo additionalInfo;
	
	public UserTypeEnum getType() {
		return type;
	}
	public void setType(UserTypeEnum type) {
		this.type = type;
	}
	public String getGradeIDs() {
		return gradeIDs;
	}
	public void setGradeIDs(String gradeIDs) {
		this.gradeIDs = gradeIDs;
	}
	public SchoolJoinAdditionalInfo getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(SchoolJoinAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public String toString()
	{
		return gradeIDs;
	}
	
	
}
