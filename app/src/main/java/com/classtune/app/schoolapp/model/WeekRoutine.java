package com.classtune.app.schoolapp.model;

import java.util.ArrayList;

public class WeekRoutine {

	public WeekRoutine(String time, ArrayList<Period> periodList) {
		this.time = time;
		this.periodList = periodList;
	}
	private String time;
	private ArrayList<Period> periodList;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public ArrayList<Period> getPeriodList() {
		return periodList;
	}
	public void setPeriodList(ArrayList<Period> periodList) {
		this.periodList = periodList;
	}
}
