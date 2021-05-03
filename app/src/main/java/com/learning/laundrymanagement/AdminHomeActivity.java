package com.learning.laundrymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void updateProfile(View view) {
        Intent intent = new Intent(AdminHomeActivity.this, AdminProfileActivity.class);
        startActivity(intent);
    }

    public void SeeOrder(View view) {
        Intent intent = new Intent(AdminHomeActivity.this, AdminOrderActivity.class);
        startActivity(intent);
    }
}