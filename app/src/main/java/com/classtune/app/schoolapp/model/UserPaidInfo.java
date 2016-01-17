package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserPaidInfo {

	
	@SerializedName("id")
	private String id;
	@SerializedName("full_name")
	private String fullName;
	
	@SerializedName("is_admin")
	private boolean admin;
	@SerializedName("is_student")
	private boolean student;
	@SerializedName("is_teacher")
	private boolean teacher;
	@SerializedName("is_parent")
	private boolean parent;
	@SerializedName("school_id")
	private String schoolId = "";
	@SerializedName("secret")
	private String secret;
	@SerializedName("batch_id")
	private String batchId;
	@SerializedName("profile_id")
	private String profileId;
	@SerializedName("terms")
	private ArrayList<Term> termList;
	@SerializedName("course_name")
	private String course_name;
	@SerializedName("section_name")
	private String section_name;
	@SerializedName("batch_name")
	private String batch_name;
	@SerializedName("profile_image")
	private String profile_image;
	@SerializedName("school_name")
	private String school_name;


    @SerializedName("school_logo")
    private String school_logo;

    @SerializedName("school_picture")
    private String school_picture;

    @SerializedName("school_cover")
    private String school_cover;



    @SerializedName("unread_total")
    private String unread_total_notification;

	@SerializedName("school_type")
	private int schoolType;

	public String getIs_first_login() {
        return is_first_login;
    }

    public void setIs_first_login(String is_first_login) {
        this.is_first_login = is_first_login;
    }

    @SerializedName("is_first_login")
    private String is_first_login;
	
	
	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getBatch_name() {
		return batch_name;
	}

	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}

	public String getProfile_image() {
		return profile_image;
	}

	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}

	public boolean isParent() {
		return parent;
	}

	public void setStudent(boolean student) {
		this.student = student;
	}

	public boolean isStudent() {
		return student;
	}

	public void setTeacher(boolean teacher) {
		this.teacher = teacher;
	}

	public boolean isTeacher() {
		return teacher;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public void setSchoolType(int schoolType) {
		this.schoolType = schoolType;
	}

	public int getSchoolType() { return schoolType; }

	public String getSchoolId() {
		return schoolId;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBatchId() {
		return batchId;
	}
	
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setTermList(ArrayList<Term> termList) {
		this.termList = termList;
	}

	public ArrayList<Term> getTermList() {
		return termList;
	}
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


    public String getSchool_cover() {
        return school_cover;
    }





    public void setSchool_cover(String school_cover) {
        this.school_cover = school_cover;
    }

    public String getUnread_total_notification() {
        return unread_total_notification;
    }

    public void setUnread_total_notification(String unread_total_notification) {
        this.unread_total_notification = unread_total_notification;
    }

    public String getSchool_picture() {
        return school_picture;
    }

    public void setSchool_picture(String school_picture) {
        this.school_picture = school_picture;
    }

    public String getSchool_logo() {
        return school_logo;
    }

    public void setSchool_logo(String school_logo) {
        this.school_logo = school_logo;
    }

}
