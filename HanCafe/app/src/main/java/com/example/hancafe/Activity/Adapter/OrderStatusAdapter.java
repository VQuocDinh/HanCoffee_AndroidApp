package com.example.hancafe.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Domain.Order_Management;
import com.example.hancafe.R;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>{
        private List<Order_Management> data;

        public OrderStatusAdapter(List<Order_Management> data) {
                this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_productlist,parent,false);
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
        }

        @Override
        public int getItemCount() {
                return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
                TextView tvNameProduct,tvPriceProduct;
                ImageView imgProduct;
                public ViewHolder(@NonNull View itemView) {
                        super(itemView);
                        tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
                        tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
                        imgProduct =itemView.findViewById(R.id.imgProduct);
                }
        }
}
