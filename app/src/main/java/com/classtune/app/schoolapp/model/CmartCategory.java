package com.classtune.app.schoolapp.model;

public class CmartCategory implements BaseType{

	private String label;
	private String entity_id;
	private String content_type;
	private String icon;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getEntity_id() {
		return entity_id;
	}
	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String toString()
	{
		return this.getLabel()+"\n"+this.getEntity_id()+"\n"+this.getContent_type()+"\n"+this.getIcon()+"\n";
	}
	@Override
	public PickerType getType() {
		return PickerType.CMART_CATEGORY;
	}
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return this.label;
	}
	
}
