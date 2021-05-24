package com.example.eatingincqupt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Identification extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btn = findViewById(R.id.btn1);;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Identification.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}