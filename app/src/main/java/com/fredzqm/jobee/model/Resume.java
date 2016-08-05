package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 7/13/2016.
 */
public class Resume extends ArrayList<ResumeCategory> implements Parcelable {
    private String resumeName;
    private String name;
    private String major;
    private String address;

    public Resume(String resumeName) {
        super();
        this.resumeName = resumeName;
        this.major = "";
        this.address = "";
    }

    public static Resume newInstance(String resumeName) {
        Resume resume = new Resume(resumeName);
        resume.add(ResumeCategory.newInstance("Skills"));
        resume.add(ResumeCategory.newInstance("Education"));
        resume.add(ResumeCategory.newInstance("Experience"));
        return resume;
    }

    public static Resume newInstance(String resumeName, JobSeeker jobSeeker) {
        Resume resume = new Resume(resumeName);
        resume.add(ResumeCategory.newInstance("Skills"));
        resume.add(ResumeCategory.newInstance("Education"));
        resume.add(ResumeCategory.newInstance("Experience"));
        resume.major = jobSeeker.getMajor();
        resume.address = jobSeeker.getAddress();
        return resume;
    }

    // getters and setters
    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // static test data
    public static List<Resume> ITEMS = new ArrayList<>();

    static {
        ITEMS.add(Resume.newInstance("a"));
        ITEMS.add(Resume.newInstance("b"));
        ITEMS.add(Resume.newInstance("c"));
    }

    // -------------------

    protected Resume(Parcel in) {
        resumeName = in.readString();
    }

    public static final Creator<Resume> CREATOR = new Creator<Resume>() {
        @Override
        public Resume createFromParcel(Parcel in) {
            return new Resume(in);
        }

        @Override
        public Resume[] newArray(int size) {
            return new Resume[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(resumeName);
    }

}
