package com.learning.laundrymanagement.Model;

import com.google.firebase.Timestamp;

public class BookinInformation {
    private String customerName, customerAddress,time,shopId,shopName,laundryId,laundryName,laundryAddress;
    private long slot;
    private Timestamp timestamp;
    private boolean done;

    public BookinInformation() {
    }

    public BookinInformation(String customerName, String customerAddress, String time, String shopId, String shopName, String laundryId, String laundryName, String laundryAddress, long slot) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.time = time;
        this.shopId = shopId;
        this.shopName = shopName;
        this.laundryId = laundryId;
        this.laundryName = laundryName;
        this.laundryAddress = laundryAddress;
        this.slot = slot;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLaundryId() {
        return laundryId;
    }

    public void setLaundryId(String laundryId) {
        this.laundryId = laundryId;
    }

    public String getLaundryName() {
        return laundryName;
    }

    public void setLaundryName(String laundryName) {
        this.laundryName = laundryName;
    }

    public String getLaundryAddress() {
        return laundryAddress;
    }

    public void setLaundryAddress(String laundryAddress) {
        this.laundryAddress = laundryAddress;
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(long slot) {
        this.slot = slot;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
