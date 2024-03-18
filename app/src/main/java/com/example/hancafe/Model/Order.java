package com.example.hancafe.Model;

public class Order {
    private String id;
    private String name;
    private String pic;
    private Float price;
    private String dateTime;

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Order(String id, String name, String pic, Float price, String dateTime) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.price = price;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", price=" + price +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
