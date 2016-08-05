package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by zhang on 7/12/2016.
 */
public class Recruiter {
    @Exclude
    private String key;

    private String emailAccount;
    private String name;
    private String company;

    public Recruiter(){
        // empty constructor required by Jackson
    }

    public static Recruiter newInstance(String userID) {
        Recruiter recruiter = new Recruiter();
        recruiter.emailAccount = userID;
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
}
