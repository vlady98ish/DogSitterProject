package com.example.dogsitterproject.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
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
import com.example.dogsitterproject.model.DogSitter;


import com.example.dogsitterproject.utils.ImageUtils;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<DogSitter> userList;
    private CallBackFavClicked callBackFavClicked;


    public UserAdapter(Context context, List<DogSitter> userList) {
        this.context = context;
        this.userList = userList;

    }

    public UserAdapter setCallBackDogSitter(CallBackFavClicked callBackFavClicked){
        this.callBackFavClicked = callBackFavClicked;
        return this;

    }
    public  SpannableString styleBold(String s){
        SpannableString spannableString = new SpannableString(s);
        int startIndex = 0;
        int endIndex = s.length();
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
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
        DogSitter user = getItem(position);

        holder.user_name.setText(user.getFullName());
        holder.user_city.setText(user.getCity());
        holder.user_phone.setText(user.getPhone());
        holder.user_price.setText(user.getSalary()+"₪/hour");
        String photoLink = user.getProfilepictureurl();
        ImageUtils.getInstance().load(photoLink,holder.photo);

    }

    private DogSitter getItem(int position) {
        return userList.get(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView photo;
        private TextView user_name;
        private TextView user_city;
        private TextView user_phone;
        private TextView user_price;
        private ImageButton fav_btn;

        private AppCompatButton chatBtn;

        private boolean flag = false;



        public int PERMISSION_CODE = 100;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
            init();

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

            fav_btn.setOnClickListener(v -> {
                flag = !flag;
                if(flag) {
                    callBackFavClicked.favClicked(getItem(getAdapterPosition()));
                    fav_btn.setImageResource(R.drawable.ic_favorite_red);

                }
                else{
                    fav_btn.setImageResource(R.drawable.ic_favorite_border);
                }
            });








            chatBtn.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 100);
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + getItem(getAdapterPosition()).getPhone()));
                context.startActivity(intent);
            });
        }




    }
}
