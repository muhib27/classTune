package com.classtune.app.schoolapp.model;

public class DrawerChildMenuDiary implements DrawerChildBase{

	public String getClazzName() {
		return clazzName;
	}
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	private String text;
	private String id;
	private String imageName;
	private String clazzName;
	private boolean pressed;
	
	public boolean isPressed() {
		return pressed;
	}
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
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
		return CHILD_TYPE.DIARY;
	}
	
	
}
