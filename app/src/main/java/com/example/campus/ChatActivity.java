package com.example.campus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    private String TAG = "ChatActivity";
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;


    EditText etMessage;
    ImageButton ibSend;
    RecyclerView rvChat;
    ArrayList mMessages;
    Boolean mFirstLoad;
    ChatAdapter mAdapter;
    private Object LoginResult;
    Club club;
    LiveData<PagedList<Message>> messages;
    ParseDataSourceFactory sourceFactory;
    PagedList.Config pagedListConfig;
    Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        club = (Club) Parcels.unwrap(getIntent().getParcelableExtra(Club.class.getSimpleName()));

        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
            // initial page size to fetch can be configured here
            pagedListConfig =
                    new PagedList.Config.Builder().setEnablePlaceholders(true)
                            .setPrefetchDistance(10)
                            .setInitialLoadSizeHint(20)
                            .setPageSize(10).build();

            sourceFactory = new ParseDataSourceFactory();
            final String userId = ParseUser.getCurrentUser().getObjectId();
            final ParseUser user = ParseUser.getCurrentUser();

            messages = new LivePagedListBuilder<>(sourceFactory, pagedListConfig).build();
            mAdapter = new ChatAdapter(ChatActivity.this, user, userId);
            rvChat = (RecyclerView) findViewById(R.id.rvChat);

            rvChat.setAdapter(mAdapter);
            // associate the LayoutManager with the RecyclerView
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
            linearLayoutManager.setReverseLayout(true);
            rvChat.setLayoutManager(linearLayoutManager);
            thisContext = this;

            messages.observe(this, new Observer<PagedList<Message>>() {
                @Override
                public void onChanged(@Nullable PagedList<Message> chats) {
                    mAdapter.submitList(chats);
                }
            });

        }
        else { // If not logged in, login as a new anonymous user
            login();
        }



    }

    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Set up button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        ibSend = (ImageButton) findViewById(R.id.ibSend);

        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        final ParseUser user = ParseUser.getCurrentUser();
        //mAdapter = new ChatAdapter(ChatActivity.this, user, userId, mMessages);





        // When send button is clicked, create message object on Parse
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setUserId(ParseUser.getCurrentUser().getObjectId());
                message.setUser(ParseUser.getCurrentUser());
                message.setClub(club);
                message.setBody(data);

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                            //refreshMessages();

                        } else {
                            Toast.makeText(ChatActivity.this, "Failed to save message" +e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                etMessage.setText(null);
            }
        });
    }

    // force login
    void login() {
        Intent i = new Intent(this , LoginActivity.class);
        startActivity(i);
    }

    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {

        sourceFactory = new ParseDataSourceFactory();
        messages = new LivePagedListBuilder<>(sourceFactory, pagedListConfig).build();
        messages.observe(this, new Observer<PagedList<Message>>() {
            @Override
            public void onChanged(@Nullable PagedList<Message> chats) {
                mAdapter.submitList(chats);
            }
        });
        mAdapter.notifyDataSetChanged();
    }

}
