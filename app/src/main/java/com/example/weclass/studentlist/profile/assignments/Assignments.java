package com.example.weclass.studentlist.profile.assignments;

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
import com.example.weclass.studentlist.profile.activities.ActivitiesFinalsAdapter;
import com.example.weclass.studentlist.profile.activities.ActivitiesMidtermAdapter;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;

import java.util.ArrayList;

public class Assignments extends AppCompatActivity {

    TextView _studentID, _subjectID, noText, noText2, _assignments, _midterm, _finals;
    ImageButton _backButton;
    ExtendedRecyclerView extendedRecyclerView, extendedRecyclerView2;
    ActivitiesMidtermAdapter activitiesAdapter;
    ActivitiesFinalsAdapter activitiesFinalsAdapter;
    ArrayList<ActivitiesItems> activitiesItems, activitiesItems2;
    DataBaseHelper dataBaseHelper;
    View noView, noView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();   // INITIALIZE VIEWS
        backToStudentProfile(); // BACK BUTTON
        getDataFromProfile();   // GET DATA FROM PROFILE
        display();  //DISPLAY DATA FROM MIDTERM TO RECYCLERVIEW
        initializeAdapter();    // ADAPTER FOR RECYCLERVIEW
        display2(); //DISPLAY DATA FROM FINALS TO RECYCLERVIEW
        initializeAdapter2();   // ADAPTER FOR RECYCLERVIEW

    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeAdapter();
        initializeAdapter2();
    }

    public void initialize(){

        _studentID = findViewById(R.id.studentIDStudentAssignments);
        _subjectID = findViewById(R.id.subjectIDStudentAssignments);
        _backButton = findViewById(R.id.backButtonAssignments);
        extendedRecyclerView = findViewById(R.id.studentAssignmentsRecView);
        extendedRecyclerView2 = findViewById(R.id.studentAssignmentsRecView2);
        noView = findViewById(R.id.noViewViewAssignments);
        noView2 = findViewById(R.id.noViewViewAssignments2);
        noText = findViewById(R.id.noTextTextViewAssignments);
        noText2 = findViewById(R.id.noTextTextViewAssignments2);
        _assignments = findViewById(R.id.assignmentsStudentAssignments);
        _midterm = findViewById(R.id.midtermTextViewStudentAssignments);
        _finals = findViewById(R.id.finalsStudentAssignments);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        activitiesAdapter = new ActivitiesMidtermAdapter(Assignments.this, activitiesItems);
        extendedRecyclerView.setAdapter(activitiesAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(Assignments.this));
        extendedRecyclerView.setEmptyView(noView, noText);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){

        activitiesItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(Assignments.this);
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
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + _assignments.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + _midterm.getText().toString() + "'", null);

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
        activitiesFinalsAdapter = new ActivitiesFinalsAdapter(activitiesItems,Assignments.this);
        extendedRecyclerView2.setAdapter(activitiesFinalsAdapter);
        extendedRecyclerView2.setLayoutManager(new LinearLayoutManager(Assignments.this));
        extendedRecyclerView2.setEmptyView(noView2, noText2);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display2(){

        activitiesItems2 = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(Assignments.this);
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
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + _assignments.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + _finals.getText().toString() + "'", null);

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