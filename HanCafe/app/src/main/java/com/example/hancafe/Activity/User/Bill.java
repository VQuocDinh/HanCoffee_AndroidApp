package com.example.hancafe.Activity.User;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Activity.Adapter.OrderStatusAdapter;
import com.example.hancafe.Domain.OrderDetail;
import com.example.hancafe.Domain.Order_Management;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bill extends AppCompatActivity {
    private ImageView btnBack;
    private TextView tvIdOrder, tvStatus, tvDateOrder, tvTotalPrice;
    private Button btnCancel;
    private RecyclerView rvProduct;
    List<Order_Management> orderManagements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        setControl();
        setEvent();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Order_Management orderManagement = intent.getParcelableExtra("orderManagement");
        ArrayList<OrderDetail> orderDetails = intent.getParcelableArrayListExtra("orderDetails");
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);
        OrderStatusAdapter.OrderDetailAdapter orderDetailAdapter = new OrderStatusAdapter.OrderDetailAdapter(orderDetails);
        rvProduct.setAdapter(orderDetailAdapter);
        tvIdOrder.setText(orderManagement.getId());
        if (orderManagement.getIdCategory() == 1) {
            tvStatus.setText("Chờ xác nhận");
        } else if (orderManagement.getIdCategory() == 2) {
            tvStatus.setText("Đang vận chuyển");
        } else if (orderManagement.getIdCategory() == 3) {
            tvStatus.setText("Đã giao");
        } else {
            tvStatus.setText("Đã hủy");
        }
        tvDateOrder.setText(orderManagement.getDate());
        tvTotalPrice.setText(String.valueOf(orderManagement.getPrice()));
    }

    private void setEvent() {
        Order_Management orderManagement = getIntent().getParcelableExtra("orderManagement");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (orderManagement.getIdCategory() != 1){
            btnCancel.setEnabled(false);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Xác nhận hủy đơn hàng!")
                        .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String idOrder = orderManagement.getId();
                                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");
                                orderRef.orderByChild("id").equalTo(idOrder).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            dataSnapshot.getRef().child("idCategory").setValue(4).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    Intent intent = new Intent(Bill.this,Orders.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//                notifyDataSetChanged();

            }
        });
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);
        rvProduct = findViewById(R.id.rvProduct);
        tvIdOrder = findViewById(R.id.tvIdOrder);
        tvStatus = findViewById(R.id.tvStatus);
        tvDateOrder = findViewById(R.id.tvDateOrder);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

    }


}