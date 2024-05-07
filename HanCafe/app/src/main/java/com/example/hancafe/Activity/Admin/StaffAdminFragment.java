package com.example.hancafe.Activity.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hancafe.Activity.Adapter.StaffAdapter;
import com.example.hancafe.Model.Staff;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffAdminFragment extends Fragment {

    RecyclerView rcvStaff;
    TextView tvUserNameStaff;
    Spinner spnUser;
    ArrayAdapter<String> adapter;
    List<String> list;
    List<Staff> staffList;
    StaffAdapter staffAdapter;
    ImageButton imgBtnSaveRole;
    String selectedRole;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_staff, container, false);

        setControl(view);
        setEvent();

        return view;
    }

    private void setEvent() {

        rcvStaff.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                staffList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    String id = userSnapshot.child("id").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    String name = userSnapshot.child("name").getValue(String.class);
                    String date = userSnapshot.child("date").getValue(String.class);
                    String phone = userSnapshot.child("phone").getValue(String.class);
                    int role = userSnapshot.child("role").getValue(Integer.class);

                    Staff staff = new Staff(id, email, name, date, phone, role);
                    staffList.add(staff);
                }

                staffAdapter = new StaffAdapter(getContext(), staffList, true);
                rcvStaff.setAdapter(staffAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

    }

    private void setControl(View view) {
        tvUserNameStaff = view.findViewById(R.id.tvUserName);
        spnUser = view.findViewById(R.id.spnUser);
        imgBtnSaveRole = view.findViewById(R.id.imgBtnSaveRole);

        rcvStaff = view.findViewById(R.id.rcvStaff);

    }
}