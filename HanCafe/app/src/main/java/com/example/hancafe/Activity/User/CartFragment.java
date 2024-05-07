package com.example.hancafe.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hancafe.Activity.Adapter.CartItemAdapter;
import com.example.hancafe.Activity.Adapter.ProductsAdapter;
import com.example.hancafe.Domain.CartItem;
import com.example.hancafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements ProductsAdapter.OnItemClickListener {
    private ImageView btnBack;
    private TextView tvCartEmpty;
    private Button btnOrderProceed;
    private RecyclerView rvProduct;
    private CheckBox cbSelectAll;
    List<CartItem> cartItems = new ArrayList<>();
    CartItemAdapter cartItemAdapter;
    int countItem = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        btnOrderProceed = view.findViewById(R.id.btnOrderProceed);
        btnBack = view.findViewById(R.id.btnBack);
        rvProduct = view.findViewById(R.id.rvProduct);
        cbSelectAll = view.findViewById(R.id.cbSelectAll);
        tvCartEmpty = view.findViewById(R.id.tvCartEmpty);
        initCartDetail();
        setEvent();

        return view;
    }

    private void setEvent() {
        btnOrderProceed.setBackgroundColor(getResources().getColor(R.color.mainColor));
        btnOrderProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                boolean isChecked = true;
                for (int i = 0; i < rvProduct.getChildCount(); i++) {
                    View view = rvProduct.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.cbProduct);
                    if (checkBox.isChecked()) {
                        count++;
                    }
                }
                if (count <= 0) {
                    Toast.makeText(getContext(), "Chưa có sản phẩm nào được chọn", Toast.LENGTH_SHORT).show();
                } else {
                    List<CartItem> selectedList = new ArrayList<>(cartItemAdapter.getSelectedList());
                    Gson gson = new Gson();
                    String jsonSelectedList = gson.toJson(selectedList);
                    Intent intent = new Intent(getActivity(), Pay.class);
                    intent.putExtra("selectedList", jsonSelectedList);
                    startActivity(intent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < rvProduct.getChildCount(); i++) {
                    View view = rvProduct.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.cbProduct);
                    checkBox.setChecked(isChecked);
                }
            }
        });
//        if (countItem == 0 ){
//            tvCartEmpty.setText("Giỏ hàng trống");
//        } else {
//            tvCartEmpty.setText("");
//        }
    }

    private void initCartDetail() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String idUser = "";
        if (currentUser != null) {
            idUser = currentUser.getUid();
        }
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartDetailRef = database.getReference("CartDetail");
        cartItems = new ArrayList<>();


        cartDetailRef.orderByChild("idCart").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot cartItemSnapshot, @Nullable String previousChildName) {

                String idProduct = cartItemSnapshot.child("idProduct").getValue(String.class);
                int idSize = cartItemSnapshot.child("idSize").getValue(Integer.class);
                int quantity = cartItemSnapshot.child("quantity").getValue(Integer.class);
                int idCartItem = cartItemSnapshot.child("idCartItem").getValue(Integer.class);

                DatabaseReference productRef = database.getReference("Products").child(String.valueOf(idProduct));
                productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot productSnapshot) {

                        if (productSnapshot.exists()) {
                            String productName = productSnapshot.child("name").getValue(String.class);
                            String productImg = productSnapshot.child("purl").getValue(String.class);
                            int productPrice = productSnapshot.child("price").getValue(Integer.class);

                            CartItem cartItem = new CartItem(idProduct, quantity, idSize, idCartItem, productPrice, productName, productImg);
                            cartItems.add(cartItem);
                            cartItemAdapter.notifyDataSetChanged();
                            countItem++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseData", "Lỗi khi truy cập dữ liệu từ Firebase: " + error.getMessage());
                    }
                });
                cartItemAdapter = new CartItemAdapter(cartItems);
                rvProduct.setAdapter(cartItemAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Lỗi khi truy cập dữ liệu từ Firebase: " + error.getMessage());
            }
        });
    }

    @Override
    public void onItemProductClick(int position) {
        List<CartItem> cartItemList = cartItemAdapter.getData();
        CartItem cartItem = cartItemList.get(position);
        Intent intent = new Intent(getActivity(), ProductDetail.class);
        intent.putExtra("cartItem", cartItem);
        startActivity(intent);
    }
}