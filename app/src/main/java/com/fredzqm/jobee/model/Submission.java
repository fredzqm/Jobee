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
    @Exclude
    private Resume resume;
    @Exclude
    private RecyclerView.Adapter mAdapter;

    private String resumeKey;
    private String jobKey;
    private String recruiterKey;
    private String jobSeekerKey;

    private Date date;

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference().child(PATH);
    }

    public static Submission newInstance(Job mJob, String content) {
        Submission submission = new Submission();
        String[] contents = content.split("\n");
        submission.recruiterKey = mJob.getRecruiterKey();
        submission.jobKey = mJob.getKey();
        submission.resumeKey = contents[1];
        submission.jobSeekerKey = contents[0];
        submission.date = new Date();
        return submission;
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


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        resume = dataSnapshot.getValue(Resume.class);
        resume.setKey(resumeKey);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("Error", "onCancelled: " + databaseError.getMessage());
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public void setmAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}
