package com.classtune.app.schoolapp.classtune;

/**
 * Created by BLACK HAT on 07-Dec-15.
 */
public class InfoFeatureModel {

    private int iconResId;
    private String title;
    private String details;

    public InfoFeatureModel(int iconResId, String title, String details) {
        this.iconResId = iconResId;
        this.title = title;
        this.details = details;
    }

    public InfoFeatureModel() {
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
