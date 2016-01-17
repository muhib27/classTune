package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Batch implements BaseType{

    public Batch(String id, String name){
        this.id = id;
        this.name = name;
    }
	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;
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
		return PickerType.TEACHER_BATCH;
	}
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	
	
}
