package com.example.hancafe.Activity.User.Order.Management;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hancafe.Activity.Adapter.OrderManagementUserAdapter;

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

public class DeliveryOrderFragment extends Fragment {
    RecyclerView rcvCategoryOrderManagement;
    OrderManagementUserAdapter orderManagementAdapter;
    Context context;
    ArrayList<OrderManagement> list = new ArrayList<>();

    DecimalFormat df;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_category_order_management, container, false);

        rcvCategoryOrderManagement = view.findViewById(R.id.rcvOrderManagementCategory);
        rcvCategoryOrderManagement.setLayoutManager(new LinearLayoutManager(context));

        orderManagementAdapter= new OrderManagementUserAdapter(context, list);
        rcvCategoryOrderManagement.setAdapter(orderManagementAdapter);

        // Lấy idCategory cho fragment hiện tại
        int categoryId = 2;

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");

        // Tạo query để lọc theo idCategory
        Query query = orderRef.orderByChild("idCategory").equalTo(categoryId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String id = orderSnapshot.child("id").getValue(String.class);
                    String name = orderSnapshot.child("name").getValue(String.class);
                    Integer price = orderSnapshot.child("price").getValue(Integer.class);
                    String dateTime = orderSnapshot.child("date").getValue(String.class);
                    String picture = orderSnapshot.child("picure").getValue(String.class);
                    Integer idCategory = orderSnapshot.child("idCategory").getValue(Integer.class);
                    String idUser = orderSnapshot.child("idUser").getValue(String.class);

                    OrderManagement order = new OrderManagement(id, name, price, dateTime, picture, idCategory, idUser);
                    list.add(order);
                }
                orderManagementAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}