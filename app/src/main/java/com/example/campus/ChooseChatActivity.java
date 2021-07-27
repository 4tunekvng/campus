package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.parceler.Parcels;

public class ChooseChatActivity extends AppCompatActivity {
    Club club;
    ImageView ivClubPicture;
    TextView tvMemberCount;
    TextView tvAbout;
    TextView tvChatMemberCount;
    TextView tvAboutChat;
    RelativeLayout chat1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_chat);
        // unwrap the club passed in via intent using its simple name as a key
        club = (Club) Parcels.unwrap(getIntent().getParcelableExtra(Club.class.getSimpleName()));
        // get the views
        ivClubPicture = findViewById(R.id.ivClubPicture);
        tvMemberCount = findViewById(R.id.tvMemberCount);
        tvAbout = findViewById(R.id.tvAbout);
        tvChatMemberCount = findViewById(R.id.tvChatMemberCount);
        tvAboutChat = findViewById(R.id.tvAboutChat);
        chat1 = findViewById(R.id.chat1);
        try {
            Picasso.with(this).load(Uri.parse(String.valueOf(club.getPicture().get("url")))).fit().centerCrop().into(ivClubPicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // set the text in the views

        tvAbout.setText("Announcements for " +club.getName());
        tvMemberCount.setText(club.getSize().toString() + " members");
        tvChatMemberCount.setText(club.getSize().toString() + " members");
        tvAboutChat.setText("General chat for "+club.getName());


        // on click listener on chat
        chat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseChatActivity.this, ChatActivity.class);
                intent.putExtra(Club.class.getSimpleName(), Parcels.wrap(club));
                ChooseChatActivity.this.startActivity(intent);
            }
        });





    }
}