package com.wristcode.deliwala.Pojo;

public class Cart {

    public String id, resid, resname, name, qty, price, image;

    public Cart() {
    }

    public Cart(String id, String resid, String resname, String name, String qty, String price, String image) {
        this.id = id;
        this.resid = resid;
        this.resname = resname;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResid() { return resid; }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }
}
