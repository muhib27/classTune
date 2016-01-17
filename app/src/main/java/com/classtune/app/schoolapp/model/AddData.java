package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class AddData {
	@SerializedName("ad_image")
	private String ad_image;
	@SerializedName("ad_image_link")
	private String ad_image_link;
	@SerializedName("ad_image_caption")
	private String ad_image_caption;
	@SerializedName("ad_image_category")
	private String ad_image_category;
	@SerializedName("ad_image_subcategory")
	private String ad_image_subcategory;


	public String getAd_image() {
		return ad_image;
	}

	public void setAd_image(String ad_image) {
		this.ad_image = ad_image;
	}

	public String getAd_image_link() {
		return ad_image_link;
	}

	public void setAd_image_link(String ad_image_link) {
		this.ad_image_link = ad_image_link;
	}

	public String getAd_image_caption() {
		return ad_image_caption;
	}

	public void setAd_image_caption(String ad_image_caption) {
		this.ad_image_caption = ad_image_caption;
	}

	public String getAd_image_subcategory() {
		return ad_image_subcategory;
	}

	public void setAd_image_subcategory(String ad_image_subcategory) {
		this.ad_image_subcategory = ad_image_subcategory;
	}

	public String getAd_image_category() {
		return ad_image_category;
	}

	public void setAd_image_category(String ad_image_category) {
		this.ad_image_category = ad_image_category;
	}
}
