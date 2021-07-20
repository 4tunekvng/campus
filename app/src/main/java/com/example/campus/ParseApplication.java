package com.example.campus;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Club.class);
        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);



        // Set applicationId and server based on the values in the Back4App settings.


        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("RQcFkxORLNTdGO6Paq5tBUObHAY4w2pArc001ceK")
                        .clientKey("wUsKiWAWJMXiZYL4iyB6HhkGryPZsfOAJvpJswza")
                        .server("https://parseapi.back4app.com")
                        .build()
        );

    }
}
