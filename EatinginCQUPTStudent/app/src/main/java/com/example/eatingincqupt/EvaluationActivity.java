package com.example.eatingincqupt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EvaluationActivity extends AppCompatActivity {
    private Handler mHandler;
    private static final int JUMP_TO_MAIN = 1;
    private List<Order> list = new ArrayList<>();
    private MyApplication myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        Intent intent = getIntent();
        String foodname = intent.getStringExtra("Food_name");
        int storeid = intent.getIntExtra("Store_id",0);
        String storename = intent.getStringExtra("Store_name");
        String price = intent.getStringExtra("Price");
        String image = intent.getStringExtra("Image");
        getSupportActionBar().setTitle(foodname);
        ImageView foodImage = findViewById(R.id.food_image);
        TextView foodName = findViewById(R.id.food_name);
        TextView foodPrice = findViewById(R.id.food_price);
        foodName.setText(foodname);
        foodPrice.setText(price);
        if (image == null)
        {
            foodImage.setImageResource(R.drawable.food_pic);
        }
        else
        {
            byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            foodImage.setImageBitmap(decodedImage);
        }
        Button button = findViewById(R.id.btn_check);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myapp = (MyApplication)getApplication();
                myapp.setSelectfood(foodname, price.replace("￥",""), storeid, storename);
                myapp.getSelectfood().setImage(image);
                Log.d("Test",myapp.getUnifynumber());
                Log.d("Test",myapp.getSelectfood().getPrice());
            }
        });


        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == JUMP_TO_MAIN){
                    Log.d("count",Integer.toString(list.size()));
                    ListView listView = (ListView) findViewById(R.id.list_view);
                    if (list.size() > 0)
                    {
                        OrderAdapter adapter = new OrderAdapter(EvaluationActivity.this,R.layout.user_evaluation,list);
                        listView.setAdapter(adapter);
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                conn =(Connection) mysqlconnect.getConn();
                int id_num = 0;
                Statement st;
                String sql = "select unifynumber,evaluate from order_list where food_name = '" + foodname + "' and store_id = " + storeid;
                try {
                    Message msg = new Message();;
                    st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            String unifynumber = rs.getString("unifynumber");
                            String evaluate = rs.getString("evaluate");
                            if (evaluate != null)
                            {
                                Order order = new Order(unifynumber, evaluate);
                                list.add(order);
                                Log.d("evaluate", evaluate);
                            }
                        }
                        msg.what = JUMP_TO_MAIN;
                    }
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