package com.example.weclass.studentlist.profile.quiz;

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

import java.util.ArrayList;

public class Quiz extends AppCompatActivity {

    TextView _studentID, _subjectID, noText, _quiz;
    ImageButton _backButton;
    ExtendedRecyclerView extendedRecyclerView;
    ActivitiesAdapter activitiesAdapter;
    ArrayList<ActivitiesItems> activitiesItems;
    DataBaseHelper dataBaseHelper;
    View noView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

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

        _studentID = findViewById(R.id.studentIDStudentQuiz);
        _subjectID = findViewById(R.id.subjectIDStudentQuiz);
        _backButton = findViewById(R.id.backButtonQuiz);
        extendedRecyclerView = findViewById(R.id.studentQuizRecView);
        noView = findViewById(R.id.noViewViewQuiz);
        noText = findViewById(R.id.noTextTextViewQuiz);
        _quiz = findViewById(R.id.quizStudentQuiz);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        activitiesAdapter = new ActivitiesAdapter(Quiz.this, activitiesItems);
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
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + _quiz.getText().toString() + "'", null);

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
}