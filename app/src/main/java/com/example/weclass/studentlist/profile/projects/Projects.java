package com.example.weclass.studentlist.profile.projects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.activities.ActivitiesFinalsAdapter;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;
import com.example.weclass.studentlist.profile.activities.ActivitiesMidtermAdapter;
import com.example.weclass.studentlist.profile.quiz.Quiz;

import java.util.ArrayList;

public class Projects extends AppCompatActivity {

    TextView _studentID, _subjectID, _noText, _noText2;
    ImageButton backButton;
    ExtendedRecyclerView extendedRecyclerView, extendedRecyclerView2;
    ActivitiesMidtermAdapter activitiesMidtermAdapter;
    ActivitiesFinalsAdapter activitiesFinalsAdapter;
    DataBaseHelper dataBaseHelper;
    ArrayList<ActivitiesItems> activitiesItems, activitiesItems2;
    View _view , _view2;
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
        setContentView(R.layout.activity_projects);

        //status bar white background
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.WHITE);



        initialize();
        getDataFromProfile();
        display();
        initializeAdapter();
        display2();
        initializeAdapter2();
        backToStudentProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeAdapter();
        initializeAdapter2();
    }

    private void initialize() {
        _studentID = findViewById(R.id.studentIDStudentProjects);
        _subjectID = findViewById(R.id.subjectIDStudentProjects);
        _noText = findViewById(R.id.noTextTextViewProjects);
        _noText2 = findViewById(R.id.noTextTextViewProjects2);
        backButton = findViewById(R.id.backButtonProjects);
        extendedRecyclerView = findViewById(R.id.studentProjectsRecView);
        extendedRecyclerView2 = findViewById(R.id.studentProjectsRecView2);
        _view = findViewById(R.id.noViewViewProjects);
        _view2 = findViewById(R.id.noViewViewProjects2);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        activitiesMidtermAdapter = new ActivitiesMidtermAdapter(Projects.this, activitiesItems);
        extendedRecyclerView.setAdapter(activitiesMidtermAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(Projects.this));
        extendedRecyclerView.setEmptyView(_view, _noText);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){

        activitiesItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(Projects.this);
        activitiesItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<ActivitiesItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = "
                + _studentID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = "
                + _subjectID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " LIKE '%Project%' " + " AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "'", null);

        ArrayList<ActivitiesItems> activitiesItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                activitiesItems.add(new ActivitiesItems(
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return activitiesItems;
    }


    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter2(){
        activitiesFinalsAdapter = new ActivitiesFinalsAdapter(activitiesItems2 ,Projects.this);
        extendedRecyclerView2.setAdapter(activitiesFinalsAdapter);
        extendedRecyclerView2.setLayoutManager(new LinearLayoutManager(Projects.this));
        extendedRecyclerView2.setEmptyView(_view2, _noText2);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display2(){

        activitiesItems2 = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(Projects.this);
        activitiesItems2 = displayData2();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<ActivitiesItems> displayData2(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = "
                + _studentID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = "
                + _subjectID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " LIKE '%Project%' " + " AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "'", null);

        ArrayList<ActivitiesItems> activitiesItems2 = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                activitiesItems2.add(new ActivitiesItems(
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return activitiesItems2;
    }

    public void getDataFromProfile(){
        Intent intent = getIntent();
        String studentID = intent.getStringExtra("studentID");
        String subjectID = intent.getStringExtra("subjectID");

        _studentID.setText(studentID);
        _subjectID.setText(subjectID);
    }

    public void backToStudentProfile(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
            }
        });
    }
}