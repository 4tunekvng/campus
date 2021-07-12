package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class StartLoadActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_load);
        if (getSupportActionBar()!= null){
            getSupportActionBar().hide();
        }


        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);


    }
}