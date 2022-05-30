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
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.schedule.WeekViewActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

public class Subject extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SubjectAdapter.OnNoteListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton imageButton;
    FloatingActionButton addSubject;
    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    ArrayList<SubjectItems> subjectItems, id;
    SubjectAdapter subjectAdapter;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        init();             // INITIALIZE ALL VIEWS
        navigationOpen();   //NAVIGATION DRAWER
        addSubject();       //FLOATING ACTION BUTTON
        display();          // DISPLAY DATA FROM DATABASE TO RECYCLERVIEW
        textListener();     // FILTER SEARCH IN SUBJECT ACTIVITY

        subjectAdapter = new SubjectAdapter(Subject.this, subjectItems, this);
        recyclerView.setAdapter(subjectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Subject.this));

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // hide action bar title
    }

    public void textListener(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                subjectAdapter.getFilter().filter(editable);

            }
        });
    }

    // DISPLAY DATA FROM DATABASE TO RECYCLERVIEW
    public void display(){
        id = new ArrayList<>();
        subjectItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(this);
        subjectItems = displayData();

    }


    // GET THE DATA IN THE DATABASE
    private ArrayList<SubjectItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHelper.TABLE_NAME, null);
        ArrayList<SubjectItems> subjectItems = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                subjectItems.add(new SubjectItems (
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return subjectItems;
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

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, BottomNavi.class);
        startActivity(intent);
    }


}