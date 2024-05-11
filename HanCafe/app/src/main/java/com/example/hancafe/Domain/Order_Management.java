package com.example.hancafe.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Order_Management implements Parcelable {
    int idCategory, price;
    String date, id, idUser, promotion;
    List<OrderDetail> orderDetails;

    public Order_Management() {
    }

    public Order_Management(int idCategory, int price, String date, String id, String idUser) {
        this.idCategory = idCategory;
        this.price = price;
        this.date = date;
        this.id = id;
        this.idUser = idUser;
    }

    protected Order_Management(Parcel in) {
        idCategory = in.readInt();
        price = in.readInt();
        date = in.readString();
        id = in.readString();
        idUser = in.readString();
        orderDetails = in.createTypedArrayList(OrderDetail.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCategory);
        dest.writeInt(price);
        dest.writeString(date);
        dest.writeString(id);
        dest.writeString(idUser);
        dest.writeTypedList(orderDetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order_Management> CREATOR = new Creator<Order_Management>() {
        @Override
        public Order_Management createFromParcel(Parcel in) {
            return new Order_Management(in);
        }

        @Override
        public Order_Management[] newArray(int size) {
            return new Order_Management[size];
        }
    };

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
