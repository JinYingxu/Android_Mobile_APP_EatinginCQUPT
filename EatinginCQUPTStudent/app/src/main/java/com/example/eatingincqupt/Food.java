package com.example.eatingincqupt;

public class Food {
    private String name;
    private String price;
    private int store_id;
    private String store_name;
    private String image;

    public Food(String name, String price, int id, String store_name)
    {
        this.name = name;
        this.price = "￥"+price;
        this.store_id = id;
        this.store_name = store_name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setPrice(String price)
    {
        this.price = "￥"+price;
    }

    public String getPrice()
    {
        return this.price;
    }

    public int getStore_id()
    {
        return this.store_id;
    }

    public String getStore_name()
    {
        return this.store_name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
