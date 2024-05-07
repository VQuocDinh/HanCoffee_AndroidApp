package com.example.hancafe.Domain;

import java.io.Serializable;

public class Product implements Serializable {
    private String image, name, desc, id;
    private int status, price;

    public Product() {
    }

    public Product(String image, String name, String desc, String id, int status, int price) {
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.status = status;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}