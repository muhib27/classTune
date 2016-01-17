package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tasvir on 5/4/2015.
 */
public class SubjectSeries {
    @SerializedName("name")
    private String name;
    @SerializedName("color")
    private String color;

    public List<ProgressExam> getAllExam() {
        return allExam;
    }

    public void setAllExam(List<ProgressExam> allExam) {
        this.allExam = allExam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @SerializedName("exam")
    private List<ProgressExam> allExam;


}
