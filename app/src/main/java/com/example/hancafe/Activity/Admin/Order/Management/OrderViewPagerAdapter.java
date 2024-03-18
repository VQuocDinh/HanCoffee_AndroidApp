package com.example.hancafe.Admin.Order.Management;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hancafe.Admin.OrderAdminFragment;

public class OrderViewPagerAdapter extends FragmentStateAdapter {


    public OrderViewPagerAdapter(@NonNull OrderAdminFragment fragmentActivity) {
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
