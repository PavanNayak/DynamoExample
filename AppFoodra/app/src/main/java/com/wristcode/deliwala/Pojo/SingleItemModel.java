package com.wristcode.deliwala.Pojo;

/**
 * Created by pratap.kesaboyina on 01-12-2015.
 */
public class SingleItemModel {


    private String name;
    private String price;


    public SingleItemModel() {}

    public SingleItemModel(String name, String price) {
        this.name = name;
        this.price = price;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
