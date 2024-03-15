package com.example.hancoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancoffee.Activity.Home;
import com.example.hancoffee.Domain.Product;
import com.example.hancoffee.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<Product> products;
    private Context context;

    public CategoryAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.productName.setText(products.get(position).getName());
        String picUrl = "";
        switch (position) {
            case 0: {
                picUrl = "sp1";
//                holder.productPic.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.pd4));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            }

            case 1: {
                picUrl = "sp2";
//                holder.productPic.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.pd1));
                break;
            }

            case 2: {
                picUrl = "sp3";
//                holder.productPic.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.pd2));
                break;
            }

            case 3: {
                picUrl = "sp4";
//                holder.productPic.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.pd3));
                break;
            }
        }

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.productPic);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productPic;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productPic = itemView.findViewById(R.id.commonPic);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
