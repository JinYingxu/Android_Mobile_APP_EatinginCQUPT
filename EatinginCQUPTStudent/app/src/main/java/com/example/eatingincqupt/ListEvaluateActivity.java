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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListEvaluateActivity extends AppCompatActivity {
    private Handler mHandler;
    private Handler mHandler1;
    private static final int JUMP_TO_MAIN = 1;
    private String store_name;
    private String food_name;
    private String evaluate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int list_id = intent.getIntExtra("List_id",0);

        mHandler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == JUMP_TO_MAIN){
                    setContentView(R.layout.activity_list_evaluate);
                    TextView foodname = (TextView) findViewById(R.id.food_name);
                    TextView storename = (TextView) findViewById(R.id.store_name);
                    TextView eval = (TextView) findViewById(R.id.evaluate);
                    foodname.setText(food_name);
                    storename.setText(store_name);
                    eval.setText(evaluate);
                }
            }
        };

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == JUMP_TO_MAIN){
                    if (evaluate != null)
                    {
                        setContentView(R.layout.activity_list_evaluate);
                        TextView foodname = (TextView) findViewById(R.id.food_name);
                        TextView storename = (TextView) findViewById(R.id.store_name);
                        TextView eval = (TextView) findViewById(R.id.evaluate);
                        foodname.setText(food_name);
                        storename.setText(store_name);
                        eval.setText(evaluate);
                    }
                    else
                    {
                        setContentView(R.layout.provideevaluate);
                        TextView foodname = (TextView) findViewById(R.id.food_name);
                        TextView storename = (TextView) findViewById(R.id.store_name);
                        EditText eval = (EditText) findViewById(R.id.evaluate);
                        Button button = (Button) findViewById(R.id.pub);
                        foodname.setText(food_name);
                        storename.setText(store_name);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                evaluate = eval.getText().toString();
                                if (!TextUtils.isEmpty(evaluate))
                                {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Connection conn = null;
                                            conn =(Connection) mysqlconnect.getConn();

                                            String sql = "update order_list set evaluate = ? where list_id = " + list_id;
                                            PreparedStatement pst;
                                            try {
                                                Message msg = new Message();
                                                pst = (PreparedStatement) conn.prepareStatement(sql);
                                                pst.setString(1,evaluate);
                                                pst.executeUpdate();
                                                pst.close();
                                                conn.close();
                                                msg.what = JUMP_TO_MAIN;
                                                mHandler1.sendMessage(msg);
                                            } catch (SQLException e) {
                                                Log.d("input error","更新失败");
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }
                        });
                    }

                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                conn =(Connection) mysqlconnect.getConn();

                String sql = "select store_name,food_name,evaluate from order_list where list_id = " + list_id;
                Statement st;
                try {
                    Message msg = new Message();
                    st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    rs.first();
                    store_name = rs.getString("store_name");
                    food_name = rs.getString("food_name");
                    evaluate = rs.getString("evaluate");
                    msg.what = JUMP_TO_MAIN;
                    rs.close();
                    st.close();
                    conn.close();
                    mHandler.sendMessage(msg);
                } catch (SQLException e) {
                    Log.d("select error","查找失败");
                    //Toast.makeText(MainActivity.this, "统一认证码不存在", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }).start();
    }
}