package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class ShareLink {

	@SerializedName("link")
	private String link;
	@SerializedName("use_link")
	private int use_link;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getUse_link() {
		return use_link;
	}

	public void setUse_link(int use_link) {
		this.use_link = use_link;
	}
}
