package com.example.dogsitterproject.activity;

import static com.example.dogsitterproject.utils.ConstUtils.CITY;
import static com.example.dogsitterproject.utils.ConstUtils.DOGS_DATA;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_OWNER;

import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.EMAIL;
import static com.example.dogsitterproject.utils.ConstUtils.FULL_NAME;
import static com.example.dogsitterproject.utils.ConstUtils.PHONE;
import static com.example.dogsitterproject.utils.ConstUtils.TYPE;
import static com.example.dogsitterproject.utils.ConstUtils.USER_DATA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.example.dogsitterproject.R;

import com.example.dogsitterproject.calback.CallBackFavClicked;

import com.example.dogsitterproject.db.FirebaseDB;
import com.example.dogsitterproject.fragments.ContactUsFragment;
import com.example.dogsitterproject.fragments.FavoriteFragment;
import com.example.dogsitterproject.fragments.HomeFragment;
import com.example.dogsitterproject.fragments.UpdateProfileFragment;
import com.example.dogsitterproject.model.Dog;
import com.example.dogsitterproject.model.DogSitter;

import com.example.dogsitterproject.utils.ImageUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private NavigationView navigationView;


    private LinearLayout navigation_LYT_dog;


    private TextView userName, userCity, userPhone, userEmail;

    private TextView dogWeight;
    private TextView dogYear;
    private TextView dogType;
    private TextView dogName;
    private View line;
    private String typeUser = "";


    private HomeFragment homeFragment;
    private FavoriteFragment favoriteFragment;
    private UpdateProfileFragment updateProfileFragment;
    private ContactUsFragment contactUsFragment;

    private CircleImageView navigation_IMG_dog;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

        findViews();

        updateInfo();
        updateRecycle();

        initNav();


    }


    private void initFragment() {
        homeFragment = new HomeFragment();
        favoriteFragment = new FavoriteFragment();
        updateProfileFragment = new UpdateProfileFragment();
        contactUsFragment = new ContactUsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void findViews() {
        drawerLayout = findViewById(R.id.main_drawerLayout);
        toolbar = findViewById(R.id.main_BAR_tool);
        navigationView = findViewById(R.id.main_NAV_view);
        setSupportActionBar(toolbar);


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

    private void initNav() {
        navigationView.getMenu().getItem(0).setChecked(true);
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
                    String name = snapshot.child(FULL_NAME).getValue().toString();
                    userName.setText(name);
                    String city = snapshot.child(CITY).getValue().toString();
                    userCity.setText(city);
                    String phone = snapshot.child(PHONE).getValue().toString();
                    userPhone.setText(phone);
                    String email = snapshot.child(EMAIL).getValue().toString();
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
                                Log.d("ptttt", photoLink);
                                ImageUtils.getInstance().load(photoLink, navigation_IMG_dog);

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

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child(TYPE).getValue().toString();

                if (type.equals(DOG_SITTER)) {
                    renderUiDog();


                }

                if (type.equals(DOG_OWNER)) {


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
                typeUser = snapshot.child(TYPE).getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void renderUiDog() {
        navigation_LYT_dog.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_logOut:
                intent = new Intent(MainActivity.this, AutintificateActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case R.id.menu_favourite:
                setTitle("Favourite");
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, favoriteFragment).commit();
                break;
            case R.id.menu_home:
                setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, homeFragment).commit();
                break;
            case R.id.menu_profile:
                setTitle("Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, updateProfileFragment).commit();
                break;
            case R.id.menu_aboutUs:
                setTitle("Contact Us");
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,contactUsFragment).commit();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }


}