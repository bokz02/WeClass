package com.example.weclass.studentlist.profile.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;

import java.util.ArrayList;

public class Present extends AppCompatActivity {

    TextView _studentID, _subjectID, noText, _always1;
    ImageButton _backButton;
    ExtendedRecyclerView extendedRecyclerView;
    AttendanceViewAdapter attendanceViewAdapter;
    ArrayList<PresentAndAbsentItems> presentAndAbsentItems;
    DataBaseHelper dataBaseHelper;
    View noView;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //This will set the theme depends on save state of switch button
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.WHITE);
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        //status bar white background
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.WHITE);


        initialize();
        backToStudentProfile();
        getDataFromProfile();
        display();
        initializeAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeAdapter();
    }

    public void initialize(){

        _studentID = findViewById(R.id.studentIDStudentPresent);
        _subjectID = findViewById(R.id.subjectIDStudentPresent);
        _backButton = findViewById(R.id.backButtonPresent);
        extendedRecyclerView = findViewById(R.id.studentPresentRecView);
        noView = findViewById(R.id.noViewViewPresent);
        noText = findViewById(R.id.noTextTextViewPresent);
        _always1 = findViewById(R.id.always1TextViewPresent);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        attendanceViewAdapter = new AttendanceViewAdapter(Present.this, presentAndAbsentItems);
        extendedRecyclerView.setAdapter(attendanceViewAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(Present.this));
        extendedRecyclerView.setEmptyView(noView, noText);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){

        presentAndAbsentItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(Present.this);
        presentAndAbsentItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<PresentAndAbsentItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = '"
                + _studentID.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + _subjectID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + " = "
                + _always1.getText().toString(), null);

        ArrayList<PresentAndAbsentItems> presentAndAbsentItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                presentAndAbsentItems.add(new PresentAndAbsentItems(
                        cursor.getString(4)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return presentAndAbsentItems;
    }

    public void getDataFromProfile(){
        Intent intent = getIntent();
        String studentID = intent.getStringExtra("studentID");
        String subjectID = intent.getStringExtra("subjectID");

        _studentID.setText(studentID);
        _subjectID.setText(subjectID);
    }

    public void backToStudentProfile(){
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
            }
        });
    }
}