package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class NoticeAcknowledge {

	@SerializedName("notice_id")
	private String notice_id;
	@SerializedName("acknowledge_status")
	private String acknowledge_status;
	@SerializedName("acknowledge_msg")
	private String acknowledge_msg;

	public String getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}

	public String getAcknowledge_status() {
		return acknowledge_status;
	}

	public void setAcknowledge_status(String acknowledge_status) {
		this.acknowledge_status = acknowledge_status;
	}

	public String getAcknowledge_msg() {
		return acknowledge_msg;
	}

	public void setAcknowledge_msg(String acknowledge_msg) {
		this.acknowledge_msg = acknowledge_msg;
	}

}
