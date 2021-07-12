package com.example.campus;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("PzMHBmCQA3RrCyM4uvpEI31JCftX44ckkh0oGFUJ")
                .clientKey("Ub0EJGq2m9aH1Yg5XskDg5kWmR3Mjd2xbn4a2ewj")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
