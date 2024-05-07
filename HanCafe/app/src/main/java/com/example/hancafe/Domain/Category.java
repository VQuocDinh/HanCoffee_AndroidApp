package com.example.hancafe.Domain;

import java.io.Serializable;

public class Category implements Serializable {
    private String catName;
    private String catId;
    private String catImg;

    public Category() {
    }

    public Category(String catId, String catName,  String catImg) {
        this.catName = catName;
        this.catId = catId;
        this.catImg = catImg;
    }

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
}
