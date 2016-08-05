package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by zhang on 7/12/2016.
 */
public class JobSeeker{
    @Exclude
    private String key;

    private String emailAccount;
    private String name;

    private ArrayList<String> resumeKeys;
    private String address;
    private String major;

    public JobSeeker() {
        // empty constructor required by Jackson
    }

    public static JobSeeker newInstance(String key) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.key = key;
        jobSeeker.emailAccount = "userID";
        jobSeeker.name = "";
        jobSeeker.address = "";
        return jobSeeker;
    }

    //  getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getResumeKeys() {
        return resumeKeys;
    }

    public void setResumeKeys(ArrayList<String> resumeKeys) {
        this.resumeKeys = resumeKeys;
    }

}
