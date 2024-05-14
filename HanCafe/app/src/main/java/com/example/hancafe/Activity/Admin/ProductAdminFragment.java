package com.example.hancafe.Activity.Admin;

import static androidx.appcompat.widget.SearchView.OnClickListener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hancafe.Activity.Adapter.ProductAdapter;
import com.example.hancafe.Activity.Admin.Product.AddProductFragment;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProductAdminFragment extends Fragment {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    SearchView searchView;
    FloatingActionButton floatingActionButton;
    List<Product> productList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_product, container, false);

        recyclerView = view.findViewById(R.id.rv);
        searchView = view.findViewById(R.id.svProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        // Lấy dữ liệu sản phẩm từ Firebase và thêm vào danh sách productList
        FirebaseDatabase.getInstance().getReference().child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productList.clear();
                        // Duyệt qua mỗi nút con trong nút "Products"
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Lấy dữ liệu của mỗi sản phẩm
                            Product product = snapshot.getValue(Product.class);
                            // Thêm sản phẩm vào danh sách productList
                            productList.add(product);
                        }
                        // Sau khi đã thêm hết sản phẩm vào danh sách, cập nhật adapter
                        productAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi nếu có

                    }
                });

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                // Hiển thị thông tin chi tiết của sản phẩm
                showProductDetails(product);
            }

            private void showProductDetails(Product product) {
                // Tạo một Dialog hoặc một Activity mới để hiển thị thông tin chi tiết

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Product Details");

                // Sử dụng layout custom để hiển thị thông tin chi tiết của sản phẩm

                View view = getLayoutInflater().inflate(R.layout.layout_product_detail_dialog, null);
                builder.setView(view);

                // Ánh xạ các thành phần trong layout

                ImageView img1 = view.findViewById(R.id.img1);
                TextView tvName = view.findViewById(R.id.tvName);
                TextView tvPrice = view.findViewById(R.id.tvPrice);
                TextView tvDescribe = view.findViewById(R.id.tvDescribe);

                // Thiết lập thông tin của sản phẩm vào các thành phần tương ứng

                tvName.setText(product.getName());
                tvPrice.setText(product.getPrice() + " đ");
                tvDescribe.setText(product.getDescribe());
                // Load ảnh sản phẩm (nếu có) vào ImageView sử dụng Glide hoặc thư viện tương tự

                Glide.with(getContext())
                        .load(product.getPurl())
                        .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                        .into(img1);

                // Tạo và hiển thị Dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        searchView.setOnQueryTextListener(new  SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductFragment addProductFragment = new AddProductFragment();
                // Sử dụng FragmentTransaction để thêm hoặc thay thế Fragment hiện tại bằng Fragment mới
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container_admin, addProductFragment); // R.id.fragment_container là ID của Container Fragment trong layout của bạn
                transaction.addToBackStack(null); // Để cho phép người dùng quay lại Fragment trước đó bằng nút Back
                transaction.commit();
            }
        });

        return view;
    }

    private void txtSearch(String str){
        List<Product> filteredList = new ArrayList<>();

        // Duyệt qua danh sách sản phẩm và thêm vào danh sách lọc các sản phẩm phù hợp với từ khóa tìm kiếm
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(str.toLowerCase())) {
                filteredList.add(product);
            }
        }

        // Tạo một adapter mới với danh sách sản phẩm lọc và cập nhật RecyclerView
        ProductAdapter searchAdapter = new ProductAdapter(getContext(), filteredList);
        recyclerView.setAdapter(searchAdapter);
    }
}