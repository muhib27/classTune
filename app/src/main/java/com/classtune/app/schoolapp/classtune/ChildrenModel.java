package com.classtune.app.schoolapp.classtune;

/**
 * Created by BLACK HAT on 19-Nov-15.
 */
public class ChildrenModel {

    private String childId;
    private String relationName;
    private String childName;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }


    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public ChildrenModel(String childId, String relationName, String childName) {
        this.childId = childId;
        this.relationName = relationName;
        this.childName = childName;
    }

    public ChildrenModel() {
    }
}
