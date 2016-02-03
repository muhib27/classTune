package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tasvir on 1/27/2016.
 */
public class FreeFeed {
    @SerializedName("title")
    private String title;

    @SerializedName("created")
    private String created;

    @SerializedName("body1")
    private String body1;

    @SerializedName("body2")
    private String body2;

    @SerializedName("body3")
    private String body3;

    @SerializedName("attachment_file_name")
    private String attachment_file_name;

    @SerializedName("is_read")
    private String is_read;

    @SerializedName("rtype")
    private String rtype;

    @SerializedName("rid")
    private String rid;

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getAttachment_file_name() {
        return attachment_file_name;
    }

    public void setAttachment_file_name(String attachment_file_name) {
        this.attachment_file_name = attachment_file_name;
    }

    public String getBody3() {
        return body3;
    }

    public void setBody3(String body3) {
        this.body3 = body3;
    }

    public String getBody2() {
        return body2;
    }

    public void setBody2(String body2) {
        this.body2 = body2;
    }

    public String getBody1() {
        return body1;
    }

    public void setBody1(String body1) {
        this.body1 = body1;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
