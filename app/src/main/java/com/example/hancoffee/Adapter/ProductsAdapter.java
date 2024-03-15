package com.example.hancoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hancoffee.Domain.Product;
import com.example.hancoffee.R;

import java.util.List;

public class ProductsAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<Product> data;

    public ProductsAdapter(@NonNull Context context, int resource, List<Product> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource,null);
        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
        TextView tvNameProduct = convertView.findViewById(R.id.tvNameProduct);
        TextView tvPriceProduct = convertView.findViewById(R.id.tvPriceProduct);


        Product sp =data.get(position);
        tvNameProduct.setText(sp.getName());
        tvPriceProduct.setText(sp.getPrice());
        imgProduct.setImageResource(R.drawable.product1);


        return convertView;
    }
}
