package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by RR on 14-Nov-17.
 */

public class DefaulterModel {
    @SerializedName("student_id")
    @Expose
    private String student_id;
    @SerializedName("class_roll_no")
    @Expose
    private String class_roll_no;
    @SerializedName("student_name")
    @Expose
    private String student_name;
    @SerializedName("defaulter")
    @Expose
    private String defaulter;

    private boolean selected;

    public DefaulterModel(String student_id, String class_roll_no, String student_name, String defaulter) {
        this.student_id = student_id;
        this.class_roll_no = class_roll_no;
        this.student_name = student_name;
        this.defaulter = defaulter;
    }

    public DefaulterModel() {
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getClass_roll_no() {
        return class_roll_no;
    }

    public void setClass_roll_no(String class_roll_no) {
        this.class_roll_no = class_roll_no;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getDefaulter() {
        return defaulter;
    }

    public void setDefaulter(String defaulter) {
        this.defaulter = defaulter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
