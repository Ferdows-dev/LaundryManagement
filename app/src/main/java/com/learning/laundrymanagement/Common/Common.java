package com.learning.laundrymanagement.Common;


import android.os.Parcelable;

import com.learning.laundrymanagement.Model.Laundry;
import com.learning.laundrymanagement.Model.Shop;
import com.learning.laundrymanagement.Model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final String KEY_ENABLE_NEXT_BUTTON = "ENABLE_NEXT_BUTTON";
    public static final String KEY_LAUNDRY_SHOP = "SHOP_SAVE";
    public static final String KEY_SHOP_LOAD_DONE = "SHOP_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_SHOP_SELECTED = "SHOP_SELECTED";
    public static final int TIME_SLOT_TOTAL =20 ;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT ="TIME_SLOT" ;
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static String Is_LogIN = "isLogIn";
    public static User currentUser;
    public static Laundry currentLaundry;
    public static int step;
    public static String city="";
    public static Shop currentShop;
    public static int currentTimeSlot =-1;
    public static Calendar bookingDate =Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String ConverTimeSlottoString(int slot) {
        switch (slot)
        {
            case 0:
                return "9.00 - 9.30";
            case 1:
                return "9.30 - 10.00";
            case 2:
                return "10.00 - 10.30";
            case 3:
                return "10.30 - 11.00";
            case 4:
                return "11.00 - 11.30";
            case 5:
                return "11.30 - 12.00";
            case 6:
                return "12.00 - 12.30";
            case 7:
                return "12.30 - 1.00";
            case 8:
                return "1.00 - 1.30";
            case 9:
                return "1.30 - 2.00";
            case 10:
                return "2.00- 2.30";
            case 11:
                return "2.30 - 3.00";
            case 12:
                return "3.00 - 3.30";
            case 13:
                return "3.30 - 4.00";
            case 14:
                return "4.00 - 4.30";
            case 15:
                return "4.30 - 5.00";
            case 16:
                return "5.00 - 5.30";
            case 17:
                return "5.30 - 6.00";
            case 18:
                return "6.00 - 6.30";
            case 19:
                return "6.30 - 7.00";
            default:
                return "Closed";

        }
    }
}
