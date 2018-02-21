package com.wristcode.appfoodra.Pojo;

/**
 * Created by gururaj on 12/17/2016.
 */

public class Items {

    private String id;
    private String name;
    private String price;
    private String descp;

    int image;

    public Items(String s, String s1, int cover,String price) {

        this.name=s;
        this.descp=s1;
        this.image=cover;
        this.price=price;
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

    public String getPrice(){
        return  price;

    }

    public void setPrice(String price){
        this.price=price;

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


}
