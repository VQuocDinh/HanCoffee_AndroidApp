package com.example.hancafe.Activity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.hancafe.Activity.Admin.MainAdminActivity;
import com.example.hancafe.Activity.Admin.Product.EditCategoryFragment;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder> {
    private List<CategoryProduct> categories;
    private OnItemClickListener listener;
    private DatabaseReference categoryRef;
    private Context mContext;


    public CategoryProductAdapter(Context context,List<CategoryProduct> catData) {
        this.categories = catData;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryProduct categoryProduct = categories.get(position);

        holder.tvCategoryName.setText(categoryProduct.getCatName());
        Glide.with(holder.itemView.getContext())
                .load(categoryProduct.getCatImg())
                .into(holder.catImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemCategoryClick(position);
                }
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("category",categoryProduct);

                EditCategoryFragment editCategoryFragment = new EditCategoryFragment();
                editCategoryFragment.setArguments(bundle);

                ((MainAdminActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_admin, editCategoryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public int getItemCount() {
        if(categories != null){
            return categories.size();
        }
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView tvCategoryName;
        Button btnEdit;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.catImg);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }


    }

    public interface OnItemClickListener {
        void onItemCategoryClick(int position);
    }

    public void setOnItemCategoryClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

