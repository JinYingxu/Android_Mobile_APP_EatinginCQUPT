package com.example.eatingincqupt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DiningHallAdapter extends ArrayAdapter<DiningHall> {
    private int resourceId;

    public DiningHallAdapter(Context context, int textViewResourceId, List<DiningHall> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        DiningHall diningHall = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView dininghallImage = (ImageView) view.findViewById(R.id.dininghall_image);
        TextView dininghallName = (TextView) view.findViewById(R.id.dininghall_name);
        dininghallImage.setImageResource(diningHall.getImageId());
        dininghallName.setText(diningHall.getName());
        return view;
    }
}
