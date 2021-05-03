package com.learning.laundrymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.learning.laundrymanagement.Common.Common;
import com.learning.laundrymanagement.Fragments.HomeFragment;
import com.learning.laundrymanagement.Fragments.ShoppingFragment;
import com.learning.laundrymanagement.Model.User;
import com.learning.laundrymanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        userRef = FirebaseFirestore.getInstance().collection("User");
        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        if (getIntent() !=null){
            boolean isLogin = getIntent().getBooleanExtra(Common.Is_LogIN,false);
            if (isLogin)
            {
                alertDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference currentUser = userRef.document(user.getPhoneNumber());
                currentUser.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    DocumentSnapshot userSnapshot = task.getResult();
                                    if (!userSnapshot.exists())
                                    {
                                        showUpdateDialogue(user.getPhoneNumber());
                                    }else
                                    {
                                        //if user alredy avaiable in our system
                                        Common.currentUser = userSnapshot.toObject(User.class);
                                        bottomNavigationView.setSelectedItemId(R.id.action_home);
                                    }
                                    if (alertDialog.isShowing())
                                        alertDialog.dismiss();
                                }
                            }
                        });


            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if (item.getItemId() == R.id.action_shopping)
                    fragment = new ShoppingFragment();
                return loadFragment(fragment);
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Nav,fragment).commit();
            return true;
        }
        return false;
    }

    private void showUpdateDialogue(String phoneNumber) {



        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information,null);

        Button updateBtn = sheetView.findViewById(R.id.update_btn);
        final TextInputEditText name = sheetView.findViewById(R.id.nameEt);
        final TextInputEditText address = sheetView.findViewById(R.id.addressEt);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!alertDialog.isShowing())
                    alertDialog.show();

              final   User user = new User(name.getText().toString(),
                        address.getText().toString(),phoneNumber);
                userRef.document(phoneNumber)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                bottomSheetDialog.dismiss();
                                if (alertDialog.isShowing())
                                    alertDialog.dismiss();

                                Common.currentUser = user;
                                Toast.makeText(HomeActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bottomSheetDialog.dismiss();
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}