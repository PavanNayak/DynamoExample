package com.wristcode.deliwala.Pojo;

public class Category {

    private String id;
    private String name;
    int img;

    public Category(String id, String name, int img) {

        this.id = id;
        this.name = name;
        this.img = img;
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
        this.name = name;
    }

    public int getImg(){
        return  img;
    }

    public void setImg(int img) { this.img = img; }
}
