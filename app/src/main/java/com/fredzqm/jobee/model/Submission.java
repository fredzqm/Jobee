package com.fredzqm.jobee.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by zhang on 8/6/2016.
 */
public class Submission implements ValueEventListener {
    public static final String PATH = "submission";
    public static final String RECRUITER_KEY = "recruiterKey";
    public static final String JOBSEEKER_KEY = "jobSeekerKey";

    @Exclude
    private String key;

    private Resume resume;
    private String jobKey;
    private String resumeKey;
    private String recruiterKey;
    private String jobSeekerKey;
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

    public String getRecruiterKey() {
        return recruiterKey;
    }

    public void setRecruiterKey(String recruiterKey) {
        this.recruiterKey = recruiterKey;
    }

    public String getJobSeekerKey() {
        return jobSeekerKey;
    }

    public void setJobSeekerKey(String jobSeekerKey) {
        this.jobSeekerKey = jobSeekerKey;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }



    public static void handleNewSubmission(Job mJob, String resumeKey) {
        Submission subs = new Submission();
        subs.jobKey = mJob.getKey();
        subs.resumeKey = resumeKey;
        subs.recruiterKey = mJob.getRecruiterKey();
        subs.date = new Date();
        Resume.getReference().child(resumeKey).addValueEventListener(subs);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        resume = dataSnapshot.getValue(Resume.class);
        resume.setKey(resumeKey);
        jobSeekerKey = resume.getJobSeekerKey();
        Submission.getReference().push().setValue(this);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("Error", "onCancelled: " + databaseError.getMessage());
    }

}
