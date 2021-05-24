package com.example.eatingincqupt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class EvaluationAdapter extends ArrayAdapter<Evaluation> {
    private int resourceId;

    public EvaluationAdapter(Context context, int textViewResourceId, List<Evaluation> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        Evaluation evaluation = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView ordernum = (TextView) view.findViewById(R.id.order_num);
        TextView foodName = (TextView) view.findViewById(R.id.food_name);
        TextView finishTime = (TextView) view.findViewById(R.id.finish_time);
        TextView finishflag = (TextView) view.findViewById(R.id.finish_flag);
        ordernum.setText("订单编号："+String.format("%06d",evaluation.getOrder_list()));
        foodName.setText(evaluation.getFood_name());
        if (evaluation.getFinishflag() == 0)
        {
            finishTime.setText("取餐时间："+evaluation.getFinishtime());
            finishflag.setBackgroundResource(R.color.orange_500);
            finishflag.setText("未完成");
        }
        else
        {
            finishTime.setText("");
            finishflag.setBackgroundResource(R.color.green_500);
            finishflag.setText("已完成");
        }
        return view;
    }
}