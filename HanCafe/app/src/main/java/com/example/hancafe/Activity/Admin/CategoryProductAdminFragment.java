package com.example.hancafe.Activity.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Activity.Adapter.CategoryProductAdapter;
import com.example.hancafe.Activity.Admin.Product.AddCategoryProductFragment;
import com.example.hancafe.Activity.Admin.Product.AddProductFragment;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CategoryProductAdminFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    List<CategoryProduct> categories = new ArrayList<>();
    CategoryProductAdapter categoryProductAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_category_product, container, false);
        setControl(view);
        initCategory();
        setEvent();

        return view;
    }

    private void setEvent(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryProductFragment addCategoryProductFragment = new AddCategoryProductFragment();
                // Sử dụng FragmentTransaction để thêm hoặc thay thế Fragment hiện tại bằng Fragment mới
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container_admin, addCategoryProductFragment); // R.id.fragment_container là ID của Container Fragment trong layout của bạn
                transaction.addToBackStack(null); // Để cho phép người dùng quay lại Fragment trước đó bằng nút Back
                transaction.commit();
            }
        });
    }
    private void initCategory() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category_Products");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String catId = dataSnapshot.child("id").getValue(String.class);
                    String catName = dataSnapshot.child("name").getValue(String.class);
                    String catImg = dataSnapshot.child("curl").getValue(String.class);

                    CategoryProduct categoryProduct = new CategoryProduct(catId, catName, catImg);
                    categories.add(categoryProduct);
                }
                categoryProductAdapter = new CategoryProductAdapter(getContext(),categories);
                recyclerView.setAdapter(categoryProductAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl(View view) {
        recyclerView = view.findViewById(R.id.rv);
        floatingActionButton = view.findViewById(R.id.fab);
    }
}