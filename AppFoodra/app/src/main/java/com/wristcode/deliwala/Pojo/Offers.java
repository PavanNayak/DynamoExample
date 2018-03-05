package com.wristcode.deliwala.Pojo;

/**
 * Created by gururaj on 12/17/2016.
 */

public class Offers {

    public String id, name, km, descp, time;
    int image;

    public Offers(){}

    public Offers(String s, String s1, int cover, String km,String time) {

        this.name=s;
        this.descp=s1;
        this.image=cover;
        this.km=km;
        this.time=time;
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

    public  void setName(String name){
        this.name=name;
    }

    public String getKm(){
        return  km;

    }

    public void setKm(String km){
        this.km=km;

    }
    public  String getDescp(){
        return  descp;
    }

    public void setDescp(String descp){
        this.descp=descp;
    }

    public int getImage(){
        return  image;
    }


    public  String getTime(){
        return  time;
    }

}
