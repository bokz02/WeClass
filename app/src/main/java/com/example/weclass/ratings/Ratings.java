package com.example.weclass.ratings;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.weclass.BottomNavi;
import com.example.weclass.MyProgressBar;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.ratings.fragments.FinalRating;
import com.example.weclass.ratings.fragments.Finals;
import com.example.weclass.ratings.fragments.Midterm;
import com.example.weclass.ratings.fragments.RatingsViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Ratings extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    RatingsViewPagerAdapter ratingsViewPagerAdapter;
    String parentId, notArchive;
    ImageButton optionButton;
    String classType, midterm="Midterm", finals="Finals";
    DataBaseHelper db;
    SQLiteDatabase sql;
    int presentCount=0, absentCount=0, lateCount=0, totalAttendance=0,
            currentTotalAttendance=0, recitationCount
            , reportCount, assignmentCount, seatWorkCount, activityCount;
    double activityTotal, grade,exam, attendanceTotal, writtenTotalScore
            ,performanceTotalScore, recitationTotal, assignmentTotal
            , seatWorkTotal, quizTotal, reportTotal, projectTotal, quizCount;
    private static final String TAG = "Ratings";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ratings, container, false);

        initialize();
        getDataFromBottomNaviActivity();
        viewPagerFragmentManager();
        optionButton();
        hideOptionButton();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initialize(){
        tabLayout = view.findViewById(R.id.tabLayoutRatings);
        viewPager2 = view.findViewById(R.id.viewPagerRatings);
        optionButton = view.findViewById(R.id.optionButtonRatings);
    }

    private void hideOptionButton(){
        if (notArchive.equals("Archive")){
            if (optionButton.getVisibility() == View.VISIBLE){
                optionButton.setVisibility(View.GONE);
            }
        }
    }

    private void wait3seconds(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Midterm.getInstance() != null){
                    Midterm.getInstance().initializeRecView();
                    Midterm.getInstance().initializeAdapter();
                }

                if (Finals.getInstance() != null){
                    Finals.getInstance().initializeRecView();
                    Finals.getInstance().initializeAdapter();
                }

                if (FinalRating.getInstance() != null){
                    FinalRating.getInstance().initializeRecView();
                    FinalRating.getInstance().initializeAdapter();
                }

            }
        }, 3000);
    }

    public void optionButton(){
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), optionButton);
                popupMenu.getMenuInflater().inflate(R.menu.option_ratings, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.generateMidterm:
                                if (classType.equals("Lecture")){
                                    if (getActivity() instanceof MyProgressBar) {
                                        wait3seconds();
                                        ((BottomNavi) getActivity()).showProgressBAr();
                                        ((BottomNavi) getActivity()).hideProgressBAr();
                                        // pass the parameter string finals to method
                                        computeLectureGrades(midterm);
                                    }
                                }else{
                                    if (getActivity() instanceof MyProgressBar) {
                                        wait3seconds();
                                        ((BottomNavi) getActivity()).showProgressBAr();
                                        ((BottomNavi) getActivity()).hideProgressBAr();
                                        // pass the parameter string finals to method
                                        computeLaboratoryGrades(midterm);
                                    }
                                }
                                break;
                            case R.id.generateFinals:
                                if (classType.equals("Lecture")){
                                    if (getActivity() instanceof MyProgressBar) {
                                        wait3seconds();
                                        ((BottomNavi) getActivity()).showProgressBAr();
                                        ((BottomNavi) getActivity()).hideProgressBAr();
                                        // pass the parameter string finals to method
                                        computeLectureGrades(finals);
                                    }
                                }else {
                                    if (getActivity() instanceof MyProgressBar) {
                                        wait3seconds();
                                        ((BottomNavi) getActivity()).showProgressBAr();
                                        ((BottomNavi) getActivity()).hideProgressBAr();
                                        // pass the parameter string finals to method
                                        computeLaboratoryGrades(finals);
                                    }
                                }
                                break;
                            case R.id.generateFInalRating:
                                if (getActivity() instanceof MyProgressBar) {
                                    wait3seconds();
                                    ((BottomNavi) getActivity()).showProgressBAr();
                                    ((BottomNavi) getActivity()).hideProgressBAr();
                                    // pass the parameter string finals to method
                                    computeFinalRating();
                                }
                                break;
                        }
                        return false;
                    }
                });


                popupMenu.show();

            }
        });
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentId = bundle.getString("IDParent");
            classType = bundle.getString("classType");
            notArchive = bundle.getString("NotArchive");

        }
    }

    public void viewPagerFragmentManager(){
        ratingsViewPagerAdapter = new RatingsViewPagerAdapter(getParentFragmentManager(),getLifecycle(),parentId, classType);
        viewPager2.setAdapter(ratingsViewPagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    // method for computing lecture's midterm and finals grades
    private void computeLectureGrades(String gradingPeriod){
        db = DataBaseHelper.getInstance(getContext());
        sql = db.getReadableDatabase();

        quizCount =0;

        // count all tasks
        countQuiz(gradingPeriod);
        countRecitation(gradingPeriod);
        countReports(gradingPeriod);
        countAssignment(gradingPeriod);
        countSeatWork(gradingPeriod);
        countActivity(gradingPeriod);

        currentTotalAttendance = 0;

        Cursor cursor = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_STUDENTS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                + parentId + "'",null);

            if (cursor.moveToFirst()){
                do {
                    countAttendance(cursor, gradingPeriod);
                    // total all attendance
                    totalAttendance = presentCount + absentCount + lateCount;
                    if(totalAttendance > currentTotalAttendance ){
                        currentTotalAttendance = totalAttendance;
                        //Log.d(TAG,"" + cursor.getString(3) + " "+ currentTotalAttendance);
                    }
                }while (cursor.moveToNext());
            }

            if (cursor.moveToFirst()) {

                do {

                    computeActivity(cursor, gradingPeriod);
                    computeAssignment(cursor, gradingPeriod);
                    computeSeatWork(cursor, gradingPeriod);
                    computeQuiz(cursor, gradingPeriod);
                    computeReport(cursor, gradingPeriod);
                    computeRecitation(cursor, gradingPeriod);
                    computeProject(cursor, gradingPeriod);
                    getPresent(cursor, gradingPeriod);
                    getLate(cursor, gradingPeriod);
                    getAbsent(cursor, gradingPeriod);
                    getExamScore(cursor, gradingPeriod);

                    // get 10% of a student attendance
                    attendanceTotal = presentCount + lateCount;
                    if (currentTotalAttendance!=0) {
                        attendanceTotal = attendanceTotal / currentTotalAttendance;
                        attendanceTotal = attendanceTotal * 50;
                        attendanceTotal = attendanceTotal + 50;
                        attendanceTotal = attendanceTotal * .10;
                    } else {
                        attendanceTotal =0;
                    }

                    /*get 30% midterm exam of a student */
                        exam = exam * .3;

                    // get total quiz
                    if(quizCount!=0) {
                        quizTotal = quizTotal / quizCount;
                        quizTotal = quizTotal * 50;
                        quizTotal = quizTotal + 50;
                    }else {
                        quizTotal =0;
                    }

                    // get total recitation
                    if (recitationCount!=0) {
                        recitationTotal = recitationTotal / recitationCount;
                        recitationTotal = recitationTotal * 50;
                        recitationTotal = recitationTotal + 50;
                    } else {
                        recitationTotal=0;
                    }

                    // get total reports
                    if (reportCount!=0) {
                        reportTotal = reportTotal / reportCount;
                        reportTotal = reportTotal * 50;
                        reportTotal = reportTotal + 50;
                    } else {
                        reportTotal=0;
                    }

                    // compute 40% of the performance tasks of a student
                    performanceTotalScore = quizTotal + reportTotal + recitationTotal + projectTotal;
                    performanceTotalScore = performanceTotalScore / 4;
                    performanceTotalScore = performanceTotalScore * .4;

                    // get total assignment
                    if (assignmentCount!=0) {
                        assignmentTotal = assignmentTotal / assignmentCount;
                        assignmentTotal = assignmentTotal * 50;
                        assignmentTotal = assignmentTotal + 50;
                    }else {
                        assignmentTotal =0;
                    }

                    // get total seatWork
                    if (seatWorkCount!=0) {
                        seatWorkTotal = seatWorkTotal / seatWorkCount;
                        seatWorkTotal = seatWorkTotal * 50;
                        seatWorkTotal = seatWorkTotal + 50;
                    }else {
                        seatWorkTotal=0;
                    }

                    // get total activity
                    if (activityCount!=0) {
                        activityTotal = activityTotal / activityCount;
                        //Log.d(TAG,"" + cursor.getString(3) + " " + activityCount);
                        activityTotal = activityTotal * 50;
                        activityTotal = activityTotal + 50;
                    }else {
                        activityTotal=0;
                    }

                    // compute 20% of the written tasks of a student
                    writtenTotalScore = assignmentTotal + seatWorkTotal + activityTotal;
                    writtenTotalScore = writtenTotalScore / 3;
                    writtenTotalScore = writtenTotalScore * .2;
                    //Log.d(TAG,"" + cursor.getString(3) + " " + assignmentTotal + " " + seatWorkTotal + " " + activityTotal);
                    // get the midterm grade
                    grade = attendanceTotal + exam + performanceTotalScore + writtenTotalScore;
                    //Log.d(TAG,"" + cursor.getString(3) + " " + attendanceTotal + " " + exam + " " + performanceTotalScore + " " +writtenTotalScore);

                    if (gradingPeriod.equals("Midterm")) {
                        // insert midterm grade
                        db.updateMidtermGrade(cursor.getString(1),
                                parentId,
                                grade);
                    }else {
                        db.updateFinalsGrade(cursor.getString(1),
                                parentId,
                                grade);
                    }


                    activityTotal=0;
                    seatWorkTotal=0;
                    assignmentTotal=0;
                    writtenTotalScore = 0;

                    recitationTotal=0;
                    reportTotal=0;
                    quizTotal=0;
                    projectTotal=0;
                    performanceTotalScore=0;

                    presentCount=0;
                    lateCount=0;
                    absentCount=0;
                    attendanceTotal =0;

                    exam=0;

                    grade=0;

                }while (cursor.moveToNext());
            }cursor.close();
    }

    private void computeAssignment(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Assignment" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                assignmentTotal = assignmentTotal + cursor2.getInt(8);
                //Toast.makeText(getContext(), "" + midtermTotal, Toast.LENGTH_SHORT).show();
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void countAssignment(String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Assignment" + "'", null);

            if (cursor2.moveToNext()) {
                assignmentCount = cursor2.getInt(0);
                cursor2.close();
            }
        assignmentCount = assignmentCount * 100;
    }

    private void computeSeatWork(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Seatwork" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                seatWorkTotal = seatWorkTotal + cursor2.getInt(8);
                //Toast.makeText(getContext(), "" + midtermTotal, Toast.LENGTH_SHORT).show();
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void countSeatWork(String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Seatwork" + "'", null);

            if (cursor2.moveToNext()) {
                seatWorkCount = cursor2.getInt(0);
                cursor2.close();
            }
        seatWorkCount = seatWorkCount * 100;
    }

    private void computeActivity(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Activity" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                activityTotal = activityTotal + cursor2.getInt(8);
                //Toast.makeText(getContext(), "" + midtermTotal, Toast.LENGTH_SHORT).show();
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void countActivity(String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Activity" + "'", null);

        if (cursor2.moveToNext()) {
            activityCount = cursor2.getInt(0);
            cursor2.close();
        }
        activityCount = activityCount * 100;
    }

    private void computeQuiz(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Quiz" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                quizTotal = quizTotal + cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void countQuiz(String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Quiz" + "'", null);

            if (cursor2.moveToFirst()) {
                do {
                    quizCount = quizCount + cursor2.getInt(4);
                }while (cursor2.moveToNext());
            }
        //Log.d(TAG," " + quizCount);
        cursor2.close();
    }

    private void countRecitation(String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Recitation" + "'", null);

            if (cursor2.moveToNext()) {
                recitationCount = cursor2.getInt(0);
                cursor2.close();
            }
        recitationCount = recitationCount * 100;
        //Log.d(TAG," " + recitationCount);
    }

    private void computeRecitation(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Recitation" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                recitationTotal = recitationTotal + cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void computeReport(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Report" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                reportTotal = reportTotal + cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void countReports(String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Report" + "'", null);

        if (cursor2.moveToNext()) {
            reportCount = cursor2.getInt(0);
            cursor2.close();
        }
        reportCount = reportCount * 100;
    }

    private void getExamScore(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Exam" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                exam = cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void getPresent(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
                 presentCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void getAbsent(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
            absentCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void getLate(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_LATE_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
            lateCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void countAttendance(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "'", null);

        if (cursor2.moveToNext()) {
            presentCount = cursor2.getInt(5);
            absentCount = cursor2.getInt(6);
            lateCount = cursor2.getInt(7);
           // Log.d(TAG, " "+ presentCount + " " + absentCount + " " + lateCount);
        }
        cursor2.close();
    }

    private void computeLaboratoryGrades(String gradingPeriod) {
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        quizCount =0;

        // count all tasks
        countQuiz(gradingPeriod);
        countRecitation(gradingPeriod);
        countReports(gradingPeriod);
        countAssignment(gradingPeriod);
        countSeatWork(gradingPeriod);
        countActivity(gradingPeriod);

        Cursor cursor = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_STUDENTS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                + parentId + "'",null);

        if (cursor.moveToFirst()){
            do {
                countAttendance(cursor, gradingPeriod);
                // total all attendance
                totalAttendance = presentCount + absentCount + lateCount;
                if(totalAttendance > currentTotalAttendance ){
                    currentTotalAttendance = totalAttendance;
                }
            }while (cursor.moveToNext());
        }

        if (cursor.moveToFirst()) {

            do {

                computeActivity(cursor, gradingPeriod);
                computeAssignment(cursor, gradingPeriod);
                computeSeatWork(cursor, gradingPeriod);
                computeQuiz(cursor, gradingPeriod);
                computeReport(cursor, gradingPeriod);
                computeRecitation(cursor, gradingPeriod);
                computeProject(cursor, gradingPeriod);
                getPresent(cursor, gradingPeriod);
                getLate(cursor, gradingPeriod);
                getAbsent(cursor, gradingPeriod);
                getExamScore(cursor, gradingPeriod);

                // get 10% of a student attendance
                attendanceTotal = presentCount + lateCount;
                if (currentTotalAttendance != 0) { // total will become infinite if the value is 0
                    attendanceTotal = attendanceTotal / currentTotalAttendance;
                    attendanceTotal = attendanceTotal * 50;
                    attendanceTotal = attendanceTotal + 50;
                    attendanceTotal = attendanceTotal * .10;
                }else {
                    attendanceTotal =0;
                }

                // get total assignment
                if (assignmentCount!=0) {
                    assignmentTotal = assignmentTotal / assignmentCount;
                    assignmentTotal = assignmentTotal * 50;
                    assignmentTotal = assignmentTotal + 50;
                }else {
                    assignmentTotal = 0;
                }

                // get total seatWork
                if (seatWorkCount!=0) {
                    seatWorkTotal = seatWorkTotal / seatWorkCount;
                    seatWorkTotal = seatWorkTotal * 50;
                    seatWorkTotal = seatWorkTotal + 50;
                }else {
                    seatWorkTotal =0;
                }

                // get total quiz
                if(quizCount!=0) {
                    quizTotal = quizTotal / quizCount;
                    quizTotal = quizTotal * 50;
                    quizTotal = quizTotal + 50;
                }else {
                    quizTotal =0;
                }

                // compute 10% of the written tasks of a student
                writtenTotalScore = assignmentTotal + seatWorkTotal + quizTotal;
                writtenTotalScore = writtenTotalScore / 3;
                writtenTotalScore = writtenTotalScore * .1;

                // get total activity
                if (activityCount!=0) {
                    activityTotal = activityTotal / activityCount;
                    activityTotal = activityTotal * 50;
                    activityTotal = activityTotal + 50;
                }else {
                    activityTotal =0;
                }

                // get total project

                // get total reports
                if (reportCount!=0) {
                    reportTotal = reportTotal / reportCount;
                    reportTotal = reportTotal * 50;
                    reportTotal = reportTotal + 50;
                }else {
                    reportTotal =0;
                }

                // compute 50% of the performance tasks of a student
                performanceTotalScore = activityTotal + reportTotal + projectTotal;
                performanceTotalScore = performanceTotalScore / 3;
                performanceTotalScore = performanceTotalScore * .5;

                /* get 20% midterm exam of a student */
                exam = exam * .2;

                // get 10% of recitation of a student
                if (reportCount!=0) {
                    recitationTotal = recitationTotal / recitationCount;
                    recitationTotal = recitationTotal * 50;
                    recitationTotal = recitationTotal + 50;
                    recitationTotal = recitationTotal * .1;
                }else {
                    recitationTotal =0;
                }

                // get the midterm grade
                grade = attendanceTotal + exam + performanceTotalScore + recitationTotal + writtenTotalScore;

                if (gradingPeriod.equals("Midterm")) {
                    // insert midterm grade
                    db.updateMidtermGrade(cursor.getString(1),
                            parentId,
                            grade);
                }else {
                    db.updateFinalsGrade(cursor.getString(1),
                            parentId,
                            grade);
                }

                activityTotal=0;
                seatWorkTotal=0;
                assignmentTotal=0;
                writtenTotalScore = 0;

                recitationTotal=0;
                reportTotal=0;
                projectTotal=0;
                performanceTotalScore=0;

                presentCount=0;
                lateCount=0;
                absentCount=0;

                exam=0;
                grade=0;

                quizTotal=0;

            }while (cursor.moveToNext());
        }cursor.close();
    }

    private void computeProject(@NonNull Cursor cursor, String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Project" + "'", null);
        if (cursor2.moveToNext()) {
                projectTotal = cursor2.getInt(8);
        }
        cursor2.close();
    }

    private void computeFinalRating(){
        DataBaseHelper db = DataBaseHelper.getInstance(getContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DataBaseHelper.TABLE_MY_STUDENTS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                + parentId + "'", null);
        if (cursor.moveToNext()) {

            do {
                double midterm=0;
                double finals=0;
                double finalRating=0;
                double sum =0 ;
                midterm = cursor.getDouble(10);
                finals = cursor.getDouble(11);

                String a = String.valueOf(midterm);
                String b = String.valueOf(finals);

                if (finals == 0 || midterm == 0 ) {
                    Toast.makeText(getContext(),"Make sure student have their midterm and final grades",Toast.LENGTH_SHORT).show();
                }else if (a.equals("-") || b.equals("-")){
                    Toast.makeText(getContext(),"Make sure student have their midterm and final grades",Toast.LENGTH_SHORT).show();
                }else {
                    sum = midterm + finals;
                    sum = sum / 200;
                    sum = sum * 50;
                    sum = sum + 50;

                    if (sum >= 97 && sum<= 100){
                        finalRating = 1.00;
                    }else if (sum >= 94 && sum < 97){
                        finalRating = 1.25;
                    }else if (sum >= 91 && sum < 94){
                        finalRating = 1.50;
                    }else if (sum >= 88 && sum < 91){
                        finalRating = 1.75;
                    }else if (sum >= 85 && sum < 88){
                        finalRating = 2.05;
                    }else if (sum >= 82 && sum < 85){
                        finalRating = 2.25;
                    }else if (sum >= 79 && sum < 82){
                        finalRating = 2.50;
                    }else if (sum > 75 && sum <= 79){
                        finalRating = 2.75;
                    }else if (sum == 75){
                        finalRating = 3.00;
                    }else{
                        finalRating = 5.00;
                    }
                    db.updateFinalRatingGrade(cursor.getString(1),
                            parentId,
                            finalRating);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}