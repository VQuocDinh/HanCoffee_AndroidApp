package com.example.hancafe.Activity.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Activity.User.Bill;
import com.example.hancafe.Activity.User.Pay;

import com.example.hancafe.Model.OrderManagement;
import com.example.hancafe.Model.OrderDetail;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {
    private List<OrderManagement> orderManagements;
    private Context context;


    public OrderStatusAdapter(List<OrderManagement> orderManagements, Context context) {
        this.orderManagements = orderManagements;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderManagement orderManagement = orderManagements.get(position);
        holder.tvTotalPrice.setText(String.valueOf(orderManagement.getPrice()));
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderManagement.getOrderDetails());
        holder.chillRecycleView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.chillRecycleView.setAdapter(orderDetailAdapter);
//        holder.orderLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Pay.showBottomSheetDialog();
////                Intent intent = new Intent(context, Bill.class);
////                context.startActivity(intent);
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setMessage("Xác nhận hủy đơn hàng!")
//                        .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                String idOrder = orderManagement.getId();
//                                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");
//                                orderRef.orderByChild("id").equalTo(idOrder).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                            dataSnapshot.getRef().child("idCategory").setValue(4).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    orderManagements.remove(position);
//                                                    notifyDataSetChanged();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//
//                                                }
//                                            });
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                notifyDataSetChanged();
//
//            }
//        });
        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Bill.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("orderManagement", (Parcelable) orderManagement);
                intent.putParcelableArrayListExtra("orderDetails", (ArrayList<? extends Parcelable>) orderManagement.getOrderDetails());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderManagements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalPrice, tvDetail;
        RecyclerView chillRecycleView;
        LinearLayout orderLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            chillRecycleView = itemView.findViewById(R.id.chillRecycleView);
            orderLayout = itemView.findViewById(R.id.orderLayout);
            tvDetail = itemView.findViewById(R.id.tvDetail);
        }
    }

    public static class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ChildViewHolder> {
        private List<OrderDetail> orderDetails;

        public OrderDetailAdapter(List<OrderDetail> orderDetails) {
            this.orderDetails = orderDetails;
        }

        @NonNull
        @Override
        public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_detail, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
            OrderDetail orderDetail = orderDetails.get(position);
            holder.tvNameProduct.setText(orderDetail.getNameProduct());
            holder.tvPriceProduct.setText(String.valueOf(orderDetail.getPriceProduct()));
            holder.tvQuantity.setText(String.valueOf(orderDetail.getQuantity()));
            if (orderDetail.getIdSize() == 0) {
                holder.tvSize.setText("S");
            } else if (orderDetail.getIdSize() == 1) {
                holder.tvSize.setText("M");
            } else {
                holder.tvSize.setText("L");
            }
            Glide.with(holder.itemView.getContext())
                    .load(orderDetail.getImgProduct())
                    .into(holder.imgProduct);
        }

        @Override
        public int getItemCount() {
            return orderDetails.size();
        }

        public class ChildViewHolder extends RecyclerView.ViewHolder {
            TextView tvNameProduct, tvPriceProduct, tvSize, tvQuantity;
            ImageView imgProduct;

            public ChildViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
                tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
                imgProduct = itemView.findViewById(R.id.imgProduct);
                tvSize = itemView.findViewById(R.id.tvSize);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);

            }
        }
    }
}
