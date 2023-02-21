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
import com.example.dogsitterproject.calback.CallBack_AddToFav;
import com.example.dogsitterproject.calback.CallBack_RemoveFromFav;
import com.example.dogsitterproject.calback.OnClickItemRecycleView;
import com.example.dogsitterproject.model.DogSitter;


import com.example.dogsitterproject.utils.ImageUtils;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<DogSitter> userList;

    private List<Boolean> favList;
    private CallBack_AddToFav callBack_addToFav;
    private CallBack_RemoveFromFav callBackRemoveFromFav;
    private OnClickItemRecycleView onClickItemRecycleView;


    private boolean fav = false;


    public UserAdapter(Context context, List<DogSitter> userList, List<Boolean> favList, boolean fav) {
        this.context = context;
        this.userList = userList;
        this.fav = fav;
        this.favList = favList;
    }

    public UserAdapter(Context context, List<DogSitter> userList, boolean fav) {
        this.context = context;
        this.userList = userList;
        this.fav = fav;

    }

    public UserAdapter setCallBack_addToFav(CallBack_AddToFav callBack_addToFav) {
        this.callBack_addToFav = callBack_addToFav;
        return this;
    }

    public UserAdapter setCallBackRemoveFromFav(CallBack_RemoveFromFav callBackRemoveFromFav) {
        this.callBackRemoveFromFav = callBackRemoveFromFav;
        return this;
    }

    public UserAdapter setOnClickItemRecycleView(OnClickItemRecycleView onClickItemRecycleView) {
        this.onClickItemRecycleView = onClickItemRecycleView;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DogSitter user = getItem(position);
        final boolean favFlag;
        if (fav) {
            holder.fav_btn.setImageResource(R.drawable.ic_favourite);
            holder.flag = true;
        } else {
            favFlag = favList.get(position);
            if (favFlag) {
                holder.fav_btn.setImageResource(R.drawable.ic_favourite);
                holder.flag = true;
            }
        }
        holder.user_name.setText(user.getFullName());
        holder.user_city.setText(user.getCity());
        holder.user_phone.setText(user.getPhone());
        holder.user_price.setText(user.getSalary() + "₪/hour");
        String photoLink = user.getProfilepictureurl();
        ImageUtils.getInstance().load(photoLink, holder.photo);

    }

    private DogSitter getItem(int position) {
        return userList.get(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView photo;
        private TextView user_name;
        private TextView user_city;
        private TextView user_phone;
        private TextView user_price;
        private ImageButton fav_btn;

        private AppCompatButton chatBtn;

        private boolean flag;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
            init();

        }

        public void findViews() {
            photo = itemView.findViewById(R.id.user_view_IMG_photo);
            user_name = itemView.findViewById(R.id.user_view_name);
            user_city = itemView.findViewById(R.id.user_view_city);
            user_phone = itemView.findViewById(R.id.user_view_time);
            user_price = itemView.findViewById(R.id.user_view_price);
            fav_btn = itemView.findViewById(R.id.user_view_BTN_fav);
            chatBtn = itemView.findViewById(R.id.user_view_BTN_chat);
        }

        public void init() {
            flag = false;
            fav_btn.setOnClickListener(v -> {
                flag = !flag;
                if (flag) {
                    callBack_addToFav.addDogSitterToFav(getItem(getAdapterPosition()));
                    fav_btn.setImageResource(R.drawable.ic_favourite);

                } else {
                    callBackRemoveFromFav.removeDogSitterFromFav(getItem(getAdapterPosition()));
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
            itemView.setOnClickListener(v -> onClickItemRecycleView.onClickItem(getItem(getAdapterPosition())));
        }


    }
}
