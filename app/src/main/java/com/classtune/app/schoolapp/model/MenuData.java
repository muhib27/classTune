package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class MenuData implements BaseType{

	public MenuData(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String title;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.CANDLE_CATEGORY;
	}
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.title;
	}
	
	
}
