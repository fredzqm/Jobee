package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhang on 7/12/2016.
 */
public class JobSeeker implements Parcelable {
    private String userID;
    private String emailAccount;
    private String name;
    private String address;
    private String major;

    public JobSeeker() {
        // empty constructor required by Jackson
    }

    public JobSeeker(String userID) {
        this.userID = userID;
        this.emailAccount = userID;
        this.name = "";
        this.address = "";
    }

    public static JobSeeker newInstance() {
        return new JobSeeker("fredzqm@gmail.com");
    }

    //  getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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

    // parcelable implementation


    protected JobSeeker(Parcel in) {
        emailAccount = in.readString();
        name = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailAccount);
        dest.writeString(name);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobSeeker> CREATOR = new Creator<JobSeeker>() {
        @Override
        public JobSeeker createFromParcel(Parcel in) {
            return new JobSeeker(in);
        }

        @Override
        public JobSeeker[] newArray(int size) {
            return new JobSeeker[size];
        }
    };

    public static JobSeeker createNewAccount(String userID) {
        return new JobSeeker(userID);
    }
}
