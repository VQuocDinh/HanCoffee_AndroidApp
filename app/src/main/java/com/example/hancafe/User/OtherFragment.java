package com.example.hancafe.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.hancafe.User.Order.Management.MainOrderActivity;
import com.example.hancafe.R;

public class OtherFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Lịch sử đơn hàng
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        ImageButton ibLSDonHang = view.findViewById(R.id.imgBtnTienIch1);

        ibLSDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainOrderActivity.class);
                startActivity(intent);
            }
        });

        // Điều khoản
        ImageButton ibDieuKhoan = view.findViewById(R.id.imgBtnTienIch2);
        ibDieuKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://thecoffeehouse.com/pages/dieu-khoan-su-dung";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }
}