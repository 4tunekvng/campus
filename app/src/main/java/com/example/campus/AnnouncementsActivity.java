package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    ImageView ivGroupPic;
    TextView tvGroupName;
    TextView tvGroupMemberCount;
    ImageView ivCurrentUser;
    Club club;
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    public static final String TAG = "AnnouncementsActivity";
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        club = (Club) Parcels.unwrap(getIntent().getParcelableExtra(Club.class.getSimpleName()));

        setContentView(R.layout.activity_announcements);
        ivGroupPic = findViewById(R.id.ivGroupPic);
        tvGroupName = findViewById(R.id.tvGroupName);
        tvGroupMemberCount = findViewById(R.id.tvGroupMemberCount);
        ivCurrentUser = findViewById(R.id.ivCurrentUser);

        relativeLayout = findViewById(R.id.relativeLayout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnnouncementsActivity.this, CreateAnnouncementActivity.class);
                intent.putExtra(Club.class.getSimpleName(), Parcels.wrap(club));
                AnnouncementsActivity.this.startActivity(intent);
            }
        });
        Glide.with(this)
                .load(ParseUser.getCurrentUser().getParseFile("Profile_pic").getUrl())
                .circleCrop() // create an effect of a round profile picture
                .into(ivCurrentUser);
        try {
            Glide.with(this)
                    .load(club.getPicture().get("url"))
                    // create an effect of a round profile picture
                    .into(ivGroupPic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvGroupMemberCount.setText(club.getSize().toString() + " members");
        tvGroupName.setText(club.getName());

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Lookup the recycler view
        rvPosts = findViewById(R.id.rvPosts);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // initialize the array that will hold posts and create a PostsAdapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // query posts from Parse server
        queryPosts();
    }
    private void queryPosts() {
        adapter.clear();
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // select only posts related to the club
        query.whereEqualTo("club", club);
        // include data referred by user key
        query.include(Post.KEY_POSTER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground((posts, e) -> {
            // check for errors
            if (e != null) {
                return;
            }

            // save received posts to list and notify adapter of new data
            allPosts.addAll(posts);
            adapter.notifyDataSetChanged();
        });
    }


}