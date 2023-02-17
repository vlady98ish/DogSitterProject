package com.example.dogsitterproject.fragments;

import static com.example.dogsitterproject.utils.ConstUtils.DOG_OWNER;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.NO_DOGS;
import static com.example.dogsitterproject.utils.ConstUtils.NO_DOG_SITTERS;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.adapter.DogAdapter;
import com.example.dogsitterproject.adapter.UserAdapter;
import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.db.FirebaseDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        findViews(view);
        initLayerManager();
        initViews();
        return view;
    }


    private void initViews() {
//        loader.setMessage("Uploading data...");
//        loader.setCanceledOnTouchOutside(false);
//        loader.show();
        FirebaseDB.CallBack_FavData callBack_favData = new FirebaseDB.CallBack_FavData() {
            @Override
            public void dogDataReady(ArrayList<Dog> dogs) {
                DogAdapter dogAdapter = new DogAdapter(getActivity(), dogs, true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                if (dogs.isEmpty()) {
                    Toast.makeText(getActivity(), NO_DOGS, Toast.LENGTH_SHORT).show();

                }
//                loader.dismiss();

                dogAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(dogAdapter);
            }

            @Override
            public void dogSittersDataReady(ArrayList<DogSitter> dogSitters) {
                UserAdapter dogSitterAdapter = new UserAdapter(getActivity(), dogSitters, true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                if (dogSitters.isEmpty()) {
                    Toast.makeText(getActivity(), NO_DOG_SITTERS, Toast.LENGTH_SHORT).show();

                }
//                loader.dismiss();

                dogSitterAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(dogSitterAdapter);
            }
        };


        triggerFB(callBack_favData);


    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.fav_recycleView);
    }

    private void initLayerManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
    }

    private void triggerFB(FirebaseDB.CallBack_FavData callBack_favData) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA)
                .child(FirebaseDB.getCurrentId());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String typeUser = snapshot.child(TYPE).getValue().toString();
                if (typeUser.equals(DOG_SITTER)) {
                    FirebaseDB.getFavDogs(callBack_favData);
                } else if (typeUser.equals(DOG_OWNER)) {
                    FirebaseDB.getFavDogSitters(callBack_favData);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
