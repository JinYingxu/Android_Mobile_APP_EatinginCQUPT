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
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;
    private static String password_receive;
    private static final int JUMP_TO_MAIN = 1;
    private Button btnLogin;
    private EditText etAccount,etPassword;
    private TextView tvRegister;
    private MyApplication myapp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("登录");
        btnLogin = findViewById(R.id.btn_login);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean account_right = false;
                boolean password_right = false;
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                password_receive = null;

                if (TextUtils.isEmpty(account))
                {
                    Log.d("input error","统一认证码不可为空，请重新输入！");
                    Toast.makeText(MainActivity.this,"统一认证码格式错误，请重新输入！",Toast.LENGTH_LONG).show();
                }
                else
                {
                    boolean result=account.matches("[0-9]+");
                    if (!result || account.length() != 7)
                    {
                        Log.d("input error","统一认证码格式错误，请重新输入！");
                        Toast.makeText(MainActivity.this,"统一认证码格式错误，请重新输入！",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        account_right = true;
                    }
                }
                if (account_right)
                {
                    if (TextUtils.isEmpty(password)) {
                        Log.d("input error", "密码不可为空，请重新输入！");
                        Toast.makeText(MainActivity.this,"密码不可为空，请重新输入！",Toast.LENGTH_LONG).show();
                    } else {
                        boolean result = password.matches("[0-9]+");
                        if (!result || password.length() != 6) {
                            Log.d("input error", "密码格式错误，请重新输入！");
                            Toast.makeText(MainActivity.this,"密码格式错误，请重新输入！",Toast.LENGTH_LONG).show();
                        } else {
                            password_right = true;
                        }
                    }
                }

                if (account_right && password_right)
                {
                    mHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            if(msg.what == JUMP_TO_MAIN){
                                if (TextUtils.equals(password,password_receive))
                                {
                                    Log.d("input accute","登录成功");
                                    //将统一认证码保存为全局变量
                                    myapp = (MyApplication)getApplication();
                                    myapp.setUnifynumber(account);
                                    Toast.makeText(MainActivity.this,"登录成功！",Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(MainActivity.this,StuMainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    Log.d("input error","密码错误");
                                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                };

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connection conn = null;
                            conn =(Connection) mysqlconnect.getConn();

                            String sql = "select password from student_info where unifynumber = '" + account + "'";
                            Statement st;
                            try {
                                st = (Statement) conn.createStatement();
                                ResultSet rs = st.executeQuery(sql);
                                rs.first();
                                password_receive =rs.getString(1);
                                rs.close();
                                st.close();
                                conn.close();
                                Message msg = new Message();
                                msg.what = JUMP_TO_MAIN;
                                mHandler.sendMessage(msg);
                            } catch (SQLException e) {
                                Log.d("input error","统一认证码不存在");
                                Toast.makeText(MainActivity.this, "统一认证码不存在", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}