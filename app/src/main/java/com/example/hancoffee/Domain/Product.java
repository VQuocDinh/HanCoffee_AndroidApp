package com.example.hancoffee.Domain;

public class Product {
    private String id, name, pic, price;

    public Product() {
    }

    public Product(String id, String name, String pic, String price) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.price = price;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}