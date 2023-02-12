package com.example.dogsitterproject.activity;

import static com.example.dogsitterproject.utils.ConstUtils.EMAIL_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.ID_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.KEY_USER;
import static com.example.dogsitterproject.utils.ConstUtils.LENGTH_PASSWORD;
import static com.example.dogsitterproject.utils.ConstUtils.PASSWORD_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.PHONE_REQUIRED;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;


import com.example.dogsitterproject.R;

import com.example.dogsitterproject.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class DogOwnerRegistrationActivity extends AppCompatActivity {


    private TextView backButton;
    private TextInputEditText fullName;
    private TextInputLayout city;
    private AutoCompleteTextView city_auto;
    private TextInputEditText phone;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button continueButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_owner_registration);

        findViews();
        init();

    }


    private void findViews() {
        backButton = findViewById(R.id.dogOwnerReg_LBL_backBTN);
        fullName = findViewById(R.id.dogOwnerReg_IN_FullName);
        city = findViewById(R.id.dogOwnerReg_IN_city);
        phone = findViewById(R.id.dogOwnerReg_IN_phoneNumber);
        email = findViewById(R.id.dogOwnerReg_IN_email);
        password = findViewById(R.id.dogOwnerReg_IN_password);
        continueButton = findViewById(R.id.dogOwnerReg_BTN_continue);
        city_auto = findViewById(R.id.dogOwnerReg_AUTO_city);
        fillAdapters();

    }

    private void fillAdapters(){
        String[] cities = new String[] {"Tel Aviv", "Ramat Gan", "Hadera", "Haifa", "Eilat", "Bat Yam"};
        ArrayAdapter<String> breadsAdapter = new ArrayAdapter<>(
                this,R.layout.dropdown_item,cities
        );
        city_auto.setAdapter(breadsAdapter);
    }


    private void init() {
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(DogOwnerRegistrationActivity.this, AutintificateActivity.class);
            startActivity(intent);

        });


        continueButton.setOnClickListener(view -> getForms());

    }


    private void getForms() {
        String emailEdited = Objects.
                requireNonNull(email.getText()).toString().trim();
        String passEdited = Objects
                .requireNonNull(password.getText()).toString().trim();
        String cityEdited = Objects
                .requireNonNull(city_auto.getText()).toString().trim();
        String fullNameEdited = Objects
                .requireNonNull(fullName.getText()).toString().trim();
        String phoneEdited = Objects
                .requireNonNull(phone.getText()).toString().trim();


        //TODO: Please check for valid email
        if (TextUtils.isEmpty(emailEdited)) {
            email.setError(EMAIL_REQUIRED);
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(passEdited)) {
            password.setError(PASSWORD_REQUIRED);
            password.requestFocus();
            return;
        }
        if (passEdited.length() < 6) {
            password.setError(LENGTH_PASSWORD);
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(fullNameEdited)) {
            fullName.setError(FULL_NAME_REQUIRED);
            fullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phoneEdited)) {
            phone.setError(PHONE_REQUIRED);
            phone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(cityEdited)) {
            city.setError(ID_REQUIRED);
            city.requestFocus();

        } else {


            User user = new User(fullNameEdited, "", cityEdited, emailEdited, phoneEdited, passEdited);
            changeActivity(user);


        }
    }

    private void changeActivity(User user) {
        Intent intent = new Intent(DogOwnerRegistrationActivity.this, DogRegistration.class);
        intent.putExtra(KEY_USER, user);
        startActivity(intent);
    }
}