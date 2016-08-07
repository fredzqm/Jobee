package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by zhang on 7/13/2016.
 */
public class Resume implements Parcelable {
    public static final String PATH = "resume";

    @Exclude
    private String key;

    private String jobSeekerKey;
    private String resumeName;
    private String name;
    private String major;
    private String address;
    private ArrayList<ResumeCategory> resumeCategories;

    public Resume() {
        resumeCategories = new ArrayList<>();
    }

    public static Resume newInstance(String resumeName, String jobSeekerKey) {
        Resume resume = new Resume();
        resume.jobSeekerKey = jobSeekerKey;
        resume.resumeName = resumeName;
        resume.major = "";
        resume.address = "";
        resume.add(ResumeCategory.newInstance("Skills"));
        resume.add(ResumeCategory.newInstance("Education"));
        resume.add(ResumeCategory.newInstance("Experience"));
        return resume;
    }

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference().child(PATH);
    }

    protected Resume(Parcel in) {
        key = in.readString();
        resumeName = in.readString();
        name = in.readString();
        major = in.readString();
        address = in.readString();
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public ArrayList<ResumeCategory> getResumeCategories() {
        return resumeCategories;
    }

    public void setResumeCategories(ArrayList<ResumeCategory> resumeCategories) {
        this.resumeCategories = resumeCategories;
    }

    public int size() {
        return resumeCategories.size();
    }

    public boolean add(ResumeCategory strings) {
        return resumeCategories.add(strings);
    }

    public boolean remove(Object o) {
        return resumeCategories.remove(o);
    }

    public ResumeCategory get(int i) {
        return resumeCategories.get(i);
    }

    public ResumeCategory set(int i, ResumeCategory strings) {
        return resumeCategories.set(i, strings);
    }

    public void add(int i, ResumeCategory strings) {
        resumeCategories.add(i, strings);
    }

    public ResumeCategory remove(int i) {
        return resumeCategories.remove(i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(resumeName);
        parcel.writeString(name);
        parcel.writeString(major);
        parcel.writeString(address);
    }

    public String getJobSeekerKey() {
        return jobSeekerKey;
    }

    public void setJobSeekerKey(String jobSeekerKey) {
        this.jobSeekerKey = jobSeekerKey;
    }
}
