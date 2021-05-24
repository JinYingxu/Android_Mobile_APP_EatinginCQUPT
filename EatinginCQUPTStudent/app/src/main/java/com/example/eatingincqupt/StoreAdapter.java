package com.example.eatingincqupt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StoreAdapter extends ArrayAdapter<Store> {
    private int resourceId;

    public StoreAdapter(Context context, int textViewResourceId, List<Store> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        Store store = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView storeName = (TextView) view.findViewById(R.id.store_name);
        TextView storeId = (TextView) view.findViewById(R.id.store_id);
        ImageView storeImage = (ImageView) view.findViewById(R.id.store_image);
        storeName.setText(store.getName());
        storeId.setText(store.getId());
        String image = store.getImage();
        if (image == null)
        {
            storeImage.setImageResource(R.drawable.store_pic);
        }
        else
        {
            byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            storeImage.setImageBitmap(decodedImage);
        }
        return view;
    }
}