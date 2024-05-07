package com.example.hancafe.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CategoryProduct implements Serializable, Parcelable {
    private String catName;
    private String catId;
    private String catImg;

    public CategoryProduct() {
    }

    public CategoryProduct(String catId, String catName,  String catImg) {
        this.catName = catName;
        this.catId = catId;
        this.catImg = catImg;
    }

    public CategoryProduct(String catName, String catImg) {
    }

    protected CategoryProduct(Parcel in) {
        catName = in.readString();
        catId = in.readString();
        catImg = in.readString();
    }

    public static final Creator<CategoryProduct> CREATOR = new Creator<CategoryProduct>() {
        @Override
        public CategoryProduct createFromParcel(Parcel in) {
            return new CategoryProduct(in);
        }

        @Override
        public CategoryProduct[] newArray(int size) {
            return new CategoryProduct[size];
        }
    };

    public String getCatImg() {
        return catImg;
    }

    public void setCatImg(String catImg) {
        this.catImg = catImg;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(catName);
        dest.writeString(catId);
        dest.writeString(catImg);
    }
}
