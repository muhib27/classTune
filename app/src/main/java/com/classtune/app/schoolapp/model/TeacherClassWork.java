package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Extreme_Piash on 1/15/2017.
 */

public class TeacherClassWork {
    @SerializedName("subjects")
    private String subjects;

    @SerializedName("batch")
    private String batch;

    @SerializedName("attachment_file_name")
    private String attachment_file_name;

    @SerializedName("course")
    private String course;

    public String getSection() {
        return section;
    }

    @SerializedName("section")
    private String section;

    @SerializedName("subjects_id")
    private String subjects_id;

    @SerializedName("subjects_icon")
    private String subjects_icon;

    @SerializedName("assign_date")
    private String assign_date;

    @SerializedName("name")
    private String classwork_name;

    @SerializedName("content")
    private String content;

    @SerializedName("type")
    private String classwork_type;

    @SerializedName("id")
    private String id;

    @SerializedName("is_editable")
    private boolean is_editable;

    @SerializedName("is_published")
    private String is_published;

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getAttachment_file_name() {
        return attachment_file_name;
    }

    public void setAttachment_file_name(String attachment_file_name) {
        this.attachment_file_name = attachment_file_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSubjects_id() {
        return subjects_id;
    }

    public void setSubjects_id(String subjects_id) {
        this.subjects_id = subjects_id;
    }

    public String getSubjects_icon() {
        return subjects_icon;
    }

    public void setSubjects_icon(String subjects_icon) {
        this.subjects_icon = subjects_icon;
    }

    public String getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(String assign_date) {
        this.assign_date = assign_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClasswork_type() {
        return classwork_type;
    }

    public void setClasswork_type(String classwork_type) {
        this.classwork_type = classwork_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClasswork_name() {
        return classwork_name;
    }

    public boolean is_editable() {
        return is_editable;
    }

    public String getIs_published() {
        return is_published;
    }

    @Override
    public String toString() {
        return "TeacherClassWork{" +
                "subjects='" + subjects + '\'' +
                ", batch='" + batch + '\'' +
                ", attachment_file_name='" + attachment_file_name + '\'' +
                ", course='" + course + '\'' +
                ", section='" + section + '\'' +
                ", subjects_id='" + subjects_id + '\'' +
                ", subjects_icon='" + subjects_icon + '\'' +
                ", assign_date='" + assign_date + '\'' +
                ", classwork_name='" + classwork_name + '\'' +
                ", content='" + content + '\'' +
                ", classwork_type='" + classwork_type + '\'' +
                ", id='" + id + '\'' +
                ", is_editable=" + is_editable +
                ", is_published='" + is_published + '\'' +
                '}';
    }
}
