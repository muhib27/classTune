package com.classtune.app.schoolapp.model;

public class TypeHomeWork implements BaseType {

	private String typeName;
	private String typeId;

	public TypeHomeWork(String name, String id){
		this.typeId = id;
		this.typeName = name;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return PickerType.TEACHER_HOMEWORKTYPE;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.typeName;
	}

}
