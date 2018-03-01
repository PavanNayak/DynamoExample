package com.wristcode.appfoodra.Pojo;

public class Payment
{
    private String name;
    int img;

    public Payment(String name, int img)
    {
        this.name = name;
        this.img = img;
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
