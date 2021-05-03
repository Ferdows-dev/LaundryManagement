package com.learning.laundrymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.learning.laundrymanagement.Adapter.MyHistoryAdapter;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.EventBus.UserBookingLoadEvenet;
import com.learning.laundrymanagement.Model.BookinInformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class AdminOrderActivity extends AppCompatActivity {
    @BindView(R.id.recycler_history)
    RecyclerView recycler_history;
    @BindView(R.id.txt_history)
    TextView txt_history;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);


        ButterKnife.bind(this);

        init();
        initView();
        loadUserBookingInformation();
    }

    private void loadUserBookingInformation() {
        dialog.show();
        //    /User/+8801749454193/Booking
        ///AllLaundry/Chittagong/Branch/ux6RAXjvASQ702cXcOUD/Shop/oNzzrq8V8xkx555zSnsy/25/03/2021/1

        CollectionReference userBooking = FirebaseFirestore.getInstance()
//                .collection("User")
//                .document(Common.currentUser.getPhoneNumber())
                .collection("Booking");



        userBooking.whereEqualTo("done",true)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        EventBus.getDefault().post(new UserBookingLoadEvenet(false,e.getMessage()));
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<BookinInformation> informationArrayList = new ArrayList<>();
                    for (DocumentSnapshot userBookingSnapShot :task.getResult()){
                        BookinInformation bookinInformation = userBookingSnapShot.toObject(BookinInformation.class);
                        informationArrayList.add(bookinInformation);
                    }
                    EventBus.getDefault().post(new UserBookingLoadEvenet(true,informationArrayList));
                }
            }
        });

    }

    private void initView() {
        recycler_history.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_history.setLayoutManager(linearLayoutManager);
        recycler_history.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));

    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void displayData(UserBookingLoadEvenet evenet)
    {
        if (evenet.isSuccess())
        {
            MyHistoryAdapter adapter = new MyHistoryAdapter(this,evenet.getBookinInformationList());
            recycler_history.setAdapter(adapter);

            txt_history.setText(new StringBuilder("Order (")
                    .append(evenet.getBookinInformationList().size())
                    .append(")"));

        }else {

        }
        dialog.dismiss();
    }

}
