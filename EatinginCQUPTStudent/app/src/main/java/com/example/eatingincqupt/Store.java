package com.example.eatingincqupt;

public class Store {
    private String name;
    private String id;
    private String image = null;

    public Store(String name, String num){
        this.name = name;
        this.id = num;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return "商家ID："+id;
    }
}
