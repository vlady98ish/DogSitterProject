package com.example.dogsitterproject.activity;


import static com.example.dogsitterproject.utils.ConstUtils.DESCRIPTION_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.IMAGE;
import static com.example.dogsitterproject.utils.ConstUtils.IMAGE_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.IMG_FAILED;
import static com.example.dogsitterproject.utils.ConstUtils.IMG_UPLOADED;

import static com.example.dogsitterproject.utils.ConstUtils.PICK_IMAGE_REQUEST;
import static com.example.dogsitterproject.utils.ConstUtils.PROFILE_IMAGES;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.EMAIL_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.ID_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.LENGTH_PASSWORD;
import static com.example.dogsitterproject.utils.ConstUtils.PASSWORD_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.PHONE_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.REGISTERING_LOADER;
import static com.example.dogsitterproject.utils.ConstUtils.SALARY_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.SELECT_PICTURE;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.USER_NOT_EXIST;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;


import android.text.TextUtils;


import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.TextView;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.db.FirebaseRegistration;
import com.example.dogsitterproject.model.DogSitter;

import com.example.dogsitterproject.utils.MySignal;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;



import java.util.HashMap;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DogSitterRegistrationActivity extends AppCompatActivity {


    private TextView backButton;
    private CircleImageView profileImage;
    private TextInputEditText fullName;
    private TextInputLayout city;
    private AutoCompleteTextView city_auto;
    private TextInputEditText phone;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText salary;
    private TextInputEditText description;
    private Button regButton;


    private Uri resultUri;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_sitter_registration);

        findViews();
        init();

    }


    private void findViews() {
        backButton = findViewById(R.id.dogSitterReg_LBL_backBTN);
        profileImage = findViewById(R.id.dogSitterReg_IMG_profileImage);
        fullName = findViewById(R.id.dogSitterReg_IN_FullName);
        city = findViewById(R.id.dogSitterReg_IN_city);
        city_auto = findViewById(R.id.dogSitterReg_AUTO_city);
        phone = findViewById(R.id.dogSitterReg_IN_phoneNumber);
        email = findViewById(R.id.dogSitterReg_IN_email);
        password = findViewById(R.id.dogSitterReg_IN_password);
        regButton = findViewById(R.id.dogSitterReg_BTN_register);
        salary = findViewById(R.id.dogSitterReg_IN_salary);
        description = findViewById(R.id.dogSitterReg_IN_description);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        fillAdapters();

    }

    private void fillAdapters() {
        String[] cities = new String[]{"Tel Aviv", "Ramat Gan", "Hadera", "Haifa", "Eilat", "Bat Yam"};
        ArrayAdapter<String> breadsAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, cities
        );
        city_auto.setAdapter(breadsAdapter);
    }


    private void init() {
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(DogSitterRegistrationActivity.this, AutintificateActivity.class);
            startActivity(intent);

        });

        profileImage.setOnClickListener(view -> getImage());


        regButton.setOnClickListener(view -> getForms());

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
        String salaryEdited = Objects
                .requireNonNull(salary.getText()).toString().trim();
        String descriptionEdited = Objects
                .requireNonNull(description.getText()).toString().trim();


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
        if (TextUtils.isEmpty(salaryEdited)) {
            salary.setError(SALARY_REQUIRED);
            salary.requestFocus();
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

        }
        if (TextUtils.isEmpty(descriptionEdited)) {
            description.setError(DESCRIPTION_REQUIRED);
            description.requestFocus();
        }
        if (resultUri == null) {
            MySignal.getInstance().toast(IMAGE_REQUIRED);
        } else {
            loader.setMessage(REGISTERING_LOADER);
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            saveData(fullNameEdited, cityEdited, emailEdited, phoneEdited, passEdited, salaryEdited,descriptionEdited);
        }
    }

    private void saveImage(Uri resultUri, String currentUserId) {
        FirebaseRegistration.CallBack_saveImage callBack_save = new FirebaseRegistration.CallBack_saveImage() {
            @Override
            public void changeIntent() {
                MySignal.getInstance().toast(IMG_UPLOADED);
                Intent intent = new Intent(DogSitterRegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                loader.dismiss();
                finish();
            }

            @Override
            public void fail() {
                loader.dismiss();
                MySignal.getInstance().toast(IMG_FAILED);
            }

            @Override
            public void loading(long byteTransferred, long totalByteCount) {
                double progressPercent =
                        (100.00 * byteTransferred / totalByteCount);
                loader.setMessage("Progress:" + (int) progressPercent + "%");
            }
        };
        FirebaseRegistration.saveImage(resultUri,USER_DATA,PROFILE_IMAGES, currentUserId, callBack_save);
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE);
        startActivityForResult(Intent.createChooser(intent, SELECT_PICTURE), PICK_IMAGE_REQUEST);
    }


    private void saveData(String fullNameEdited,
                          String cityEdited,
                          String emailEdited,
                          String phoneEdited,
                          String passEdited,
                          String salaryEdited,
                          String descriptionEdited) {



        mAuth
                .createUserWithEmailAndPassword(emailEdited, passEdited)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {

                        MySignal.getInstance().toast(USER_NOT_EXIST);
                    } else {
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object> newTypeMap = new HashMap<>();
                        newTypeMap.put(TYPE, DOG_SITTER);
                        DogSitter user = new DogSitter(fullNameEdited,
                                currentUserId,
                                cityEdited,
                                emailEdited,
                                phoneEdited,
                                passEdited,
                                salaryEdited,
                                "",descriptionEdited);
                        FirebaseRegistration.CallBack_saveData callBack_saveData = new FirebaseRegistration.CallBack_saveData() {
                            @Override
                            public void continueLoading() {
                                saveImage(resultUri, currentUserId);
                            }
                        };
                        FirebaseRegistration.saveDogSitter(user, newTypeMap,callBack_saveData);



                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            profileImage.setImageURI(resultUri);

        }
    }
}
