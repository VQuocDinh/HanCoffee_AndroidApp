package com.example.hancafe.Activity.Admin.Product;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.hancafe.Model.CategoryProduct;
import com.example.hancafe.Model.Product;
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

import java.util.ArrayList;

public class EditProductFragment extends Fragment {

    private EditText edtName, edtPrice, edtDescribe;
    private ImageView ivHinh;
    private Button btnChooseImage, btnUpdate;
    private Spinner spCategory;
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private Product product;
    private CategoryProduct categoryProduct;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String productId;
    private String categoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtName = view.findViewById(R.id.edtName);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtDescribe = view.findViewById(R.id.edtDescribe);
        ivHinh = view.findViewById(R.id.ivHinh);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        spCategory = view.findViewById(R.id.spCategory);
        spCategory.setEnabled(false);
        spCategory.setClickable(false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Khởi tạo ArrayAdapter và đặt nó cho Spinner
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);

        // Load categories from Firebase
        loadCategoriesFromFirebase();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("product")) {
            product = bundle.getParcelable("product");
            if (product != null) {
                edtName.setText(product.getName());
                edtPrice.setText(String.valueOf(product.getPrice()));
                edtDescribe.setText(product.getDescribe());
                Glide.with(requireContext()).load(product.getPurl()).into(ivHinh);

                // Set category của sản phẩm lên đầu tiên trong Spinner
                String categoryId = product.getIdCategory();
                int categoryIndex = categoryList.indexOf(categoryId);
                if (categoryIndex != -1) {
                    spCategory.setSelection(categoryIndex);
                }

                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
                productsRef.orderByChild("name").equalTo(product.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Lặp qua kết quả trả về để lấy ID của sản phẩm
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                productId = snapshot.getKey();
//                                productsRef.child("idCategory").setValue(productId);
//                                break;
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
                if (product != null) {
                    String name = edtName.getText().toString().trim();
                    int price = Integer.parseInt(edtPrice.getText().toString().trim());
                    String describe = edtDescribe.getText().toString().trim();
                    String categoryName = spCategory.getSelectedItem().toString();

                    if (name.isEmpty() || categoryName.isEmpty()|| String.valueOf(price).isEmpty() || describe.isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Update product in Firebase
                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
                    productRef.child("name").setValue(name);
                    productRef.child("price").setValue(price);
                    productRef.child("describe").setValue(describe);

                    // Update idCategory based on the selected category name
                    DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");
                    categoriesRef.orderByChild("name").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String categoryId = snapshot.getKey();
                                    productRef.child("idCategory").setValue(categoryId);
                                    break; // Assuming there's only one category with the same name
                                }

                            } else {
                                Toast.makeText(requireContext(), "Không tìm thấy danh mục phù hợp", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Update image URL if a new image is chosen
                    if (imageUri != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference imageRef = storageRef.child("imagesProduct").child(productId);

                        imageRef.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                productRef.child("purl").setValue(imageUrl)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // Update image in the product list
                                                                product.setPurl(imageUrl);

                                                                Toast.makeText(requireContext(), "Chỉnh sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();

                                                                // Go back to previous fragment
                                                                requireActivity().getSupportFragmentManager().popBackStack();


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
                        Toast.makeText(requireContext(), "Chỉnh sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();

                        // Go back to previous fragment
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }

            }
        });

        // Handle choose image button click
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

    private void loadCategoriesFromFirebase() {

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("Category_Products");
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    categoryId = snapshot.getKey(); // Lấy id của danh mục
                    String categoryName = snapshot.child("name").getValue(String.class);
                    categoryList.add(categoryName);

                    // Nếu categoryId của sản phẩm trùng với categoryId hiện tại, đặt category đó lên đầu tiên trong Spinner
                    if (product != null && product.getIdCategory().equals(categoryId)) {
                        spCategory.setSelection(categoryList.size() - 1);
                    }

                }
                spinnerAdapter.notifyDataSetChanged(); // Cập nhật Spinner sau khi danh sách được cập nhật


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}