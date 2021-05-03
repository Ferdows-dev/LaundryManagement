package com.learning.laundrymanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.learning.laundrymanagement.Common.Common;


import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class
MainActivity extends AppCompatActivity {

    private static final int APP_REQUEST_CODE = 7117;
    private List<AuthUI.IdpConfig> providers;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @BindView(R.id.log_in_btn)
    Button loginButton;

    @BindView(R.id.tv_skip)
    TextView tvSkip;

    @BindView(R.id.admin_log_in_btn)
    Button adminButton;

    @OnClick(R.id.admin_log_in_btn)
    void AdminLogin(){
     Intent intent = new Intent(MainActivity.this,AdminLogInActivity.class);
     startActivity(intent);
    }

    @OnClick(R.id.log_in_btn)
    void logInUser() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers).build(), APP_REQUEST_CODE);

    }

    @OnClick(R.id.tv_skip)
    void skipToHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra(Common.Is_LogIN, false);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        if (authStateListener != null)
            mAuth.removeAuthStateListener(authStateListener);
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "Failed to Log in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        mAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
//
//
            FirebaseUser user = mAuth.getCurrentUser();

            if (user!=null){
//                checkUserFromFirebase(user);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra(Common.Is_LogIN,true);
                startActivity(intent);
                finish();
            }
            else {
                setContentView(R.layout.activity_main);
                ButterKnife.bind(MainActivity.this);
            }


        };


//        Dexter.withActivity(this)
//                .withPermissions(new String[]{
//                        Manifest.permission.READ_CALENDAR,
//                        Manifest.permission.WRITE_CALENDAR
//                }).withListener(new MultiplePermissionsListener() {
//            @Override
//            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                FirebaseUser user = mAuth.getCurrentUser();
//                if(user!=null){
//                    mAuth = FirebaseAuth.getInstance();
//                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                    intent.putExtra(Common.Is_LogIN,true);
//                    startActivity(intent);
//                    finish();
//                }
//
//
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//            }
//        }).check();









    }

//    private void checkUserFromFirebase(FirebaseUser user) {
//        mAuth = FirebaseAuth.getInstance();
//        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//        intent.putExtra(Common.Is_LogIN,true);
//        startActivity(intent);
//        finish();
//    }
}