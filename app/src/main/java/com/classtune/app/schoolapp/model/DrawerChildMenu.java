package com.classtune.app.schoolapp.model;

public class DrawerChildMenu implements DrawerChildBase{

	private String text;
	private String id;
	private String imageName;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	@Override
	public CHILD_TYPE getType() {
		// TODO Auto-generated method stub
		return CHILD_TYPE.MENU;
	}
	
	
}
