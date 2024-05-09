package com.example.hancafe.Domain;

public class OrderDetail {
    String idOrder, imgProduct, nameProduct, idOrderDetail;
    int idSize, quantity, priceProduct;

    public OrderDetail(String idOrderDetail, String idOrder, String imgProduct, String nameProduct, int idSize, int quantity, int priceProduct) {
        this.idOrder = idOrder;
        this.imgProduct = imgProduct;
        this.nameProduct = nameProduct;
        this.idSize = idSize;
        this.quantity = quantity;
        this.priceProduct = priceProduct;
        this.idOrderDetail = idOrderDetail;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getIdSize() {
        return idSize;
    }

    public void setIdSize(int idSize) {
        this.idSize = idSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public String getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(String idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }
}
