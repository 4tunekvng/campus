package com.example.campus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.DataSource;
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
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

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
    private DataSource<Integer, Message> mostRecentDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        club = (Club) Parcels.unwrap(getIntent().getParcelableExtra(Club.class.getSimpleName()));

        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
            // initial page size to fetch can be configured here
            PagedList.Config pagedListConfig =
                    new PagedList.Config.Builder().setEnablePlaceholders(true)
                            .setPrefetchDistance(10)
                            .setInitialLoadSizeHint(20)
                            .setPageSize(10).build();

            sourceFactory = new ParseDataSourceFactory(club);

            final String userId = ParseUser.getCurrentUser().getObjectId();
            final ParseUser user = ParseUser.getCurrentUser();
            messages = new LivePagedListBuilder<>(sourceFactory, pagedListConfig).build();
            rvChat = (RecyclerView) findViewById(R.id.rvChat);
            mAdapter = new ChatAdapter(ChatActivity.this, user, userId);
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
            rvChat.setAdapter(mAdapter);

            String websocketUrl = "https://atcampus.b4a.io"; // ⚠️ TYPE IN A VALID WSS:// URL HERE

            ParseLiveQueryClient parseLiveQueryClient = null;
            try {
                parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            // parse query
            ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
            // select only chats related to the club
            parseQuery.whereEqualTo("club", club);
            // Configure limit and sort order
            parseQuery.orderByDescending("createdAt");
            parseQuery.include(Message.USER_KEY);
            parseQuery.include(Message.CLUB_KEY);


            // Connect to Parse server
            SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

            // Listen for CREATE events on the Message class
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
                sourceFactory.source.invalidate();


                // RecyclerView updates need to be run on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                super.onItemRangeInserted(positionStart, itemCount);
                                if (positionStart ==0){
                                    rvChat.scrollToPosition(0);
                                }
                            }
                        });



                    }
                });
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

}

