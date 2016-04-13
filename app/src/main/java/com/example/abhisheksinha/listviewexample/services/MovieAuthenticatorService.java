package com.example.abhisheksinha.listviewexample.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.abhisheksinha.listviewexample.sync.DummyAuthenticator;

/**
 * Created by abhishek on 13/04/16.
 */
public class MovieAuthenticatorService extends Service {
    private DummyAuthenticator dummyAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        dummyAuthenticator = new DummyAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return dummyAuthenticator.getIBinder();
    }
}
