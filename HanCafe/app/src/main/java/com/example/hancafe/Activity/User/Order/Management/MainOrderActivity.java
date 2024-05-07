package com.example.hancafe.Activity.User.Order.Management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hancafe.Activity.Adapter.OrderManagementUserViewPagerAdapter;
import com.example.hancafe.R;
import com.google.android.material.tabs.TabLayout;

public class MainOrderActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OrderManagementUserViewPagerAdapter orderViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_management);

        // Nút Back
        Button btnBackStack = findViewById(R.id.btnBackStack);
        btnBackStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tlOrder);
        viewPager2 = findViewById(R.id.vpOrder);
        orderViewPagerAdapter = new OrderManagementUserViewPagerAdapter(this);
        viewPager2.setAdapter(orderViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
}