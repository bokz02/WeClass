package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavi extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;
    TextView parentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navi);

        initialize();
        hideActionBarInFragment();
        moveFragment();  //SWITCHING DIFFERENT FRAGMENTS
        backButton();
        displayData();
        fragmentLoader();
        //passDataToFragment();


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

    }

    public void displayData(){
        if (getIntent().getBundleExtra("ParentID") != null) {
            Bundle bundle = getIntent().getBundleExtra("ParentID");

            parentID.setText(bundle.getString("id"));
        }
    }


    // Load fragment
    public void fragmentLoader() {
        StudentList studentList = new StudentList();
        Bundle bundle = new Bundle();
        bundle.putString("IDParent", parentID.getText().toString());
        //bundle.putString("IDParent", String.valueOf(parentID));

        studentList.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, studentList)
                .commit();
    }

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
        parentID = findViewById(R.id.parentIDBottomNavi);
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
                        //fragmentLoader(new StudentList());
                        break;
                    case R.id.naviReport:
                        //fragmentLoader(new Record());
                        break;
                    case R.id.naviAttendance:
                        //fragmentLoader(new Attendance());
                        break;
                    case R.id.naviRanking:
                        //fragmentLoader(new Ranking());
                        break;


                }
                return true;
            }
        });
    }

}