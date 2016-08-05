package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by zhang on 7/13/2016.
 */
public class ResumeCategory {
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

}
