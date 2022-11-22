package com.example.weclass.taskGrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.StudentItems;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;
import com.example.weclass.tasks.TaskItems;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;

public class TaskGrade extends AppCompatActivity implements TaskGradeAdapter.ItemCallBack {

    ExtendedRecyclerView recyclerView;
    ArrayList<TaskGradeItems> taskGradeItems, studentID, subjectID;
    ArrayList<ActivitiesItems> activitiesItems;
    TaskGradeAdapter taskGradeAdapter;
    DataBaseHelper dataBaseHelper;
    ImageView backButton;
    TextView _progress, _score, _description, _taskType, _taskNumber,
            _subjectID , _noStudentToGradeTextView, _gradingPeriod, _due,
            totalStudent, gradedStudent, _taskId;
    View _noStudentToGradeView;
    TabLayout _tabLayout;
    ViewPager2 _viewPager2;
    TaskGradeFragmentAdapter taskGradeFragmentAdapter;
    TaskGradeViewFragmentAdapter taskGradeViewFragmentAdapter;
    SharedPreferences sharedPreferences = null;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_grade);

        //status bar white background
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.WHITE);

        initialize();
        getDataFromTaskRecView();
        backToTask();
        fragmentManager();
        getSumOfStudents();
        gradedStudent();
        updateGradedStudents();
        insertDataToTaskGrade();




    }

    @Override
    public void onResume() {
        initialize();
        getDataFromTaskRecView();
        backToTask();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
    }

    @Override
    protected void onStop() {
        super.onStop();

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
        _due = findViewById(R.id.dueTextViewTaskGrade);
        gradedStudent = findViewById(R.id.gradedTextViewTaskGraded);
        totalStudent = findViewById(R.id.totalStudentTextViewTaskGrade);
        _taskId = findViewById(R.id.taskIdTaskGrade);

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
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(15),
                        cursor.getInt(16),
                        cursor.getString(21),
                        cursor.getInt(13)));
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
        String due = taskItems.getDue();
        int taskId = taskItems.getTaskID();
        int taskNumber = taskItems.getTaskNumber();
        String a = "                        ";
        String c = a + description;

        _progress.setText(progress);
        _score.setText(score);
        _description.setText(c);
        _taskType.setText(taskType);
        _taskNumber.setText(String.valueOf(taskNumber));
        _subjectID.setText(subjectID);
        _gradingPeriod.setText(period);
        _due.setText(due);
        _taskId.setText(String.valueOf(taskId));


    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort(){
        Collections.sort(taskGradeItems, TaskGradeItems.sortAtoZComparator);
        initializeAdapter();
    }


    // Method for tab layout and viewpager2
    public void fragmentManager(){

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Pass data from this activity to pager adapter
        String taskType = _taskType.getText().toString();
        String taskNumber = _taskNumber.getText().toString();
        String gradingPeriod = _gradingPeriod.getText().toString();
        String subjectId = _subjectID.getText().toString();
        String idTask = _taskId.getText().toString();

        taskGradeFragmentAdapter = new TaskGradeFragmentAdapter(fragmentManager, getLifecycle(), taskType, taskNumber, gradingPeriod, subjectId, idTask);


        _viewPager2.setAdapter(taskGradeFragmentAdapter);

        _tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                _viewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Toast.makeText(TaskGrade.this,"Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Toast.makeText(TaskGrade.this,"Success", Toast.LENGTH_SHORT).show();
            }
        });

        _viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                _tabLayout.selectTab(_tabLayout.getTabAt(position));

                if(position == 1 ){
                    //Toast.makeText(TaskGrade.this,"Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // GET SUM OF ALL STUDENTS BASED ON THEIR SUBJECT ID
    public void getSumOfStudents(){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT COUNT(*) FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID + " = "
                + _subjectID.getText().toString(), null);

        if (cursor.moveToFirst() ){
            totalStudent.setText(String.valueOf(cursor.getInt(0)));
            cursor.close();
        }
    }

    public void gradedStudent(){

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT COUNT(*) FROM "
                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = "
                + _subjectID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " ='"
                + _taskType.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_TASK_NUMBER_MY_GRADE + " = "
                + _taskNumber.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != " + 0 + " AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + "='"
                + _gradingPeriod.getText().toString() + "'", null);

        if (cursor.moveToFirst() ){
            gradedStudent.setText(String.valueOf(cursor.getInt(0)));
            cursor.close();
        }

    }



    @Override
    public void updateStudentGrades() {

    }

    public void updateGradedStudents(){
        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                gradedStudent();

                            }
                        });
                    }
                } catch (InterruptedException e){

                }
            }

        };
        thread.start();
    }

    public void insertDataToTaskGrade(){
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        SQLiteDatabase sqL = dbHelper.getWritableDatabase();


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

        if (cursor.moveToFirst()){
            do {
                Cursor c = sqL.rawQuery("SELECT * FROM "
                        + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                        + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                        + cursor.getString(1) + "'" + " AND "
                        + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = "
                        + cursor.getInt(2) + " AND "
                        + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                        + cursor.getString(15) + "' AND "
                        + DataBaseHelper.COLUMN_TASK_NUMBER_MY_GRADE + " = "
                        + cursor.getInt(20) + " AND "
                        + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " ='"
                        + cursor.getString(21) + "'", null);

                if (!c.moveToFirst()){
                    ContentValues cv = new ContentValues();
                    cv.put(DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE, cursor.getString(1));
                    cv.put(DataBaseHelper.COLUMN_TASK_ID_MY_GRADE, _taskId.getText().toString());
                    cv.put(DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE, _subjectID.getText().toString());
                    cv.put(DataBaseHelper.COLUMN_LAST_NAME_MY_GRADE, cursor.getString(3));
                    cv.put(DataBaseHelper.COLUMN_FIRST_NAME_MY_GRADE, cursor.getString(4));
                    cv.put(DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE, _taskType.getText().toString());
                    cv.put(DataBaseHelper.COLUMN_TASK_NUMBER_MY_GRADE, _taskNumber.getText().toString());
                    cv.put(DataBaseHelper.COLUMN_GRADE_MY_GRADE, 0);
                    cv.put(DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE, _gradingPeriod.getText().toString());
                    sqL.insert(DataBaseHelper.TABLE_MY_GRADE, null,cv);
                    c.close();
                }

            }while (cursor.moveToNext());

        }cursor.close();


    }




}