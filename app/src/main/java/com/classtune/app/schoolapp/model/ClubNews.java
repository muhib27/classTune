package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class ClubNews {

	public enum clubAckTypeEnum{
		
		APPLIED,JOINED,NOT_APPLIED
	}
	
	@SerializedName("club_id")
	private String clubId;
	@SerializedName("club_activity")
	private String clubActivity;
	@SerializedName("club_category_id")
	private String clubCategoryId;
	@SerializedName("club_category_name")
	private String clubCategoryName;
	@SerializedName("club_schedule")
	private String clubSchedule;
	@SerializedName("club_fees")
	private int clubFees;
	@SerializedName("club_common")
	private String clubCommon;
	@SerializedName("club_acknowledge")
	private int clubAcknowledge;
	
	
	public ClubNews() {
		super();
	}
	public String getClubId() {
		return clubId;
	}
	public void setClubId(String clubId) {
		this.clubId = clubId;
	}
	public String getClubActivity() {
		return clubActivity;
	}
	public void setClubActivity(String clubActivity) {
		this.clubActivity = clubActivity;
	}
	public String getClubCategoryId() {
		return clubCategoryId;
	}
	public void setClubCategoryId(String clubCategoryId) {
		this.clubCategoryId = clubCategoryId;
	}
	public String getClubCategoryName() {
		return clubCategoryName;
	}
	public void setClubCategoryName(String clubCategoryName) {
		this.clubCategoryName = clubCategoryName;
	}
	public String getClubSchedule() {
		return clubSchedule;
	}
	public void setClubSchedule(String clubSchedule) {
		this.clubSchedule = clubSchedule;
	}
	public int getClubFees() {
		return clubFees;
	}
	public void setClubFees(int clubFees) {
		this.clubFees = clubFees;
	}
	public String getClubCommon() {
		return clubCommon;
	}
	public void setClubCommon(String clubCommon) {
		this.clubCommon = clubCommon;
	}
	public clubAckTypeEnum getClubAck() {
		switch (this.clubAcknowledge) {
			case 0:
				return clubAckTypeEnum.APPLIED;
			case 1:
				return clubAckTypeEnum.JOINED;
			case 2:
				return clubAckTypeEnum.NOT_APPLIED;

			default:
				return clubAckTypeEnum.NOT_APPLIED;
			}
		
	}
	public void setClubAcknowledge(int clubAcknowledge) {
		this.clubAcknowledge = clubAcknowledge;
	}
	
	
	
	
	
}
