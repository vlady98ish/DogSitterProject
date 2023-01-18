package com.example.dogsitterproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.model.User;

import com.example.dogsitterproject.utils.ImageUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{


    private Context context;
    private List<DogSitter> userList;


    public UserAdapter(Context context, List<DogSitter> userList) {
        this.context = context;
        this.userList = userList;
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
        final DogSitter user = userList.get(position);
        holder.user_name.setText(user.getFullName());
        holder.user_city.setText(user.getCity());
        holder.user_available.setText(user.getEmail());
        holder.user_price.setText(user.getSalary()+"NIS/h");
        String photoLink = user.getProfilepictureurl();
        ImageUtils.getInstance().load(photoLink,holder.photo);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView photo;
        public TextView user_name;
        public TextView user_city;
        public TextView user_available;
        public TextView user_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
        }

        public void findViews(){
            photo = itemView.findViewById(R.id.user_view_IMG_photo);
            user_name = itemView.findViewById(R.id.user_view_name);
            user_city = itemView.findViewById(R.id.user_view_city);
            user_available = itemView.findViewById(R.id.user_view_time);
            user_price = itemView.findViewById(R.id.user_view_price);

        }
    }
}
