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
public class FolderList {
	@SerializedName("folder")
	private ArrayList<Folder> folderList;
	
	public void setFolderList(ArrayList<Folder> folderList) {
		this.folderList = folderList;
	}
	public ArrayList<Folder> getFolderList() {
		return folderList;
	}
}
