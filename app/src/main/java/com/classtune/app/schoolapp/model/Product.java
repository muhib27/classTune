package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class Product {

	@SerializedName("product_id")
	private String product_id;
	@SerializedName("name")
	private String name;
	@SerializedName("price")
	private String price;
	@SerializedName("special_price")
	private String special_price;
	@SerializedName("special_from_date")
	private String special_from_date;
	@SerializedName("special_to_date")
	private String special_to_date;
	@SerializedName("media")
	private String media;

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSpecial_price() {
		return special_price;
	}

	public void setSpecial_price(String special_price) {
		this.special_price = special_price;
	}

	public String getSpecial_from_date() {
		return special_from_date;
	}

	public void setSpecial_from_date(String special_from_date) {
		this.special_from_date = special_from_date;
	}

	public String getSpecial_to_date() {
		return special_to_date;
	}

	public void setSpecial_to_date(String special_to_date) {
		this.special_to_date = special_to_date;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

}
