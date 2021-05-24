package com.example.eatingincqupt.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.eatingincqupt.DiningHall;
import com.example.eatingincqupt.Evaluation;
import com.example.eatingincqupt.EvaluationAdapter;
import com.example.eatingincqupt.Food;
import com.example.eatingincqupt.Foodlist;
import com.example.eatingincqupt.ListEvaluateActivity;
import com.example.eatingincqupt.MyApplication;
import com.example.eatingincqupt.R;
import com.example.eatingincqupt.Store;
import com.example.eatingincqupt.StoreAdapter;
import com.example.eatingincqupt.StoreList;
import com.example.eatingincqupt.mysqlconnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private Handler mHandler;
    private static final int JUMP_TO_MAIN = 1;
    private NotificationsViewModel notificationsViewModel;
    private final List<Evaluation> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        String unifynumber = ((MyApplication)getActivity().getApplication()).getUnifynumber();
        TextView unify = (TextView) root.findViewById(R.id.unify_num);
        unify.setText(unifynumber);
        ListView listView = (ListView)root.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Evaluation evaluation = list.get(position);
                if (evaluation.getFinishflag() == 1)
                {
                    Intent intent = new Intent(getActivity(), ListEvaluateActivity.class);
                    intent.putExtra("List_id",evaluation.getOrder_list());
                    startActivity(intent);
                }
            }
        });

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == JUMP_TO_MAIN){
                    EvaluationAdapter adapter = new EvaluationAdapter(getActivity(),R.layout.evaluate_listview,list);
                    listView.setAdapter(adapter);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                conn =(Connection) mysqlconnect.getConn();

                String sql = "select list_id,food_name,finish_time,finish_flag from order_list where unifynumber = '" + unifynumber + "' order by list_id desc";
                Statement st;
                try {
                    Message msg = new Message();
                    st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            int list_id = rs.getInt("list_id");
                            String name = rs.getString("food_name");
                            String time = rs.getString("finish_time");
                            int flag = rs.getInt("finish_flag");
                            Evaluation eva = new Evaluation(list_id,name,flag,time);
                            list.add(eva);
                        }
                        msg.what = JUMP_TO_MAIN;
                    }
                    rs.close();
                    st.close();
                    conn.close();
                    mHandler.sendMessage(msg);
                } catch (SQLException e) {
                    Log.d("input error","统一认证码不存在");
                    //Toast.makeText(MainActivity.this, "统一认证码不存在", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }).start();
        return root;
    }
}