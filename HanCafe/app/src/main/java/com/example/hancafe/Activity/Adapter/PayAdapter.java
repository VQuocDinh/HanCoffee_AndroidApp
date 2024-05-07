package com.example.hancafe.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Domain.CartItem;
import com.example.hancafe.R;


import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {
    private List<CartItem> data;

    public PayAdapter(List<CartItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_pay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = data.get(position);
        Glide.with(holder.itemView.getContext())
                .load(cartItem.getProductImg())
                .into(holder.imgProduct);

        holder.tvNameProduct.setText(cartItem.getProductName());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        int price = cartItem.getQuantity() * cartItem.getProductPrice();
        if (cartItem.getSizeId() == 0) {
            holder.tvSize.setText("S");
        } else if (cartItem.getSizeId() == 1) {
            holder.tvSize.setText("M");
        } else {
            holder.tvSize.setText("L");
        }
        holder.tvPriceProduct.setText(String.valueOf(price));
    }
    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (CartItem cartItem : data) {
            totalPrice += cartItem.getQuantity() *cartItem.getProductPrice();
        }
        return totalPrice;
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private Button btnDelFromCart;
        TextView tvNameProduct, tvPriceProduct;
        TextView tvQuantity, tvSize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSize = itemView.findViewById(R.id.tvSize);
        }
    }
}
