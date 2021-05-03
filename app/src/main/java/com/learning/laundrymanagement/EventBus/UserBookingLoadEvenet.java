package com.learning.laundrymanagement.EventBus;

import com.learning.laundrymanagement.Model.BookinInformation;

import java.util.List;

public class UserBookingLoadEvenet {
    private boolean success;
    private String message;
    private List<BookinInformation> bookinInformationList;

    public UserBookingLoadEvenet(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UserBookingLoadEvenet(boolean success, List<BookinInformation> bookinInformationList) {
        this.success = success;
        this.bookinInformationList = bookinInformationList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String isMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BookinInformation> getBookinInformationList() {
        return bookinInformationList;
    }

    public void setBookinInformationList(List<BookinInformation> bookinInformationList) {
        this.bookinInformationList = bookinInformationList;
    }
}
