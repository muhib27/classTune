package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

public class NotificationReminder {

	@SerializedName("id")
	private String id;
	@SerializedName("subject")
	private String subject;
	@SerializedName("is_read")
	private String is_read;
	@SerializedName("body")
	private String body;
	@SerializedName("rtype")
	private String rtype;
	@SerializedName("rid")
	private String rid;

    @SerializedName("student_id")
    private String studentId;

    @SerializedName("batch_id")
    private String batchId;

	public boolean isAlreadyRead() {
		return isAlreadyRead;
	}

	public void setIsAlreadyRead(boolean isAlreadyRead) {
		this.isAlreadyRead = isAlreadyRead;
	}

	private boolean isAlreadyRead = false;


    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getIs_read() {
		return is_read;
	}
	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getRtype() {
		return rtype;
	}
	public void setRtype(String rtype) {
		this.rtype = rtype;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
