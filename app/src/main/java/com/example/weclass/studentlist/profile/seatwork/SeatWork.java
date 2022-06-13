package com.example.weclass.studentlist.profile.seatwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.activities.ActivitiesAdapter;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;
import com.example.weclass.studentlist.profile.quiz.Quiz;

import java.util.ArrayList;

public class SeatWork extends AppCompatActivity {

    TextView _studentID, _subjectID, noText, _seatWork;
    ImageButton _backButton;
    ExtendedRecyclerView extendedRecyclerView;
    ActivitiesAdapter activitiesAdapter;
    ArrayList<ActivitiesItems> activitiesItems;
    DataBaseHelper dataBaseHelper;
    View noView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seatwork);

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

        _studentID = findViewById(R.id.studentIDStudentSeatWork);
        _subjectID = findViewById(R.id.subjectIDStudentSeatWork);
        _backButton = findViewById(R.id.backButtonSeatWork);
        extendedRecyclerView = findViewById(R.id.studentSeatWorkRecView);
        noView = findViewById(R.id.noViewViewSeatWork);
        noText = findViewById(R.id.noTextTextViewSeatWork);
        _seatWork = findViewById(R.id.seatWorkStudentSeatWork);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        activitiesAdapter = new ActivitiesAdapter(SeatWork.this, activitiesItems);
        extendedRecyclerView.setAdapter(activitiesAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(SeatWork.this));
        extendedRecyclerView.setEmptyView(noView, noText);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){

        activitiesItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(SeatWork.this);
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
                + _seatWork.getText().toString() + "'", null);

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
            }
        });
    }
}
