package com.example.hancafe.Activity.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Activity.Adapter.OrderStatusAdapter;
import com.example.hancafe.Activity.Adapter.PayAdapter;
import com.example.hancafe.Domain.CartItem;
import com.example.hancafe.Domain.Order_Management;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Pay extends AppCompatActivity {
    private ImageView btnBack;
    private TextView btnUpdateAddress, tvTotalPrice, tvTen, tvSdt, tvDiaChi;
    private Button btnOrder;
    private RecyclerView rvProduct;
    private PayAdapter payAdapter;
    List<CartItem> receivedList;
    int totalPrice = 0;
    private Spinner spnDelivery, spnPay;
    List<String> shipMethod = new ArrayList<>();
    List<String> payMethod = new ArrayList<>();
    OrderStatusAdapter orderStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setControl();
        initData();
        setEvent();
    }

    private void initData() {
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedList")) {
            String jsonString = intent.getStringExtra("selectedList");

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<CartItem>>() {
            }.getType();
            receivedList = gson.fromJson(jsonString, listType);
            payAdapter = new PayAdapter(receivedList);
            rvProduct.setAdapter(payAdapter);
            receivedList.size();
        }

        totalPrice = payAdapter.calculateTotalPrice();
        tvTotalPrice.setText(String.valueOf(totalPrice));
    }

    private void setEvent() {

        shipMethod.add("Chuyển phát nhanh");
        shipMethod.add("Giao hàng tiết kiệm");
        payMethod.add("Chuyển khoản");
        payMethod.add("Tiền mặt");

        ArrayAdapter<String> adapterShipMethod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shipMethod) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(14);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(14);
                return view;
            }
        };

        ArrayAdapter<String> adapterPayMethod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, payMethod) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(14); // Đặt kích thước chữ là 16dp
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(14); // Đặt kích thước chữ là 16dp
                return view;
            }
        };

        adapterShipMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPayMethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDelivery.setAdapter(adapterShipMethod);
        spnPay.setAdapter(adapterPayMethod);

        btnOrder.setBackgroundColor(getResources().getColor(R.color.mainColor));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pay.this, MainActivity.class);
                intent.putExtra("openFragment", "cart");
                startActivity(intent);
            }
        });

        btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference orderManagementRef = firebaseDatabase.getReference("Order_Management");

                orderManagementRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String idUser = "";
                        if (currentUser != null) {
                            idUser = currentUser.getUid();
                        }
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        String curentDay = day + "/" + month + "/" + year;
//
//                        int maxId = 0;
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            int id = dataSnapshot.child("id").getValue(Integer.class);
//                            if (id > maxId) {
//                                maxId = id;
//                            }
//                        }
//                        int newId = maxId + 1;
                        for (CartItem cartItem : receivedList) {
                            DatabaseReference newRef = orderManagementRef.push();
                            String id = newRef.getKey();
                            Order_Management orderManagement = new Order_Management(1, cartItem.getProductPrice(), cartItem.getProductName(), cartItem.getProductImg(), curentDay,id, idUser);
                            orderManagementRef.child(id).setValue(orderManagement);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                for (CartItem cartItem : receivedList) {
                    DatabaseReference cartDetailRef = FirebaseDatabase.getInstance().getReference("CartDetail");
                    cartDetailRef.orderByChild("idCartItem").equalTo(cartItem.getIdCartItem()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
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
                Toast.makeText(Pay.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Pay.this, Orders.class);
                startActivity(intent);
            }
        });


    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_address, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        Button btnUpdate = bottomSheetDialog.findViewById(R.id.btnUpdate);
        TextView tvTenUpdate = bottomSheetDialog.findViewById(R.id.tvTenUpdate);
        TextView tvSdtUpdate = bottomSheetDialog.findViewById(R.id.tvSdtUpdate);
        TextView tvDiaChiUpdate = bottomSheetDialog.findViewById(R.id.tvDiaChiUpdate);

        btnUpdate.setBackgroundColor(getResources().getColor(R.color.mainColor));
        tvTenUpdate.setText(tvTen.getText());
        tvSdtUpdate.setText(tvSdt.getText());
        tvDiaChiUpdate.setText(tvDiaChi.getText());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTenUpdate.getText() == "" || tvSdtUpdate.getText() == "" || tvDiaChiUpdate.getText() == "") {
                    Toast.makeText(Pay.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();

                } else {
                    tvTen.setText(tvTenUpdate.getText());
                    tvSdt.setText(tvSdtUpdate.getText());
                    tvDiaChi.setText(tvDiaChiUpdate.getText());
                    bottomSheetDialog.cancel();
                }
            }
        });
        bottomSheetDialog.show();
//        orderStatusAdapter.notifyDataSetChanged();

    }
//
//    private void showBottomSheetAddressPlus(){
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_add_address, null);
//        bottomSheetDialog.setContentView(bottomSheetView);
//        bottomSheetDialog.show();
//    }

    public void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setControl() {

        btnUpdateAddress = findViewById(R.id.btnUpdateAddress);
        btnBack = findViewById(R.id.btnBack);
        btnOrder = findViewById(R.id.btnOrder);
        rvProduct = findViewById(R.id.rvProduct);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        spnDelivery = findViewById(R.id.spnDelivery);
        spnPay = findViewById(R.id.spnPay);
        tvTen = findViewById(R.id.tvTen);
        tvSdt = findViewById(R.id.tvSdt);
        tvDiaChi = findViewById(R.id.tvDiaChi);
    }


}