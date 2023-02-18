package com.example.dogsitterproject.activity;


import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;

import static com.example.dogsitterproject.utils.ConstUtils.DOG_IMAGES;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_OWNER;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_PIC_URL;

import static com.example.dogsitterproject.utils.ConstUtils.IMAGE;
import static com.example.dogsitterproject.utils.ConstUtils.IMG_FAILED;
import static com.example.dogsitterproject.utils.ConstUtils.IMG_UPLOADED;
import static com.example.dogsitterproject.utils.ConstUtils.PICK_IMAGE_REQUEST;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;

import static com.example.dogsitterproject.utils.ConstUtils.EMAIL_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME_REQUIRED;
import static com.example.dogsitterproject.utils.ConstUtils.KEY_USER;
import static com.example.dogsitterproject.utils.ConstUtils.LENGTH_PASSWORD;
import static com.example.dogsitterproject.utils.ConstUtils.PASSWORD_REQUIRED;
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
import android.widget.Toast;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.db.FirebaseRegistration;
import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

public class DogRegistration extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputLayout gender;
    private AutoCompleteTextView gender_auto;
    private TextInputEditText age;
    private TextInputLayout breed;
    private AutoCompleteTextView breed_auto;
    private TextInputEditText weight;
    private CircleImageView petImage;

    private DatabaseReference reference;
    private DatabaseReference dogReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private FirebaseStorage storage;
    private Uri resultUri;
    private ProgressDialog loader;
    private Button regButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_registration);


        findViews();
        init();
        initFirebase();
    }

    private User getUserFromIntent() {
        return (User) getIntent().getSerializableExtra(KEY_USER);
    }


    private void findViews() {
        petImage = findViewById(R.id.dogReg_IMG_profileImage);
        weight = findViewById(R.id.dogReg_IN_weight);
        breed = findViewById(R.id.dogReg_IN_breed);
        age = findViewById(R.id.dogReg_IN_age);
        gender = findViewById(R.id.dogReg_IN_gender);
        name = findViewById(R.id.dogReg_IN_Name);
        regButton = findViewById(R.id.dogReg_BTN_signUp);
        loader = new ProgressDialog(this);
        gender_auto = findViewById(R.id.dogReg_AUTO_gender);
        breed_auto = findViewById(R.id.dogReg_AUTO_breed);
        fillAdapters();


    }

    private void fillAdapters() {
        String[] gender = new String[]{"Female", "Male"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, gender
        );
        gender_auto.setAdapter(genderAdapter);

        String[] breeds = new String[]{"Boxer", "Borzoi", "Bullmastiff", "Chow Chow", "Chihuahua"};
        ArrayAdapter<String> breadsAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, breeds
        );
        breed_auto.setAdapter(breadsAdapter);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        reference = db.getReference().child(USER_DATA);
        dogReference = db.getReference().child(DOGS_DATA);
        storage = FirebaseStorage.getInstance();
    }


    private void init() {
        petImage.setOnClickListener(view -> {
            getImage();

        });

        regButton.setOnClickListener(view -> getForms());

    }


    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE);
        startActivityForResult(Intent.createChooser(intent, SELECT_PICTURE), PICK_IMAGE_REQUEST);
    }


    private void getForms() {
        String nameEdited = Objects.
                requireNonNull(name.getText()).toString().trim();
        String ganderEdited = Objects
                .requireNonNull(gender_auto.getText()).toString().trim();
        String ageEdited = Objects
                .requireNonNull(age.getText()).toString().trim();
        String breedEdited = Objects
                .requireNonNull(breed_auto.getText()).toString().trim();
        String weightEdited = Objects
                .requireNonNull(weight.getText()).toString().trim();


        //TODO: Please check for valid email
        if (TextUtils.isEmpty(nameEdited)) {
            name.setError(EMAIL_REQUIRED);
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(ganderEdited)) {
            gender.setError(PASSWORD_REQUIRED);
            gender.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(ageEdited)) {
            age.setError(LENGTH_PASSWORD);
            age.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(breedEdited)) {
            breed.setError(FULL_NAME_REQUIRED);
            breed.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(weightEdited)) {
            weight.setError(SALARY_REQUIRED);
            weight.requestFocus();

        } else {
            loader.setMessage(REGISTERING_LOADER);
            loader.setCanceledOnTouchOutside(false);
            loader.show();


            saveData(nameEdited, ganderEdited, ageEdited, breedEdited, weightEdited);


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            petImage.setImageURI(resultUri);

        }
    }

    private void saveImage(Uri resultUri, String currentUserId) {
        final StorageReference imageRef = storage.getReference().child(DOG_IMAGES).child(currentUserId);
        if (resultUri != null) {
            imageRef.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Map newImageMap = new HashMap();
                                    newImageMap.put(DOG_PIC_URL, downloadUrl);
                                    dogReference.child(currentUserId).updateChildren(newImageMap);

                                    Toast
                                            .makeText(
                                                    DogRegistration.this,
                                                    IMG_UPLOADED,
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    Intent intent = new Intent(DogRegistration.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
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
                                            DogRegistration.this,
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


    private void saveData(String nameEdited, String ganderEdited, String ageEdited, String breedEdited, String weightEdited) {
        User user = getUserFromIntent();

        mAuth
                .createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    String currentUserId = mAuth.getCurrentUser().getUid();
                    user.setId(currentUserId);
                    if (!task.isSuccessful()) {
                        String error = task.getException().toString();
                        //TODO: Toast to class
                        Toast.makeText(DogRegistration.this, "Error"
                                + error, Toast.LENGTH_SHORT).show();
                    } else {


                        HashMap<String, Object> newTypeMap = new HashMap<>();
                        newTypeMap.put(TYPE, DOG_OWNER);


                        Dog dog = new Dog(nameEdited,
                                currentUserId,
                                ganderEdited,
                                ageEdited,
                                breedEdited,
                                weightEdited, "",
                                user.getPhone());


                        FirebaseRegistration.saveData(user, newTypeMap, dog, DogRegistration.this);
                        saveImage(resultUri, currentUserId);

                    }
                });
    }
}