package com.example.weclass.taskGrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.StudentItems;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;
import com.example.weclass.tasks.TaskItems;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class TaskGrade extends AppCompatActivity implements TaskGradeAdapter.OnNoteListener {

    ExtendedRecyclerView recyclerView;
    ArrayList<TaskGradeItems> taskGradeItems, studentID, subjectID;
    ArrayList<ActivitiesItems> activitiesItems;
    TaskGradeAdapter taskGradeAdapter;
    DataBaseHelper dataBaseHelper;
    ImageView backButton;
    TextView _progress, _deadline, _score, _description, _taskType, _taskNumber, _subjectID , _noStudentToGradeTextView, _gradingPeriod;
    View _noStudentToGradeView;
    TabLayout _tabLayout;
    ViewPager2 _viewPager2;
    TaskGradeFragmentAdapter taskGradeFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_grade);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen


        initialize();
        getDataFromTaskRecView();
        //display();
        //initializeAdapter();
        backToTask();
        //automaticSort();
        fragmentManager();

    }

    @Override
    public void onResume() {
        initialize();
        getDataFromTaskRecView();
        //display();
        //initializeAdapter();
        backToTask();
        super.onResume();
    }

    private void initialize() {
        backButton = findViewById(R.id.backToTaskButton);
        _progress = findViewById(R.id.proressTextViewGrade);
        _score = findViewById(R.id.scoreTextViewGrade);
        _description = findViewById(R.id.descriptionTextViewGrade);
        _taskType = findViewById(R.id.taskTypeTextViewGrade);
        _taskNumber = findViewById(R.id.taskNumberTextViewGrade);
        _subjectID = findViewById(R.id.subjectIDTextViewGrade);
        recyclerView = findViewById(R.id.taskGradeRecyclerView);
        _noStudentToGradeTextView = findViewById(R.id.noStudentTextViewGrade);
        _noStudentToGradeView = findViewById(R.id.noStudentViewGrade);
        _gradingPeriod = findViewById(R.id.gradingPeriodTextViewTaskGrade);
        _viewPager2 = findViewById(R.id.viewPagerTaskGrade);
        _tabLayout = findViewById(R.id.tabLayoutTaskGrade);

    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskGrade.this);
        taskGradeAdapter = new TaskGradeAdapter(taskGradeItems, TaskGrade.this, this);
        recyclerView.setAdapter(taskGradeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TaskGrade.this));
        recyclerView.setEmptyView(_noStudentToGradeView, _noStudentToGradeTextView);
        linearLayoutManager.setStackFromEnd(true);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){
        studentID = new ArrayList<>();
        subjectID = new ArrayList<>();
        taskGradeItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(TaskGrade.this);
        taskGradeItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<TaskGradeItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        // MERGE 2 TABLES USING LEFT JOIN

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                        + DataBaseHelper.TABLE_MY_STUDENTS + " LEFT JOIN "
                        + DataBaseHelper.TABLE_MY_TASKS + " ON "
                        + DataBaseHelper.TABLE_MY_STUDENTS + "."
                        + DataBaseHelper.COLUMN_PARENT_ID + " = "
                        + DataBaseHelper.TABLE_MY_TASKS + "."
                        + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " WHERE "
                        + DataBaseHelper.TABLE_MY_STUDENTS + "."
                        + DataBaseHelper.COLUMN_PARENT_ID + " = "
                        + _subjectID.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                        + _taskType.getText().toString() + "' AND "
                        + DataBaseHelper.COLUMN_TASK_NUMBER + " = "
                        + _taskNumber.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                        + _gradingPeriod.getText().toString() + "'", null);

        ArrayList<TaskGradeItems> taskGradeItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                taskGradeItems.add(new TaskGradeItems(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(14),
                        cursor.getInt(19),
                        cursor.getString(20)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return taskGradeItems;
    }



    private void backToTask(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getDataFromTaskRecView(){
        Intent intent = getIntent();
        TaskItems taskItems = intent.getParcelableExtra("Task");
        String subjectID = intent.getStringExtra("id");


        String progress =taskItems.getProgress();
        String score = taskItems.getScore();
        String description = taskItems.getTaskDescription();
        String taskType = taskItems.getTaskType();
        String period = taskItems.getGradingPeriod();
        int taskNumber = taskItems.getTaskNumber();

        _progress.setText(progress);
        _score.setText(score);
        _description.setText(description);
        _taskType.setText(taskType);
        _taskNumber.setText(String.valueOf(taskNumber));
        _subjectID.setText(subjectID);
        _gradingPeriod.setText(period);


    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort(){
        Collections.sort(taskGradeItems, TaskGradeItems.sortAtoZComparator);
        initializeAdapter();
    }

    @Override
    public void OnNoteClick(int position) {

    }

    // Method for tab layout and viewpager2
    public void fragmentManager(){

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Pass data from this activity to pager adapter
        String taskType = _taskType.getText().toString();
        String taskNumber = _taskNumber.getText().toString();
        String gradingPeriod = _gradingPeriod.getText().toString();
        String subjectId = _subjectID.getText().toString();

        taskGradeFragmentAdapter = new TaskGradeFragmentAdapter(fragmentManager, getLifecycle(), taskType, taskNumber, gradingPeriod, subjectId);


        _viewPager2.setAdapter(taskGradeFragmentAdapter);

        _tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                _viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        _viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                _tabLayout.selectTab(_tabLayout.getTabAt(position));
            }
        });
    }


}