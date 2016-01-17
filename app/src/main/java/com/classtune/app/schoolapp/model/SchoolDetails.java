package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SchoolDetails {
	
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("division")
	private String division;
	
	@SerializedName("location")
	private String location;

	@SerializedName("views")
	private String views;
	
	@SerializedName("logo")
	private String logoUrl;
	
	@SerializedName("cover")
	private String coverUrl;
	
	@SerializedName("like_link")
	private String likeLinkUrl;
	
	
	@SerializedName("boys")
	private String boys;
	
	@SerializedName("picture")
	private String picture;


	@SerializedName("is_join")
	private int joinStatus;


	@SerializedName("girls")
	private String girls;
	
	
	private List<SchoolPages> listSchoolPages;
	
	
	public int getJoinStatus() {
		return joinStatus;
	}


	public void setJoinStatus(int joinStatus) {
		this.joinStatus = joinStatus;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDivision() {
		return division;
	}


	public void setDivision(String division) {
		this.division = division;
	}

	
	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}

	public String getViews() {
		return views;
	}


	public void setViews(String views) {
		this.views = views;
	}


	public String getLogoUrl() {
		return logoUrl;
	}


	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}


	public String getCoverUrl() {
		return coverUrl;
	}


	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}


	public String getLikeLinkUrl() {
		return likeLinkUrl;
	}


	public void setLikeLinkUrl(String likeLinkUrl) {
		this.likeLinkUrl = likeLinkUrl;
	}

	
	public List<SchoolPages> getListSchoolPages() {
		return listSchoolPages;
	}


	public void setListSchoolPages(List<SchoolPages> listSchoolPages) {
		this.listSchoolPages = listSchoolPages;
	}

	public String getBoys() {
		return boys;
	}


	public void setBoys(String boys) {
		this.boys = boys;
	}


	public String getGirls() {
		return girls;
	}


	public void setGirls(String girls) {
		this.girls = girls;
	}



	public String getPicture() {
		return picture;
	}


	public void setPicture(String picture) {
		this.picture = picture;
	}

	
	

	public class SchoolPages 
	{
		@SerializedName("id")
		private String id;
		
		@SerializedName("menu_id")
		private String menuId;
		
		@SerializedName("name")
		private String name;
		
		@SerializedName("gallery")
		private ArrayList<String> gallery;
		
		@SerializedName("content")
		private String content;
		
		@SerializedName("web-view")
		private String web_view;
		
		@SerializedName("title")
		private String title;
		
		
		@SerializedName("image")
		private String image;
		
		

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getWeb_view() {
			return web_view;
		}

		public void setWeb_view(String web_view) {
			this.web_view = web_view;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public ArrayList<String> getGallery() {
			return gallery;
		}

		public void setGallery(ArrayList<String> gallery) {
			this.gallery = gallery;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMenuId() {
			return menuId;
		}

		public void setMenuId(String menuId) {
			this.menuId = menuId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
		
		
	}
	
	/*public class SchoolActivities
	{
		
		private String content;
		private String summary;
		private String imgUrl;
		private List<String> listGallery;
		
		
		public SchoolActivities(String content, String summary, String imgUrl, List<String> listGallery)
		{
			this.content = content;
			this.summary = summary;
			this.imgUrl = imgUrl;
			this.listGallery = listGallery;
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
		
		
	}*/

}
