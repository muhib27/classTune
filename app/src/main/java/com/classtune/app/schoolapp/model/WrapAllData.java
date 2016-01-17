package com.classtune.app.schoolapp.model;

import com.classtune.app.schoolapp.model.FreeVersionPost.Authors;
import com.classtune.app.schoolapp.model.FreeVersionPost.Categories;
import com.classtune.app.schoolapp.model.FreeVersionPost.Keywords;
import com.classtune.app.schoolapp.model.FreeVersionPost.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class WrapAllData
{
	private List<FreeVersionPost> listPost;
	private List<Categories> listCategories;
	private List<Tags> listTags;
	private List<Keywords> listKeywords;
	private List<Authors> listAuthors;
	


	public WrapAllData(String listPostName, String listCategoriesName, String listTagsName,
			String listKeywordsName, String listAuthorsName)
	{
		this.listPost = parsePost(listPostName);
		this.listCategories = parseCategories(listCategoriesName);
		this.listTags = parseTags(listTagsName);
		this.listKeywords = parseKeywords(listKeywordsName);
		this.listAuthors = parseAuthors(listAuthorsName);
	}
	
	public WrapAllData(String listPostName)
	{
		this.listPost = parsePost(listPostName);
		
	}
	
	
	public List<FreeVersionPost> getListPost() {
		return listPost;
	}
	public void setListPost(List<FreeVersionPost> listPost) {
		this.listPost = listPost;
	}
	
	
	
	public List<Categories> getListCategories() {
		return listCategories;
	}
	public void setListCategories(List<Categories> listCategories) {
		this.listCategories = listCategories;
	}
	
	
	
	public List<Tags> getListTags() {
		return listTags;
	}
	public void setListTags(List<Tags> listTags) {
		this.listTags = listTags;
	}
	
	
	
	public List<Keywords> getListKeywords() {
		return listKeywords;
	}
	public void setListKeywords(List<Keywords> listKeywords) {
		this.listKeywords = listKeywords;
	}
	
	
	
	public List<Authors> getListAuthors() {
		return listAuthors;
	}
	public void setListAuthors(List<Authors> listAuthors) {
		this.listAuthors = listAuthors;
	}
	
	

	

	
	private List<FreeVersionPost> parsePost(String object) {

		List<FreeVersionPost> post=new ArrayList<FreeVersionPost>();
		Type listType = new TypeToken<List<FreeVersionPost>>(){}.getType();
		post = (List<FreeVersionPost>) new Gson().fromJson(object, listType);
		return post;
	}
	
	private List<Categories> parseCategories(String object) {

		List<Categories> cat = new ArrayList<Categories>();
		Type listType = new TypeToken<List<Categories>>(){}.getType();
		cat = (List<Categories>) new Gson().fromJson(object, listType);
		return cat;
	}
	
	private List<Tags> parseTags(String object) {

		List<Tags> tag = new ArrayList<Tags>();
		Type listType = new TypeToken<List<Tags>>(){}.getType();
		tag = (List<Tags>) new Gson().fromJson(object, listType);
		return tag;
	}
	
	private List<Keywords> parseKeywords(String object) {

		List<Keywords> key = new ArrayList<Keywords>();
		Type listType = new TypeToken<List<Keywords>>(){}.getType();
		key = (List<Keywords>) new Gson().fromJson(object, listType);
		return key;
	}
	
	private List<Authors> parseAuthors(String object) {

		List<Authors> auth = new ArrayList<Authors>();
		Type listType = new TypeToken<List<Authors>>(){}.getType();
		auth = (List<Authors>) new Gson().fromJson(object, listType);
		return auth;
	}
	
	
	
}