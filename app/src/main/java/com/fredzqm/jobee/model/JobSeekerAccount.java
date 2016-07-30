package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhang on 7/12/2016.
 */
public class JobSeekerAccount implements Parcelable {
    private String emailAccount;
    private String name;
    private String address;

    public JobSeekerAccount(){
        // empty constructor required by Jackson
    }

    public JobSeekerAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    protected JobSeekerAccount(Parcel in) {
        emailAccount = in.readString();
        name = in.readString();
        address = in.readString();
    }

    public static final Creator<JobSeekerAccount> CREATOR = new Creator<JobSeekerAccount>() {
        @Override
        public JobSeekerAccount createFromParcel(Parcel in) {
            return new JobSeekerAccount(in);
        }

        @Override
        public JobSeekerAccount[] newArray(int size) {
            return new JobSeekerAccount[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(emailAccount);
        parcel.writeString(name);
        parcel.writeString(address);
    }
}
