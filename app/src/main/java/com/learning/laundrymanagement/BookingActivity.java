package com.learning.laundrymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learning.laundrymanagement.Adapter.MyViewPagerAdapter;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.Common.NonSwipeViewPager;
import com.learning.laundrymanagement.EventBus.ConfirmBookingEvent;
import com.learning.laundrymanagement.EventBus.DisplayTimeSlotEvent;
import com.learning.laundrymanagement.EventBus.EnableNextButton;
import com.learning.laundrymanagement.EventBus.ShopDoneEvent;
import com.learning.laundrymanagement.Model.Shop;
import com.shuhart.stepview.StepView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

//    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference shopRef;

    @BindView(R.id.stepView)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.button_previous)
    Button previousBtn;
    @BindView(R.id.button_next)
    Button nextBtn;

    @OnClick(R.id.button_previous)
    void previousBtn(){
        if (Common.step ==3 || Common.step > 0){
            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if(Common.step<3)
            {
                nextBtn.setEnabled(true);
                setColorButton();
            }
        }
    }

    @OnClick(R.id.button_next)
    void nextClick(){

       if (Common.step < 3|| Common.step ==0)
       {
           Common.step++;
           if (Common.step == 1)
           {
               if (Common.currentLaundry !=null)
                   loadShopByLaundry(Common.currentLaundry.getLaundryId());
           }
           else if (Common.step ==2 ){
               if (Common.currentShop != null)
                   loadTimeSlotofShop(Common.currentShop.getShopID());


           }
           else if (Common.step ==3 ){
               if (Common.currentTimeSlot != -1)
                   confirmBooking();


           }
           viewPager.setCurrentItem(Common.step);
       }
    }

    private void confirmBooking() {


        EventBus.getDefault().postSticky(new ConfirmBookingEvent(true));
    }

    private void loadTimeSlotofShop(String shopID) {


        EventBus.getDefault().postSticky(new DisplayTimeSlotEvent(true));
    }

    private void loadShopByLaundry(String shopId) {
        dialog.show();
        ///AllLaundry/Chittagong/Branch/ux6RAXjvASQ702cXcOUD/Shop
        if (!TextUtils.isEmpty(Common.city)){
            shopRef = FirebaseFirestore.getInstance()
                    .collection("AllLaundry")
                    .document(Common.city)
                    .collection("Branch")
                    .document(shopId)
                    .collection("Shop");

            shopRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            ArrayList<Shop> shops = new ArrayList<>();
                            for (QueryDocumentSnapshot shopSnapshot: task.getResult())
                            {
                                Shop shop = shopSnapshot.toObject(Shop.class);
                                shop.setPassword("");
                                shop.setShopID(shopSnapshot.getId());
                                shops.add(shop);
                            }
//

                            EventBus.getDefault().postSticky(new ShopDoneEvent(shops));

                            dialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    dialog.dismiss();
                }
            });
        }

    }



    //eventbus
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void buttonNextReciver(EnableNextButton event)
    {
        int step =event.getStep();
        if (step == 1)
            Common.currentLaundry = event.getLaundry();
        else if (step == 2)
            Common.currentShop = event.getShop();
        else if (step == 3)
            Common.currentTimeSlot = event.getTimeSlot();



        nextBtn.setEnabled(true);
        setColorButton();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        dialog= new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

//        localBroadcastManager = LocalBroadcastManager.getInstance(this);
//        localBroadcastManager.registerReceiver(buttonNextReciver,new IntentFilter(Common.KEY_ENABLE_NEXT_BUTTON));
        
        setupStepView();
        setColorButton();

        //view
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stepView.go(position,true);
                if (position == 0){
                    previousBtn.setEnabled(false);
                }else {
                    previousBtn.setEnabled(true);

                    nextBtn.setEnabled(false);
                    setColorButton();
                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setColorButton() {
        if (nextBtn.isEnabled())
        {
            nextBtn.setBackgroundResource(R.color.purple_500);
        }
        else {
            nextBtn.setBackgroundResource(R.color.design_default_color_background);
        }
        if (previousBtn.isEnabled())
        {
            previousBtn.setBackgroundResource(R.color.purple_500);
        }
        else {
            previousBtn.setBackgroundResource(R.color.design_default_color_background);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Laundry");
        stepList.add("Shop");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}