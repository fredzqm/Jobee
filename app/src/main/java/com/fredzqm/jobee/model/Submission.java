package com.fredzqm.jobee.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by zhang on 8/6/2016.
 */
public class Submission {
    public static final String PATH = "Recruiter";

    @Exclude
    private String key;

    private String resumeKey;
    private String jobKey;
    private Date date;

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference().child(PATH);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResumeKey() {
        return resumeKey;
    }

    public void setResumeKey(String resumeKey) {
        this.resumeKey = resumeKey;
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static String getPATH() {
        return PATH;
    }
}
