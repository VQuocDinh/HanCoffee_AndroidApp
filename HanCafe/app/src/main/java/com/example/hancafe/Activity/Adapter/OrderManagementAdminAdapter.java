package com.example.hancafe.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.hancafe.Model.OrderManagement;
import com.example.hancafe.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderManagementAdminAdapter extends RecyclerView.Adapter<OrderManagementAdminAdapter.OrderManagementViewHolder> {
    Context context;
    ArrayList<OrderManagement> list;
//    private int selectedPosition;

    public interface OnItemClickListener {
        void onItemClick(OrderManagement order);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public OrderManagementAdminAdapter(Context context, ArrayList<OrderManagement> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item_order_management, parent, false);
        return new OrderManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManagementViewHolder holder, int position) {
        OrderManagement orderManagement = list.get(position);
        if(orderManagement == null){
            return;
        }

        holder.tvCategoryItemName.setText(orderManagement.getName());

        // Cập nhật định dạng tiền tệ
        int price = orderManagement.getPrice();
        DecimalFormat df = new DecimalFormat("###,###.##");
        String formattedPrice = df.format(price) + "đ";
        holder.tvCategoryItemPrice.setText(formattedPrice);

//        String dateTimeStr = orderManagement.getDateTime();
//        String formattedDateTime = dateTimeStr.replace("-", "/");
//        holder.tvCategoryItemDateTime.setText(formattedDateTime);
        holder.tvCategoryItemDateTime.setText(orderManagement.getDate());

        //tải ảnh từ firebase
        String urlImage = orderManagement.getPicure();
        Glide.with(holder.itemView.getContext()).load(urlImage).transform(new CircleCrop()).placeholder(R.drawable.milk_coffee).into(holder.ivCategoryItemImage);

//        holder.tvCategoryItemId.setText(orderManagement.getIdUser());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(orderManagement);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class OrderManagementViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCategoryItemName;
        private TextView tvCategoryItemPrice;
        private TextView tvCategoryItemDateTime;
        private TextView tvCategoryItemId;
        private ImageView ivCategoryItemImage;
        public OrderManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryItemName = itemView.findViewById(R.id.tvCategoryItemName);
            tvCategoryItemPrice = itemView.findViewById(R.id.tvCategoryItemPrice);
            tvCategoryItemDateTime = itemView.findViewById(R.id.tvCategoryItemDateTime);
            ivCategoryItemImage = itemView.findViewById(R.id.ivCategoryItemImage);
            tvCategoryItemId = itemView.findViewById(R.id.tvCategoryItemId);
        }
    }

}
