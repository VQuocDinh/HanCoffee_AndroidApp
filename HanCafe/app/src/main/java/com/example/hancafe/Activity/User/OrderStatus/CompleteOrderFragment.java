package com.example.hancafe.Activity.User.OrderStatus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Activity.Adapter.OrderStatusAdapter;
import com.example.hancafe.Domain.Order_Management;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompleteOrderFragment extends Fragment {
    RecyclerView rvProduct;
    List<Order_Management> data;
    OrderStatusAdapter orderStatusAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_complete, container, false);
        rvProduct = view.findViewById(R.id.rvProduct);
        initData();
        return view;
    }

    private void initData() {
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Order_Management");
        Query query = myRef.orderByChild("idCategory").equalTo(3);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String date = dataSnapshot.child("dateTime").getValue(String.class);
                    String id = dataSnapshot.child("id").getValue(String.class);
                    int idCategory = dataSnapshot.child("idCategory").getValue(Integer.class);
                    String productName = dataSnapshot.child("name").getValue(String.class);
                    String productImg = dataSnapshot.child("picure").getValue(String.class);
                    int totalPrice = dataSnapshot.child("price").getValue(Integer.class);
                    String idUser = dataSnapshot.child("idUser").getValue(String.class);

                    Order_Management product = new Order_Management(idCategory, totalPrice, date, id, idUser);
                    data.add(product);

                }

                orderStatusAdapter = new OrderStatusAdapter(data,  getActivity().getApplicationContext());
                rvProduct.setAdapter(orderStatusAdapter);

//                productsAdapter.setOnItemProductClickListener(HomeFragment.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}