package com.wristcode.deliwala.Pojo;

/**
 * Created by gururaj on 12/17/2016.
 */

public class Items {

    public String id, resid, resname, name, price, descp, vid, vname, vprice;
    int image;

    public Items(String id, String resid, String resname, String name, String price, String descp, String vid, String vname, String vprice, int image)
    {
        this.id = id;
        this.resid = resid;
        this.resname = resname;
        this.name = name;
        this.price = price;
        this.descp = descp;
        this.vid = vid;
        this.vname = vname;
        this.vprice = vprice;
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

    public String getVid() { return vid; }

    public void setVid(String vid) { this.vid = vid; }

    public String getVname() { return vname; }

    public void setVname(String vname) { this.vname = vname; }

    public String getVprice() { return vprice; }

    public void setVprice(String vprice) { this.vprice = vprice; }

    public int getImage(){
        return  image;
    }
}
