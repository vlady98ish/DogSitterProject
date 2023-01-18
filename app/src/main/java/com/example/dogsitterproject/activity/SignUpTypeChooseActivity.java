package com.example.dogsitterproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dogsitterproject.R;

public class SignUpTypeChooseActivity extends AppCompatActivity {

    private AppCompatButton dogSitterBtn;
    private AppCompatButton dogOwnerBtn;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_type);

        findViews();
        init();
    }


    private void findViews() {
        dogSitterBtn = findViewById(R.id.signUpType_BTN_dogSitter);
        dogOwnerBtn = findViewById(R.id.signUpType_BTN_dogOwner);
        backButton = findViewById(R.id.signUpType_LBL_backBTN);
    }

    private void init() {
        dogSitterBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpTypeChooseActivity.this,
                    DogSitterRegistrationActivity.class);
            startActivity(intent);
        });


        dogOwnerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpTypeChooseActivity.this,
                    DogOwnerRegistrationActivity.class);
            startActivity(intent);
        });


        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpTypeChooseActivity.this,
                    AutintificateActivity.class);
            startActivity(intent);
        });


    }
}