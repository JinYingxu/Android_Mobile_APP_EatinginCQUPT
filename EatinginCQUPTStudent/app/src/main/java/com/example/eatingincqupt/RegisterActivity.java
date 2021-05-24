package com.example.eatingincqupt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler mHandler;
    private static final int JUMP_TO_MAIN = 1;
    private Button btnRegister;
    private EditText etAccount,etPass,etPassConfirm,etPhone;
    private MyApplication myapp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("注册");

        etAccount = findViewById(R.id.et_account);
        etPass = findViewById(R.id.et_password);
        etPassConfirm = findViewById(R.id.et_password_confirm);
        btnRegister = findViewById(R.id.btn_register);
        etPhone = findViewById(R.id.et_telenum);

        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String name = etAccount.getText().toString();
        String pass = etPass.getText().toString();
        String passconfirm = etPassConfirm.getText().toString();
        String phonenum = etPhone.getText().toString();

        if (TextUtils.isEmpty(name)){
            Log.d("input error","统一认证码不可为空，请重新输入！");
            Toast.makeText(RegisterActivity.this,"统一认证码不可为空，请重新输入！",Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            boolean result=name.matches("[0-9]+");
            if (!result || name.length() != 7)
            {
                Log.d("input error","统一认证码格式错误，请重新输入！");
                Toast.makeText(RegisterActivity.this,"统一认证码格式错误，请重新输入！",Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (TextUtils.isEmpty(phonenum)){
            Log.d("input error","手机号码不可为空，请重新输入！");
            Toast.makeText(RegisterActivity.this,"手机号码不可为空，请重新输入！",Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            boolean result=phonenum.matches("[0-9]+");
            if (!result || phonenum.length() != 11)
            {
                Log.d("input error","手机号码格式错误，请重新输入！");
                Toast.makeText(RegisterActivity.this,"手机号码格式错误，请重新输入！",Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (TextUtils.isEmpty(pass)){
            Log.d("input error","密码不可为空，请重新输入！");
            Toast.makeText(RegisterActivity.this,"密码不可为空，请重新输入！",Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            boolean result=pass.matches("[0-9]+");
            if (!result || pass.length() != 6)
            {
                Log.d("input error","密码格式错误，请重新输入！");
                Toast.makeText(RegisterActivity.this,"密码格式错误，请重新输入！",Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (!TextUtils.equals(pass,passconfirm)){
            Log.d("input error","两次输入密码不一致，请重新输入！");
            Toast.makeText(RegisterActivity.this,"两次输入密码不一致，请重新输入！",Toast.LENGTH_LONG).show();
            return;
        }

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == JUMP_TO_MAIN){
                    Log.d("input accute","注册成功");
                    //Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    myapp = (MyApplication)getApplication();
                    myapp.setUnifynumber(name);
                    Intent i = new Intent(RegisterActivity.this,StuMainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                conn =(Connection) mysqlconnect.getConn();
                String password_receive = "";

                String sql = "select password from student_info where unifynumber = '" + name + "'";
                Statement st;
                try {
                    st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    rs.first();
                    password_receive =rs.getString(1);
                    rs.close();
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(password_receive))
                {
                    sql = "insert into student_info (unifynumber,password,phonenumber) values(?,?,?)";
                    PreparedStatement pst;
                    try {
                        pst = (PreparedStatement) conn.prepareStatement(sql);
                        //将输入的edit框的值获取并插入到数据库中
                        pst.setString(1,name);
                        pst.setString(2,pass);
                        pst.setString(3,phonenum);
                        pst.executeUpdate();
                        pst.close();
                        conn.close();
                        Message msg = new Message();
                        msg.what = JUMP_TO_MAIN;
                        mHandler.sendMessage(msg);
                    } catch (SQLException e) {
                        Log.d("input error","注册失败，请重试");
                        //Toast.makeText(MainActivity.this, "统一认证码不存在", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else
                    Log.d("input error","该统一认证码已注册，请直接登录");
            }
        }).start();
    }
}