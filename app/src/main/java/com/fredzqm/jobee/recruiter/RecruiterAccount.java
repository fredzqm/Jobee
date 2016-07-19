package com.fredzqm.jobee.recruiter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhang on 7/12/2016.
 */
public class RecruiterAccount implements Parcelable {
    private String emailAccount;
    private String name;
    private String company;

    public RecruiterAccount(){
        // empty constructor required by Jackson
    }

    public RecruiterAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    protected RecruiterAccount(Parcel in) {
        emailAccount = in.readString();
        name = in.readString();
        company = in.readString();
    }

    public static final Creator<RecruiterAccount> CREATOR = new Creator<RecruiterAccount>() {
        @Override
        public RecruiterAccount createFromParcel(Parcel in) {
            return new RecruiterAccount(in);
        }

        @Override
        public RecruiterAccount[] newArray(int size) {
            return new RecruiterAccount[size];
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
}
