package com.wristcode.appfoodra.Pojo;

public class Reviews
{
    private String id;
    private String name;
    private String desc;
    int image;

    public Reviews(String id, String name, String desc, int image) {

        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image = image;
    }


    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public  void setName(String name){
        this.name = name;
    }

    public  String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public int getImage(){
        return  image;
    }

    public void setImage(int image) { this.image = image; }
}
