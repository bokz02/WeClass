package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.attendance.Attendance;
import com.example.weclass.studentlist.StudentList;
import com.example.weclass.tasks.Record;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavi extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;
    TextView parentID, subjectCode, courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navi);

        initialize();   // INITIALIZE ALL VIEWS
        hideActionBarInFragment();  // HIDE ACTIONBAR IN FRAGMENTS
        moveFragment();  //SWITCHING DIFFERENT FRAGMENTS
        backButton();   // BACK BUTTON
        displayData();  // GET DATA FROM SUBJECT ADAPTER (RECYCLERVIEW ITEM CLICK)
        fragmentStudentList();   // GET THE VALUES OF STRING IN displayData() method to PASS THE DATA WE GOT FROM SUBJECT ADAPTER TO STUDENT LIST FRAGMENT


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

    }

    // GET DATA FROM SUBJECT ADAPTER (RECYCLERVIEW ITEM CLICK)
    public void displayData(){
        if (getIntent().getBundleExtra("ParentID") != null) {
            Bundle bundle = getIntent().getBundleExtra("ParentID");


            parentID.setText(bundle.getString("id"));
            subjectCode.setText(bundle.getString("subject_code"));
            courseName.setText(bundle.getString("course"));
        }
    }


    // FRAGMENT TRANSACTION
    public void fragmentLoader(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .commit();
    }

    // GET THE VALUES OF STRING IN displayData() method to PASS THE DATA WE GOT FROM SUBJECT ADAPTER TO STUDENT LIST FRAGMENT
    public void fragmentStudentList() {
        StudentList studentList = new StudentList();
        Bundle bundle = new Bundle();
        bundle.putString("IDParent", parentID.getText().toString());
        bundle.putString("SubjectCode", subjectCode.getText().toString());
        bundle.putString("CourseCode", courseName.getText().toString());

        studentList.setArguments(bundle);
        fragmentLoader(studentList);
    }

    // PASS THE DATA WE GOT FROM SUBJECT ADAPTER TO STUDENT RECORD FRAGMENT
    public void fragmentRecord(){
        Record record = new Record();
        Bundle bundle = new Bundle();
        bundle.putString("IDParent", parentID.getText().toString());
        bundle.putString("SubjectCode", subjectCode.getText().toString());
        bundle.putString("CourseCode", courseName.getText().toString());

        record.setArguments(bundle);
        fragmentLoader(record);
    }

    // OPEN ATTENDANCE FRAGMENT FUNCTION
    public void fragmentAttendance(){
        Attendance attendance = new Attendance();
        fragmentLoader(attendance);
    }

    // OPEN ATTENDANCE FRAGMENT FUNCTION
    public void fragmentRanks(){
        Ranking ranking = new Ranking();
        fragmentLoader(ranking);
    }

    // BACK BUTTON
    public void backButton(){
        ImageButton imageButton = (ImageButton) findViewById(R.id.backListOfStudents);
         imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // HIDE ACTIONBAR IN FRAGMENTS
    public void hideActionBarInFragment() {
        ActionBar supportActionBar = ((AppCompatActivity) this).getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.hide();
    }

    // INITIALIZE ALL VIEWS
    public void initialize(){
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        parentID = findViewById(R.id.parentIDBottomNavi);
        subjectCode = findViewById(R.id.subjectCodeBottomNavi);
        courseName = findViewById(R.id.courseNameBottomNavi);
    }

    //SWITCHING DIFFERENT FRAGMENTS
    public void moveFragment(){
        //SWITCHING DIFFERENT FRAGMENTS
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.naviStudents:
                        fragmentStudentList();
                        break;
                    case R.id.naviReport:
                        fragmentRecord();
                        break;
                    case R.id.naviAttendance:
                        fragmentAttendance();
                        break;
                    case R.id.naviRanking:
                        fragmentRanks();
                        break;
                }
                return true;
            }
        });
    }

}