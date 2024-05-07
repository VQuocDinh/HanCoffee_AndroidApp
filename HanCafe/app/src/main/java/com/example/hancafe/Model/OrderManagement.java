package com.example.hancafe.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class OrderManagement implements Serializable {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order_Management");

    private String id;
    private String name;
    private int price;
    private String date;
    private String picure;
    private String idUser;
    private int idCategory;
    private DatabaseReference categoryRef;


    public OrderManagement() {
    }

    public OrderManagement(String id, String name, int price, String date, String picure, int idCategory, String idUser) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.picure = picure;
        this.idCategory = idCategory;
        this.idUser = idUser;
        this.categoryRef = database.getReference("Category_Order_Management").child(String.valueOf(idCategory));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String dateTime) {
        this.date = dateTime;
    }

    public String getPicure() {
        return picure;
    }

    public void setPicure(String picture) {
        this.picure = picure;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
