/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class ModelContainer {
	@SerializedName("data")
	private CommonData data;
	@SerializedName("status")
	private Status status;
	
	public void setData(CommonData data) {
		this.data = data;
	}
	public CommonData getData() {
		return data;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	public Status getStatus() {
		return status;
	}
}
