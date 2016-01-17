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
public class CTReportSubjectExam {
	@SerializedName("class_test")
	private ArrayList<ClassTestItem> classtestList;
	@SerializedName("project")
	private ArrayList<ClassTestItem> projectList;
	
	public void setClasstestList(ArrayList<ClassTestItem> classtestList) {
		this.classtestList = classtestList;
	}
	public ArrayList<ClassTestItem> getClasstestList() {
		return classtestList;
	}
	
	public void setProjectList(ArrayList<ClassTestItem> projectList) {
		this.projectList = projectList;
	}
	public ArrayList<ClassTestItem> getProjectList() {
		return projectList;
	}
}
