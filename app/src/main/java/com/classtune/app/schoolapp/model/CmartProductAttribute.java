package com.classtune.app.schoolapp.model;

public class CmartProductAttribute implements BaseType{
	
	private String superAttributeId;
	private String code;
	private String label;
	private PickerType type;
	
	public String getSuperAttributeId() {
		return superAttributeId;
	}
	public void setSuperAttributeId(String superAttributeId) {
		this.superAttributeId = superAttributeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public PickerType getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	public void setType(PickerType typ)
	{
		this.type=typ;
	}
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return label;
	}
	
}
