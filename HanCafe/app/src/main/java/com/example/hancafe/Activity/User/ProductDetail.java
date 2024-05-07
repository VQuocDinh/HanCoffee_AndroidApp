package com.example.hancafe.Activity.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.hancafe.Domain.CartDetail;
import com.example.hancafe.Domain.Product;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetail extends AppCompatActivity {
    private TextView tvQuantity, tvNameProduct, tvProductInfor;
    private Button btn1, btn2, btn3, btnDecrease, btnIncrease, btnAddCart, btnSmallSize, btnMidSize, btnBigSize;
    private ImageView btnBack, btnCart, imgProduct;
    private int count = 1;
    private Boolean btnSmallSizeIsActive, btnMidSizeIsActive, btnBigSizeIsActive;
    private int idSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setControl();
        setEvent();
    }

    private void setControl() {
        btn1 = findViewById(R.id.btnSmallSize);
        btn2 = findViewById(R.id.btnMidSize);
        btn3 = findViewById(R.id.btnBigSize);
        btnBack = findViewById(R.id.btnBack);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnSmallSize = findViewById(R.id.btnSmallSize);
        btnMidSize = findViewById(R.id.btnMidSize);
        btnBigSize = findViewById(R.id.btnBigSize);
        btnCart = findViewById(R.id.btnCart);
        imgProduct = findViewById(R.id.imgProduct);
        tvNameProduct = findViewById(R.id.tvNameProduct);
        tvProductInfor = findViewById(R.id.tvProductInfor);

    }

    private void setEvent() {
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");
        Glide.with(this)
                .load(product.getImage())
                .into(imgProduct);
        tvNameProduct.setText(product.getName());
        tvProductInfor.setText(product.getDesc());

        btn1.setBackgroundColor(getResources().getColor(R.color.black));
        btn2.setBackgroundColor(getResources().getColor(R.color.mainColor));
        btn3.setBackgroundColor(getResources().getColor(R.color.mainColor));
        btnDecrease.setBackgroundColor(getResources().getColor(R.color.transparent));
        btnIncrease.setBackgroundColor(getResources().getColor(R.color.transparent));
        btnAddCart.setBackgroundColor(getResources().getColor(R.color.mainColor));

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    tvQuantity.setText(String.valueOf(count));
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                tvQuantity.setText(String.valueOf(count));
            }
        });


        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnSmallSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonState((Button) v);
                idSize = 0;
            }
        });

        btnMidSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMidSize.setBackgroundColor(getResources().getColor(R.color.black));
                updateButtonState((Button) v);
                idSize = 1;
            }
        });
        btnBigSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBigSize.setBackgroundColor(getResources().getColor(R.color.black));
                updateButtonState((Button) v);
                idSize = 2;
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, MainActivity.class);
                intent.putExtra("openFragment", "cart");
                startActivity(intent);

            }
        });
    }

    private void addProductToCart() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("CartDetail");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idUser = "";
                if (currentUser != null) {
                    idUser = currentUser.getUid();
                }
                int maxIdCartItem = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idCartItem = dataSnapshot.child("idCartItem").getValue(Integer.class);
                    if (idCartItem > maxIdCartItem) {
                        maxIdCartItem = idCartItem;
                    }
                }
                int newIdCartItem = maxIdCartItem + 1;
                String idCart = idUser;
                String idProduct = product.getId();
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                CartDetail newItem = new CartDetail(newIdCartItem, idCart, idProduct, idSize, quantity);
                myRef.push().setValue(newItem)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                showAlert(ProductDetail.this,"Thông báo", "Sản phẩm đã được thêm vào giỏ hàng");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductDetail.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateButtonState(Button clickedButton) {
        btnSmallSizeIsActive = clickedButton == btnSmallSize;
        btnMidSizeIsActive = clickedButton == btnMidSize;
        btnBigSizeIsActive = clickedButton == btnBigSize;

        btnSmallSize.setBackgroundColor(btnSmallSizeIsActive ? getResources().getColor(R.color.black) : getResources().getColor(R.color.mainColor));
        btnMidSize.setBackgroundColor(btnMidSizeIsActive ? getResources().getColor(R.color.black) : getResources().getColor(R.color.mainColor));
        btnBigSize.setBackgroundColor(btnBigSizeIsActive ? getResources().getColor(R.color.black) : getResources().getColor(R.color.mainColor));
    }

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
}