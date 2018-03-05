package com.wristcode.deliwala.Pojo;

/**
 * Created by gururaj on 12/17/2016.
 */

public class Restaurants {

    public String resid, resname, resadd, reslat, reslong, resmob, resisopen, respop, resimg, resdescp;

    public Restaurants() {
    }

    public Restaurants(String resid, String resname, String resadd, String reslat, String reslong, String resmob, String resisopen, String respop, String resdescp, String resimg) {

        this.resid = resid;
        this.resname = resname;
        this.resadd = resadd;
        this.reslat = reslat;
        this.reslong = reslong;
        this.resmob = resmob;
        this.resisopen = resisopen;
        this.respop = respop;
        this.resdescp = resdescp;
        this.resimg = resimg;
    }


    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getResadd() {
        return resadd;
    }

    public void setResadd(String resadd) {
        this.resadd = resadd;
    }

    public String getReslat() {
        return reslat;
    }

    public void setReslat(String reslat) {
        this.reslat = reslat;
    }

    public String getReslong() {
        return reslong;
    }

    public void setReslong(String reslong) {
        this.reslong = reslong;
    }

    public String getResmob() {
        return resmob;
    }

    public void setResmob(String resmob) {
        this.resmob = resmob;
    }

    public String getResisopen() {
        return resisopen;
    }

    public void setResisopen(String resisopen) {
        this.resisopen = resisopen;
    }

    public String getRespop() {
        return respop;
    }

    public void setRespop(String respop) {
        this.respop = respop;
    }

    public String getResdescp() {
        return resdescp;
    }

    public void setResdescp(String resdescp) {
        this.resdescp = resdescp;
    }

    public String getResimg() {
        return resimg;
    }

    public void setResimg(String resimg) {
        this.resimg = resimg;
    }

}
