package com.example.eatingincqupt;

import android.app.Application;

public class MyApplication extends Application {
    private String unifynumber = "";
    private Food selectfood = null;

    public String getUnifynumber()
    {
        return this.unifynumber;
    }

    public void setUnifynumber(String str)
    {
        this.unifynumber = str;
    }

    public void setSelectfood(String name, String price, int id, String store_name)
    {
        this.selectfood = new Food(name, price, id, store_name);
    }

    public Food getSelectfood() {
        return selectfood;
    }
}
