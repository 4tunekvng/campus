package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private LoginButton loginButton;
    private static final String EMAIL = "email";



    CallbackManager callbackManager = CallbackManager.Factory.create();
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar()!= null){
            getSupportActionBar().hide();
        }
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        // code below is for facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());

        ParseFacebookUtils.initialize(getApplicationContext());

        final List<String> permissions = Arrays.asList("public_profile", "email");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this,
                        permissions,
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException err) {
                                if (user == null) {

                                    Toast.makeText(getApplicationContext(), "Log-out from Facebook and try again please!", Toast.LENGTH_SHORT).show();

                                    ParseUser.logOut();
                                }
                                else if (user.isNew()) {
                                    Toast.makeText(getApplicationContext(), "User signed up and logged in through Facebook!", Toast.LENGTH_SHORT).show();
                                    if (!ParseFacebookUtils.isLinked(user)) {
                                        checkUser(user, permissions);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "You can change your personal data in Settings tab!", Toast.LENGTH_SHORT).show();
                                    }
                                    goMainActivity();
                                }
                                else {
                                    if (!ParseFacebookUtils.isLinked(user)) {
                                        checkUser(user, permissions);
                                    }
                                    goMainActivity();
                                }
                            }
                        });
            }
        });


    }


    // function implementing user login
    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {
                if (e!=null){
                    // Better Error Handling
                    Toast.makeText(LoginActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT);
                    return;
                }
                // navigate to main activity once the user has signed in properly.
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();


            }
        });


    }

    // go to main activity class
    private void goMainActivity(){
        Intent i = new Intent(LoginActivity.this , ChatActivity.class);
        startActivity(i);
        finish();
    }
    // handle on click text view
    public void to_signup(View v){
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setTextColor(Color.BLUE);
        Intent to_signup = new Intent(this , SignupActivity.class);
        startActivity(to_signup);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }


    // check if user already uses app
    public void checkUser(ParseUser user, List<String> permissions){
        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, permissions, new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ParseFacebookUtils.isLinked(user)) {
                    Toast.makeText(getApplicationContext(), "Woohoo, user logged in with Facebook!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}