package com.example.hancafe.Activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.Model.Staff;
import com.example.hancafe.Model.User;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> list;
    private Context context;
    private List<String> roleList; // Danh sách vai trò từ Firebase
    private DatabaseReference mDatabase;
    private boolean showSpinnerForUser;
    public UserAdapter(Context context, List<User> list, boolean showSpinnerForUser) {
        this.context = context;
        this.list = list;
        this.showSpinnerForUser = showSpinnerForUser;
        roleList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fetchRolesFromFirebase();
    }

    private void fetchRolesFromFirebase() {
        DatabaseReference usersRef = mDatabase.child("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roleList.clear();

                roleList.add("Admin");
//                roleList.add("Manage");
                roleList.add("User");
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    User user = userSnapshot.getValue(User.class);

                    if (user != null) {
                        if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            // Nếu người dùng hiện tại có quyền được lưu trong Firebase
                            // thì hiển thị quyền của người dùng đó trên Spinner
                            switch (user.getRole()) {
                                case 0:
                                    roleList.set(0, "Admin");
                                    break;
//                                case 1:
//                                    roleList.set(1, "Manager");
//                                    break;
                                case 2:
                                    roleList.set(2, "User");
                                    break;
                                default:
                                    break;
                            }

                        }
                    }

                }
                notifyDataSetChanged(); // Thông báo rằng dữ liệu đã được cập nhật
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseHelper", "Error fetching data", databaseError.toException());
            }
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_staff_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = list.get(position);
        holder.tvUserName.setText(user.getEmail());

        if(showSpinnerForUser && user.getRole() == 2) {
            holder.spnUser.setVisibility(View.VISIBLE);
            holder.tvUserName.setVisibility(View.VISIBLE);
            holder.imgBtnSaveRole.setVisibility(View.VISIBLE);

            // Thiết lập dữ liệu cho Spinner từ roleList
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, roleList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spnUser.setAdapter(spinnerAdapter);

            // Lấy vai trò của người dùng hiện tại và thiết lập nó trong Spinner
            String userRole = getUserRole(user);
            int roleIndex = roleList.indexOf(userRole);
            holder.spnUser.setSelection(roleIndex);
        }else {
            holder.spnUser.setVisibility(View.GONE);
            holder.tvUserName.setVisibility(View.GONE);
            holder.imgBtnSaveRole.setVisibility(View.GONE);
        }



        // Thiết lập người nghe cho sự kiện nhấn ImageButton
        holder.imgBtnSaveRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí của Spinner và giá trị đã chọn
                int spinnerPosition = holder.spnUser.getSelectedItemPosition();
                String selectedRole = roleList.get(spinnerPosition);
                // Xử lý khi nhấn ImageButton
                // Gửi dữ liệu về Firebase để cập nhật quyền của người dùng
                updateRoleInFirebase(user, selectedRole);
            }
        });
    }

    private String getUserRole(User user) {
        switch (user.getRole()) {
            case 0:
                return "Admin";
//            case 1:
//                return "Manager";
            case 2:
                return "User";
            default:
                return "";
        }
    }

    private int convertRoleStringToInt(String selectedRoleString) {
        int selectedRoleInt = -1; // Giá trị mặc định nếu không tìm thấy vai trò phù hợp
        switch (selectedRoleString) {
            case "Admin":
                selectedRoleInt = 0;
                break;
//            case "Manager":
//                selectedRoleInt = 1;
//                break;
            case "User":
                selectedRoleInt = 2;
                break;
            default:
                break;
        }
        return selectedRoleInt;
    }

    private void updateRoleInFirebase(User emailUser, String selectedRole) {

        int selectedRoleInt = convertRoleStringToInt(selectedRole);
        DatabaseReference userRef = mDatabase.child("Users").child(emailUser.getId()).child("role");
        userRef.setValue(selectedRoleInt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xử lý thành công khi cập nhật quyền
                        Toast.makeText(context, "Cập nhật quyền thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi cập nhật quyền thất bại
                        Toast.makeText(context, "Cập nhật quyền thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView tvUserName;
        Spinner spnUser;
        ImageButton imgBtnSaveRole;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            spnUser = itemView.findViewById(R.id.spnUser);
            imgBtnSaveRole = itemView.findViewById(R.id.imgBtnSaveRole);
        }
    }
}
