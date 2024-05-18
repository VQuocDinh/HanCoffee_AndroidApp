package com.example.hancafe.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Activity.Adapter.CategoryMainAdapter;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryMainAdapter.OnItemClickListener {
    RecyclerView rvCategory;
    List<CategoryProduct> categories;
    CategoryMainAdapter categoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);



        rvCategory = view.findViewById(R.id.rvCategory);
        initCategory();
        return view;
    }

    private void initCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory.setLayoutManager(linearLayoutManager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category_Products");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String catId = dataSnapshot.child("id").getValue(String.class);
                    String catName = dataSnapshot.child("name").getValue(String.class);
                    String catImg = dataSnapshot.child("curl").getValue(String.class);
                    int status = dataSnapshot.child("status").getValue(Integer.class);
                    CategoryProduct category = new CategoryProduct(catId, catName, catImg, status);

                    if(category != null && category.getStatus() != 1){
                        categories.add(category);
                    }
                }
                categoryAdapter = new CategoryMainAdapter(categories);
                rvCategory.setAdapter(categoryAdapter);
                categoryAdapter.setOnItemCategoryClickListener(CategoryFragment.this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onItemCategoryClick(int position) {
        List<CategoryProduct> categoryList = categoryAdapter.getCatData();
        CategoryProduct category = categoryList.get(position);
        Intent intent = new Intent(getActivity(), CategoryDetail.class);
        intent.putExtra("category", (Serializable) category);
        startActivity(intent);
    }
}