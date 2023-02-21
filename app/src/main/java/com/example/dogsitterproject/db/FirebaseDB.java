package com.example.dogsitterproject.db;

import static com.example.dogsitterproject.utils.ConstUtils.DOGSITTER_IMAGES;
import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;

import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.EMAIL;
import static com.example.dogsitterproject.utils.ConstUtils.FAVORITE_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.FAV_IN;

import static com.example.dogsitterproject.utils.ConstUtils.FILE_NOT_SELECTED;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME;
import static com.example.dogsitterproject.utils.ConstUtils.IMAGES;
import static com.example.dogsitterproject.utils.ConstUtils.NAME;
import static com.example.dogsitterproject.utils.ConstUtils.PASSWORD;
import static com.example.dogsitterproject.utils.ConstUtils.PHONE;
import static com.example.dogsitterproject.utils.ConstUtils.PROFILE_PIC_URL;
import static com.example.dogsitterproject.utils.ConstUtils.SITTER_IMAGES;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.UPLOAD_SUCCESS;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.WEIGHT;


import android.net.Uri;

import androidx.annotation.NonNull;


import com.example.dogsitterproject.activity.FullDescriptionActivity;

import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;


import com.example.dogsitterproject.model.User;
import com.example.dogsitterproject.utils.MySignal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FirebaseDB {


    public static void getAllDogs(CallBack_Data callBack_data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(DOGS_DATA);
        ref.addValueEventListener(new ValueEventListener() {
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
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId = FirebaseAuth.getInstance().getUid();
                ArrayList<DogSitter> dogSitters = new ArrayList<>();
                ArrayList<Boolean> flags = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                        if(child.child(TYPE).getValue() != null) {
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
                    }

                if (callBack_data != null) callBack_data.dogSittersDataReady(dogSitters, flags);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getPropertyImageList(FullDescriptionActivity.CallBack_Images callBack_images,String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(USER_DATA).child(id).child(IMAGES);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> imageList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    imageList.add(map.get(SITTER_IMAGES));
                }
                if (callBack_images != null) {
                    callBack_images.imagesReady(imageList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public static void getFavDogs(CallBack_FavData callBack_favData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(USER_DATA).child(getCurrentId()).child(FAVORITE_DATA);
        ref.addValueEventListener(new ValueEventListener() {
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
        ref.addValueEventListener(new ValueEventListener() {
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


    public static void updateFavDog(Dog dog) {
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


    public static void updateFavDogSitter(DogSitter dogSitter) {
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

    public static void removeFromFavDog(Dog dog) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .child(FAVORITE_DATA)
                .child(dog.getIdUser()).removeValue();


        removeFavInToDog(dog);
    }

    public static void removeFromFavDogSitter(DogSitter dogSitter) {
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


    public interface CallBack_Update {
        void updateToStartDogSitter(String nameDb, String emailDb, String passwordDb, String phoneDb, String profilePicture);

        void updateToStartDog(String nameDb, String emailDb, String passwordDb, String phoneDb, String profilePicture, String dogNameDb, String dogWeightDb);
    }

    public static void getStartData(CallBack_Update callBack_update) {

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(getCurrentId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(TYPE).getValue().equals(DOG_SITTER)) {
                    getInfoDogSitter(snapshot, callBack_update);
                } else {
                    getInfoDog(snapshot, callBack_update);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private static void getInfoDogSitter(DataSnapshot snapshot, CallBack_Update callBack_update) {
        String name = snapshot.child(FULL_NAME).getValue(String.class);
        String email = snapshot.child(EMAIL).getValue(String.class);
        String password = snapshot.child(PASSWORD).getValue(String.class);
        String phone = snapshot.child(PHONE).getValue(String.class);
        String pictureUrl = snapshot.child(PROFILE_PIC_URL).getValue(String.class);
        callBack_update.updateToStartDogSitter(name, email, password, phone, pictureUrl);
    }

    private static void getInfoDog(DataSnapshot snapshot, CallBack_Update callBack_update) {
        String name = snapshot.child(FULL_NAME).getValue(String.class);
        String email = snapshot.child(EMAIL).getValue(String.class);
        String password = snapshot.child(PASSWORD).getValue(String.class);
        String phone = snapshot.child(PHONE).getValue(String.class);
        DatabaseReference dogRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DOGS_DATA)
                .child(getCurrentId());
        dogRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pictureUrl = snapshot.child(PROFILE_PIC_URL).getValue(String.class);
                String dogName = snapshot.child(NAME).getValue(String.class);
                String dogWeight = snapshot.child(WEIGHT).getValue(String.class);
                callBack_update.updateToStartDog(name, email, password, phone, pictureUrl, dogName, dogWeight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public static void updateInfo(String name, String email, String password, String phone) {
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(getCurrentId());
        dsRef.child(FULL_NAME).setValue(name);
        dsRef.child(EMAIL).setValue(email);
        dsRef.child(PASSWORD).setValue(password);
        dsRef.child(PHONE).setValue(phone);
    }

    public static void updateDogInfo(String dogName, String dogWeight) {
        DatabaseReference dsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DOGS_DATA)
                .child(getCurrentId());
        dsRef.child(NAME).setValue(dogName);
        dsRef.child(WEIGHT).setValue(dogWeight);
    }

    public static void getType(CallBack_Type callBack_type){
                DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                        .child(getCurrentId());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String typeUser = snapshot.child(TYPE).getValue().toString();
                callBack_type.getAll(typeUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface CallBack_Type{
        void getAll(String type);
    }
    public interface CallBack_GetInfo{
        void setInfoUser(User User);
        void setInfoDog(Dog dog);
    }

    public static void getInfo(CallBack_GetInfo callBack_getInfo){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference= db.getReference().child(USER_DATA).child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        DatabaseReference dogRef = db.getReference(DOGS_DATA);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child(TYPE).getValue().toString();
                if(type.equals(DOG_SITTER)){
                    DogSitter dogSitter = snapshot.getValue(DogSitter.class);
                    callBack_getInfo.setInfoUser(dogSitter);
                }
                else{
                    User user = snapshot.getValue(User.class);
                    callBack_getInfo.setInfoUser(user);
                    dogRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Dog dog = snapshot.child(getCurrentId()).getValue(Dog.class);
                            callBack_getInfo.setInfoDog(dog);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public static void saveImages(ArrayList<Uri> uriArrayList) {
        if (uriArrayList != null) {
            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child(IMAGES);
            for (int i = 0; i < uriArrayList.size(); i++) {
                Uri currentImg = uriArrayList.get(i);
                StorageReference imageName = ImageFolder.child("ImagesForId-" + FirebaseDB.getCurrentId()).child(String.valueOf(i));

                imageName.putFile(currentImg).addOnSuccessListener(taskSnapshot ->
                        imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                            String url = String.valueOf(uri);
                            storeToUser(url);
                        }));
            }
            MySignal.getInstance().toast(UPLOAD_SUCCESS);
        } else {
            MySignal.getInstance().toast(FILE_NOT_SELECTED);
        }
    }

    private static void storeToUser(String url){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(USER_DATA).child(FirebaseDB.getCurrentId()).child(IMAGES);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(DOGSITTER_IMAGES, url);
        databaseReference.push().setValue(hashMap);
    }


}
