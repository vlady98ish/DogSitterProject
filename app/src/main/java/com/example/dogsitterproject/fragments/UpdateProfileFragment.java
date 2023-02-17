package com.example.dogsitterproject.fragments;

import static com.example.dogsitterproject.utils.ConstUtils.EMAIL_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.IMAGE;
import static com.example.dogsitterproject.utils.ConstUtils.LENGTH_PASSWORD;
import static com.example.dogsitterproject.utils.ConstUtils.PASSWORD_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.PHONE_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.PICK_IMAGE_REQUEST;
import static com.example.dogsitterproject.utils.ConstUtils.REGISTERING_LOADER;
import static com.example.dogsitterproject.utils.ConstUtils.SALARY_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.SELECT_PICTURE;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;


import com.example.dogsitterproject.R;
import com.example.dogsitterproject.db.FirebaseDB;
import com.example.dogsitterproject.utils.ImageUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileFragment extends Fragment {
    private AppCompatActivity activity;

    private TextInputEditText name;
    private TextInputEditText e_mail;
    private TextInputEditText password;
    private TextInputEditText phone;
    private TextInputEditText dogName;
    private TextInputEditText dogWeight;
    private CircleImageView image;
    private AppCompatButton update;
    private LinearLayoutCompat dogList;
    private boolean flag = false;


    public UpdateProfileFragment setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updateprofile, container, false);
        findViews(view);
        initViews();
        return view;

    }

    private void findViews(View view) {
        name = view.findViewById(R.id.update_IN_Name);
        e_mail = view.findViewById(R.id.update_IN_email);
        password = view.findViewById(R.id.update_IN_password);
        phone = view.findViewById(R.id.update_IN_phone);
        image = view.findViewById(R.id.update_IMG_logo);
        update = view.findViewById(R.id.update_BTN_register);
        dogList = view.findViewById(R.id.update_LIST_dog);
        dogName = view.findViewById(R.id.update_IN_dogName);
        dogWeight = view.findViewById(R.id.update_IN_dogWeight);
    }

    private void initViews() {
        FirebaseDB.CallBack_Update callBack_update = new FirebaseDB.CallBack_Update() {
            @Override
            public void updateToStartDogSitter(String nameDb, String emailDb, String passwordDb, String phoneDb, String profilePicture) {
                dogList.setVisibility(View.GONE);
                setUserData(nameDb, emailDb, passwordDb, phoneDb);

                ImageUtils.getInstance().load(profilePicture, image);
            }

            @Override
            public void updateToStartDog(String nameDb, String emailDb, String passwordDb, String phoneDb, String profilePicture, String dogNameDb, String dogWeightDb) {
                flag = true;
                setUserData(nameDb, emailDb, passwordDb, phoneDb);
                dogName.setText(dogNameDb);
                dogWeight.setText(dogWeightDb);
                ImageUtils.getInstance().load(profilePicture, image);
            }


        };
        FirebaseDB.getStartData(callBack_update);

        update.setOnClickListener(v -> getForms());
    }


    private void setUserData(String nameDb, String emailDb, String passwordDb, String phoneDb) {
        name.setText(nameDb);
        e_mail.setText(emailDb);
        password.setText(passwordDb);
        phone.setText(phoneDb);
    }

    private void getForms() {
        String nameEdited = Objects.
                requireNonNull(name.getText()).toString().trim();
        String emailEdited = Objects
                .requireNonNull(e_mail.getText()).toString().trim();
        String passwordEdited = Objects
                .requireNonNull(password.getText()).toString().trim();
        String phoneEdited = Objects
                .requireNonNull(phone.getText()).toString().trim();


        if (TextUtils.isEmpty(nameEdited)) {
            name.setError(EMAIL_REQUIRED);
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(emailEdited)) {
            e_mail.setError(EMAIL_REQUIRED);
            e_mail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(passwordEdited)) {
            password.setError(PASSWORD_REQUIRED);
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phoneEdited)) {
            phone.setError(PHONE_REQUIRED);
            phone.requestFocus();

        } else {
            if (flag) {
                String dogNameEdited = Objects
                        .requireNonNull(dogName.getText()).toString().trim();
                if (TextUtils.isEmpty(nameEdited)) {
                    name.setError(EMAIL_REQUIRED);
                    name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(emailEdited)) {
                    e_mail.setError(EMAIL_REQUIRED);
                    e_mail.requestFocus();
                    return;
                }
                FirebaseDB.updateDogInfo(dogNameEdited, dogWeight.getText().toString());
            }

            FirebaseDB.updateInfo(nameEdited,
                    emailEdited,
                    passwordEdited,
                    phoneEdited);
            Toast.makeText(getContext(), "Info Updated", Toast.LENGTH_SHORT).show();

        }

    }


}




