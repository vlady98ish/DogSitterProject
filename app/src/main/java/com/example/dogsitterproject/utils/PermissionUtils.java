package com.example.dogsitterproject.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PermissionUtils {

    private static PermissionUtils instance;
    private static Context appContext;

    public static PermissionUtils getInstance() {
        return instance;
    }

    private PermissionUtils(Context context) {
        appContext = context;
    }

    public static PermissionUtils initHelper(Context context) {
        if (instance == null)
            instance = new PermissionUtils(context);
        return instance;
    }


    public void checkCallPermission() {
        if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) appContext, new String[]{Manifest.permission.CALL_PHONE}, 100);
        }
    }
}
