/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class Status {
	@SerializedName("code")
	private int code;
	@SerializedName("msg")
	private String msg;
	
	public int getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
}
