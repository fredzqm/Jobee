package com.fredzqm.jobee.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhang on 7/12/2016.
 */
public class Recruiter implements Parcelable {
    private String emailAccount;
    private String name;
    private String company;

    public Recruiter(){
        // empty constructor required by Jackson
    }

    public Recruiter(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    protected Recruiter(Parcel in) {
        emailAccount = in.readString();
        name = in.readString();
        company = in.readString();
    }

    public static final Creator<Recruiter> CREATOR = new Creator<Recruiter>() {
        @Override
        public Recruiter createFromParcel(Parcel in) {
            return new Recruiter(in);
        }

        @Override
        public Recruiter[] newArray(int size) {
            return new Recruiter[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
        parcel.writeString(company);
    }

    public static Recruiter createNewAccount(String userID) {
        return new Recruiter(userID);
    }
}
