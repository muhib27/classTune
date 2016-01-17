package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tasvir on 4/9/2015.
 */
public class ProgressExam {
    @SerializedName("exam_id")
    private String exam_id;
    @SerializedName("exam_name")
    private String exam_name;
    @SerializedName("exam_date")
    private String exam_date;
    @SerializedName("your_grade")
    private String your_grade;
    @SerializedName("grade_point")
    private String grade_point;
    @SerializedName("your_mark")
    private String your_mark;
    @SerializedName("your_percent")
    private int your_percent;
    @SerializedName("max_mark")
    private String max_mark;
    @SerializedName("max_mark_percent")
    private double max_mark_percent;
    @SerializedName("avg_mark")
    private double avg_mark;
    @SerializedName("avg_mark_percent")
    private double avg_mark_percent;
    @SerializedName("category")
    private String category;
    @SerializedName("point")
    private String point;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @SerializedName("name")
    private String name;

    public String getTotal_mark() {
        return total_mark;
    }

    public void setTotal_mark(String total_mark) {
        this.total_mark = total_mark;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getExam_date() {
        return exam_date;
    }

    public void setExam_date(String exam_date) {
        this.exam_date = exam_date;
    }

    public String getYour_grade() {
        return your_grade;
    }

    public void setYour_grade(String your_grade) {
        this.your_grade = your_grade;
    }

    public String getGrade_point() {
        return grade_point;
    }

    public void setGrade_point(String grade_point) {
        this.grade_point = grade_point;
    }

    public String getYour_mark() {
        return your_mark;
    }

    public void setYour_mark(String your_mark) {
        this.your_mark = your_mark;
    }

    public int getYour_percent() {
        return your_percent;
    }

    public void setYour_percent(int your_percent) {
        this.your_percent = your_percent;
    }

    public String getMax_mark() {
        return max_mark;
    }

    public void setMax_mark(String max_mark) {
        this.max_mark = max_mark;
    }

    public double getMax_mark_percent() {
        return max_mark_percent;
    }

    public void setMax_mark_percent(int max_mark_percent) {
        this.max_mark_percent = max_mark_percent;
    }

    public double getAvg_mark() {
        return avg_mark;
    }

    public void setAvg_mark(int avg_mark) {
        this.avg_mark = avg_mark;
    }

    public double getAvg_mark_percent() {
        return avg_mark_percent;
    }

    public void setAvg_mark_percent(int avg_mark_percent) {
        this.avg_mark_percent = avg_mark_percent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @SerializedName("total_mark")
    private String total_mark;
}
