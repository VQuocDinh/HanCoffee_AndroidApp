package com.example.hancafe.Activity.Admin.Product;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hancafe.Activity.Adapter.CategoryProductAdapter;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddCategoryProductFragment extends Fragment{
    CategoryProductAdapter categoryProductAdapter;
    Bitmap bitmap;
    List<CategoryProduct> list;
    private EditText catName;
    private Button btnSave, btnBack, btnChooseImage;
    private DatabaseReference categoryRef;
    private ImageView imgPreview;

    FirebaseStorage storage;

    StorageReference storageReference;
    Uri imageUri;

    String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_category_product_add, container, false);

        // Ánh xạ các view từ layout XML
        catName = view.findViewById(R.id.edtName);
        imgPreview = view.findViewById(R.id.imgPreview);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Lấy tham chiếu đến node "category" trong Firebase Database
        categoryRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");

        categoryProductAdapter = new CategoryProductAdapter(getContext(),list);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        // Thêm sự kiện click cho nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        // Thêm sự kiện click cho nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc Fragment và quay lại Fragment hoặc Activity trước đó
                getActivity().onBackPressed();
            }
        });

        return view;
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

                        imgPreview.setImageURI(imageUri);

                    }
                }else{
                    Toast.makeText(getContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_LONG).show();
                }
            }
    );

    private void uploadImage() {
        Boolean canNext = true;

        if (catName.getText().toString().trim().equals("")) {
            catName.setError("Vui lòng nhập tên danh mục!");
            catName.setFocusable(true);
            canNext = false;
        }
        if (canNext) {
            if(imageUri!=null){
                final String randomName = UUID.randomUUID().toString();
                byte[] bytes = new byte[0];
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.requireActivity().getContentResolver(), imageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                    bytes = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(randomName);
                System.out.println("1");

                StorageReference ref = storageReference.child("imagesCategory/" + randomName);
                System.out.println("2");
                ref.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // getDownLoadUrl to store in string
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (uri != null) {
                                    imageUrl = uri.toString();
                                    uploadCategoryInfo();

                                }
                                imageUri = null;
                                //Toast.makeText(ProductManagement.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //Toast.makeText(ProductManagement.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
    private void uploadCategoryInfo() {
// Lấy dữ liệu từ các trường nhập liệu
        String name = catName.getText().toString().trim();

        // Kiểm tra xem các trường có rỗng không
        if (name.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter name and image URL", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo một đối tượng Category với dữ liệu từ các trường nhập liệu
        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("name", name);
        categoryMap.put("curl", imageUrl);

        DatabaseReference newCategoryRef = categoryRef.push();
        String productId = newCategoryRef.getKey(); // Lấy ID tự động tăng
        categoryMap.put("id", productId);
        newCategoryRef.setValue(categoryMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Thêm loại thành công", Toast.LENGTH_SHORT).show();
                        categoryProductAdapter.notifyDataSetChanged();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "Thêm loại thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}