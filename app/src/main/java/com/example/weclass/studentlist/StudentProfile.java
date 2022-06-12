package com.example.weclass.studentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.weclass.R;
import com.example.weclass.profile.Activities;

import java.nio.charset.StandardCharsets;

public class StudentProfile extends AppCompatActivity {

    ImageButton backButton;
    TextView _id, _subjectID, _lastName, _firstName;
    ImageView _activities, _quiz, _assignments, _seatWork;

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


    }

    public void initialize(){
        backButton = findViewById(R.id.backButtonProfile);
        _id = findViewById(R.id.studentIDProfile);
        _subjectID = findViewById(R.id.subjectIDProfile);
        _activities = findViewById(R.id.activityButtonProfile);
        _firstName = findViewById(R.id.studFirstnameProfile);
        _lastName = findViewById(R.id.studLastnameProfile);
    }

    public void goToActivities(){
        _activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Activities.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
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


        int studentID = studentItems.getId();
        int subjectID = studentItems.getParent_id();

        String lastName = studentItems.getLastname();
        String firstName = studentItems.getFirstname();

        _id.setText(String.valueOf(studentID));
        _subjectID.setText(String.valueOf(subjectID));
        _lastName.setText(lastName);
        _firstName.setText(firstName);


    }
}