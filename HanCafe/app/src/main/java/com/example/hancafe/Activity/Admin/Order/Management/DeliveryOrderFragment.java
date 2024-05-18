package com.example.hancafe.Activity.Admin.Order.Management;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Activity.Adapter.OrderManagementAdminAdapter;
import com.example.hancafe.Activity.Dialog.BottomSheetDialogOrderManagement;
import com.example.hancafe.Model.OrderDetail;
import com.example.hancafe.Model.OrderManagement;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DeliveryOrderFragment extends Fragment{
    RecyclerView rcvCategoryOrderManagement;
    LinearLayout lnEmpty;
    TextView tvEmpty;
    private static final int STATUS_DELIVERY = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_category_order_management, container, false);

        rcvCategoryOrderManagement = view.findViewById(R.id.rcvOrderManagementCategory);

        rcvCategoryOrderManagement = view.findViewById(R.id.rcvOrderManagementCategory);

        lnEmpty = view.findViewById(R.id.lnEmpty);

        tvEmpty = view.findViewById(R.id.tvEmpty);
        String title = getResources().getString(R.string.title_order_status_delivery_empty);
        tvEmpty.setText(title);

        HelperOrderManagement.loadDataOrderManagement(getContext(), rcvCategoryOrderManagement, lnEmpty, STATUS_DELIVERY);

        return view;
    }

}