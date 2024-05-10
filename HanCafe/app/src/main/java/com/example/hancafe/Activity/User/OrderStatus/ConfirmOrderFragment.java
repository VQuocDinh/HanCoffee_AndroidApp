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
import com.example.hancafe.Activity.Adapter.OrderStatusConfirmAdapter;
import com.example.hancafe.Domain.OrderDetail;
import com.example.hancafe.Domain.Order_Management;
import com.example.hancafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderFragment extends Fragment {
    RecyclerView rvProduct;
    List<Order_Management> orderManagements;
    List<OrderDetail> orderDetails;
    OrderStatusAdapter orderStatusAdapter;
    int countOrderDetail =0;
    private static final int confirm = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_confirm, container, false);
        rvProduct = view.findViewById(R.id.rvProduct);
        initData();
        return view;
    }

    private void initData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String idUser = currentUser != null ? currentUser.getUid() : "";
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderManagementRef = database.getReference("Order_Management");
        Query query = orderManagementRef.orderByChild("idUser").equalTo(idUser);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderManagements = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int status = dataSnapshot.child("idCategory").getValue(Integer.class);
                    if (status == confirm) {
                        String date = dataSnapshot.child("date").getValue(String.class);
                        String idOrder = dataSnapshot.child("id").getValue(String.class);
                        int totalPrice = dataSnapshot.child("price").getValue(Integer.class);
                        String idUser = dataSnapshot.child("idUser").getValue(String.class);
                        Order_Management orderManagement = new Order_Management(status, totalPrice, date, idOrder, idUser);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference orderDetailRef = database.getReference("OrderDetail");
                        Query query = orderDetailRef.orderByChild("idOrder").equalTo(idOrder);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                orderDetails = new ArrayList<>();
                                for (DataSnapshot orderDetailSnaphot: snapshot.getChildren()){
                                    int size = orderDetailSnaphot.child("idSize").getValue(Integer.class);
                                    int priceProduct = orderDetailSnaphot.child("priceProduct").getValue(Integer.class);
                                    int quantity = orderDetailSnaphot.child("quantity").getValue(Integer.class);

                                    String idOrder = orderDetailSnaphot.child("idOrder").getValue(String.class);
                                    String imgProduct = orderDetailSnaphot.child("imgProduct").getValue(String.class);
                                    String nameProduct = orderDetailSnaphot.child("nameProduct").getValue(String.class);
                                    String idOrderDetail = orderDetailSnaphot.child("idOrderDetail").getValue(String.class);
                                    OrderDetail orderDetail = new OrderDetail(idOrderDetail,idOrder,imgProduct,nameProduct,size,quantity,priceProduct);
                                    orderDetails.add(orderDetail);
                                }
                                orderManagement.setOrderDetails(orderDetails);
                                orderManagements.add(orderManagement);
                                orderStatusAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                }
                orderStatusAdapter = new OrderStatusAdapter(orderManagements,  getActivity().getApplicationContext());
                rvProduct.setAdapter(orderStatusAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}