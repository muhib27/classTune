package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FreeUserWrapper {
	
	@SerializedName("user_info")
	private User user;
	@SerializedName("is_login")
	private boolean isLoggedIn;
	@SerializedName("countries")
	private ArrayList<String> countryList;
	@SerializedName("free_id")
	private String freeId;
	@SerializedName("user_type")
	private String userType;
	
	public void setCountryList(ArrayList<String> countryList) {
		this.countryList = countryList;
	}
	public ArrayList<String> getCountryList() {
		return countryList;
	}
	
	public void setFreeId(String freeId) {
		this.freeId = freeId;
	}
	public String getFreeId() {
		return freeId;
	}
	
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserType() {
		return userType;
	}
}
