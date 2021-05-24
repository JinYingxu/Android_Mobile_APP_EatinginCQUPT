package com.example.eatingincqupt;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class mysqlconnect {
    final static String diver = "com.mysql.jdbc.Driver";
    //加入utf-8是为了后面往表中输入中文，表中不会出现乱码的情况
    final static String url = "jdbc:mysql://rm-uf655ju44afmpr9vr9o.mysql.rds.aliyuncs.com:3306/eatingincqupt?characterEncoding=utf-8";
    final static String user = "root_name";//用户名
    final static String password = "CQUPT0512qaz";//密码

    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(diver);
            conn = (Connection) DriverManager.getConnection(url,user,password);//获取连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
