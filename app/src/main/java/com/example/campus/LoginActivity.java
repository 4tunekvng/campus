package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {
    public static final String TAG ="LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
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
        Intent i = new Intent(this , MainActivity.class);
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
}