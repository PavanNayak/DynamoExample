package com.wristcode.deliwala.Pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gururaj on 12/17/2016.
 */

public class Items
{
    public String id, resid, resname, type, name, price, descp;
    public List<String> vid = new ArrayList<>();
    public List<String> vname = new ArrayList<>();
    public List<String> vprice = new ArrayList<>();
    public List<extraPackage> epackage = new ArrayList<>();
    int image;

    public Items() {}

    public String getId() { return id; }

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

    public List<extraPackage> getEpackage() { return epackage; }

    public int getImage(){
        return  image;
    }

    public static class extraPackage
    {
        public List<String> pid = new ArrayList<>();
        public List<String> pname = new ArrayList<>();
        public List<String> ptype = new ArrayList<>();
        public List<packageExtra> pExtra = new ArrayList<>();

        public List<String> getPid(){
            return pid;
        }

        public List<String> getPname(){
            return pname;
        }

        public List<String> getPtype() { return ptype; }

        public List<packageExtra> getpExtra() { return pExtra; }
    }

    public static class packageExtra
    {
        public List<String> epid = new ArrayList<>();
        public List<String> eptypename = new ArrayList<>();
        public List<String> epprice = new ArrayList<>();

        public List<String> getEpid() { return epid; }

        public List<String> getEptypename() { return eptypename; }

        public List<String> getEpprice() { return epprice; }
    }
}
