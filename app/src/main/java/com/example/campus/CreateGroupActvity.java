package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.plattysoft.leonids.ParticleSystem;

public class CreateGroupActvity extends AppCompatActivity {
    int numParticles = 80;
    long timeToLive = 10000;
    Context thiscontext;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_actvity);
        thiscontext = CreateGroupActvity.this;
        floatingActionButton = findViewById(R.id.floatingActionCreate);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ParticleSystem((Activity)thiscontext, 100, R.drawable.animated_confetti, 5000)
                        .setSpeedRange(0.1f, 0.25f)
                        .setRotationSpeedRange(90, 180)
                        .setInitialRotationRange(0, 360)
                        .oneShot(v, 100);


                }

        });


    }

}