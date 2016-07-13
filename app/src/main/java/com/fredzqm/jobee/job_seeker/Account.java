package com.fredzqm.jobee.job_seeker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhang on 7/12/2016.
 */
public class Account implements Parcelable {
    private String emailAccount;
    private String name;
    private String address;

    public Account(){
        // empty constructor required by Jackson
    }

    public Account(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    protected Account(Parcel in) {
        emailAccount = in.readString();
        name = in.readString();
        address = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
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
