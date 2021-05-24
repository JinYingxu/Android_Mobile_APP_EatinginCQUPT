package com.example.eatingincqupt.ui.dashboard;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.eatingincqupt.Food;
import com.example.eatingincqupt.MyApplication;
import com.example.eatingincqupt.R;
import com.example.eatingincqupt.mysqlconnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Food food = ((MyApplication)getActivity().getApplication()).getSelectfood();
        String unifynum = ((MyApplication)getActivity().getApplication()).getUnifynumber();
        Button button = root.findViewById(R.id.btn_check);
        TextView storename = root.findViewById(R.id.store_name);
        TextView foodname = root.findViewById(R.id.food_name);
        TextView foodprice = root.findViewById(R.id.food_price);
        EditText hour = root.findViewById(R.id.et_hour);
        EditText min = root.findViewById(R.id.et_min);
        ImageView foodImage = root.findViewById(R.id.food_image);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("还没有订单哦，快去看看想要吃什么吧！");
        builder.setPositiveButton("好的",null);
        if (food == null)
        {
            button.setEnabled(false);
            hour.setEnabled(false);
            min.setEnabled(false);
            builder.show();
        }
        else
        {
            String image = food.getImage();
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
            storename.setText(food.getStore_name());
            foodname.setText(food.getName());
            foodprice.setText("总价："+food.getPrice());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hour_val = hour.getText().toString();
                    String min_val = min.getText().toString();
                    if (TextUtils.isEmpty(hour_val) || TextUtils.isEmpty(min_val))
                    {
                        Toast.makeText(getActivity(),"请输入预计取餐时间！",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        boolean result1 = hour_val.matches("[0-9]+");
                        boolean result2 = min_val.matches("[0-9]+");
                        if ((!result1) || (!result2)) {
                            Toast.makeText(getActivity(),"时间输入错误，请重新输入！",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            int temp1 = Integer.parseInt(hour_val);
                            int temp2 = Integer.parseInt(min_val);
                            hour_val = String.format("%02d",temp1);
                            min_val = String.format("%02d",temp2);
                            time = hour_val+":"+min_val;
                            if (temp1 >= 0 && temp1 <= 23 && temp2 >= 0 && temp2 <= 59)
                            {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Connection conn = null;
                                        conn =(Connection) mysqlconnect.getConn();

                                        String sql = "insert into order_list (store_id,store_name,food_name,unifynumber,finish_time,price) values(?,?,?,?,?,?)";
                                        PreparedStatement pst;
                                        try {
                                            Log.d("Input Data",String.valueOf(food.getStore_id())+"|"+food.getStore_name()+"|"+food.getName()+"|"+unifynum+"|"+time+"|"+food.getPrice().replace("￥",""));
                                            pst = (PreparedStatement) conn.prepareStatement(sql);
                                            //将输入的edit框的值获取并插入到数据库中
                                            pst.setInt(1,food.getStore_id());
                                            pst.setString(2,food.getStore_name());
                                            pst.setString(3,food.getName());
                                            pst.setString(4,unifynum);
                                            pst.setString(5,time);
                                            pst.setString(6,food.getPrice().replace("￥",""));
                                            pst.executeUpdate();
                                            pst.close();
                                            conn.close();
                                            Log.d("input success","插入新语句成功");
                                            Looper.prepare();
                                            Toast.makeText(getActivity(), "下单成功！", Toast.LENGTH_LONG).show();
                                            Looper.loop();
                                        } catch (SQLException e) {
                                            Log.d("input error","插入新语句失败");
                                            //Toast.makeText(MainActivity.this, "统一认证码不存在", Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"小时为[0,23]之间的整数，分钟为[0,59]之间的整数，请重新输入！",Toast.LENGTH_LONG).show();                                return;
                            }
                        }
                    }
                }
            });
        }


        return root;
    }
}