package com.example.eatingincqupt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<Food> {
    private int resourceId;

    public FoodAdapter(Context context, int textViewResourceId, List<Food> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        Food food = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView foodName = (TextView) view.findViewById(R.id.food_name);
        TextView foodPrice = (TextView) view.findViewById(R.id.food_price);
        ImageView foodImage = (ImageView) view.findViewById(R.id.food_image);
        foodName.setText(food.getName());
        foodPrice.setText(food.getPrice());
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
        return view;
    }
}
