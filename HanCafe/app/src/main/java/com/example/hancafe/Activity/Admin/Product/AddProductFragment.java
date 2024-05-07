package com.example.hancafe.Activity.Admin.Product;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hancafe.Activity.Adapter.ProductAdapter;
import com.example.hancafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
public class AddProductFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText name,price,describe;
    Spinner spCategory;
    ArrayList<String> categoryList = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;

    Button btnSave,btnBack, btnChooseImage;
    Uri imageUri;
    ImageView imageView;
    Bitmap bitmap;
    FirebaseStorage storage;
    String imageUrl;
    StorageReference storageReference;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_admin_product_add, container, false);

        name = view.findViewById(R.id.edtName);

        price = view.findViewById(R.id.edtPrice);
        describe = view.findViewById(R.id.edtDescribe);
        imageView = view.findViewById(R.id.imgPreview);
        btnSave= view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        spCategory = view.findViewById(R.id.spCategory);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        spCategory.setAdapter(spinnerAdapter);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImageFromGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        loadCategoriesFromFirebase();

        return view;
    }


    private void insertData() {
        String productName = name.getText().toString();
        String productPrice = price.getText().toString();
        String productDescription = describe.getText().toString();
        String productCategoryName = spCategory.getSelectedItem().toString();

        // Kiểm tra nếu bất kỳ trường nào cũng không được để trống
        if (productName.isEmpty() || productCategoryName.isEmpty() || productPrice.isEmpty() || productDescription.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy tham chiếu đến cơ sở dữ liệu
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // Truy vấn cơ sở dữ liệu để lấy idCategory từ tên danh mục
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");
        categoriesRef.orderByChild("name").equalTo(productCategoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lặp qua các danh mục để lấy idCategory
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String categoryId = String.valueOf((snapshot.getKey()));

                        // Tạo một đối tượng Map để lưu thông tin sản phẩm
                        Map<String, Object> productMap = new HashMap<>();
                        productMap.put("name", productName);
                        productMap.put("price", productPrice);
                        productMap.put("describe", productDescription);
                        if (imageUri != null) {
                            productMap.put("purl", imageUri.toString());
                        }
                        productMap.put("idCategory", categoryId); // Lấy idCategory từ tên danh mục
                        productMap.put("status", 0); // Set status mặc định là 0

                        // Thêm sản phẩm vào cơ sở dữ liệu
                        DatabaseReference newProductRef = productsRef.push();
                        String productId = newProductRef.getKey(); // Lấy ID tự động tăng
                        productMap.put("id", productId);
                        newProductRef.setValue(productMap)
                                .addOnCompleteListener(task ->{
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    } else {
                                        Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy danh mục phù hợp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategoriesFromFirebase() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryName = snapshot.child("name").getValue(String.class);
                    categoryList.add(categoryName);
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                        imageView.setImageURI(imageUri);
                    }
                }else{
                    Toast.makeText(getContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_LONG).show();
                }
            }
    );
    private void uploadImage() {
        String productName = name.getText().toString();
        int productPrice = Integer.parseInt(price.getText().toString());
        String productDescription = describe.getText().toString();
        String productCategoryName = spCategory.getSelectedItem().toString();

        // Kiểm tra nếu bất kỳ trường nào cũng không được để trống
        if (productName.isEmpty() || productCategoryName.isEmpty() || String.valueOf(productPrice).isEmpty() || productDescription.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

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


            StorageReference ref = storageReference.child("imagesProduct/" + randomName);

            ref.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // getDownLoadUrl to store in string
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null) {
                                imageUrl = uri.toString();
                                uploadProductInfo();

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

    private void uploadProductInfo() {
// Lấy dữ liệu từ các trường nhập liệu
        String productName = name.getText().toString();
        int productPrice = Integer.parseInt(price.getText().toString());
        String productDescription = describe.getText().toString();
        String productCategoryName = spCategory.getSelectedItem().toString();

        // Kiểm tra xem các trường có rỗng không
        if (productName.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter name and image URL", Toast.LENGTH_SHORT).show();
            return;
        }
        // Lấy tham chiếu đến cơ sở dữ liệu
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // Truy vấn cơ sở dữ liệu để lấy idCategory từ tên danh mục
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");
        categoriesRef.orderByChild("name").equalTo(productCategoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lặp qua các danh mục để lấy idCategory
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String categoryId = String.valueOf((snapshot.getKey()));

                        // Tạo một đối tượng Map để lưu thông tin sản phẩm
                        Map<String, Object> productMap = new HashMap<>();
                        productMap.put("name", productName);
                        productMap.put("price", productPrice);
                        productMap.put("describe", productDescription);
                        if (imageUrl != null) {
                            productMap.put("purl", imageUrl.toString());
                        }
                        productMap.put("idCategory", categoryId); // Lấy idCategory từ tên danh mục
                        productMap.put("status", 0); // Set status mặc định là 0

                        // Thêm sản phẩm vào cơ sở dữ liệu
                        DatabaseReference newProductRef = productsRef.push();
                        String productId = newProductRef.getKey(); // Lấy ID tự động tăng
                        productMap.put("id", productId);
                        newProductRef.setValue(productMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    } else {
                                        Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy danh mục phù hợp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}