package com.example.eatingincqupt;

public class DiningHall {
    private String name;
    private int imageId;

    public DiningHall(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }
}
