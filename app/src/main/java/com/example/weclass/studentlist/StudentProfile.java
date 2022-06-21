package com.example.weclass.studentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.activities.Activities;
import com.example.weclass.studentlist.profile.assignments.Assignments;
import com.example.weclass.studentlist.profile.attendance.Absent;
import com.example.weclass.studentlist.profile.attendance.Present;
import com.example.weclass.studentlist.profile.exams.Exams;
import com.example.weclass.studentlist.profile.projects.Projects;
import com.example.weclass.studentlist.profile.quiz.Quiz;
import com.example.weclass.studentlist.profile.seatwork.SeatWork;
import com.example.weclass.tasks.AddTask;
import com.google.android.material.snackbar.Snackbar;

public class StudentProfile extends AppCompatActivity {

    ImageButton backButton;
    TextView _id, _subjectID, _lastName, _firstName, _presentTextview, _absentTextView, _courseTextView, _subjectTextView;
    ImageView _activities, _quiz, _assignments, _seatWork, _present, _absent, _exams , _projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();
        backToStudentListActivity();
        getDataFromStudentListRecView();
        goToActivities();
        goToAssignments();
        goToQuiz();
        goToSeatWork();
        goToPresent();
        goToAbsent();
        goToProjects();
        goToExams();

    }

    public void initialize(){
        backButton = findViewById(R.id.backButtonProfile);
        _id = findViewById(R.id.studentIDProfile);
        _subjectID = findViewById(R.id.subjectIDProfile);
        _activities = findViewById(R.id.activityButtonProfile);
        _firstName = findViewById(R.id.studFirstnameProfile);
        _lastName = findViewById(R.id.studLastnameProfile);
        _assignments = findViewById(R.id.assignmentButtonProfile);
        _quiz = findViewById(R.id.quizButtonProfile);
        _seatWork = findViewById(R.id.seatWorkButtonProfile);
        _presentTextview = findViewById(R.id.presentTextViewProfile);
        _absentTextView = findViewById(R.id.absentTextViewProfile);
        _present = findViewById(R.id.presentButtonProfile);
        _absent = findViewById(R.id.absentButtonProfile);
        _courseTextView = findViewById(R.id.courseTextViewProfile);
        _subjectTextView = findViewById(R.id.subjectTextViewProfile);
        _exams = findViewById(R.id.examButtonProfile);
        _projects = findViewById(R.id.projectButtonProfile);

    }

    public void goToActivities(){
        _activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Activities.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToAssignments(){
        _assignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Assignments.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToQuiz(){
        _quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Quiz.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToSeatWork(){
        _seatWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, SeatWork.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToProjects(){
        _projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Projects.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToExams(){
        _exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Exams.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToPresent(){
        _present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Present.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToAbsent(){
        _absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Absent.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void backToStudentListActivity(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getDataFromStudentListRecView(){
        Intent intent = getIntent();
        StudentItems studentItems = intent.getParcelableExtra("Student");

        String subjectCode = intent.getStringExtra("subject");
        String course = intent.getStringExtra("course");

        int studentID = studentItems.getId();
        int subjectID = studentItems.getParent_id();

        String lastName = studentItems.getLastname();
        String firstName = studentItems.getFirstname();
        int present = studentItems.getPresent();
        int absent = studentItems.getAbsent();

        _id.setText(String.valueOf(studentID));
        _subjectID.setText(String.valueOf(subjectID));
        _lastName.setText(lastName);
        _firstName.setText(firstName);
        _presentTextview.setText(String.valueOf(present));
        _absentTextView.setText(String.valueOf(absent));
        _courseTextView.setText(course);
        _subjectTextView.setText(subjectCode);
    }

}