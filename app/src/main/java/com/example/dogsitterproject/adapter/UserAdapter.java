package com.example.dogsitterproject.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.model.User;

import com.example.dogsitterproject.utils.ImageUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.Instant;
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
        holder.user_phone.setText(user.getPhone());
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
        public TextView user_phone;
        public TextView user_price;
        public ImageButton fav_btn;
        public FirebaseDatabase databaseReference;
        public DatabaseReference fav_ref;
        public AppCompatButton chatBtn;


        public boolean flag = false;

        public int PERMISSION_CODE = 100;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
            init();
            initFirebase();
        }

        public void findViews(){
            photo = itemView.findViewById(R.id.user_view_IMG_photo);
            user_name = itemView.findViewById(R.id.user_view_name);
            user_city = itemView.findViewById(R.id.user_view_city);
            user_phone = itemView.findViewById(R.id.user_view_time);
            user_price = itemView.findViewById(R.id.user_view_price);
            fav_btn = itemView.findViewById(R.id.user_view_BTN_fav);
            chatBtn = itemView.findViewById(R.id.user_view_BTN_chat);
        }
        public void init(){
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = !flag;
                    if(flag) {
                        fav_btn.setImageResource(R.drawable.ic_favorite_red);
                    }
                    else{
                        fav_btn.setImageResource(R.drawable.ic_favorite_border);
                    }
                }
            });


            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.CALL_PHONE},100);
                    }
                    else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + user_phone.getText().toString()));
                        context.startActivity(intent);
                    }
                }
            });
        }
        public void initFirebase(){
            databaseReference = FirebaseDatabase.getInstance();
        }



    }
}
