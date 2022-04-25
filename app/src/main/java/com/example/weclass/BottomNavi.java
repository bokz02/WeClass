package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavi extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navi);

        initialize();
        fragmentLoader(new StudentList());
        hideActionBarInFragment();
        moveFragment();
        backButton();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

    }

    public void fragmentLoader(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }//FRAGMENT LOADER

    public void backButton(){
        ImageButton imageButton = (ImageButton) findViewById(R.id.backListOfStudents);
         imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void hideActionBarInFragment() {
        ActionBar supportActionBar = ((AppCompatActivity) this).getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.hide();
    }

    public void initialize(){
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
    }

    public void moveFragment(){
        //SWITCHING DIFFERENT FRAGMENTS
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.naviStudents:
                        fragmentLoader(new StudentList());
                        break;
                    case R.id.naviReport:
                        fragmentLoader(new Record());
                        break;
                    case R.id.naviAttendance:
                        fragmentLoader(new Attendance());
                        break;
                    case R.id.naviRanking:
                        fragmentLoader(new Ranking());
                        break;


                }
                return true;
            }
        });
    }
}