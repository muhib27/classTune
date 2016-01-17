package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BLACK HAT on 08-Apr-15.
 */
public class LessonPlanStudentParentSubject {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    @SerializedName("icon")
    private String icon;

    @SerializedName("total")
    private String total;

    @SerializedName("lastupdated")
    private String lastUpdated;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
