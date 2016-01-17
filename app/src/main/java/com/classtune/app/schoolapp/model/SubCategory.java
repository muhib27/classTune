package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class SubCategory {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;
	
	private boolean checked=true;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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

}
