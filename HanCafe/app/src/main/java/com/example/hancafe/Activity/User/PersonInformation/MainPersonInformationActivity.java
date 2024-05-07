package com.example.hancafe.Activity.User.PersonInformation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hancafe.Activity.User.MainActivity;
import com.example.hancafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainPersonInformationActivity extends AppCompatActivity {
    ImageView imgAvt;
    EditText edtName, edtPhone, edtDate;
    TextView tvExit, tvEdit, tvEmail;
    ImageView ivDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_person_information);
        setControl();
        setEvent();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            // Thêm ValueEventListener để lấy dữ liệu từ Firebase
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy dữ liệu từ dataSnapshot và hiển thị lên các EditText
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);
                        String date = dataSnapshot.child("date").getValue(String.class);

                        edtName.setText(name);
                        edtPhone.setText(phone);
                        edtDate.setText(date);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Log.e("InformationPrivate", "Firebase Database Error: " + databaseError.getMessage());
                }
            });
        }

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các EditText
                String name = edtName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String date = edtDate.getText().toString().trim();

                // Kiểm tra xem người dùng đã nhập đủ thông tin hay chưa
                if (name.isEmpty() || phone.isEmpty() || date.isEmpty()) {
                    Toast.makeText(MainPersonInformationActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // Lưu dữ liệu vào Firebase Realtime Database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    userRef.child("name").setValue(name);
                    userRef.child("phone").setValue(phone);
                    userRef.child("date").setValue(date);

                    // Hiển thị thông báo hoàn thành
                    Toast.makeText(MainPersonInformationActivity.this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                    edtName.clearFocus();
                    edtPhone.clearFocus();
                    edtDate.clearFocus();
                }
            }
        });

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPersonInformationActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo một DatePickerDialog và hiển thị nó
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Hiển thị ngày được chọn trong ô edtDate
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        calendar.set(year, month, dayOfMonth);
                        String formattedDate = sdf.format(calendar.getTime());
                        edtDate.setText(formattedDate);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }
    private void setControl() {
        imgAvt = findViewById(R.id.imgAvt);
        tvEmail = findViewById(R.id.tvEmail);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtDate = findViewById(R.id.edtDate);
        tvEdit = findViewById(R.id.tvEdit);
        tvExit = findViewById(R.id.tvExit);
        ivDate = findViewById(R.id.ivDate);
    }

    private void setEvent() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Nếu người dùng đã đăng nhập, lấy email của họ và hiển thị lên EditText edtEmail
            String userEmail = currentUser.getEmail();
            tvEmail.setText(userEmail);
            Uri PhotoUrl = currentUser.getPhotoUrl();
            Glide.with(this).load(PhotoUrl).error(R.drawable.avtdefault).into(imgAvt);
        }
    }
}