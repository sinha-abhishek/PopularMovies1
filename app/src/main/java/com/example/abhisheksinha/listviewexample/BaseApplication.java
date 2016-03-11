package com.example.abhisheksinha.listviewexample;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by abhishek on 10/03/16.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
         ActiveAndroid.initialize(this);
        super.onCreate();
    }
}
