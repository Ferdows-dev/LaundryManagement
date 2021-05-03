package com.learning.laundrymanagement.Interface;


import com.learning.laundrymanagement.Model.TimeSlot;

import java.util.List;

public interface ITimeSlotListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotEmpty();

}
