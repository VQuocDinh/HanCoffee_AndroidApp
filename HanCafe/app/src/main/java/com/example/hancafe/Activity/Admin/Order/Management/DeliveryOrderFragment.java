package com.example.hancafe.Activity.Admin.Order.Management;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Activity.Adapter.OrderManagementAdminAdapter;
import com.example.hancafe.Activity.Dialog.BottomSheetDialogOrderManagement;
import com.example.hancafe.Model.OrderManagement;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DeliveryOrderFragment extends Fragment{
    RecyclerView rcvCategoryOrderManagement;
    OrderManagementAdminAdapter orderManagementAdapter;
    Context context;
    ArrayList<OrderManagement> list = new ArrayList<>();

    DecimalFormat df;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_category_order_management, container, false);

        rcvCategoryOrderManagement = view.findViewById(R.id.rcvOrderManagementCategory);

        orderManagementAdapter= new OrderManagementAdminAdapter(context, list);
        rcvCategoryOrderManagement.setLayoutManager(new LinearLayoutManager(context));
        rcvCategoryOrderManagement.setAdapter(orderManagementAdapter);

        // Lấy idCategory cho fragment hiện tại
        int categoryId = 2;

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");

        // Tạo query để lọc theo idCategory
        Query query = orderRef.orderByChild("idCategory").equalTo(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String id = orderSnapshot.child("id").getValue(String.class);
                    String name = orderSnapshot.child("name").getValue(String.class);
//                    int price = orderSnapshot.child("price").getValue(Integer.class);
                    Integer priceInteger = orderSnapshot.child("price").getValue(Integer.class);
                    int price;
                    if (priceInteger != null) {
                        price = priceInteger.intValue();
                        Log.d("Error", "Không lỗi");
                    } else {
                        price = 0;
                        Log.d("Error", "Lỗi hehe");
                    }
                    String dateTime = orderSnapshot.child("date").getValue(String.class);
                    String picture = orderSnapshot.child("picure").getValue(String.class);
                    int idCategory = orderSnapshot.child("idCategory").getValue(Integer.class);
                    String idUser = orderSnapshot.child("idUser").getValue(String.class);

                    OrderManagement order = new OrderManagement(id, name, price, dateTime, picture, idCategory, idUser);
                    list.add(order);
//                    OrderManagement order = orderSnapshot.getValue(OrderManagement.class);
//                    if (order != null) {
//                        list.add(order);
//                    }
                }
                orderManagementAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderManagementAdapter.setOnItemClickListener(new OrderManagementAdminAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderManagement order) {
                // Mở BottomSheetDialog và truyền dữ liệu của order được click vào đó
                BottomSheetDialogOrderManagement bottomSheetDialog = BottomSheetDialogOrderManagement.newInstance(order);
                bottomSheetDialog.show(getChildFragmentManager(), bottomSheetDialog.getTag());
            }
        });

        return view;
    }
}