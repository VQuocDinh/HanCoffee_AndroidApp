package com.example.hancafe.Activity.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.hancafe.Activity.Admin.MainAdminActivity;
import com.example.hancafe.Activity.Admin.Product.EditCategoryFragment;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        holder.tvCategoryName.setText(categoryProduct.getName());
        Glide.with(holder.itemView.getContext())
                .load(categoryProduct.getCurl())
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
                // Hiển thị hộp thoại xác nhận xóa
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Bạn có chắc muốn xóa loại sản phẩm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference categoryProductRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");
                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
                        productRef.orderByChild("idCategory").equalTo(categoryProduct.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        snapshot.getRef().child("status").setValue(1)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @SuppressLint("NotifyDataSetChanged")
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        categoryProductRef.child(categoryProduct.getId()).child("status").setValue(1)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                categories.remove(position);
//                                                                                notifyItemRemoved(position);
//                                                                                notifyItemRangeChanged(position, categories.size());
                                                                                notifyDataSetChanged();

                                                                                // Thông báo xóa sản phẩm thành công
                                                                                Toast.makeText(mContext, "Đã xóa loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(mContext, "Lỗi khi cập nhật trạng thái loại sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(mContext, "Lỗi khi cập nhật trạng thái loại sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
//                                        String productId = snapshot.getKey();
//                                        // Cập nhật trạng thái của sản phẩm thành 1
//                                        productRef.child(productId).child("status").setValue(1)
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @SuppressLint("NotifyDataSetChanged")
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        // Loại bỏ sản phẩm khỏi danh sách và cập nhật lại RecyclerView
//                                                        categories.remove(position);
//                                                        notifyItemRemoved(position);
//                                                        notifyItemRangeChanged(position, categories.size());
//                                                        notifyDataSetChanged();
//
//                                                        // Thông báo xóa sản phẩm thành công
//                                                        Toast.makeText(mContext, "Đã xóa loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        // Xử lý khi cập nhật thất bại
//                                                        Toast.makeText(mContext, "Lỗi khi cập nhật trạng thái loại sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
                                    }
                                } else {
                                    Toast.makeText(mContext, "Không tìm thấy loại sản phẩm trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(mContext, "Lỗi khi truy vấn cơ sở dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Người dùng không muốn xóa, đóng dialog
                        dialog.dismiss();
                    }
                });
                builder.create().show();
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

