package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import com.fredzqm.jobee.job_seeker.JobSeekerActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by zhang on 7/12/2016.
 */
public class JobSeeker {
    public static final String PATH = "JobSeeker";
    public static final String JOB_SEEKER_KEY = "jobSeekerKey";

    @Exclude
    private String key;

    private String emailAccount;
    private String name;

    private String address;
    private String major;

    public JobSeeker() {
        // empty constructor required by Jackson
    }

    public static DatabaseReference getRefernce() {
        return FirebaseDatabase.getInstance().getReference().child(PATH);
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

}
