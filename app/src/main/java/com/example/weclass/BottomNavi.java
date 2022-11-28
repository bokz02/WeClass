package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.archive.ArchiveItems;
import com.example.weclass.attendance.Attendance;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.ratings.Ratings;
import com.example.weclass.studentlist.StudentAdapter;
import com.example.weclass.studentlist.StudentList;
import com.example.weclass.studentlist.profile.image.DrawableUtils;
import com.example.weclass.subject.Subject;
import com.example.weclass.subject.SubjectItems;
import com.example.weclass.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BottomNavi extends AppCompatActivity {
    SharedPreferences sharedPreferences = null;
    SharedPref sharedPref;
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;
    TextView parentID, subjectCode, courseName, _archive
            , _schoolYear;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(BottomNavi.this, R.color.titleBar));
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(BottomNavi.this, R.color.red2));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navi);

        initialize();   // INITIALIZE ALL VIEWS
        hideActionBarInFragment();  // HIDE ACTIONBAR IN FRAGMENTS
        moveFragment();  //SWITCHING DIFFERENT FRAGMENTS
        backButton();   // BACK BUTTON
        displayData();  // GET DATA FROM SUBJECT ADAPTER (RECYCLERVIEW ITEM CLICK)
        fragmentStudentList();   // GET THE VALUES OF STRING IN displayData() method to PASS THE DATA WE GOT FROM SUBJECT ADAPTER TO STUDENT LIST FRAGMENT

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
    }


    // GET DATA FROM SUBJECT ADAPTER (RECYCLERVIEW ITEM CLICK)
    public void displayData(){

        if(getIntent().getParcelableExtra("Subject") != null) {

            Intent intent = getIntent();
            SubjectItems subjectItems = intent.getParcelableExtra("Subject");

            int idParent = subjectItems.getId();
            String codeSubject = subjectItems.getSubjectCode();
            String nameCourse = subjectItems.getCourse();
            String sy = subjectItems.getSchoolYearSubject();

            parentID.setText(String.valueOf(idParent));
            subjectCode.setText(codeSubject);
            courseName.setText(nameCourse);
            _schoolYear.setText(sy);
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
        bundle.putString("archive_text", _archive.getText().toString());
        bundle.putString("sy", _schoolYear.getText().toString());

        studentList.setArguments(bundle);
        fragmentLoader(studentList);
    }

    // PASS THE DATA WE GOT FROM SUBJECT ADAPTER TO STUDENT RECORD FRAGMENT
    public void fragmentRecord(){
        Tasks record = new Tasks();
        Bundle bundle = new Bundle();
        bundle.putString("IDParent", parentID.getText().toString());
        bundle.putString("SubjectCode", subjectCode.getText().toString());
        bundle.putString("CourseCode", courseName.getText().toString());
        bundle.putString("sy", _schoolYear.getText().toString());
        record.setArguments(bundle);
        fragmentLoader(record);
    }

    // OPEN ATTENDANCE FRAGMENT FUNCTION
    public void fragmentAttendance(){
        Attendance attendance = new Attendance();
        Bundle bundle = new Bundle();
        bundle.putString("IDParent", parentID.getText().toString());
        bundle.putString("SubjectCode", subjectCode.getText().toString());
        bundle.putString("sy", _schoolYear.getText().toString());
        bundle.putString("CourseCode", courseName.getText().toString());

        attendance.setArguments(bundle);
        fragmentLoader(attendance);
    }

    // OPEN ATTENDANCE FRAGMENT FUNCTION
    public void fragmentRanks(){
        Ratings ranking = new Ratings();
        Bundle bundle = new Bundle();
        bundle.putString("IDParent", parentID.getText().toString());
        bundle.putString("SubjectCode", subjectCode.getText().toString());
        bundle.putString("CourseCode", courseName.getText().toString());
        bundle.putString("sy", _schoolYear.getText().toString());

        ranking.setArguments(bundle);
        fragmentLoader(ranking);
    }

    // BACK BUTTON
    public void backButton(){
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
         imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
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
        _archive = findViewById(R.id.archiveTextViewBottomNavi);
        _schoolYear = findViewById(R.id.schoolYearTextViewBottomNavigation);
        progressBar = findViewById(R.id.progressBarBotNavi);

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
                    case R.id.drawerLogout:

                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(BottomNavi.this);
                        builder.setTitle("Confirm Exit");
                        builder.setIcon(R.drawable.ic_baseline_warning_24);
                        builder.setMessage("Are you sure you want to exit?");
                        builder.setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> finishAffinity())
                            .setNegativeButton("No", null)
                            .show();
                        break;
                }
                return true;
            }
        });
    }

    // This method will run after uploading a CSV file in student list fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK && data != null){
                Uri uri = data.getData();
                String path = uri.getPath();
                String[] format = null;
                format = path.split("");

                    // here we want to check if the file format is CSV
                    if (format[format.length-1].equals("v")){

                        byte[] image = DrawableUtils.getBytes(BitmapFactory.decodeResource(getResources(), R.drawable.prof1));

                        DataBaseHelper dbHelper = new DataBaseHelper(this);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line ="";
                            db.beginTransaction();

                            while ((line = bufferedReader.readLine()) != null){
                                String[] columns = line.split(",");
                                if (columns.length != 4){
                                    Toast.makeText(this, "Check column format", Toast.LENGTH_SHORT).show();

                                }else {

                                    // in this method we would like to check database if student number already added
                                    Cursor cursor = db.rawQuery(" SELECT * FROM "
                                            + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                                            + DataBaseHelper.COLUMN_STUDENT_NUMBER_STUDENT + " ='"
                                            + columns[0] + "' AND "
                                            + DataBaseHelper.COLUMN_PARENT_ID + " = "
                                            + parentID.getText().toString(), null);

                                    //if student number is already added, it will not save
                                    if (!cursor.moveToFirst()){
                                        // else, save student number in this subject
                                        ContentValues cv = new ContentValues(3);
                                        cv.put(DataBaseHelper.COLUMN_PARENT_ID, parentID.getText().toString());
                                        cv.put(DataBaseHelper.COLUMN_STUDENT_NUMBER_STUDENT, columns[0].trim());
                                        cv.put(DataBaseHelper.COLUMN_LAST_NAME, columns[1]);
                                        cv.put(DataBaseHelper.COLUMN_FIRST_NAME, columns[2]);
                                        cv.put(DataBaseHelper.COLUMN_PROFILE_PICTURE, image);
                                        cv.put(DataBaseHelper.COLUMN_MIDDLE_NAME, columns[3]);
                                        cv.put(DataBaseHelper.COLUMN_GENDER, "-");
                                        cv.put(DataBaseHelper.COLUMN_PRESENT, 0);
                                        cv.put(DataBaseHelper.COLUMN_ABSENT, 0);
                                        cv.put(DataBaseHelper.COLUMN_MIDTERM_GRADE_STUDENT, 0);
                                        cv.put(DataBaseHelper.COLUMN_FINAL_GRADE_STUDENT, 0);
                                        cv.put(DataBaseHelper.COLUMN_FINAL_RATING_STUDENT, 0);
                                        cv.put(DataBaseHelper.COLUMN_LATE_STUDENT, 0);
                                        db.insert(DataBaseHelper.TABLE_MY_STUDENTS, null, cv);

                                        ContentValues cValues = new ContentValues(2);
                                        cValues.put(DataBaseHelper.COLUMN_PARENT_ID_TODAY, parentID.getText().toString());
                                        cValues.put(DataBaseHelper.COLUMN_LAST_NAME_TODAY, columns[1]);
                                        cValues.put(DataBaseHelper.COLUMN_FIRST_NAME_TODAY, columns[2]);
                                        cValues.put(DataBaseHelper.COLUMN_DATE_TODAY, "date");
                                        cValues.put(DataBaseHelper.COLUMN_PROFILE_PICTURE, image);
                                        cValues.put(DataBaseHelper.COLUMN_PRESENT_COUNT_TODAY, 0);
                                        cValues.put(DataBaseHelper.COLUMN_ABSENT_COUNT_TODAY, 0);
                                        cValues.put(DataBaseHelper.COLUMN_STUDENT_NUMBER_TODAY, columns[0]);
                                        cValues.put(DataBaseHelper.COLUMN_LATE_TODAY, 0);
                                        db.insert(DataBaseHelper.TABLE_ATTENDANCE_TODAY, null, cValues);
                                        cursor.close();

                                    }
                                    progressBar.setVisibility(View.VISIBLE);
                                    loadingAfterImport();


                                    // query database if student exist, if exist, it will update its picture to other subjects
                                    Cursor cursor1 = db.rawQuery("select * from "
                                            + DataBaseHelper.TABLE_MY_STUDENTS + " where "
                                            + DataBaseHelper.COLUMN_STUDENT_NUMBER_STUDENT + " ='"
                                            + columns[0] + "'",null);

                                    if (cursor1.moveToFirst()){
                                        byte[] pic = cursor1.getBlob(9);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0 , pic.length);
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                        byte[] dp = byteArrayOutputStream.toByteArray();

                                        do {
                                            dbHelper.updateProfilePicture(cursor1.getString(1),
                                                    dp);

                                            dbHelper.updateProfilePictureAttendanceToday(cursor1.getString(1),
                                                    dp);
                                        }while (cursor1.moveToNext());

                                    }cursor1.close();



                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();
                        dbHelper.close();
                    }else {
                        Toast.makeText(this, "Make sure the file is in CSV format" , Toast.LENGTH_SHORT).show();
                    }




            }
    }

    public void loadingAfterImport(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BottomNavi.this, "Import complete", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }
}