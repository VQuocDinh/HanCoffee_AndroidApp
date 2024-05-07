package com.example.hancafe.Model;

public class CTHD {
    private String nameProduct;
    private int SL, total;

    public CTHD() {
    }

    public CTHD(String nameProduct, int SL, int total) {
        this.SL = SL;
        this.total = total;
        this.nameProduct = nameProduct;
    }

    public int getSL() {
        return SL;
    }

    public void setSL(int SL) {
        this.SL = SL;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }
}
