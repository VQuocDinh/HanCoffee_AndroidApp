package com.example.hancafe.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Domain.Category;
import com.example.hancafe.R;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> catData;
    private OnItemClickListener listener;

    public CategoryAdapter(List<Category> catData) {
        this.catData = catData;
    }

    public List<Category> getCatData() {
        return catData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = catData.get(position);

        holder.tvCategoryName.setText(category.getCatName());
        Glide.with(holder.itemView.getContext())
                .load(category.getCatImg())
                .into(holder.catImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemCategoryClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return catData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        LinearLayout mainLayout;
        TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.catImg);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    public interface OnItemClickListener {
        void onItemCategoryClick(int position);
    }

    public void setOnItemCategoryClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
