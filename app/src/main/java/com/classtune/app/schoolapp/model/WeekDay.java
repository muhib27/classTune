package com.classtune.app.schoolapp.model;

public class WeekDay implements BaseType{

	private String id;
	private String name;
	
	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.WEEK_DAYS;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return name;
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
