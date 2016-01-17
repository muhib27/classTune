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
public class CommonData {
	@SerializedName("user")
	private User user;
	@SerializedName("weekend")
	private ArrayList<Integer> weekends;
	@SerializedName("session")
	private String session;
	@SerializedName("notice")
	private ArrayList<Notice> allNotice;
	@SerializedName("homework")
	private ArrayList<HomeworkData> homeworkList;
	@SerializedName("notice_ack")
	private NoticeAcknowledge notice_ack;
	@SerializedName("has_next")
	private boolean hasNext;

	public NoticeAcknowledge getNotice_ack() {
		return notice_ack;
	}

	public void setNotice_ack(NoticeAcknowledge notice_ack) {
		this.notice_ack = notice_ack;
	}

	public ArrayList<Notice> getAllNotice() {
		return allNotice;
	}

	public void setAllNotice(ArrayList<Notice> allNotice) {
		this.allNotice = allNotice;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setHomeworkList(ArrayList<HomeworkData> homeworkList) {
		this.homeworkList = homeworkList;
	}

	public ArrayList<HomeworkData> getHomeworkList() {
		return homeworkList;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getSession() {
		return session;
	}

	public void setWeekends(ArrayList<Integer> weekends) {
		this.weekends = weekends;
	}

	public ArrayList<Integer> getWeekends() {
		return weekends;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasNext() {
		return hasNext;
	}
}
