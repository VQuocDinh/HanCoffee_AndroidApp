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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

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

        // Khởi tạo ArrayAdapter và đặt nó cho Spinner
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        spCategory.setAdapter(spinnerAdapter);

        // Load categories from Firebase
        loadCategoriesFromFirebase();

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("product")) {
            product = bundle.getParcelable("product");
            if (product != null) {
                edtName.setText(product.getName());
                edtPrice.setText(String.valueOf(product.getPrice()) + " đ");
                edtDescribe.setText(product.getDescribe());
                Glide.with(requireContext()).load(product.getPurl()).into(ivHinh);
                // Set category của sản phẩm lên đầu tiên trong Spinner
//                int categoryIndex = categoryList.indexOf("name");
//                if (categoryIndex != -1) {
//                    spCategory.setSelection(categoryIndex);
//                }
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    String name = edtName.getText().toString().trim();
                    String price = edtPrice.getText().toString().trim();
                    String describe = edtDescribe.getText().toString().trim();

                    if (name.isEmpty() || price.isEmpty() || describe.isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Update product in Firebase
                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(product.getProductId());
                    productRef.child("name").setValue(name);
                    productRef.child("price").setValue(price);
                    productRef.child("describe").setValue(describe)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(requireContext(), "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    requireActivity().onBackPressed(); // Go back to previous fragment
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), "Sửa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
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
                    String categoryName = snapshot.child("name").getValue(String.class);
                    categoryList.add(categoryName);
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
