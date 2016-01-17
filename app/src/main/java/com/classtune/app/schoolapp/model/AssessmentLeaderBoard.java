package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class AssessmentLeaderBoard {
	
	@SerializedName("user_name")
	private String userName;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("mark")
	private String mark;
	
	@SerializedName("time_taken")
	private String timeTaken;
	
	@SerializedName("profile_image")
	private String profileImage;
	

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}
	

}
