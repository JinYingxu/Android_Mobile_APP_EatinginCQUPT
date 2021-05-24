package com.example.eatingincqupt.ui.home;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.eatingincqupt.DiningHallAdapter;
import com.example.eatingincqupt.R;
import com.example.eatingincqupt.StoreList;
import com.example.eatingincqupt.ui.dashboard.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private final List<DiningHall> dininghallList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View view= inflater.inflate(R.layout.fragment_home , container, false);
        initDiningHall();
        DiningHallAdapter adapter = new DiningHallAdapter(getActivity(), R.layout.dininghall_listview, dininghallList);
        ListView listView = (ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                DiningHall diningHall = dininghallList.get(position);
                Intent intent = new Intent(getActivity(), StoreList.class);
                intent.putExtra("Dining_Hall",diningHall.getName());
                startActivity(intent);
            }
        });

        return view;
    }

    private void initDiningHall() {
        DiningHall center = new DiningHall("中心食堂", R.drawable.center_pic);
        dininghallList.add(center);
        DiningHall flower = new DiningHall("樱花食堂", R.drawable.flower_pic);
        dininghallList.add(flower);
        DiningHall kinghey = new DiningHall("千喜鹤食堂", R.drawable.kinghey_pic);
        dininghallList.add(kinghey);
        DiningHall northwest = new DiningHall("大西北食堂", R.drawable.northwest_pic);
        dininghallList.add(northwest);
        DiningHall yan = new DiningHall("延生食堂", R.drawable.yan_pic);
        dininghallList.add(yan);
    }

}