package com.example.hancafe.Activity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
import com.example.hancafe.Activity.Admin.Product.EditProductFragment;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {

    private List<Product> productList;
    private OnItemClickListener mListener;
    private Context mContext;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductAdapter(Context context, List<Product> products) {
        mContext = context;
        productList = products;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product model = productList.get(position);

        holder.name.setText(model.getName());
        holder.price.setText(String.valueOf(model.getPrice()) + " đ");
        holder.describe.setText(model.getDescribe());
        Glide.with(holder.itemView.getContext())
                .load(model.getPurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("product", model);

                EditProductFragment editProductFragment = new EditProductFragment();
                editProductFragment.setArguments(bundle);

                ((MainAdminActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_admin, editProductFragment)
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
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,price,describe;

        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            price = (TextView) itemView.findViewById(R.id.pricetext);
            describe = (TextView) itemView.findViewById(R.id.describetext);

            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);


            // Xử lý sự kiện click trên item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(productList.get(position)); // Gọi phương thức onItemClick
                    }
                }
            });

        }
    }
}

