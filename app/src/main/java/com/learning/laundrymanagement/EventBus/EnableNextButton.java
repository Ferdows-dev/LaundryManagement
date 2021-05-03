package com.learning.laundrymanagement.EventBus;

import com.learning.laundrymanagement.Model.Laundry;
import com.learning.laundrymanagement.Model.Shop;

public class EnableNextButton {

    private int step;
    private Shop shop;
    private Laundry laundry;
    private int timeSlot;

    public EnableNextButton(int step, int timeSlot) {
        this.step = step;
        this.timeSlot = timeSlot;
    }

    public EnableNextButton(int step, Shop shop) {
        this.step = step;
        this.shop = shop;
    }

    public EnableNextButton(int step, Laundry laundry) {
        this.step = step;
        this.laundry = laundry;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setLaundry(Laundry laundry) {
        this.laundry = laundry;
    }

    public Laundry getLaundry() {
        return laundry;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }
}
