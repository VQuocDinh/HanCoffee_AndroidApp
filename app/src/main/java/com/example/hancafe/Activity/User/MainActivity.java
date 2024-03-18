package com.example.hancafe.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.MenuItem;

import com.example.hancafe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    OrderFragment orderFragment = new OrderFragment();
    StoreFragment storeFragment = new StoreFragment();
    OtherFragment otherFragment = new OtherFragment();

    //Quản lý đơn hàng


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Menu chính
        bottomNavigationView = findViewById(R.id.bottomNavView);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.order) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, orderFragment).commit();
                    return true;
                } else if (itemId == R.id.store) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, storeFragment).commit();
                    return true;
                } else if (itemId == R.id.other) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, otherFragment).commit();
                    return true;
                }

                return false;
            }
        });

    }
}