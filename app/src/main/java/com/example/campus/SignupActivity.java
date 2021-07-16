package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    private EditText etNewUsername;
    private EditText etNewPassword;
    private Button btnSignUp;
    private EditText etEmail;
    private EditText etFullName;
    private EditText etGradDate;
    private EditText etMajor;
    private EditText etAbout;
    private EditText etIg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (getSupportActionBar()!= null){
            getSupportActionBar().hide();
        }
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        etGradDate = findViewById(R.id.etGradDate);
        etMajor = findViewById(R.id.etMajor);
        etAbout = findViewById(R.id.etAbout);
        etIg = findViewById(R.id.etIg);

        btnSignUp = findViewById(R.id.btnSignUp);

        // on click handler for sign up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= etNewUsername.getText().toString();
                String password = etNewPassword.getText().toString();
                String email = etEmail.getText().toString();
                String full_name = etFullName.getText().toString();
                String grad_date = etGradDate.getText().toString();
                String major = etMajor.getText().toString();
                String about = etAbout.getText().toString();
                String ig = etIg.getText().toString();
                signupUser( username,  password, email,full_name,  grad_date,
                         major, about,  ig);
            }
        });
    }


    // sign up function
    private void signupUser(String username, String password, String email, String full_name, String grad_date,
                            String major, String about, String ig) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Set custom properties
        user.put("full_name",full_name );
        user.put("graduation_year", Integer.parseInt(grad_date));
        user.put("major", major);
        user.put("about", about );
        user.put("ig_handle", ig);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    goMainActivity();
                    Toast.makeText(SignupActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Toast.makeText(SignupActivity.this, "Something's wrong :(", Toast.LENGTH_SHORT);
                    return;
                }
            }
        });


    }


    // go to the main activity after Sign Up
    private void goMainActivity(){
        Intent i = new Intent(this , ChatActivity.class);
        startActivity(i);
        finish();
    }

    // handle on click text view
    public void to_signin(View v){
        TextView tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setTextColor(Color.BLUE);
        Intent to_signin = new Intent(this , LoginActivity.class);
        startActivity(to_signin);
        finish();
    }

}
