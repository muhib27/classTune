/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class Wrapper {
	@SerializedName("data")
	private JsonObject data;
	@SerializedName("status")
	private Status status;
	
	public void setData(JsonObject data) {
		this.data = data;
	}
	public JsonObject getData() {
		return data;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Status getStatus() {
		return status;
	}
}
