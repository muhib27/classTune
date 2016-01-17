package com.classtune.app.schoolapp.model;

public class City implements BaseType{
	private String name;
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.CITY;
	}
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	
}
