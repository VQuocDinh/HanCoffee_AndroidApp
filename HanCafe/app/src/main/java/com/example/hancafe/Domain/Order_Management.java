package com.example.hancafe.Domain;

public class Order_Management {
    int idCategory, price;
    String name, picure, date, id, idUser;

    public Order_Management() {
    }

    public Order_Management(int idCategory, int price, String name, String picure, String date, String id, String idUser) {
        this.idCategory = idCategory;
        this.price = price;
        this.name = name;
        this.picure = picure;
        this.date = date;
        this.id = id;
        this.idUser = idUser;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicure() {
        return picure;
    }

    public void setPicure(String picure) {
        this.picure = picure;
    }
}
