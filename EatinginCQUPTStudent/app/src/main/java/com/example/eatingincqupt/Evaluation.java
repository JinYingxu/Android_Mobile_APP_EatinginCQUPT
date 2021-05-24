package com.example.eatingincqupt;

public class Evaluation {
    private int order_list;
    private String food_name;
    private String finishtime = "";
    private int finishflag;

    public Evaluation(int num, String name, int flag, String time)
    {
        this.order_list = num;
        this.food_name = name;
        this.finishflag = flag;
        this.finishtime = time;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishflag(int finishflag) {
        this.finishflag = finishflag;
    }

    public int getFinishflag() {
        return finishflag;
    }

    public int getOrder_list() {
        return order_list;
    }

    public void setOrder_list(int order_list) {
        this.order_list = order_list;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }
}
