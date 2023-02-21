package com.example.dogsitterproject.activity;


import static com.example.dogsitterproject.utils.ConstUtils.CONTACT_US;
import static com.example.dogsitterproject.utils.ConstUtils.DOG_SITTER;
import static com.example.dogsitterproject.utils.ConstUtils.FAVOURITE;
import static com.example.dogsitterproject.utils.ConstUtils.HOME;
import static com.example.dogsitterproject.utils.ConstUtils.PROFILE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;

import android.os.Bundle;


import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.example.dogsitterproject.R;


import com.example.dogsitterproject.db.FirebaseDB;
import com.example.dogsitterproject.fragments.ContactUsFragment;
import com.example.dogsitterproject.fragments.FavoriteFragment;
import com.example.dogsitterproject.fragments.HomeFragment;
import com.example.dogsitterproject.fragments.UpdateProfileFragment;
import com.example.dogsitterproject.model.Dog;


import com.example.dogsitterproject.model.User;
import com.example.dogsitterproject.utils.ImageUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;


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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(HOME);
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

        FirebaseDB.CallBack_GetInfo callBack_getInfo = new FirebaseDB.CallBack_GetInfo() {
            @Override
            public void setInfoUser(User user) {
                String name = user.getFullName();
                userName.setText(name);
                String city = user.getCity();
                userCity.setText(city);
                String phone = user.getPhone();
                userPhone.setText(phone);
                String email = user.getEmail();
                userEmail.setText(email);
            }

            @Override
            public void setInfoDog(Dog dog) {
                String dog_name = dog.getName();
                dogName.setText(dog_name);
                String dog_type = dog.getBreed();
                dogType.setText(dog_type);
                String dog_year = dog.getAge();
                dogYear.setText(dog_year + " Years old");
                String dog_weight = dog.getWeight();
                dogWeight.setText(dog_weight + " Kg");
                String photoLink = dog.getProfilepictureurl();
                ImageUtils.getInstance().load(photoLink, navigation_IMG_dog);

            }
        };
        FirebaseDB.getInfo(callBack_getInfo);

    }

    private void updateRecycle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        FirebaseDB.CallBack_Type callBack_type = type -> {
            if (type.equals(DOG_SITTER)) {
                renderUiDog();
            }
        };
        FirebaseDB.getType(callBack_type);

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
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(MainActivity.this, AutintificateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_favourite:
                setTitle(FAVOURITE);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, favoriteFragment).commit();
                break;
            case R.id.menu_home:
                setTitle(HOME);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, homeFragment).commit();
                break;
            case R.id.menu_profile:
                setTitle(PROFILE);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, updateProfileFragment).commit();
                break;
            case R.id.menu_contactUs:
                setTitle(CONTACT_US);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, contactUsFragment).commit();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }


}