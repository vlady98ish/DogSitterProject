package com.example.dogsitterproject.fragments;

import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_OWNER;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.NO_DOGS;
import static com.example.dogsitterproject.utils.ConstUtils.NO_DOG_SITTERS;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.adapter.DogAdapter;
import com.example.dogsitterproject.adapter.UserAdapter;
import com.example.dogsitterproject.calback.CallBackFavClicked;
import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.db.FirebaseDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;


    private AppCompatActivity activity;


    public HomeFragment setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);
        initLayerManager();
        initViews();


        return view;

    }

    private void initViews() {
//        loader.setMessage("Uploading data...");
//        loader.setCanceledOnTouchOutside(false);
//        loader.show();
        FirebaseDB.CallBack_Data callBack_data = new FirebaseDB.CallBack_Data() {
            @Override
            public void dogDataReady(ArrayList<Dog> dogs, ArrayList<Boolean> flags) {
                DogAdapter dogAdapter = new DogAdapter(getActivity(), dogs, flags, false);
                dogAdapter.setCallBackDog(callBackFavClicked);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                if (dogs.isEmpty()) {
                    Toast.makeText(activity, NO_DOGS, Toast.LENGTH_SHORT).show();

                }
//                loader.dismiss();

                dogAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(dogAdapter);


            }

            @Override
            public void dogSittersDataReady(ArrayList<DogSitter> dogSitters, ArrayList<Boolean> flags) {
                UserAdapter dogSitterAdapter = new UserAdapter(getActivity(), dogSitters, flags, false);
                dogSitterAdapter.setCallBackDogSitter(callBackFavClicked);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                if (dogSitters.isEmpty()) {
                    Toast.makeText(activity, NO_DOG_SITTERS, Toast.LENGTH_SHORT).show();

                }
//                loader.dismiss();

                dogSitterAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(dogSitterAdapter);

            }
        };

        triggerFB(callBack_data);


    }

    private void triggerFB(FirebaseDB.CallBack_Data callBack_data) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String typeUser = snapshot.child(TYPE).getValue().toString();
                if (typeUser.equals(DOG_SITTER)) {
                    FirebaseDB.getAllDogs(callBack_data);
                } else if (typeUser.equals(DOG_OWNER)) {
                    FirebaseDB.getAllDogSitters(callBack_data);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.home_recycleView);

    }


    private void initLayerManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
    }


    CallBackFavClicked callBackFavClicked = new CallBackFavClicked() {
        @Override
        public void favClicked(Dog dog) {
            FirebaseDB.updateFavDog(dog);
        }

        @Override
        public void favClicked(DogSitter dogSitter) {
            FirebaseDB.updateFavDogSitter(dogSitter);
        }

        @Override
        public void removeFromFav(Dog item) {
            FirebaseDB.removeFromFavDog(item);

        }

        @Override
        public void removeFromFav(DogSitter dogSitter) {
            FirebaseDB.removeFromFavDogSitter(dogSitter);
        }
    };


}
