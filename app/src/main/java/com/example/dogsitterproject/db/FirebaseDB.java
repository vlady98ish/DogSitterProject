package com.example.dogsitterproject.db;

import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.FAVORITE_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.FAV_IN;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;

import com.google.android.gms.tasks.OnSuccessListener;
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
                String userid = FirebaseAuth.getInstance().getUid();
                ArrayList<Dog> dogs = new ArrayList<>();
                ArrayList<Boolean> flags = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Dog dog = child.getValue(Dog.class);
                    if (child.child(FAV_IN).child(userid).exists()) {
                        dogs.add(dog);
                        flags.add(true);
                    } else {
                        dogs.add(dog);
                        flags.add(false);
                    }

                }
                if (callBack_data != null) callBack_data.dogDataReady(dogs, flags);
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
                String userId = FirebaseAuth.getInstance().getUid();
                ArrayList<DogSitter> dogSitters = new ArrayList<>();
                ArrayList<Boolean> flags = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child(TYPE).getValue().toString().equals(DOG_SITTER)) {
                        DogSitter dogSitter = child.getValue(DogSitter.class);
                        if (child.child(FAV_IN).child(userId).exists()) {
                            dogSitters.add(dogSitter);
                            flags.add(true);
                        } else {
                            dogSitters.add(dogSitter);
                            flags.add(false);
                        }

                    }
                }
                if (callBack_data != null) callBack_data.dogSittersDataReady(dogSitters, flags);
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
        void dogDataReady(ArrayList<Dog> dogs, ArrayList<Boolean> flags);

        void dogSittersDataReady(ArrayList<DogSitter> dogSitters, ArrayList<Boolean> flags);
    }



    public static void updateFavDog(Dog dog){
        DatabaseReference dogRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .child(FAVORITE_DATA)
                .child(dog.getIdUser());
        dogRef.setValue(dog);
        addFavInToDog(dog);
    }


    public static void updateFavDogSitter(DogSitter dogSitter){
        DatabaseReference sitterRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .child(FAVORITE_DATA)
                .child(dogSitter.getId());
        sitterRef.setValue(dogSitter);
        addFavInToDogSitter(dogSitter);
    }

    public static void removeFromFavDog(Dog dog){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .child(FAVORITE_DATA)
                .child(dog.getIdUser()).removeValue();


        removeFavInToDog(dog);
    }

    public static void removeFromFavDogSitter(DogSitter dogSitter){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .child(FAVORITE_DATA)
                .child(dogSitter.getId()).removeValue()
        ;
        removeFavInToDogSitter(dogSitter);
    }



    private static void addFavInToDog(Dog dog) {
        DatabaseReference dogRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DOGS_DATA)
                .child(dog.getIdUser())
                .child(FAV_IN)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        dogRef.setValue(true);
    }

    private static void removeFavInToDog(Dog dog) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DOGS_DATA)
                .child(dog.getIdUser())
                .child(FAV_IN)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .removeValue();
    }

    private static void addFavInToDogSitter(DogSitter dogSitter) {
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(dogSitter.getId())
                .child(FAV_IN)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dsRef.setValue(true);
    }

    private static void removeFavInToDogSitter(DogSitter dogSitter) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(dogSitter.getId())
                .child(FAV_IN)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .removeValue();
    }





}
