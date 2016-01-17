/**
 * 
 */
package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Amit
 *
 */
public class Term {
	@SerializedName("id")
	private String termId;
	@SerializedName("title")
	private String termTitle;
	
	@SerializedName("exam_date")
	private String exam_date;
	
	
	public void setTermId(String termId) {
		this.termId = termId;
	}
	public String getTermId() {
		return termId;
	}
	
	public void setTermTitle(String termTitle) {
		this.termTitle = termTitle;
	}
	public String getTermTitle() {
		return termTitle;
	}
	
	public String getExam_date() {
		return exam_date;
	}
	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
}
