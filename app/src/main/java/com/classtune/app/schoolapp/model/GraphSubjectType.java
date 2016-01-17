package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tasvir on 4/9/2015.
 */
public class GraphSubjectType implements BaseType {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    public GraphSubjectType(String name, String id){
        this.name = name;
        this.id = id;
    }
    @Override
    public PickerType getType() {
        return PickerType.GRAPH;
    }

    @Override
    public String getText() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
