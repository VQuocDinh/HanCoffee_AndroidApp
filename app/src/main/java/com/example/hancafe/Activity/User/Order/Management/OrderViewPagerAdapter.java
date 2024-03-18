package com.example.hancafe.Activity.User.Order.Management;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderViewPagerAdapter extends FragmentStateAdapter {


    public OrderViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ConfirmOrderFragment();
            case 1:
                return new DeliveryOrderFragment();
            case 2:
                return new CompleteOrderFragment();
            case 3:
                return new CancelOrderFragment();
            default:
                return new ConfirmOrderFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
