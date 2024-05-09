package com.example.hancafe.Activity.Admin.Product;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditCategoryFragment extends Fragment {

    EditText edtName;
    ImageView ivHinh;
    Button btnChooseImage, btnUpdate;
    Uri imageUri;
    CategoryProduct categoryProduct;
    private String categoryId;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtName = view.findViewById(R.id.edtName);
        ivHinh = view.findViewById(R.id.ivHinh);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("category")) {
            categoryProduct = bundle.getParcelable("category");
            if (categoryProduct != null) {
                edtName.setText(categoryProduct.getCatName());
                Glide.with(requireContext()).load(categoryProduct.getCatImg()).into(ivHinh);

                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");
                productsRef.orderByChild("name").equalTo(categoryProduct.getCatName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Lặp qua kết quả trả về để lấy ID của sản phẩm
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                categoryId = snapshot.getKey();
                                // Bây giờ bạn có thể sử dụng productId để thực hiện các thao tác cần thiết trên sản phẩm đó
                            }
                        } else {
                            // Không tìm thấy sản phẩm
                            Toast.makeText(requireContext(), "Không tìm thấy sản phẩm trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi khi truy vấn cơ sở dữ liệu
                        Toast.makeText(requireContext(), "Lỗi khi truy vấn cơ sở dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryProduct != null) {
                    String name = edtName.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Update product in Firebase
                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("Category_Products").child(categoryId);
                    categoryRef.child("name").setValue(name);



                    // Update image URL if a new image is chosen
                    if (imageUri != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference imageRef = storageRef.child("imagesCategory").child(categoryId);

                        imageRef.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                categoryRef.child("curl").setValue(imageUrl)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // Update image in the product list
                                                                categoryProduct.setCatImg(imageUrl);
                                                                // Notify the adapter about the change
                                                                // Assuming you have access to the adapter instance
                                                                // adapter.notifyDataSetChanged();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(requireContext(), "Lỗi khi cập nhật URL ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(requireContext(), "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(requireContext(), "Chỉnh sửa danh mục thành công", Toast.LENGTH_SHORT).show();

                        // Go back to previous fragment
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }

            }
        });


        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }

            private void pickImageFromGallery() {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                resultLauncher.launch(intent);
            }

            ActivityResultLauncher<Intent>
                    resultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                imageUri = data.getData();
                                ivHinh.setImageURI(imageUri);
                            }
                        }else{
                            Toast.makeText(getContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_LONG).show();
                        }
                    }
            );
        });


    }

}