package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

public class Subject extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton imageButton;
    FloatingActionButton addSubject;
    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    ArrayList<String> _id, courseName, subjectCode, subjectTitle, subjectDate, subjectTime;
    SubjectAdapter subjectAdapter;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        init();             // INITIALIZE ALL VIEWS
        navigationOpen();   //NAVIGATION DRAWER
        addSubject();       //FLOATING ACTION BUTTON
        initializeDB();     // INITIALIZE DATABASE
        displayDataOnArray();   // DISPLAY DATA ON RECYCLERVIEW

        subjectAdapter = new SubjectAdapter(Subject.this, courseName, subjectCode, subjectTitle, subjectDate, subjectTime);
        recyclerView.setAdapter(subjectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Subject.this));

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // hide action bar title
    }

    public void initializeDB(){
        dataBaseHelper = new DataBaseHelper(Subject.this);
        _id = new ArrayList<>();
        courseName = new ArrayList<>();
        subjectCode = new ArrayList<>();
        subjectTitle = new ArrayList<>();
        subjectDate = new ArrayList<>();
        subjectTime = new ArrayList<>();
    }

    // DISPLAY DATA ON RECYCLERVIEW
    public void displayDataOnArray(){
        Cursor cursor = dataBaseHelper.readAllData();
        if(cursor.getCount() == 0){
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                _id.add(cursor.getString(0));
                courseName.add(cursor.getString(1));
                subjectCode.add(cursor.getString(2));
                subjectTitle.add(cursor.getString(3));
                subjectDate.add(cursor.getString(4));
                subjectTime.add(cursor.getString(5));
            }
        }

    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);  // When back button is pressed while navigation drawer is open, it will close the navigation drawer.
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> finish())
                    .setNegativeButton("No", null)
                    .show();                    // Exit pop up when back button is pressed if navigation drawer is not open
        }
    }



    public void init(){

        searchEditText = findViewById(R.id.searchEditTextSubject);
        recyclerView = findViewById(R.id.recyclerViewAddSubject);
        drawerLayout = findViewById(R.id.drawerSubject);
        navigationView = findViewById(R.id.navSubject);
        toolbar = findViewById(R.id.toolbarSubject);
    }

    public void navigationOpen() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();     // Show navigation drawer when clicked
        navigationView.setNavigationItemSelectedListener(this); //navigation drawer item clickable
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawerHome:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSched:
                intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSettings:
                intent = new Intent(this,Settings.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    public void moveToFragment(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Subject.this, BottomNavi.class);
                startActivity(intent);

            }
        });
    }

    public void addSubject(){
        addSubject = findViewById(R.id.addSubject);
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Subject.this, AddSubjectActivity.class);
                startActivity(intent);
            }
        });
    }
}