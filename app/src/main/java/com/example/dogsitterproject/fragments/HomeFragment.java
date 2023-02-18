package com.example.dogsitterproject.fragments;



import static com.example.dogsitterproject.utils.ConstUtils.DOG_OWNER;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.NO_DOGS;
import static com.example.dogsitterproject.utils.ConstUtils.NO_DOG_SITTERS;


import android.app.ProgressDialog;
import android.os.Bundle;

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
import com.example.dogsitterproject.calback.CallBack_AddToFav;
import com.example.dogsitterproject.calback.CallBack_RemoveFromFav;
import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.db.FirebaseDB;


import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;


    private AppCompatActivity activity;
    private DogAdapter dogAdapter;
    private ProgressDialog loader;

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
        loader = new ProgressDialog(getContext());
        loader.setMessage("Uploading data...");
        loader.setCanceledOnTouchOutside(false);
        loader.show();
        FirebaseDB.CallBack_Data callBack_data = new FirebaseDB.CallBack_Data() {
            @Override
            public void dogDataReady(ArrayList<Dog> dogs, ArrayList<Boolean> flags) {
                dogAdapter = new DogAdapter(getActivity(), dogs, flags, false);
                dogAdapter.setCallBack_addToFav(callBack_addToFav);
                dogAdapter.setCallBackRemoveFromFav(callBackRemoveFromFav);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                if (dogs.isEmpty()) {
                    Toast.makeText(activity, NO_DOGS, Toast.LENGTH_SHORT).show();

                }
                loader.dismiss();


                recyclerView.setAdapter(dogAdapter);
                dogAdapter.notifyDataSetChanged();


            }

            @Override
            public void dogSittersDataReady(ArrayList<DogSitter> dogSitters, ArrayList<Boolean> flags) {
                UserAdapter dogSitterAdapter = new UserAdapter(getActivity(), dogSitters, flags, false);
                dogSitterAdapter.setCallBack_addToFav(callBack_addToFav);
                dogSitterAdapter.setCallBackRemoveFromFav(callBackRemoveFromFav);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                if (dogSitters.isEmpty()) {
                    Toast.makeText(activity, NO_DOG_SITTERS, Toast.LENGTH_SHORT).show();

                }
                loader.dismiss();


                recyclerView.setAdapter(dogSitterAdapter);
                dogSitterAdapter.notifyDataSetChanged();
            }
        };

        triggerFB(callBack_data);


    }

    private void triggerFB(FirebaseDB.CallBack_Data callBack_data) {
        FirebaseDB.CallBack_Type callBack_type = new FirebaseDB.CallBack_Type() {
            @Override
            public void getAll(String type) {
                if(type.equals(DOG_SITTER)){
                    FirebaseDB.getAllDogs(callBack_data);
                }
                else if(type.equals(DOG_OWNER)){
                    FirebaseDB.getAllDogSitters(callBack_data);
                }
            }
        };

        FirebaseDB.getType(callBack_type);
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




    CallBack_AddToFav callBack_addToFav = new CallBack_AddToFav() {
        @Override
        public void addDogToFav(Dog dog) {
            FirebaseDB.updateFavDog(dog);
        }

        @Override
        public void addDogSitterToFav(DogSitter dogSitter) {
            FirebaseDB.updateFavDogSitter(dogSitter);
        }
    };


    CallBack_RemoveFromFav callBackRemoveFromFav = new CallBack_RemoveFromFav() {
        @Override
        public void removeDogFromFav(Dog item) {
            FirebaseDB.removeFromFavDog(item);
        }

        @Override
        public void removeDogSitterFromFav(DogSitter dogSitter) {
            FirebaseDB.removeFromFavDogSitter(dogSitter);
        }
    };


}
