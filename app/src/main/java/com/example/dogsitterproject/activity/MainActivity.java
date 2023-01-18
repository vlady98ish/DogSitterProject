package com.example.dogsitterproject.activity;

import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_OWNER;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_PIC_URL;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.dogsitterproject.R;
import com.example.dogsitterproject.adapter.DogAdapter;
import com.example.dogsitterproject.adapter.UserAdapter;
import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;
import com.example.dogsitterproject.model.User;
import com.example.dogsitterproject.utils.ImageUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private NavigationView navigationView;


    private RecyclerView recyclerView;
    private ProgressBar progressBar;


    private List<DogSitter> userList;
    private List<Dog> dogList;
    private UserAdapter userAdapter;
    private DogAdapter dogAdapter;

    private LinearLayout navigation_LYT_dog;


    private TextView userName, userCity, userPhone, userEmail;

    private TextView dogWeight;
    private TextView dogYear;
    private TextView dogType;
    private TextView dogName;
    private View line;
    private String typeUser = "";


    private CircleImageView navigation_IMG_dog;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initLayerManager();
        updateInfo();
        updateRecycle();

        init();
    }


    private void findViews() {
        drawerLayout = findViewById(R.id.main_drawerLayout);
        toolbar = findViewById(R.id.main_BAR_tool);
        navigationView = findViewById(R.id.main_NAV_view);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.main_BAR_progress);
        recyclerView = findViewById(R.id.main_recycleView);

        userName = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_name);
        userCity = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_city);
        userPhone = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_phone);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_email);


        dogWeight = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_dogWeight);
        dogYear = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_dogYear);
        dogType = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_dogType);
        dogName = navigationView.getHeaderView(0).findViewById(R.id.navigation_LBL_dogName);
        navigation_IMG_dog = navigationView.getHeaderView(0).findViewById(R.id.navigation_IMG_dog);

        navigation_LYT_dog = navigationView.getHeaderView(0).findViewById(R.id.navigation_LYT_dog);
        line = navigationView.getHeaderView(0).findViewById(R.id.navigation_line);

    }

    private void init() {
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void updateInfo() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child(USER_DATA).child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        getType();
        DatabaseReference dogRef = db.getReference(DOGS_DATA);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("fullName").getValue().toString();
                    userName.setText(name);
                    String city = snapshot.child("city").getValue().toString();
                    userCity.setText(city);
                    String phone = snapshot.child("phone").getValue().toString();
                    userPhone.setText(phone);
                    String email = snapshot.child("email").getValue().toString();
                    userEmail.setText(email);
                    if (typeUser.equals(DOG_OWNER)) {
                        dogRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Dog dog = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Dog.class);

                                String dog_name = dog.getName();
                                dogName.setText(dog_name);
                                String dog_type = dog.getBreed();
                                dogType.setText(dog_type);
                                String dog_year = dog.getAge();
                                dogYear.setText(dog_year + " Years old");
                                String dog_weight = dog.getWeight();
                                dogWeight.setText(dog_weight + " Kg");
                                String photoLink = dog.getDogpictureurl();
                                Log.d("ptttt",photoLink);
                                ImageUtils.getInstance().load(photoLink,navigation_IMG_dog);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateRecycle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        userList = new ArrayList<>();
        dogList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this, userList);
        dogAdapter = new DogAdapter(MainActivity.this, dogList);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();

                if (type.equals(DOG_SITTER)) {
                    renderUiDog();
                    recyclerView.setAdapter(dogAdapter);
                    readDogs();

                }

                if (type.equals(DOG_OWNER)) {
                    recyclerView.setAdapter(userAdapter);
                    readUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getType() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                typeUser = snapshot.child("type").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initLayerManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }


    private void renderUiDog() {
        navigation_LYT_dog.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
    }


    private void readDogs() {
        DatabaseReference reference_dog = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DOGS_DATA);

        reference_dog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dogList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dog dog = dataSnapshot.getValue(Dog.class);
                    Object photo = dataSnapshot.child(DOG_PIC_URL).getValue();
                    if (photo != null) {
                        dog.setDogpictureurl(photo.toString());
                    }
                    dogList.add(dog);
                }
                dogAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (dogList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No users", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USER_DATA);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("type").getValue().toString().equals(DOG_SITTER)) {
                        DogSitter user = dataSnapshot.getValue(DogSitter.class);
                        Object photo = dataSnapshot.child("profilepictureurl").getValue();
                        if (photo != null) {
                            user.setProfilepictureurl(photo.toString());
                        }
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (userList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No users", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logOut:
                Intent intent = new Intent(MainActivity.this, AutintificateActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }

}