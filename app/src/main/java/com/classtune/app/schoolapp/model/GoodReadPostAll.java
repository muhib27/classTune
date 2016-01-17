/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Amit
 *
 */
public class GoodReadPostAll {
	@SerializedName("post")
	private ArrayList<GoodReadPost> goodreadPostList;
	
	public void setGoodreadPostList(ArrayList<GoodReadPost> goodreadPostList) {
		this.goodreadPostList = goodreadPostList;
	}
	public ArrayList<GoodReadPost> getGoodreadPostList() {
		return goodreadPostList;
	}
}
