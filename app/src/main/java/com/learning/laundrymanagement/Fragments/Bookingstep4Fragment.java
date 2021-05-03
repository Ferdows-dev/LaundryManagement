package com.learning.laundrymanagement.Fragments;

import android.app.AlertDialog;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.EventBus.ConfirmBookingEvent;
import com.learning.laundrymanagement.Model.BookinInformation;
import com.learning.laundrymanagement.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class Bookingstep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;

    Unbinder unbinder;
    AlertDialog dialog;

    @BindView(R.id.booking_shop_text)
    TextView booking_shop_text;
    @BindView(R.id.booking_time_text)
    TextView booking_time_text;
    @BindView(R.id.booking_user_adresss_text)
    TextView booking_user_adresss_text;
    @BindView(R.id.shop_location_text)
    TextView shop_address_text;
    @BindView(R.id.laundry_name_text)
    TextView shop_name;
    @BindView(R.id.shop_open_hours_text)
    TextView shop_open_hours_text;
    @BindView(R.id.shop_phone_text)
    TextView shop_phone_text;
    @BindView(R.id.shop_website_text)
    TextView shop_website_text;
    @OnClick(R.id.btn_confirm)
            void confirmBooking(){

        dialog.show();

//        String startTime = Common.ConverTimeSlottoString(Common.currentTimeSlot);
//        String[] convertTime= startTime.split("-");
//        String[] startTimeConvert = convertTime[0].split(":");
//        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
//        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());
//
//
//        Calendar bookingDatewithHourHouse = Calendar.getInstance();
//        bookingDatewithHourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
//        bookingDatewithHourHouse.set(Calendar.HOUR_OF_DAY,startHourInt);
//        bookingDatewithHourHouse.set(Calendar.MINUTE,startMinInt);

//        Timestamp timestamp = new Timestamp(bookingDatewithHourHouse.getTime());



        BookinInformation bookinInformation= new BookinInformation();

//        bookinInformation.setTimestamp(timestamp);
        bookinInformation.setDone(true);
        bookinInformation.setShopId(Common.currentShop.getShopID());
        bookinInformation.setShopName(Common.currentShop.getName());
        bookinInformation.setCustomerName(Common.currentUser.getName());
        bookinInformation.setCustomerAddress(Common.currentUser.getPhoneNumber());
        bookinInformation.setLaundryId(Common.currentLaundry.getLaundryId());
        bookinInformation.setLaundryAddress(Common.currentLaundry.getAddress());
        bookinInformation.setLaundryName(Common.currentLaundry.getName());
        bookinInformation.setTime(new StringBuilder(Common.ConverTimeSlottoString(Common.currentTimeSlot))
                .append("at")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())).toString());
        bookinInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        DocumentReference bookinDate = FirebaseFirestore.getInstance()
                .collection("AllLaundry")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentLaundry.getLaundryId())
                .collection("Shop")
                .document(Common.currentShop.getShopID())
                .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        bookinDate.set(bookinInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addToUserBookin(bookinInformation);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToUserBookin( BookinInformation bookinInformation) {


        CollectionReference userBooking = FirebaseFirestore.getInstance()
//                .collection("User")
//                .document(Common.currentUser.getPhoneNumber())
                .collection("Booking");


        userBooking.whereEqualTo("done",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty())
                        {
                            userBooking.document()
                                    .set(bookinInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            if (dialog.isShowing())
                                                dialog.dismiss();

                                            resetStaticData();
                                            getActivity().finish();
                                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
//                                            addToCalender(Common.bookingDate,Common.ConverTimeSlottoString(Common.currentTimeSlot));


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();

                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            if (dialog.isShowing())
                                dialog.dismiss();

                            resetStaticData();
                            getActivity().finish();
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

//    private void addToCalender(Calendar bookingDate, String startDate) {
//        String startTime = Common.ConverTimeSlottoString(Common.currentTimeSlot);
//        String[] convertTime= startTime.split("-");
//        String[] startTimeConvert = convertTime[0].split(":");
//        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
//        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());
//
//        String[] endTimeConvert = convertTime[0].split(":");
//        int endHourInt = Integer.parseInt(endTimeConvert[0].trim());
//        int endMinInt = Integer.parseInt(endTimeConvert[1].trim());
//
//        Calendar startEvent = Calendar.getInstance();
//        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
//        startEvent.set(Calendar.HOUR_OF_DAY,startHourInt);
//        startEvent.set(Calendar.MINUTE,startMinInt);
//
//        Calendar endEvent = Calendar.getInstance();
//        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
//        endEvent.set(Calendar.HOUR_OF_DAY,endHourInt);
//        endEvent.set(Calendar.MINUTE,endMinInt);
//
//        SimpleDateFormat calenderDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//        String startEvenetTime =calenderDateFormat.format(startEvent.getTime());
//        String endEvenetTime =calenderDateFormat.format(endEvent.getTime());
//
//        addToDeviceCalender(startEvenetTime,endEvenetTime,"Laundry Booking",
//                new StringBuilder("Booking from ")
//        .append(startTime)
//        .append("with")
//        .append(Common.currentShop.getName())
//        .append("at")
//        .append(Common.currentLaundry.getName()),
//        new StringBuilder("Address: ").append(Common.currentLaundry.getAddress()));
//
//
//    }

//    private void addToDeviceCalender(String startEvenetTime, String endEvenetTime, String title, StringBuilder description, StringBuilder location) {
//        SimpleDateFormat calenderDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//
//        try {
//            Date start = calenderDateFormat.parse(startEvenetTime);
//            Date end = calenderDateFormat.parse(endEvenetTime);
//
//            ContentValues event = new ContentValues();
//            event.put(CalendarContract.Events.CALENDAR_ID,getCalender(getContext()));
//            event.put(CalendarContract.Events.TITLE,title);
//            event.put(CalendarContract.Events.DESCRIPTION, String.valueOf(description));
//            event.put(CalendarContract.Events.EVENT_LOCATION, String.valueOf(location));
//
//            event.put(CalendarContract.Events.DTSTART,start.getTime());
//            event.put(CalendarContract.Events.DTEND,end.getTime());
//            event.put(CalendarContract.Events.ALL_DAY,0);
//            event.put(CalendarContract.Events.HAS_ALARM,1);
//
//            String timeZone = TimeZone.getDefault().getID();
//            event.put(CalendarContract.Events.EVENT_TIMEZONE,timeZone);
//
//            Uri calenders;
//            if (Build.VERSION.SDK_INT>= 8)
//            calenders = Uri.parse("content://com.android.calender/events");
//            else
//                calenders = Uri.parse("content://calender/events");
//
//
//            getActivity().getContentResolver().insert(calenders,event);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }


//    private String getCalender(Context context) {
//        String gmailIdCalender = "";
//        String projection[] = {"_id","calender_displayName"};
//        Uri calenders = Uri.parse("content://com.android.calender/calenders");
//
//        ContentResolver contentResolver = context.getContentResolver();
//
//        Cursor managedCursor = contentResolver.query(calenders,projection,null,null,null);
//        if (managedCursor.moveToFirst()){
//            String calName;
//            int nameCol = managedCursor.getColumnIndex(projection[1]);
//            int idCol = managedCursor.getColumnIndex(projection[0]);
//            do {
//                calName =managedCursor.getString(nameCol);
//                if (calName.contains("@gmail.com")){
//                    gmailIdCalender = managedCursor.getString(idCol);
//                    break;
//                }
//
//
//            }while (managedCursor.moveToNext());
//            managedCursor.close();
//
//        }
//
//
//        return gmailIdCalender;
//    }

    private void resetStaticData() {
        Common.step=0;
        Common.currentTimeSlot =-1;
        Common.currentLaundry = null;
        Common.currentShop = null;
        Common.bookingDate.add(Calendar.DATE,0);
    }


    //Eventbus
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void setDataBooking(ConfirmBookingEvent event)
    {
        if (event.isConfirm())
        {
            setData();
        }
    }

    private void setData() {
        booking_shop_text.setText(Common.currentShop.getName());
        booking_user_adresss_text.setText(Common.currentUser.getAddress());
        booking_time_text.setText(new StringBuilder(Common.ConverTimeSlottoString(Common.currentTimeSlot))
        .append("at")
        .append(simpleDateFormat.format(Common.bookingDate.getTime())));

        shop_address_text.setText(Common.currentLaundry.getAddress());
        shop_website_text.setText(Common.currentLaundry.getWebsite());
        shop_name.setText(Common.currentLaundry.getName());
        shop_open_hours_text.setText(Common.currentLaundry.getOpenHour());


    }


    static Bookingstep4Fragment instance;
    public static Bookingstep4Fragment getInstance(){
        if (instance==null)
            instance = new Bookingstep4Fragment();

        return instance;


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.booking_step_four_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }
}
