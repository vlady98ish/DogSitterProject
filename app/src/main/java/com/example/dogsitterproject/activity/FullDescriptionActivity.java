package com.example.dogsitterproject.activity;

import static com.example.dogsitterproject.utils.ConstUtils.KEY_USER;


import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dogsitterproject.R;
import com.example.dogsitterproject.db.FirebaseDB;

import com.example.dogsitterproject.model.DogSitter;



import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class FullDescriptionActivity extends AppCompatActivity {



    private ImageCarousel imageCarousel;
    private TextView name;
    private TextView city;
    private TextView phone;
    private TextView salary;
    private AppCompatButton sendBtn;
    private TextView description;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_description);

        findViews();
        initView();

    }

    private void findViews(){
        imageCarousel = findViewById(R.id.description_carouselView_fullCard);
        name = findViewById(R.id.description_name);
        city = findViewById(R.id.description_city);
        phone = findViewById(R.id.description_phone);
        salary = findViewById(R.id.description_price);
        sendBtn = findViewById(R.id.description_BTN_chat);
        description = findViewById(R.id.description_LBL_description);
    }

    private void initView(){
        DogSitter dogSitter = getDogSitterFromIntent();
        name.setText(dogSitter.getFullName());
        city.setText(dogSitter.getCity());
        phone.setText(dogSitter.getPhone());
        salary.setText(dogSitter.getSalary()+ "₪/hour");
        description.setText(dogSitter.getDescription());
        CallBack_Images callBack_images =
                imagesLinks -> setImagesToCarousel(dogSitter.getProfilepictureurl(),imagesLinks);
        FirebaseDB.getPropertyImageList(callBack_images,dogSitter.getId());


        sendBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(FullDescriptionActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FullDescriptionActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + dogSitter.getPhone()));
                startActivity(intent);
            }
        });


    }



    private void setImagesToCarousel(String url,ArrayList<String> urlList){
        List<CarouselItem> carouselItemList = new ArrayList<>();

                carouselItemList.add(new CarouselItem(url));
                if(urlList != null){
                    for (int i=0; i<urlList.size();i++){
                        carouselItemList.add(new CarouselItem(urlList.get(i)));
                    }
                }



        imageCarousel.setData(carouselItemList);

    }



    private DogSitter getDogSitterFromIntent(){
        return (DogSitter) getIntent().getSerializableExtra(KEY_USER);
    }


    public interface CallBack_Images{
        void imagesReady(ArrayList<String> imagesLinks);
    }




}



