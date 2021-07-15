package com.example.campus;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Message.class);
        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // Set applicationId and server based on the values in the Back4App settings.
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("PzMHBmCQA3RrCyM4uvpEI31JCftX44ckkh0oGFUJ")
                .clientKey("Ub0EJGq2m9aH1Yg5XskDg5kWmR3Mjd2xbn4a2ewj")
                /*.server("https://campus.b4a.io/")*/
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
