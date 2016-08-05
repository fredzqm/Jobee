package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by zhang on 7/13/2016.
 */
public class ResumeCategory implements Parcelable{
    private String type;
    private ArrayList<String> details;

    public ResumeCategory() {
        details = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<String> details) {
        this.details = details;
    }

    public void addContent(String str) {
        details.add(str);
    }

    public static ResumeCategory newInstance(String type) {
        ResumeCategory resumeCategory = new ResumeCategory();
        resumeCategory.setType(type);
        resumeCategory.addContent("Element 1");
        resumeCategory.addContent("Element 2");
        return resumeCategory;
    }

    public int size() {
        return details.size();
    }

    public String get(int i) {
        return details.get(i);
    }

    public void add(String str) {
        details.add(str);
    }

    public void set(int position, String str) {
        details.set(position, str);
    }

    public void remove(int position) {
        details.remove(position);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeStringList(details);
    }

    protected ResumeCategory(Parcel in) {
        type = in.readString();
        details = in.createStringArrayList();
    }

    public static final Creator<ResumeCategory> CREATOR = new Creator<ResumeCategory>() {
        @Override
        public ResumeCategory createFromParcel(Parcel in) {
            return new ResumeCategory(in);
        }

        @Override
        public ResumeCategory[] newArray(int size) {
            return new ResumeCategory[size];
        }
    };
}
