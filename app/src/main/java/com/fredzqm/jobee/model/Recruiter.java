package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fredzqm.jobee.job_seeker.JobSeekerActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * Created by zhang on 7/12/2016.
 */
public class Recruiter {
    public static final String PATH = "recruiter";

    @Exclude
    private String key;
    private String token;

    private String emailAccount;
    private String name;

    private String company;


    public Recruiter(){
        // empty constructor required by Jackson
    }

    public static DatabaseReference getRefernce() {
        return FirebaseDatabase.getInstance().getReference().child(PATH);
    }

    public static Recruiter newInstance(String userID) {
        Recruiter recruiter = new Recruiter();
        recruiter.emailAccount = userID;
        recruiter.token = FirebaseInstanceId.getInstance().getToken();
        return recruiter;
    }

    // --------- getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
