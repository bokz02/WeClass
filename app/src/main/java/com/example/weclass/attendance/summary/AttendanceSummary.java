package com.example.weclass.attendance.summary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.attendance.AttendanceItems;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.ratings.RatingsAdapter;
import com.example.weclass.ratings.RatingsModel;
import com.example.weclass.studentlist.StudentProfile;

import java.util.ArrayList;
import java.util.Collections;

public class AttendanceSummary extends AppCompatActivity {

    SharedPref sharedPref;
    ImageButton backButton;
    String parentId, date, gradingPeriod;
    TextView dateTextView;
    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<AttendanceSummaryModel> attendanceSummaryModel;
    AttendanceSummaryAdapter attendanceSummaryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.titleBar));

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.red2));

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_summary);

        initialize();
        backButton();
        getDataFromAttendance();
        initializeRecView();
        initializeAdapter();
        automaticSort();
    }

    private void initialize () {
        backButton = findViewById(R.id.backButtonSummary);
        dateTextView = findViewById(R.id.dateTextViewSummary);
        extendedRecyclerView = findViewById(R.id.extendedRecViewSummary);
    }

    private void backButton(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
            }
        });
    }

    private void getDataFromAttendance(){
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        gradingPeriod = intent.getStringExtra("gradingPeriod");
        parentId = intent.getStringExtra("parentId");
        //Toast.makeText(this, "" + gradingPeriod + "" + date, Toast.LENGTH_SHORT).show();
        dateTextView.setText(date);
    }

    private void initializeAdapter(){
        attendanceSummaryAdapter = new AttendanceSummaryAdapter(this, attendanceSummaryModel, date, gradingPeriod);
        extendedRecyclerView.setAdapter(attendanceSummaryAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeRecView(){

        attendanceSummaryModel = new ArrayList<>();
        attendanceSummaryModel = getData();
    }

    private ArrayList<AttendanceSummaryModel> getData(){
        DataBaseHelper dbh = DataBaseHelper.getInstance(this);
        SQLiteDatabase sqLiteDatabase = dbh.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_ATTENDANCE_TODAY + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID + " = "
                + parentId + " and "
                + DataBaseHelper.COLUMN_DATE_TODAY + " ='"
                + date + "'", null);

        ArrayList<AttendanceSummaryModel> attendanceSummaryModel = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                attendanceSummaryModel.add(new AttendanceSummaryModel(
                        cursor.getString(8),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(1),
                        cursor.getBlob(7),
                        cursor.getString(10)));
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        cursor.close();
        return attendanceSummaryModel;
    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort() {
        attendanceSummaryModel.sort(AttendanceSummaryModel.sortAtoZComparator);
        initializeAdapter();
    }
}