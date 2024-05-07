package com.example.hancafe.Activity.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Domain.Order_Management;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderStatusConfirmAdapter extends RecyclerView.Adapter<OrderStatusConfirmAdapter.ViewHolder>{
        private List<Order_Management> data;

        public OrderStatusConfirmAdapter(List<Order_Management> data) {
                this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_status_confirm,parent,false);
                return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                Order_Management orderManagement = data.get(position);
                Glide.with(holder.itemView.getContext())
                        .load(orderManagement.getPicure())
                        .into(holder.imgProduct);
                holder.tvNameProduct.setText(orderManagement.getName());
                holder.tvPriceProduct.setText(String.valueOf(orderManagement.getPrice()));
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
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
                                                                                                data.remove(position);
                                                                                                notifyDataSetChanged();
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
                                notifyDataSetChanged();

                        }
                });

        }

        @Override
        public int getItemCount() {
                return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
                TextView tvNameProduct,tvPriceProduct;
                LinearLayout mainLayout;
                ImageView imgProduct;
                public ViewHolder(@NonNull View itemView) {
                        super(itemView);
                        tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
                        tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
                        imgProduct =itemView.findViewById(R.id.imgProduct);
                        mainLayout=itemView.findViewById(R.id.mainLayout);
                }
        }
}
