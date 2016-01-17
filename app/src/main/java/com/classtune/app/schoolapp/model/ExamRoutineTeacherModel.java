package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BLACK HAT on 27-Apr-15.
 */
public class ExamRoutineTeacherModel {

    @SerializedName("subject")
    private String subject;

    @SerializedName("subject_icon")
    private String subjectIcon;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("exam_name")
    private String examName;

    @SerializedName("batch")
    private String batch;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectIcon() {
        return subjectIcon;
    }

    public void setSubjectIcon(String subjectIcon) {
        this.subjectIcon = subjectIcon;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }




}
