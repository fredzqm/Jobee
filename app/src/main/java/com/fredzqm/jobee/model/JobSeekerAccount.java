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
    private Address address;

    private String stringAddress;

    public JobSeekerAccount() {
        // empty constructor required by Jackson
    }

    public JobSeekerAccount(String emailAccount) {
        this.emailAccount = emailAccount;
        this.name = "";
        this.address = null;
    }

    public static JobSeekerAccount newInstance() {
        return new JobSeekerAccount("fredzqm@gmail.com");
    }

    public String getDisplayedAddress(){
        if (stringAddress != null)
            return stringAddress;
        if (address == null || address.getMaxAddressLineIndex() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            sb.append(address.getAddressLine(i) + "\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }


    //  getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getStringAddress() {
        return stringAddress;
    }

    public void setStringAddress(String stringAddress) {
        this.stringAddress = stringAddress;
    }

    // parcelable implementation

    public JobSeekerAccount(Parcel in) {
        name = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(address, flags);
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
