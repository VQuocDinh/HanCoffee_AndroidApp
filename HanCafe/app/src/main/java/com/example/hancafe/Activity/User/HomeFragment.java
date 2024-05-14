package com.example.hancafe.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hancafe.Activity.Adapter.CategoryAdapter;
import com.example.hancafe.Activity.Adapter.ProductsAdapter;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.Model.Product;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.OnItemClickListener, ProductsAdapter.OnItemClickListener{
    private RecyclerView rvCategory, rvProduct;
    ProductsAdapter productsAdapter;
    CategoryAdapter categoryAdapter;
    SearchView svSearch;
    List<Product> products = new ArrayList<>();
    List<CategoryProduct> categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvCategory = view.findViewById(R.id.rvCategory);
        rvProduct = view.findViewById(R.id.rvProduct);
        svSearch = view.findViewById(R.id.svSearch);

        setEvent();
        return view;
    }

    private void setEvent(){
        initProduct();
        initCategory();

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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
    }

    private void txtSearch(String query) {
//        products.clear();

        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products){
            if (product.getName().toLowerCase().contains(query.toLowerCase())){
                filteredProducts.add(product);
            }
        }
        productsAdapter = new ProductsAdapter(filteredProducts);
        rvProduct.setAdapter(productsAdapter);

        productsAdapter.notifyDataSetChanged();
    }

    private void initCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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
                    CategoryProduct category = new CategoryProduct(catId, catName, catImg);
                    categories.add(category);
                }
                categoryAdapter = new CategoryAdapter(categories);
                rvCategory.setAdapter(categoryAdapter);
                categoryAdapter.setOnItemCategoryClickListener(HomeFragment.this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProduct.setLayoutManager(linearLayoutManagerProduct);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String productId = dataSnapshot.child("id").getValue(String.class);
                    int productStatus = dataSnapshot.child("status").getValue(int.class);
                    if (productStatus == 0) {
                        String productName = dataSnapshot.child("name").getValue(String.class);
                        String productImg = dataSnapshot.child("purl").getValue(String.class);
                        int productPrice = dataSnapshot.child("price").getValue(Integer.class);
                        String productDecs = dataSnapshot.child("describe").getValue(String.class);

                        Product product = new Product(productImg, productName, productDecs, productId, productStatus, productPrice);
                        products.add(product);
                    }
                }

                productsAdapter = new ProductsAdapter(products);
                rvProduct.setAdapter(productsAdapter);

                productsAdapter.setOnItemProductClickListener(HomeFragment.this);

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

    @Override
    public void onItemProductClick(int position) {
        List<Product> productList = productsAdapter.getData();
        Product product = productList.get(position);
        Intent intent = new Intent(getActivity(), ProductDetail.class);
        intent.putExtra("product", (Serializable) product);
        startActivity(intent);
    }
}