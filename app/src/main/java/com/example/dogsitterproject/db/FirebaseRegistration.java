package com.example.dogsitterproject.db;

import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;

import static com.example.dogsitterproject.utils.ConstUtils.DOG_SET_SUCCESSFULLY;

import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.USER_SET_SUCCESSFULLY;

import android.content.Context;

import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

import java.util.Objects;

public class FirebaseRegistration {

    public static void saveData(User user, HashMap<String,Object>type, Dog dog, Context context){
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseDB.getCurrentId());


        dsRef
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            dsRef.updateChildren(type);
                            Toast
                                    .makeText(context,
                                            USER_SET_SUCCESSFULLY,
                                            Toast.LENGTH_SHORT)
                                    .show();

                                    saveDog(dog,context);


                        }
                        else {
                            Toast.makeText(context,
                                            Objects
                                                    .requireNonNull(task
                                                            .getException())
                                                    .toString(),
                                            Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }
                });
    }

    private static void saveDog(Dog dog,Context context){
        DatabaseReference dogRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DOGS_DATA)
                .child(FirebaseDB.getCurrentId())
                ;
        dogRef
                .setValue(dog)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            Toast
                                    .makeText(context,
                                            DOG_SET_SUCCESSFULLY,
                                            Toast.LENGTH_SHORT)
                                    .show();

                        } else {
                            Toast.makeText(context,
                                            Objects
                                                    .requireNonNull(task
                                                            .getException())
                                                    .toString(),
                                            Toast.LENGTH_SHORT)
                                    .show();

                        }

                    }
                });

    }

    public static void saveDogSitter(DogSitter dogSitter, HashMap<String,Object>type, Context context){
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseDB.getCurrentId());


        dsRef
                .setValue(dogSitter)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            dsRef.updateChildren(type);
                            Toast
                                    .makeText(context,
                                            USER_SET_SUCCESSFULLY,
                                            Toast.LENGTH_SHORT)
                                    .show();



                        }
                        else {
                            Toast.makeText(context,
                                            Objects
                                                    .requireNonNull(task
                                                            .getException())
                                                    .toString(),
                                            Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }
                });
    }


}
