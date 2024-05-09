package com.example.hancafe.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Product implements Parcelable {
    public Product(int status) {
        this.status = status;
    }


    protected Product(Parcel in) {
        status = in.readInt();
        price = in.readInt();
        name = in.readString();
        purl = in.readString();
        describe = in.readString();
        productId = in.readString();
        idCategory = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    String name,purl,describe,productId, idCategory;
    int price;

    public Product(EditText price, EditText name, EditText purl, EditText describe, String categoryId) {
    }

    public Product(String productId) {
        this.productId = productId;
    }

    Product(){

    }


    public Product(int price, String name, String purl, String describe, String idCategory) {
        this.price = price;
        this.name = name;
        this.purl = purl;
        this.describe = describe;
        this.idCategory = idCategory;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(describe);
        dest.writeString(purl);
        dest.writeInt(status);
        dest.writeString(idCategory);
    }
}
