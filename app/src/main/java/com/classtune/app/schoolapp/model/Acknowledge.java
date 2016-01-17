package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Acknowledge {
	@SerializedName("acknowledge_status")
	private String acknowledge_status;
	@SerializedName("acknowledged_by")
	private String acknowledged_by;
	@SerializedName("acknowledged_by_id")
	private String acknowledged_by_id;
	@SerializedName("acknowledge_msg")
	private String acknowledge_msg;
	
	public String getAcknowledge_status() {
		return acknowledge_status;
	}
	public void setAcknowledge_status(String acknowledge_status) {
		this.acknowledge_status = acknowledge_status;
	}
	public String getAcknowledged_by() {
		return acknowledged_by;
	}
	public void setAcknowledged_by(String acknowledged_by) {
		this.acknowledged_by = acknowledged_by;
	}
	public String getAcknowledged_by_id() {
		return acknowledged_by_id;
	}
	public void setAcknowledged_by_id(String acknowledged_by_id) {
		this.acknowledged_by_id = acknowledged_by_id;
	}
	public String getAcknowledge_msg() {
		return acknowledge_msg;
	}
	public void setAcknowledge_msg(String acknowledge_msg) {
		this.acknowledge_msg = acknowledge_msg;
	}
	
}
