package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by BLACK HAT on 19-Feb-17.
 */

public class StdAtt {
    @SerializedName("roll_no")
    @Expose
    private String rollNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("absent")
    @Expose
    private Integer absent;
    @SerializedName("late")
    @Expose
    private Integer late;
    @SerializedName("present")
    @Expose
    private Integer present;

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAbsent() {
        return absent;
    }

    public void setAbsent(Integer absent) {
        this.absent = absent;
    }

    public Integer getLate() {
        return late;
    }

    public void setLate(Integer late) {
        this.late = late;
    }

    public Integer getPresent() {
        return present;
    }

    public void setPresent(Integer present) {
        this.present = present;
    }
}
