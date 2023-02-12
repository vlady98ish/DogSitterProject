package com.example.dogsitterproject.adapter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.calback.CallBackFavClicked;
import com.example.dogsitterproject.model.Dog;


import com.example.dogsitterproject.utils.ImageUtils;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.ViewHolder> {


    private Context context;
    private List<Dog> dogsList;

    private List<Boolean> favList;
    private CallBackFavClicked callBackFavClicked;

    private boolean fav = false;


    public DogAdapter(Context context, List<Dog> dogsList, List<Boolean> flags, boolean fav) {
        this.context = context;
        this.dogsList = dogsList;
        this.fav = fav;
        this.favList = flags;
    }

    public DogAdapter(Context context, List<Dog> dogsList, boolean fav) {
        this.context = context;
        this.dogsList = dogsList;
        this.fav = fav;
    }

    public DogAdapter setCallBackDog(CallBackFavClicked callBackFavClicked) {
        this.callBackFavClicked = callBackFavClicked;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_view, parent, false);
        return new ViewHolder(view);
    }

    private Dog getItem(int position) {
        return dogsList.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Dog dog = dogsList.get(position);
        final boolean favFlag;
        if (fav) {
            holder.fav_btn.setVisibility(View.GONE);
        } else {
            favFlag = favList.get(position);
            if (favFlag) {

                holder.fav_btn.setImageResource(R.drawable.ic_favourite);
                holder.flag = true;
            }
        }
        holder.dog_name.setText(dog.getName());
        holder.dog_breed.setText(dog.getBreed());
        holder.dog_age.setText(dog.getAge() + " Years");
        holder.dog_weight.setText(dog.getWeight() + " Kg");
        String photoLink = dog.getDogpictureurl();
        ImageUtils.getInstance().load(photoLink, holder.photo);


    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView photo;
        public TextView dog_name;
        public TextView dog_breed;
        public TextView dog_age;
        public TextView dog_weight;
        public ImageButton fav_btn;


        public AppCompatButton chatBtn;
        public boolean flag = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
            init();

        }

        public void findViews() {
            photo = itemView.findViewById(R.id.user_view_IMG_photo);
            dog_name = itemView.findViewById(R.id.user_view_name);
            dog_breed = itemView.findViewById(R.id.user_view_city);
            dog_age = itemView.findViewById(R.id.user_view_time);
            dog_weight = itemView.findViewById(R.id.user_view_price);
            fav_btn = itemView.findViewById(R.id.user_view_BTN_fav);
            chatBtn = itemView.findViewById(R.id.user_view_BTN_chat);

        }

        public void init() {
            fav_btn.setOnClickListener(v -> {
                flag = !flag;
                if (flag) {
                    callBackFavClicked.favClicked(getItem(getAdapterPosition()));
                    fav_btn.setImageResource(R.drawable.ic_favourite);
                } else {
                    callBackFavClicked.removeFromFav(getItem(getAdapterPosition()));
                    fav_btn.setImageResource(R.drawable.ic_favorite_border);
                }
            });


            chatBtn.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 100);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + getItem(getAdapterPosition()).getPhone()));
                    context.startActivity(intent);
                }
            });


        }


    }
}

