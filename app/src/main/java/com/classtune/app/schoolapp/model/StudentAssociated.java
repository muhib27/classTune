package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by BLACK HAT on 16-Feb-17.
 */

public class StudentAssociated {
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("roll_no")
    @Expose
    private String rollNo;
    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("att")
    @Expose
    private Integer att;

    private boolean isClickedPresent = false;
    private boolean isClickedAbsent = false;
    private boolean isClickedLate = false;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getAtt() {
        return att;
    }

    public void setAtt(Integer att) {
        this.att = att;
    }

    public boolean isClickedPresent() {
        return isClickedPresent;
    }

    public void setClickedPresent(boolean clickedPresent) {
        isClickedPresent = clickedPresent;
    }

    public boolean isClickedAbsent() {
        return isClickedAbsent;
    }

    public void setClickedAbsent(boolean clickedAbsent) {
        isClickedAbsent = clickedAbsent;
    }

    public boolean isClickedLate() {
        return isClickedLate;
    }

    public void setClickedLate(boolean clickedLate) {
        isClickedLate = clickedLate;
    }
}
