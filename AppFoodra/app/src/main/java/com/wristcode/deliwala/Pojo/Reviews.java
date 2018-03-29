package com.wristcode.deliwala.Pojo;

public class Reviews
{
    public String id, name, ratings, reviews;
    int image;

    public Reviews(String id, String name, String ratings, String reviews, int image) {

        this.id = id;
        this.name = name;
        this.ratings = ratings;
        this.reviews = reviews;
        this.image = image;
    }

    public Reviews() {}

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

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public int getImage(){
        return  image;
    }

    public void setImage(int image) { this.image = image; }
}
