package com.example.dogsitterproject.utils;



import android.content.Context;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dogsitterproject.R;


public class ImageUtils {

    private static ImageUtils instance;
    private static Context appContext;

    public static ImageUtils getInstance() {
        return instance;
    }

    private ImageUtils(Context context) {
        appContext = context;
    }

    public static ImageUtils initHelper(Context context) {
        if (instance == null)
            instance = new ImageUtils(context);
        return instance;
    }



    public void load(String link, ImageView imageView) {
        Glide
                .with(appContext)
                .load(link)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
    }

    public void load(int drawable, ImageView imageView) {
        Glide
                .with(appContext)
                .load(drawable)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
    }
}