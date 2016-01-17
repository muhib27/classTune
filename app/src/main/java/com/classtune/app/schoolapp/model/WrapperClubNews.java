package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WrapperClubNews {


	private List<ClubNews> clubs;
	private String total;
	@SerializedName("has_next")
	private boolean hasnext;
	
	public List<ClubNews> getClubs() {
		return clubs;
	}
	public void setClubs(List<ClubNews> clubs) {
		this.clubs = clubs;
	}
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public boolean isHasnext() {
		return hasnext;
	}
	public void setHasnext(boolean hasnext) {
		this.hasnext = hasnext;
	}
	
	
}
