package com.classtune.app.schoolapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SchoolCandleSetting  implements Parcelable {
    
    
    private int canComment = 0;
	private int isShow = 0;
	
	private List<Integer> typeList = new ArrayList<Integer>();
	


	public SchoolCandleSetting(int canComment, int isShow, List<Integer> typeList)
	{
		this.canComment = canComment;
		this.isShow = isShow;
		this.typeList = typeList;
	}
	

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(canComment);
        out.writeInt(isShow);
        out.writeList(typeList);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<SchoolCandleSetting> CREATOR = new Parcelable.Creator<SchoolCandleSetting>() {
        public SchoolCandleSetting createFromParcel(Parcel in) {
            return new SchoolCandleSetting(in);
        }

        public SchoolCandleSetting[] newArray(int size) {
            return new SchoolCandleSetting[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private SchoolCandleSetting(Parcel in) {
        canComment = in.readInt();
        isShow = in.readInt();
        typeList = in.readArrayList(null);
    }
    
    
    
    public int getCanComment() {
		return canComment;
	}


	public void setCanComment(int canComment) {
		this.canComment = canComment;
	}


	public int getIsShow() {
		return isShow;
	}


	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}


	public List<Integer> getTypeList() {
		return typeList;
	}


	public void setTypeList(List<Integer> typeList) {
		this.typeList = typeList;
	}
    
	public String toString()
	{
		return canComment +" "+ isShow;
	}
    
    
}