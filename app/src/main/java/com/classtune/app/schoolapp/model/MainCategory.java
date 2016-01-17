package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainCategory {
	@SerializedName("icon_url")
	private String icon_url;
	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;
	@SerializedName("sub_categories")
	private ArrayList<SubCategory> sub_categories;

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SubCategory> getSub_categories() {
		return sub_categories;
	}

	public void setSub_categories(ArrayList<SubCategory> sub_categories) {
		this.sub_categories = sub_categories;
	}
}
