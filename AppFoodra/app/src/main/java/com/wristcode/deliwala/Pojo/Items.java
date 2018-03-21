package com.wristcode.deliwala.Pojo;

/**
 * Created by gururaj on 12/17/2016.
 */

public class Items {

    public String id, resid, resname, name, price, descp;
    int image;

    public Items(String id, String resid, String resname, String name, String price, String descp, int image)
    {
        this.id = id;
        this.resid = resid;
        this.resname = resname;
        this.name = name;
        this.price = price;
        this.descp = descp;
        this.image = image;
    }

    public Items() {}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResid() { return resid;}

    public void setResid(String resid) { this.resid = resid; }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getName() {
        return name;
    }

    public  void setName(String name){
        this.name=name;
    }

    public String getPrice() { return  price;}

    public void setPrice(String price) { this.price=price; }

    public  String getDescp(){
        return  descp;
    }

    public void setDescp(String descp){
        this.descp=descp;
    }

    public int getImage(){
        return  image;
    }


}
