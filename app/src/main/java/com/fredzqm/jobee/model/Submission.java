package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;
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
public class Submission implements ValueEventListener, Parcelable {
    public static final String PATH = "submission";
    public static final String RECRUITER_KEY = "recruiterKey";
    public static final String JOBSEEKER_KEY = "jobSeekerKey";
    public static final String DECLINED = "DECLINED";

    @Exclude
    private String key;

    private Resume resume;

    private Job job;
    private String jobKey;
    private String resumeKey;
    private String recruiterKey;
    private String jobSeekerKey;
    private Date date;

    private String status;
    public static final String SUBMITTED = "Submitted";
    public static final String OFFERED = "Offer";
    public static final String REJECTED = "Rejected";
    public static final String ACCEPTED = "Accepted";

    public Submission() {

    }

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

    public Job getJob() {
        return job;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isOffer() {
        return OFFERED.equals(status);
    }

    public static void handleNewSubmission(Job mJob, String resumeKey) {
        Submission subs = new Submission();
        subs.jobKey = mJob.getKey();
        subs.resumeKey = resumeKey;
        subs.recruiterKey = mJob.getRecruiterKey();
        subs.job = mJob;
        subs.date = new Date();
        subs.status = SUBMITTED;
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


    protected Submission(Parcel in) {
        key = in.readString();
        resume = in.readParcelable(Resume.class.getClassLoader());
        job = in.readParcelable(Job.class.getClassLoader());
        jobKey = in.readString();
        resumeKey = in.readString();
        recruiterKey = in.readString();
        jobSeekerKey = in.readString();
        status = in.readString();
    }

    public static final Creator<Submission> CREATOR = new Creator<Submission>() {
        @Override
        public Submission createFromParcel(Parcel in) {
            return new Submission(in);
        }

        @Override
        public Submission[] newArray(int size) {
            return new Submission[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeParcelable(resume, i);
        parcel.writeParcelable(job, i);
        parcel.writeString(jobKey);
        parcel.writeString(resumeKey);
        parcel.writeString(recruiterKey);
        parcel.writeString(jobSeekerKey);
        parcel.writeString(status);
    }

}
