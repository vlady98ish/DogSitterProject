package com.example.dogsitterproject.db;

import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.FAVORITE_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;

import androidx.annotation.NonNull;

import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDB {
    public static void getAllDogs(CallBack_Data callBack_data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(DOGS_DATA);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Dog> dogs = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Dog dog = child.getValue(Dog.class);
                    dogs.add(dog);
                }
                if (callBack_data != null) callBack_data.dogDataReady(dogs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void getAllDogSitters(CallBack_Data callBack_data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(USER_DATA);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DogSitter> dogSitters = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("type").getValue().toString().equals(DOG_SITTER)) {
                        DogSitter dogSitter = child.getValue(DogSitter.class);
                        dogSitters.add(dogSitter);
                    }
                }
                if (callBack_data != null) callBack_data.dogSittersDataReady(dogSitters);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void getFavDogs(CallBack_FavData callBack_favData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(USER_DATA).child(getCurrentId()).child(FAVORITE_DATA);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Dog> dogs = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Dog dog = child.getValue(Dog.class);
                    dogs.add(dog);
                }
                if (callBack_favData != null) callBack_favData.dogDataReady(dogs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getFavDogSitters(CallBack_FavData callBack_favData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(USER_DATA).child(getCurrentId()).child(FAVORITE_DATA);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DogSitter> dogSitters = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {

                    DogSitter dogSitter = child.getValue(DogSitter.class);
                    dogSitters.add(dogSitter);

                }
                if (callBack_favData != null) callBack_favData.dogSittersDataReady(dogSitters);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static String getCurrentId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    }


    public interface CallBack_FavData {
        void dogDataReady(ArrayList<Dog> dogs);

        void dogSittersDataReady(ArrayList<DogSitter> dogSitters);
    }


    public interface CallBack_Data {
        void dogDataReady(ArrayList<Dog> dogs);

        void dogSittersDataReady(ArrayList<DogSitter> dogSitters);
    }


}
