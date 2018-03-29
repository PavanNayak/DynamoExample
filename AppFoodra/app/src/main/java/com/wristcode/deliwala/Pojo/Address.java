package com.wristcode.deliwala.Pojo;

/**
 * Created by Ajay Jagadish on 10-Mar-18.
 */

public class Address
{
    public String userid, useraddtype, username, useraddress, userlat, userlong;

    public Address(){}

    public Address(String userid, String useraddtype, String username, String useraddress, String userlat, String userlong)
    {
        this.userid = userid;
        this.useraddtype = useraddtype;
        this.username = username;
        this.useraddress = useraddress;
        this.userlat = userlat;
        this.userlong = userlong;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUseraddtype() {
        return useraddtype;
    }

    public void setUseraddtype(String useraddtype) {
        this.useraddtype = useraddtype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getUserlat() {
        return userlat;
    }

    public void setUserlat(String userlat) {
        this.userlat = userlat;
    }

    public String getUserlong() {
        return userlong;
    }

    public void setUserlong(String userlong) {
        this.userlong = userlong;
    }
}
