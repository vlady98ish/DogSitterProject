package com.example.dogsitterproject.db;

import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;

import static com.example.dogsitterproject.utils.ConstUtils.DOG_SET_SUCCESSFULLY;


import static com.example.dogsitterproject.utils.ConstUtils.PROFILE_PIC_URL;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.USER_NOT_EXIST;
import static com.example.dogsitterproject.utils.ConstUtils.USER_SET_SUCCESSFULLY;



import android.net.Uri;


import androidx.annotation.NonNull;


import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.model.User;
import com.example.dogsitterproject.utils.MySignal;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;

import java.util.Map;


public class FirebaseRegistration {

    public static void saveData(User user, HashMap<String, Object> type, Dog dog, CallBack_saveData callBack_saveData) {
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseDB.getCurrentId());


        dsRef
                .setValue(user)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        dsRef.updateChildren(type).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                MySignal.getInstance().toast(USER_SET_SUCCESSFULLY);

                                saveDog(dog, callBack_saveData);
                            }
                        });



                    } else {
                        MySignal.getInstance().toast(task.getException().toString());

                    }
                });
    }






    public interface CallBack_saveImage{
        void changeIntent();
        void fail();
        void loading(long byteTransferred,long totalByteCount);
    }
    public interface CallBack_saveData{
        void continueLoading();
    }

    public static void saveImage(Uri resultUri,String pathToDB, String pathToStorage, String currentUserId, CallBack_saveImage callBack_save) {
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(pathToDB)
                .child(FirebaseDB.getCurrentId());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(pathToStorage).child(currentUserId);
        if (resultUri != null) {
            storageReference.putFile(resultUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Map<String, Object> newImageMap = new HashMap<>();
                        newImageMap.put(PROFILE_PIC_URL, downloadUrl);
                        dsRef.updateChildren(newImageMap);
                        callBack_save.changeIntent();

                    }))
                    .addOnFailureListener(e -> {
                        callBack_save.fail();
                    })
                    .addOnProgressListener(snapshot -> {
                        callBack_save.loading(snapshot.getBytesTransferred(),snapshot.getTotalByteCount());
                    });
        }
    }

    private static void saveDog(Dog dog, CallBack_saveData callBack_saveData) {
        DatabaseReference dogRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DOGS_DATA)
                .child(FirebaseDB.getCurrentId());
        dogRef
                .setValue(dog)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            MySignal.getInstance().toast(DOG_SET_SUCCESSFULLY);
                            callBack_saveData.continueLoading();

                        } else {
                            MySignal.getInstance().toast(task.getException().toString());

                        }

                    }
                });

    }

    public static void saveDogSitter(DogSitter dogSitter, HashMap<String, Object> type, CallBack_saveData callBack_saveData) {
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseDB.getCurrentId());


        dsRef
                .setValue(dogSitter)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        dsRef.updateChildren(type).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                MySignal.getInstance().toast(USER_SET_SUCCESSFULLY);
                                callBack_saveData.continueLoading();
                            }
                        });


                    } else {
                        MySignal.getInstance().toast(USER_NOT_EXIST);

                    }
                });


    }


}
