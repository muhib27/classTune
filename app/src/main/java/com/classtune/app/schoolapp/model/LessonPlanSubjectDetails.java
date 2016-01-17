package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BLACK HAT on 23-Apr-15.
 */
public class LessonPlanSubjectDetails {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("publish_date")
    private String publishDate;

}
