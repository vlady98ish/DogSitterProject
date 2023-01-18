package com.example.dogsitterproject.activity;

import static com.example.dogsitterproject.utils.ConstUtils.SPLASH_SCREEN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogsitterproject.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splash_logo;
    private TextView splash_title;
    private TextView splash_slogan;

    private Animation upAnimation;
    private Animation downAnimation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_splash_screen);


        findViews();
        setFullScreen();
        loadAnimations();
        setAnimations();
        runSplash();
    }

    private void findViews(){
        splash_logo = findViewById(R.id.splash_logo);
        splash_title = findViewById(R.id.splash_title);
        splash_slogan = findViewById(R.id.splash_slogan);


    }

    private void loadAnimations(){
        upAnimation = AnimationUtils
                .loadAnimation(this, R.anim.screen_animation);
        downAnimation = AnimationUtils
                .loadAnimation(this, R.anim.down_animation);
    }

    private void setAnimations(){
        splash_logo.setAnimation(upAnimation);
        splash_title.setAnimation(downAnimation);
        splash_slogan.setAnimation(downAnimation);
    }

    private void runSplash(){
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(
                    SplashScreenActivity.this, AutintificateActivity.class
            );
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
    }
    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}