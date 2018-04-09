package com.wristcode.deliwala.Pojo;

public class Cart {

    public String id, varid, varname, resid, resname, type, name, qty, price, image;

    public Cart() {}

    public Cart(String id, String varid, String varname, String resid, String resname, String type, String name, String qty, String price, String image) {
        this.id = id;
        this.varid = varid;
        this.varname = varname;
        this.resid = resid;
        this.resname = resname;
        this.type = type;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.image = image;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getVarid() { return varid; }

    public void setVarid(String varid) { this.varid = varid; }

    public String getVarname() { return varname; }

    public void setVarname(String varname) { this.varname = varname; }

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

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

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
