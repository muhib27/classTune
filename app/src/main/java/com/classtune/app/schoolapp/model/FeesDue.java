package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class FeesDue {
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("is_paid")
	private String isPaid;
	
	@SerializedName("balance")
	private String balance;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("duedate")
	private String duedate;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuedate() {
		return duedate;
	}

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}
	

}
