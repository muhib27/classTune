package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Notice {
	@SerializedName("notice_id")
	private String noticeId;
	@SerializedName("notice_type_id")
	private String noticeTypeId;
	@SerializedName("notice_type_text")
	private String noticeTypeText;
	@SerializedName("notice_title")
	private String noticeTitle;
	@SerializedName("notice_content")
	private String noticeContent;
	@SerializedName("published_at")
	private String publishedAt;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("author_id")
	private String authorId;
	@SerializedName("author_first_name")
	private String authorFirstName;
	@SerializedName("author_full_name")
	private String authorFullName;
	@SerializedName("acknowledge")
	private ArrayList<Acknowledge> allAck;
	@SerializedName("file_name")
	private String file_name;



	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public ArrayList<Acknowledge> getAllAck() {
		return allAck;
	}

	public void setAllAck(ArrayList<Acknowledge> allAck) {
		this.allAck = allAck;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeTypeId() {
		return noticeTypeId;
	}

	public void setNoticeTypeId(String noticeTypeId) {
		this.noticeTypeId = noticeTypeId;
	}

	public String getNoticeTypeText() {
		return noticeTypeText;
	}

	public void setNoticeTypeText(String noticeTypeText) {
		this.noticeTypeText = noticeTypeText;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorFirstName() {
		return authorFirstName;
	}

	public void setAuthorFirstName(String authorFirstName) {
		this.authorFirstName = authorFirstName;
	}

	public String getAuthorFullName() {
		return authorFullName;
	}

	public void setAuthorFullName(String authorFullName) {
		this.authorFullName = authorFullName;
	}

}
