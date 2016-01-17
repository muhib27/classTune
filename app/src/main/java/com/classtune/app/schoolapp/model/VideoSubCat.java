package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class VideoSubCat implements BaseType{

	
	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;
	
	
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
	
	
	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.VIDEO_CATEGORY_TYPE;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return name;
	}

}
