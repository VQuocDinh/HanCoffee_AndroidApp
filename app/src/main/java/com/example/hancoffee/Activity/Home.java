package com.example.hancoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hancoffee.Adapter.CategoryAdapter;
import com.example.hancoffee.Adapter.ProductsAdapter;
import com.example.hancoffee.DatabaseHelper.ProductHelper;
import com.example.hancoffee.Domain.Product;
import com.example.hancoffee.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    ArrayList<Product> product = new ArrayList<>();
    private LinearLayout btnCart;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewProduct;
    private Button btnSearch;
    private EditText searchEdt;
    private ListView lvProduct;
    String search;
    ProductsAdapter adapter_lpd;
    ProductHelper dataHelper;

    List<Product> data_pd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setControl();
        setEvent();
    }

    private void setEvent() {
        dataHelper = new ProductHelper(this);
        ThemSP();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewProduct.setLayoutManager(linearLayoutManager);
        adapter = new CategoryAdapter(product, this);
        recyclerViewProduct.setAdapter(adapter);

        adapter_lpd=new ProductsAdapter(this,R.layout.viewholder_productlist,data_pd);
        lvProduct.setAdapter(adapter_lpd);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void setControl() {
        btnCart = findViewById(R.id.btnCart);
        recyclerViewProduct = findViewById(R.id.recycleView);
        btnSearch = findViewById(R.id.btnSearch);
        searchEdt = findViewById(R.id.searchEdt2);
        lvProduct = findViewById(R.id.lvProduct);
    }

    private void init() {
        product.add(new Product());
        product.add(new Product());
        product.add(new Product());
    }

    private void search() {
        search = searchEdt.getText().toString();
        if (!search.isEmpty()) {
//        ham tim kiem
        } else {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập nội dung tìm kiếm", Toast.LENGTH_SHORT).show();
        }
    }

    private void ThemSP(){
        Product sp = new Product();
        sp.setName("CAPUCHINO");
        sp.setPrice("30.000");
        sp.setPic("drawable/product.png");
//        data_sp.add(sp);
//        adapter_sp.notifyDataSetChanged();
        dataHelper.insertProduct(sp);

    }
    @Override
    protected void onResume(){
        super.onResume();
        fetchProduct();
    }

    private void fetchProduct(){
        data_pd.clear();
        data_pd.addAll(dataHelper.fetchProduct());
        adapter_lpd.notifyDataSetChanged();
    }


}