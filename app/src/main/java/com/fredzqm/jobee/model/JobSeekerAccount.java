package com.fredzqm.jobee.model;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds.Email;

/**
 * Created by zhang on 7/12/2016.
 */
public class JobSeekerAccount implements Parcelable {
    private String emailAccount;
    private String name;
    private String address;
    private String major;

    public JobSeekerAccount() {
        // empty constructor required by Jackson
    }

    public JobSeekerAccount(String emailAccount) {
        this.emailAccount = emailAccount;
        this.name = "";
        this.address = "";
    }

    public static JobSeekerAccount newInstance() {
        return new JobSeekerAccount("fredzqm@gmail.com");
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

    // parcelable implementation


    protected JobSeekerAccount(Parcel in) {
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

}
