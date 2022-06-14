package com.example.weclass.studentlist.profile.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.activities.ActivitiesAdapter;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;
import com.example.weclass.studentlist.profile.quiz.Quiz;

import java.util.ArrayList;

public class Present extends AppCompatActivity {

    TextView _studentID, _subjectID, noText, _always1;
    ImageButton _backButton;
    ExtendedRecyclerView extendedRecyclerView;
    PresentAndAbsentAdapter presentAndAbsentAdapter;
    ArrayList<PresentAndAbsentItems> presentAndAbsentItems;
    DataBaseHelper dataBaseHelper;
    View noView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

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
        presentAndAbsentAdapter = new PresentAndAbsentAdapter(Present.this, presentAndAbsentItems);
        extendedRecyclerView.setAdapter(presentAndAbsentAdapter);
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
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = "
                + _studentID.getText().toString() + " AND "
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
            }
        });
    }
}