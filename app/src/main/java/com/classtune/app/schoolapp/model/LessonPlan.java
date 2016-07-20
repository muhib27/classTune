package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BLACK HAT on 25-Mar-15.
 */
public class LessonPlan {

    @SerializedName("id")
    private String id;

    @SerializedName("category")
    private String category;

    @SerializedName("title")
    private String title;

    @SerializedName("is_show")
    private String is_show;

    @SerializedName("subjects")
    private String subjects;

    @SerializedName("publish_date")
    private String publishDate;

    @SerializedName("content")
    private String description;

    @SerializedName("attachment_file_name")
    private String attachmentFileName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private boolean checked = false;

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }
}
