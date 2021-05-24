package com.example.eatingincqupt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    private int resourceId;

    public OrderAdapter(Context context, int textViewResourceId, List<Order> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        Order order = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView unifynum = (TextView) view.findViewById(R.id.studen_id);
        TextView evaluate = (TextView) view.findViewById(R.id.eval);
        unifynum.setText(order.getUnifynumber());
        evaluate.setText(order.getEvaluate());
        return view;
    }
}
