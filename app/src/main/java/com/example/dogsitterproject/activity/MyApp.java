package com.example.dogsitterproject.activity;

import android.app.Application;


import com.example.dogsitterproject.utils.ImageUtils;
import com.example.dogsitterproject.utils.MySignal;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageUtils.initHelper(this);
        MySignal.init(this);

    }
}
