package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Extreme_Piash on 1/16/2017.
 */

public class ClassworkData {

    @SerializedName("id")
    private String id;
    @SerializedName("is_new")
    private int is_new;
    @SerializedName("attachment_file_name")
    private String attachment_file_name;
    @SerializedName("teacher_name")
    private String teacher_name;
    @SerializedName("teacher_id")
    private String teacher_id;
    @SerializedName("subjects")
    private String subjects;
    @SerializedName("subjects_id")
    private String subjects_id;
    @SerializedName("subjects_icon")
    private String subjects_icon;
    @SerializedName("assign_date")
    private String assign_date;
    @SerializedName("name")
    private String name;
    @SerializedName("content")
    private String content;
    @SerializedName("type")
    private String type;
    @SerializedName("is_editable")
    private boolean is_editable;

    public String getId() {
        return id;
    }

    public int getIs_new() {
        return is_new;
    }

    public String getAttachment_file_name() {
        return attachment_file_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getSubjects_id() {
        return subjects_id;
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

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public boolean is_editable() {
        return is_editable;
    }

    @Override
    public String toString() {
        return "ClassworkData{" +
                "id='" + id + '\'' +
                ", is_new=" + is_new +
                ", attachment_file_name='" + attachment_file_name + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                ", subjects='" + subjects + '\'' +
                ", subjects_id='" + subjects_id + '\'' +
                ", subjects_icon='" + subjects_icon + '\'' +
                ", assign_date='" + assign_date + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", is_editable=" + is_editable +
                '}';
    }
}
