package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Extreme_Piash on 1/18/2017.
 */

public class GroupExam {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("batch_id")
    private String batch_id;
    @SerializedName("school_id")
    private String school_id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("attandence_end_date")
    private String attandence_end_date;
    @SerializedName("attandence_start_date")
    private String attandence_start_date;
    @SerializedName("result_type")
    private String result_type;
    @SerializedName("published_date")
    private String published_date;
    @SerializedName("quarter_number")
    private String quarter_number;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getAttandence_end_date() {
        return attandence_end_date;
    }

    public String getAttandence_start_date() {
        return attandence_start_date;
    }

    public String getResult_type() {
        return result_type;
    }

    public String getPublished_date() {
        return published_date;
    }

    public String getQuarter_number() {
        return quarter_number;
    }

    @Override
    public String toString() {
        return "GroupExam{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", batch_id='" + batch_id + '\'' +
                ", school_id='" + school_id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", attandence_end_date='" + attandence_end_date + '\'' +
                ", attandence_start_date='" + attandence_start_date + '\'' +
                ", result_type='" + result_type + '\'' +
                ", published_date='" + published_date + '\'' +
                ", quarter_number='" + quarter_number + '\'' +
                '}';
    }
}
