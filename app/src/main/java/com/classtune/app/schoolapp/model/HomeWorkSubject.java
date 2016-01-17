package com.classtune.app.schoolapp.model;

public class HomeWorkSubject implements BaseType{
	
	
	private String name;
	private String id;
	
	
	public HomeWorkSubject(String name, String id)
	{
		this.name = name;
		this.id = id;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.HOMEWORK_SUBJECT;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
