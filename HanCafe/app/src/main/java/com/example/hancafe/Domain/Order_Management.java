package com.example.hancafe.Domain;

import java.util.List;

public class Order_Management {
    int idCategory, price;
    String date, id, idUser;
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
