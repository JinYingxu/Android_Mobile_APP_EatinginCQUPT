package com.example.eatingincqupt;

public class Order {
    private String unifynumber;
    private int finish_flag;
    private String evaluate;

    public Order(String unifynumber, String evaluate)
    {
        this.evaluate = evaluate;
        this.unifynumber = unifynumber;
    }

    public String getEvaluate() {
        return this.evaluate;
    }

    public String getUnifynumber() {
        return this.unifynumber;
    }

    public void setFinish_flag(int finish_flag) {
        this.finish_flag = finish_flag;
    }

    public int getFinish_flag() {
        return this.finish_flag;
    }
}
