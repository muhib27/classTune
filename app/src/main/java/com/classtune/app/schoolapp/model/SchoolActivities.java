package com.classtune.app.schoolapp.model;

import java.util.List;

public class SchoolActivities
{
	private String title = "";
	
	private String content = "";
	private String summary = "";
	private String imgUrl = "";
	private List<String> listGallery = null;
	
	
	public SchoolActivities(String title, String content, String summary, String imgUrl, List<String> listGallery)
	{
		this.title = title;
		this.content = content;
		this.summary = summary;
		this.imgUrl = imgUrl;
		this.listGallery = listGallery;
	}
	
	public SchoolActivities(String title, String content, String summary)
	{
		this.title = title;
		this.content = content;
		this.summary = summary;
		
		
	}
	
	
	
	
	public String getTitle()
	{
		return title;
	}


	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getImgUrl()
	{
		return imgUrl;
	}
	public void setImgUr(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}
	public List<String> getListGallery()
	{
		return listGallery;
	}
	public void setListGallery(List<String> listGallery)
	{
		this.listGallery = listGallery;
	}
	
	
}
