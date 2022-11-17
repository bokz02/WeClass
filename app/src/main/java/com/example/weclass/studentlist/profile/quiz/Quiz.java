package com.example.weclass.studentlist.profile.quiz;

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
import com.example.weclass.studentlist.profile.activities.ActivitiesMidtermAdapter;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;

import java.util.ArrayList;

public class Quiz extends AppCompatActivity {

    TextView _studentID, _subjectID, noText, noText2, _quiz;
    ImageButton _backButton;
    ExtendedRecyclerView extendedRecyclerView, extendedRecyclerView2;
    ActivitiesMidtermAdapter activitiesAdapter;
    ActivitiesFinalsAdapter activitiesFinalsAdapter;
    ArrayList<ActivitiesItems> activitiesItems, activitiesItems2;
    DataBaseHelper dataBaseHelper;
    View noView, noView2;
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
        setContentView(R.layout.activity_quiz);

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
        display2();
        initializeAdapter2();

    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeAdapter();
        initializeAdapter2();
    }

    public void initialize(){

        _studentID = findViewById(R.id.studentIDStudentQuiz);
        _subjectID = findViewById(R.id.subjectIDStudentQuiz);
        _backButton = findViewById(R.id.backButtonQuiz);
        extendedRecyclerView = findViewById(R.id.studentQuizRecView);
        extendedRecyclerView2 = findViewById(R.id.studentQuizRecView2);
        noView = findViewById(R.id.noViewViewQuiz);
        noText = findViewById(R.id.noTextTextViewQuiz);
        noView2 = findViewById(R.id.noViewViewQuiz2);
        noText2 = findViewById(R.id.noTextTextViewQuiz2);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        activitiesAdapter = new ActivitiesMidtermAdapter(Quiz.this, activitiesItems);
        extendedRecyclerView.setAdapter(activitiesAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(Quiz.this));
        extendedRecyclerView.setEmptyView(noView, noText);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){

        activitiesItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(Quiz.this);
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
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " LIKE '%Quiz%' " + " AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " LIKE '%Midterm%'", null);

        ArrayList<ActivitiesItems> activitiesItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                activitiesItems.add(new ActivitiesItems(
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getInt(7)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return activitiesItems;
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


    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter2(){
        activitiesFinalsAdapter = new ActivitiesFinalsAdapter(activitiesItems2,Quiz.this);
        extendedRecyclerView2.setAdapter(activitiesFinalsAdapter);
        extendedRecyclerView2.setLayoutManager(new LinearLayoutManager(Quiz.this));
        extendedRecyclerView2.setEmptyView(noView2, noText2);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display2(){

        activitiesItems2 = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(Quiz.this);
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
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " LIKE '%Quiz%'"+ " AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " LIKE '%Finals%'", null);

        ArrayList<ActivitiesItems> activitiesItems2 = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                activitiesItems2.add(new ActivitiesItems(
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getInt(7)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return activitiesItems2;
    }

}