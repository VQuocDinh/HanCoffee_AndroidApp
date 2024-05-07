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

public class EditCategoryFragment extends Fragment {

    EditText edtName;
    ImageView ivHinh;
    Button btnChooseImage, btnUpdate;
    Uri imageUri;
    CategoryProduct categoryProduct;

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

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("category")) {
            categoryProduct = bundle.getParcelable("category");
            if (categoryProduct != null) {
                edtName.setText(categoryProduct.getCatName());
                Glide.with(requireContext()).load(categoryProduct.getCatImg()).into(ivHinh);
            }
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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