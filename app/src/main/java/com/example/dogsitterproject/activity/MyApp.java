package com.example.dogsitterproject.activity;

import android.app.Application;


import com.example.dogsitterproject.utils.ImageUtils;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageUtils.initHelper(this);


    }
}
