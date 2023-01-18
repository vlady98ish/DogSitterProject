package com.example.dogsitterproject.adapter;



import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;


import com.example.dogsitterproject.utils.ImageUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.ViewHolder>{


    private Context context;
    private List<Dog> dogsList;


    public DogAdapter(Context context, List<Dog> dogsList) {
        this.context = context;
        this.dogsList = dogsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_view, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Dog dog = dogsList.get(position);
        holder.dog_name.setText(dog.getName());
        holder.dog_gender.setText(dog.getGender());
        holder.dog_age.setText(dog.getAge()+ " Years");
        holder.dog_weight.setText(dog.getWeight()+" Kg");
        String photoLink = dog.getDogpictureurl();
        ImageUtils.getInstance().load(photoLink,holder.photo);
    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView photo;
        public TextView dog_name;
        public TextView dog_gender;
        public TextView dog_age;
        public TextView dog_weight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
        }

        public void findViews(){
            photo = itemView.findViewById(R.id.user_view_IMG_photo);
            dog_name = itemView.findViewById(R.id.user_view_name);
            dog_gender = itemView.findViewById(R.id.user_view_city);
            dog_age = itemView.findViewById(R.id.user_view_time);
            dog_weight = itemView.findViewById(R.id.user_view_price);

        }
    }
}

