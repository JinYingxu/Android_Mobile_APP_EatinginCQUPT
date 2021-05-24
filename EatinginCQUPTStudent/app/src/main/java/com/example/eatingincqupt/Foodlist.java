package com.example.eatingincqupt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Foodlist extends AppCompatActivity {

    private Handler mHandler;
    private static final int JUMP_TO_MAIN = 1;
    private List<Food> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);
        Intent intent = getIntent();
        String storename = intent.getStringExtra("Store_name");
        int storeid = intent.getIntExtra("Store_id",0);
        getSupportActionBar().setTitle(storename);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Food food = list.get(position);
                Intent intent = new Intent(Foodlist.this, EvaluationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Food_name",food.getName());
                bundle.putString("Store_name",food.getStore_name());
                bundle.putInt("Store_id",food.getStore_id());
                bundle.putString("Price",food.getPrice());
                bundle.putString("Image",food.getImage());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == JUMP_TO_MAIN){
                    FoodAdapter adapter = new FoodAdapter(Foodlist.this,R.layout.food_listview,list);
                    listView.setAdapter(adapter);
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
                String sql  = "select * from food_list where store_id = " + storeid;
                try {
                    Message msg = new Message();;
                    st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            String image = rs.getString("food_image");
                            String name = rs.getString("food_name");
                            String price = rs.getString("price");
                            Food food = new Food(name,price,storeid,storename);
                            food.setImage(image);
                            list.add(food);
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