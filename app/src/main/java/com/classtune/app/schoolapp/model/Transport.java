package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Transport {
	
	@SerializedName("transport_weekday_id")
	private String weekDayId;
	
	@SerializedName("transport_weekday")
	private String weekDayName;
	
	@SerializedName("transport_home_pickup")
	private String homePickTime;
	
	@SerializedName("transport_school_pickup")
	private String schoolPickTime;
	
	
	
	public String getWeekDayId()
	{
		return weekDayId;
	}

	public void setWeekDayId(String weekDayId)
	{
		this.weekDayId = weekDayId;
	}

	public String getWeekDayName()
	{
		return weekDayName;
	}

	public void setWeekDayName(String weekDayName)
	{
		this.weekDayName = weekDayName;
	}

	public String getHomePickTime()
	{
		return homePickTime;
	}

	public void setHomePickTime(String homePickTime)
	{
		this.homePickTime = homePickTime;
	}

	public String getSchoolPickTime()
	{
		return schoolPickTime;
	}

	public void setSchoolPickTime(String schoolPickTime)
	{
		this.schoolPickTime = schoolPickTime;
	}

}
