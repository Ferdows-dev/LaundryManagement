package com.learning.laundrymanagement.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learning.laundrymanagement.Adapter.HomeSliderAdapter;
import com.learning.laundrymanagement.Adapter.LookbookAdapter;
import com.learning.laundrymanagement.BookingActivity;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.HistoryActivity;
import com.learning.laundrymanagement.Interface.IBannerLoadListener;
import com.learning.laundrymanagement.Interface.IBookinInfoLoadListener;
import com.learning.laundrymanagement.Interface.ILookbookLoadListener;
import com.learning.laundrymanagement.MainActivity;
import com.learning.laundrymanagement.Model.Banner;
import com.learning.laundrymanagement.Model.BookinInformation;
import com.learning.laundrymanagement.R;
import com.learning.laundrymanagement.Service.PicassoImageLoadingService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements IBannerLoadListener, ILookbookLoadListener {

    private Unbinder unbinder;

    @BindView(R.id.user_info)
    LinearLayout layout_user_info;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.banner_slider)
    Slider banner_slider;
    @BindView(R.id.recyclerViewLookBook)
    RecyclerView recyclerViewLookbook;

    @BindView(R.id.cardViewBookingInfo)
    CardView cardViewBookinInfo;
    @BindView(R.id.txt_laundry_address)
    TextView txt_laundry_address;
    @BindView(R.id.txt_shop)
    TextView txt_shop;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_time_remain)
    TextView txt_time_remain;


    @OnClick(R.id.user_info)
    void onLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sign Out")
                .setMessage("Please confirm You really wanna Sign out")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }



    @OnClick(R.id.cardViewBooking)
    void booking(){
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }
    @OnClick(R.id.cardViewHistory)
    void history(){
        startActivity(new Intent(getActivity(), HistoryActivity.class));
    }







    //Firestore
    CollectionReference bannerRef,lookbookRef;

    //interface
    IBannerLoadListener iBannerLoadListener;
    ILookbookLoadListener iLookbookLoadListener;
    IBookinInfoLoadListener iBookinInfoLoadListener;



    public HomeFragment() {
     bannerRef = FirebaseFirestore.getInstance().collection("Banner");
     lookbookRef = FirebaseFirestore.getInstance().collection("lookbook");
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        loadUserBooking();
//    }

//    private void loadUserBooking() {
//        CollectionReference userBooking = FirebaseFirestore.getInstance()
//                .collection("User")
//                .document(Common.currentUser.getPhoneNumber())
//                .collection("Booking");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE,0);
//        calendar.add(Calendar.HOUR_OF_DAY,0);
//        calendar.add(Calendar.MINUTE,0);
//
//        Timestamp todayTimestamp = new Timestamp(calendar.getTime());
//
//        userBooking
//                .whereGreaterThanOrEqualTo("timestamp",todayTimestamp)
//                .whereEqualTo("done",false)
//                .limit(1)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()){
//                            if (!task.getResult().isEmpty())
//                            {
//                                for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
//                                {
//                                    BookinInformation bookinInformation = queryDocumentSnapshot.toObject(BookinInformation.class);
//                                    iBookinInfoLoadListener.onBookinginfoLoadSuccess(bookinInformation);
//                                    break;
//                                }
//
//                            }else
//                                iBookinInfoLoadListener.onBookinInfoLoadEmpty();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                iBookinInfoLoadListener.onBookinginfoLoadFailed(e.getMessage());
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        unbinder= ButterKnife.bind(this,view);

        //init
        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener = this;
        iLookbookLoadListener = this;


        //check user is logged
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            setUserInfo();
            loadBanner();
            loadLookbook();
//            loadUserBooking();
        }

        return view;
    }

    private void loadLookbook() {
        lookbookRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> lookbook = new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot bannerSnapshot :task.getResult())
                            {
                                Banner banner = bannerSnapshot.toObject(Banner.class);
                                lookbook.add(banner);
                            }
                            iLookbookLoadListener.onLookbookLoadSuccess(lookbook);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                iLookbookLoadListener.onLookbookLoadFailed(e.getMessage());
            }
        });
    }

    private void loadBanner() {
        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners = new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot bannerSnapshot :task.getResult())
                            {
                                Banner banner = bannerSnapshot.toObject(Banner.class);
                                banners.add(banner);
                            }
                            iBannerLoadListener.onBannerLoadSuccess(banners);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    private void setUserInfo() {
        layout_user_info.setVisibility(View.VISIBLE);
        userName.setText(Common.currentUser.getName());
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLookbookLoadSuccess(List<Banner> banners) {

        recyclerViewLookbook.setHasFixedSize(true);
        recyclerViewLookbook.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewLookbook.setAdapter(new LookbookAdapter(getActivity(),banners));

    }

    @Override
    public void onLookbookLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onBookinInfoLoadEmpty() {
//        cardViewBookinInfo.setVisibility(View.GONE);
//
//    }

//    @Override
//    public void onBookinginfoLoadSuccess(BookinInformation bookinInformation) {
//
//        txt_laundry_address.setText(bookinInformation.getLaundryAddress());
//        txt_shop.setText(bookinInformation.getShopName());
//        txt_time.setText(bookinInformation.getTime());
//        String dateRemain= DateUtils.getRelativeTimeSpanString(
//                Long.valueOf(bookinInformation.getTimestamp().toDate().getTime()),
//                Calendar.getInstance().getTimeInMillis(),0).toString();
//
//        txt_time_remain.setText(dateRemain);
//
//        cardViewBookinInfo.setVisibility(View.VISIBLE);
//
//    }

//    @Override
//    public void onBookinginfoLoadFailed(String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//
//    }
}