package com.wristcode.deliwala.Pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gururaj on 12/17/2016.
 */

public class Items {

    public String id, resid, resname, type, name, price, descp;
    public List<String> vid = new ArrayList<>();
    public List<String>vname = new ArrayList<>();
    public List<String> vprice = new ArrayList<>();
    int image;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


    public List<String> getVid(){
        return vid;
    }

    public List<String> getVname(){
        return vname;
    }

    public List<String> getVprice(){
        return vprice;
    }


    public int getImage(){
        return  image;
    }
}
