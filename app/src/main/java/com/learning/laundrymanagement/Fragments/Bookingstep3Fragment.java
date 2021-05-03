package com.learning.laundrymanagement.Fragments;

import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learning.laundrymanagement.Adapter.MyShopAdapter;
import com.learning.laundrymanagement.Adapter.MyTimeSlotAdapter;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.Common.SpaceItemDecoration;
import com.learning.laundrymanagement.EventBus.DisplayTimeSlotEvent;
import com.learning.laundrymanagement.EventBus.ShopDoneEvent;
import com.learning.laundrymanagement.Interface.ITimeSlotListener;
import com.learning.laundrymanagement.Model.TimeSlot;
import com.learning.laundrymanagement.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class Bookingstep3Fragment extends Fragment implements ITimeSlotListener {

    DocumentReference shopDoc;
    ITimeSlotListener iTimeSlotListener;
    AlertDialog alertDialog;

    Unbinder unbinder;

//    Calendar select_date;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calenderView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;
//
//    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Calendar date = Calendar.getInstance();
//            date.add(Calendar.DATE,0);
//            loadAvailableTimeSlotofShop(Common.currentShop.getShopID(),simpleDateFormat.format(date.getTime()));
//        }
//    };

    //Eventbus
    @Override
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
    public void loadAllTimeSlotAvailable(DisplayTimeSlotEvent event)
    {
        if (event.isDisplay()){
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0);
            loadAvailableTimeSlotofShop(Common.currentShop.getShopID(),simpleDateFormat.format(date.getTime()));
        }

    }

    private void loadAvailableTimeSlotofShop(String ShopID, String bookDate) {
        alertDialog.show();

        // /AllLaundry/Chittagong/Branch/ux6RAXjvASQ702cXcOUD/Shop/oNzzrq8V8xkx555zSnsy
        shopDoc = FirebaseFirestore.getInstance()
                .collection("AllLaundry")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentLaundry.getLaundryId())
                .collection("Shop")
                .document(Common.currentShop.getShopID());

        shopDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists())
                    {
                        CollectionReference date =FirebaseFirestore.getInstance()
                                .collection("AllLaundry")
                                .document(Common.city)
                                .collection("Branch")
                                .document(Common.currentLaundry.getLaundryId())
                                .collection("Shop")
                                .document(Common.currentShop.getShopID())
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()){
                                        iTimeSlotListener.onTimeSlotEmpty();
                                    }else
                                    {
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document :task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotListener.onTimeSlotLoadSuccess(timeSlots);

                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });

    }


    static Bookingstep3Fragment instance;
    public static Bookingstep3Fragment getInstance(){
        if (instance==null)
            instance = new Bookingstep3Fragment();

        return instance;


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iTimeSlotListener = this;


        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

//        select_date = Calendar.getInstance();
//        select_date.add(Calendar.DATE,0);
    }

//

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.booking_step_three_fragment,container,false);
        unbinder= ButterKnife.bind(this,view);
        
        initView(view);

        return view; 
    }

    private void initView(View view) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpaceItemDecoration(8));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);

        Calendar endtDate = Calendar.getInstance();
        endtDate.add(Calendar.DATE,2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view,R.id.calenderView)
                .range(startDate,endtDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.bookingDate.getTimeInMillis() != date.getTimeInMillis())
                {
                    Common.bookingDate = date;
                    loadAvailableTimeSlotofShop(Common.currentShop.getShopID(),simpleDateFormat.format(date.getTime()));
                }
            }
        });

    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter =new MyTimeSlotAdapter(getContext(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);

        alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();

    }

    @Override
    public void onTimeSlotEmpty() {
        MyTimeSlotAdapter adapter =new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        alertDialog.dismiss();

    }
}
