package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button clickyBtn, aboutMeBtn, linkBtn, primeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickyBtn = findViewById(R.id.main_btn_clicky);
        aboutMeBtn = findViewById(R.id.main_btn_aboutme);
        linkBtn = findViewById(R.id.main_btn_link);
        primeBtn = findViewById(R.id.main_btn_prime);


        aboutMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "Name: Zhang.rick , Email: zhang.rick@northeastern.edu", Toast.LENGTH_LONG).show();
            }
        });
        clickyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClickyActivity.class));
            }
        });

        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LinkActivity.class));
            }
        });

        primeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PrimeActivity.class));
            }
        });

    }
}