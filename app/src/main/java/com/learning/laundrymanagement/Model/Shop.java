package com.learning.laundrymanagement.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements Parcelable {
    private String name,password,username,shopID;
    private long rating;

    public Shop() {

    }

    protected Shop(Parcel in) {
        name = in.readString();
        password = in.readString();
        username = in.readString();
        shopID = in.readString();
        rating = in.readLong();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(username);
        dest.writeString(shopID);
        dest.writeLong(rating);
    }
}
