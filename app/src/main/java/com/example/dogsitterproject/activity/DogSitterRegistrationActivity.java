package com.example.dogsitterproject.activity;


import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.IMAGE;
import static com.example.dogsitterproject.utils.ConstUtils.IMAGE_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.IMG_FAILED;
import static com.example.dogsitterproject.utils.ConstUtils.IMG_UPLOADED;

import static com.example.dogsitterproject.utils.ConstUtils.PICK_IMAGE_REQUEST;
import static com.example.dogsitterproject.utils.ConstUtils.PROFILE_IMAGES;
import static com.example.dogsitterproject.utils.ConstUtils.PROFILE_PIC_URL;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.USER_SET_SUCCESSFULLY;
import static com.example.dogsitterproject.utils.ConstUtils.EMAIL_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.ID_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.LENGTH_PASSWORD;
import static com.example.dogsitterproject.utils.ConstUtils.PASSWORD_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.PHONE_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.REGISTERING_LOADER;
import static com.example.dogsitterproject.utils.ConstUtils.SALARY_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.SELECT_PICTURE;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.model.DogSitter;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;
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
    private Button regButton;


    private Uri resultUri;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


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
        if (resultUri == null) {
            Toast.makeText(this, IMAGE_REQUIRED, Toast.LENGTH_SHORT).show();
        } else {
            loader.setMessage(REGISTERING_LOADER);
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            saveData(fullNameEdited, cityEdited, emailEdited, phoneEdited, passEdited, salaryEdited);
        }
    }

    private void saveImage(Uri resultUri, String currentUserId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(PROFILE_IMAGES).child(currentUserId);
        if (resultUri != null) {
            storageReference.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Map newImageMap = new HashMap();
                                    newImageMap.put(PROFILE_PIC_URL, downloadUrl);
                                    databaseReference.updateChildren(newImageMap);

                                    Toast
                                            .makeText(
                                                    DogSitterRegistrationActivity.this,
                                                    IMG_UPLOADED,
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    Intent intent = new Intent(DogSitterRegistrationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    loader.dismiss();
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loader.dismiss();
                            Toast
                                    .makeText(
                                            DogSitterRegistrationActivity.this,
                                            IMG_FAILED,
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent =
                                    (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            loader.setMessage("Progress:" + (int) progressPercent + "%");
                        }
                    });
        }
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
                          String salaryEdited) {

        mAuth
                .createUserWithEmailAndPassword(emailEdited, passEdited)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Check if the user was registered
                        if (!task.isSuccessful()) {
                            String error = task.getException().toString();
                            //TODO: Toast to class
                            Toast.makeText(DogSitterRegistrationActivity.this, "Error"
                                    + error, Toast.LENGTH_SHORT).show();
                        } else {
                            String currentUserId = mAuth.getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child(USER_DATA)
                                    .child(currentUserId);

                            Map newTypeMap = new HashMap();
                            newTypeMap.put(TYPE, DOG_SITTER);
                            DogSitter user = new DogSitter(fullNameEdited,
                                    currentUserId,
                                    cityEdited,
                                    emailEdited,
                                    phoneEdited,
                                    passEdited,
                                    salaryEdited,
                                    "8:00-16:00", "");

                            databaseReference
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                databaseReference.updateChildren(newTypeMap);
                                                Toast
                                                        .makeText(DogSitterRegistrationActivity.this,
                                                                USER_SET_SUCCESSFULLY,
                                                                Toast.LENGTH_SHORT)
                                                        .show();

                                            } else {
                                                Toast.makeText(DogSitterRegistrationActivity.this,
                                                                Objects
                                                                        .requireNonNull(task
                                                                                .getException())
                                                                        .toString(),
                                                                Toast.LENGTH_SHORT)
                                                        .show();

                                            }


                                        }
                                    });

                            saveImage(resultUri, currentUserId);
                        }
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
