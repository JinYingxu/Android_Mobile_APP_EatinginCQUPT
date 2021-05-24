package com.example.eatingincqupt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoreList extends AppCompatActivity {

    private Handler mHandler;
    private static final int JUMP_TO_MAIN = 1;
    private List<Store> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        Intent intent = getIntent();
        String dininghall = intent.getStringExtra("Dining_Hall");
        getSupportActionBar().setTitle(dininghall);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Store store = list.get(position);
                Intent intent = new Intent(StoreList.this, Foodlist.class);
                Bundle bundle = new Bundle();
                bundle.putString("Store_name",store.getName());
                bundle.putInt("Store_id",Integer.valueOf(store.getId().replace("商家ID：","")));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == JUMP_TO_MAIN){
                    StoreAdapter adapter = new StoreAdapter(StoreList.this,R.layout.store_listview,list);
                    listView.setAdapter(adapter);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                conn =(Connection) mysqlconnect.getConn();

                String sql = "select * from dininghall_info where belong = '" + dininghall + "'";
                Statement st;
                try {
                    Message msg = new Message();
                    st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            String name = rs.getString("store_name");
                            String image = rs.getString("store_image");
                            int id_num = rs.getInt("store_id");
                            String id = String.format("%06d",id_num);
                            Store store = new Store(name,id);
                            store.setImage(image);
                            list.add(store);
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