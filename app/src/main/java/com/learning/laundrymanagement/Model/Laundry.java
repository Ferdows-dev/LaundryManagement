package com.learning.laundrymanagement.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Laundry implements Parcelable {
    private String name,address, website,phone,openHour,laundryId;

    public Laundry() {
    }

    protected Laundry(Parcel in) {
        name = in.readString();
        address = in.readString();
        website = in.readString();
        phone = in.readString();
        openHour = in.readString();
        laundryId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(website);
        dest.writeString(phone);
        dest.writeString(openHour);
        dest.writeString(laundryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Laundry> CREATOR = new Creator<Laundry>() {
        @Override
        public Laundry createFromParcel(Parcel in) {
            return new Laundry(in);
        }

        @Override
        public Laundry[] newArray(int size) {
            return new Laundry[size];
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getLaundryId() {
        return laundryId;
    }

    public void setLaundryId(String laundryId) {
        this.laundryId = laundryId;
    }
}
