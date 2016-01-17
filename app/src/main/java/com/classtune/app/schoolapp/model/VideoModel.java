package com.classtune.app.schoolapp.model;

public class VideoModel {
	
	
	public String videoTitle;
	public String categoryTitle;
	public String viewcounts;
	public String imageUrl;
	
	
	
	//dummy constructor
	public VideoModel(String videoTitle, String categoryTitle, String viewcounts, String imageUrl)
	{
		this.videoTitle = videoTitle;
		this.categoryTitle = categoryTitle;
		this.viewcounts = viewcounts;
		this.imageUrl = imageUrl;
	}
	
	

	public String getVideoTitle() {
		return videoTitle;
	}
	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}
	
	
	public String getCategoryTitle() {
		return categoryTitle;
	}
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}
	
	
	public String getViewcounts() {
		return viewcounts;
	}
	public void setViewcounts(String viewcounts) {
		this.viewcounts = viewcounts;
	}
	
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	
	
	
	
}
