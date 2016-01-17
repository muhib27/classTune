/**
 * 
 */
package com.classtune.app.schoolapp.model;

import android.util.Log;

import com.classtune.app.schoolapp.utils.UserHelper.UserAccessType;
import com.classtune.app.schoolapp.utils.UserHelper.UserTypeEnum;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
	@SerializedName("user_id")
	private String userId;
	@SerializedName("full_name")
	private String fullName="";
	@SerializedName("first_name")
	private String firstName;
	@SerializedName("last_name")
	private String lastName;
	@SerializedName("middle_name")
	private String middleName;
	@SerializedName("nick_name")
	private String nickName;
	@SerializedName("medium")
	private String medium;
	@SerializedName("gender")
	private int gender;
	@SerializedName("tds_country_id")
	private String countryId;
	@SerializedName("district")
	private String city;
	@SerializedName("division")
	private String division;
	@SerializedName("grade_ids")
	private String gradeIds;
	@SerializedName("dob")
	private String dob;
	@SerializedName("mobile_no")
	private String mobileNum;
	@SerializedName("is_login")
	private boolean isLoggedIn;
	@SerializedName("email")
	private String email;
	@SerializedName("school_name")
	private String schoolName;
	@SerializedName("user_type")
	private String userTypeString;
	@SerializedName("occupation")
	private String occupation;
	@SerializedName("teaching_for")
	private String teachinFor;
	@SerializedName("profile_image")
	private String profilePicsUrl;
	@SerializedName("paid_user")
	private UserPaidInfo paidInfo;

	public String getIs_joined_spellbee() {
		return is_joined_spellbee;
	}

	public void setIs_joined_spellbee(String is_joined_spellbee) {
		this.is_joined_spellbee = is_joined_spellbee;
	}

	private String is_joined_spellbee="0";

	@SerializedName("user_schools")
	private List<School> joinedSchool;

	private boolean isJoinedToSchool;

	/*
	 * public String getUserTypeString() { return userTypeString; }
	 *
	 * public void setUserTypeString(String userTypeString) {
	 * this.userTypeString = userTypeString; }
	 */

	public String getUserTypeString() {
		return userTypeString;
	}

	public void setUserTypeString(String userTypeString) {
		this.userTypeString = userTypeString;
	}

	public List<School> getJoinedSchool() {
		return joinedSchool;
	}

	public void setJoinedSchool(List<School> joinedSchool) {
		this.joinedSchool = joinedSchool;
	}

	public boolean isJoinedToSchool() {
		return isJoinedToSchool;
	}

	public void setJoinedToSchool(boolean isJoinedToSchool) {
		this.isJoinedToSchool = isJoinedToSchool;
	}

	public UserPaidInfo getPaidInfo() {
		return paidInfo;
	}

	public void setPaidInfo(UserPaidInfo paidInfo) {
		this.paidInfo = paidInfo;
	}

	public String getProfilePicsUrl() {
		return profilePicsUrl;
	}

	public void setProfilePicsUrl(String profilePicsUrl) {
		this.profilePicsUrl = profilePicsUrl;
	}

	public String getTeachinFor() {
		return teachinFor;
	}

	public void setTeachinFor(String teachinFor) {
		this.teachinFor = teachinFor;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	private String fbId;
	private String gPlusId;

	private String username;
	private String password;
	// private int totalTerms;
	private String sessionID;
	private String udid;
	private UserTypeEnum type;
	private UserAccessType accessType;

	private ArrayList<UserPaidInfo> children;
	private UserPaidInfo selectedChild;

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public UserAccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(UserAccessType accessType) {
		this.accessType = accessType;
	}

	public String getgPlusId() {
		return gPlusId;
	}

	public void setgPlusId(String gPlusId) {
		this.gPlusId = gPlusId;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMedium() {
		if (medium == null)
			medium = "0";
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public int getGender() {
		return gender;
	}

	public String getGenderStr() {
		return gender == 1 ? "male" : "female";
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setGender(String gender) {
		this.gender = gender.equalsIgnoreCase("male") ? 1 : 2;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getGradeIds() {
		if (gradeIds == null)
			return "";
		else
			return gradeIds;
	}

	public void setGradeIds(String gradeIds) {
		this.gradeIds = gradeIds;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	/*
	 * public void setTotalTerms(int totalTerms) { this.totalTerms = totalTerms;
	 * }
	 * 
	 * public int getTotalTerms() { return totalTerms; }
	 */
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public UserTypeEnum getType() {
		return type;
	}

	public void setType(UserTypeEnum type) {
		this.type = type;
		Log.e("HERESOMETHING", this.type.toString());
	}

	public void setType() {
		if (userTypeString != null)
			this.type = UserTypeEnum.values()[Integer.parseInt(userTypeString)];
			Log.e("ENUMSTRING", this.type.toString());
	}

	public void setChildren(ArrayList<UserPaidInfo> children) {
		this.children = children;
	}

	public ArrayList<UserPaidInfo> getChildren() {
		return children;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setSelectedChild(UserPaidInfo selectedChild) {
		this.selectedChild = selectedChild;
	}

	public UserPaidInfo getSelectedChild() {
		return selectedChild;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}
}
