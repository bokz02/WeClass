package com.example.weclass.ratings;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    String parentId;
    ImageButton optionButton;
    String classType, midterm="Midterm", finals="Finals";
    DataBaseHelper db;
    SQLiteDatabase sql;
    int activityTotal=0, assignmentTotal=0, seatWorkTotal=0,quizTotal=0, reportTotal=0
            , presentCount=0, absentCount=0, lateCount=0, totalAttendance=0, quizTotalItems=0
            , totalQuiz=0, project, currentTotalAttendance=0;
    int writtenTotalCount, performanceTasksCount, recitationCountFinals;
    double computeExam=0, grade,exam=0, attendanceTotal, writtenTotalScore
            ,performanceTotalScore, recitationTotal;
    double writtenTotal, total5=0, attendanceTotal7=0;
    double performanceTotal1=0, performanceTotal5=0;
    private static final String TAG = "MyAppTag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ratings, container, false);

        initialize();
        getDataFromBottomNaviActivity();
        viewPagerFragmentManager();
        optionButton();

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
                                    }else{
                                        computeLaboratoryGrades(midterm);
                                    }

                                }
                                break;
                            case R.id.generateFinals:
                                if (!classType.equals("Laboratory")){
                                    if (getActivity() instanceof MyProgressBar) {
                                        wait3seconds();
                                        ((BottomNavi) getActivity()).showProgressBAr();
                                        ((BottomNavi) getActivity()).hideProgressBAr();
                                        // pass the parameter string finals to method
                                        computeLectureGrades(finals);
                                    }else {
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

        // count all tasks
        countTotalWritten(gradingPeriod);
        countTotalPerformance(gradingPeriod);

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
                    getPresent(cursor, gradingPeriod);
                    getLate(cursor, gradingPeriod);
                    getAbsent(cursor, gradingPeriod);

                    getMidtermExamScore(cursor, gradingPeriod);

                    // get 10% of a student attendance
                    attendanceTotal = presentCount + lateCount;
                    if (attendanceTotal!=0) {
                        attendanceTotal = attendanceTotal / currentTotalAttendance;
                    }
                        attendanceTotal = attendanceTotal * 50;
                        attendanceTotal = attendanceTotal + 50;
                        attendanceTotal = attendanceTotal * .10;

                    /*get 30% midterm exam of a student */
                        exam = exam * .3;

                    /*compute 40% of total performance task of a student*/
                    performanceTotal1 = performanceTasksCount * 100;
                    performanceTotal1 = performanceTotal1 + quizTotalItems;
                    performanceTotalScore = quizTotal + recitationTotal + reportTotal;
                    if (performanceTotalScore!=0) {
                        performanceTotalScore = performanceTotalScore / performanceTotal1;
                    }
                        performanceTotalScore = performanceTotalScore * 50;
                        performanceTotalScore = performanceTotalScore + 50;
                        performanceTotalScore = performanceTotalScore * .4;


                    /*compute 20% of total written task of a student*/
                    writtenTotal = writtenTotalCount * 100;
                    writtenTotalScore = activityTotal + assignmentTotal + seatWorkTotal;
                    if (writtenTotalScore!=0) {
                        writtenTotalScore = writtenTotalScore / writtenTotal;
                    }
                        writtenTotalScore = writtenTotalScore * 50;
                        writtenTotalScore = writtenTotalScore + 50;
                        writtenTotalScore = writtenTotalScore * .2;


                    // get the midterm grade
                    grade = attendanceTotal + exam + performanceTotalScore + writtenTotalScore;



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
                    performanceTotalScore=0;

                    presentCount=0;
                    lateCount=0;
                    absentCount=0;

                    attendanceTotal =0;
                    exam=0;
                    performanceTotal5=0;
                    grade=0;

                    quizTotal=0;
                    quizTotalItems=0;


                }while (cursor.moveToNext());
            }cursor.close();
            sql.close();
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

    // count total written tasks and multiply by 100 for getting average
    private void countTotalWritten(String gradingPeriod){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Activity" + "'or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Assignment" + "' or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Seatwork" + "'",null);
        if (cursor.moveToFirst()){
            writtenTotalCount = Integer.parseInt(cursor.getString(0));
        }cursor.close();
    }

    // count total performance tasks and multiply by 100 for getting average
    private void countTotalPerformance(String gradingPeriod){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Recitation" + "' or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Report" + "'",null);
        if (cursor.moveToFirst()){
            performanceTasksCount = cursor.getInt(0);
        }cursor.close();
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
                quizTotalItems = quizTotalItems + cursor2.getInt(10);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
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

    private void getMidtermExamScore(@NonNull Cursor cursor, String gradingPeriod){
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
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "'", null);

        if (cursor2.moveToNext()) {
            presentCount = cursor2.getInt(0);
            absentCount = cursor2.getInt(0);
            lateCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void computeLaboratoryGrades(String gradingPeriod) {
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        // count all tasks
        countTotalWrittenLab(gradingPeriod);
        countTotalPerformanceLab(gradingPeriod);
        countRecitationLab(gradingPeriod);

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
                    Log.d(TAG, ""+cursor.getString(3) +" "+ currentTotalAttendance);
                }
            }while (cursor.moveToNext());
        }

        if (cursor.moveToFirst()) {

            do {

                computeActivityLab(cursor, gradingPeriod);
                computeAssignmentLab(cursor, gradingPeriod);
                computeSeatWorkLab(cursor, gradingPeriod);
                computeQuizLab(cursor, gradingPeriod);
                computeReportLab(cursor, gradingPeriod);
                computeRecitationLab(cursor, gradingPeriod);
                computeProjectLab(cursor, gradingPeriod);
                getPresentLab(cursor, gradingPeriod);
                getLateLab(cursor, gradingPeriod);
                getAbsentLab(cursor, gradingPeriod);

                getFinalsExamScore(cursor, gradingPeriod);

                // get 10% of a student attendance
                attendanceTotal = presentCount + lateCount;
                if (currentTotalAttendance != 0) { // total will become infinite if the value is 0
                    attendanceTotal = attendanceTotal / currentTotalAttendance;
                }
                    attendanceTotal = attendanceTotal * 50;
                    attendanceTotal = attendanceTotal + 50;
                    attendanceTotal = attendanceTotal * .10;

                    /*compute 10% of total written task of a student*/
                    writtenTotalCount = writtenTotalCount * 100;
                    writtenTotalCount = writtenTotalCount + quizTotalItems;
                    writtenTotalScore = quizTotal + assignmentTotal + seatWorkTotal;
                if (writtenTotalCount != 0) { // total will become infinite if the value is 0
                    writtenTotalScore = writtenTotalScore / writtenTotalCount;
                }
                    writtenTotalScore = writtenTotalScore * 50;
                    writtenTotalScore = writtenTotalScore + 50;
                    writtenTotalScore = writtenTotalScore * .1;

                    /*get 30% midterm exam of a student */
                    computeExam = exam *.2;

                    /*compute 40% of total performance task of a student*/
                    performanceTotal1 = performanceTasksCount * 100;
                    performanceTotalScore = project + reportTotal + activityTotal;
                if (performanceTotal1!=0) { // total will become infinite if the value is 0
                    performanceTotalScore = performanceTotalScore / performanceTotal1;
                }
                    performanceTotalScore = performanceTotalScore * 50;
                    performanceTotalScore = performanceTotalScore + 50;
                    performanceTotalScore = performanceTotalScore * .5;

                    // formula for getting 10% recitation
                    recitationCountFinals = recitationCountFinals * 100;
                if (recitationCountFinals!=0) { // total will become infinite if the value is 0
                    recitationTotal = recitationTotal / recitationCountFinals;
                }
                    recitationTotal = recitationTotal * 50;
                    recitationTotal = recitationTotal + 50;
                    recitationTotal = recitationTotal * .10;

                // get the midterm grade
                grade = attendanceTotal + computeExam + performanceTotalScore + recitationTotal + writtenTotalScore;

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
                performanceTotalScore=0;

                presentCount=0;
                lateCount=0;
                absentCount=0;

                attendanceTotal7=0;
                computeExam=0;
                exam=0;
                performanceTotal5=0;
                total5=0;
                grade=0;

                quizTotal=0;
                quizTotalItems=0;
                totalQuiz=0;


            }while (cursor.moveToNext());
        }cursor.close();
    }

    private void computeAssignmentLab(@NonNull Cursor cursor, String gradingPeriod){
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

    private void computeQuizLab(@NonNull Cursor cursor, String gradingPeriod){
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
                quizTotalItems = quizTotalItems + cursor2.getInt(10);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void computeSeatWorkLab(@NonNull Cursor cursor, String gradingPeriod){
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

    private void computeActivityLab(@NonNull Cursor cursor, String gradingPeriod){
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

    private void computeProjectLab(@NonNull Cursor cursor, String gradingPeriod){
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

            do {
                project = project + cursor2.getInt(8);
                //Toast.makeText(getContext(), "" + midtermTotal, Toast.LENGTH_SHORT).show();
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    // count total written tasks and multiply by 100
    private void countTotalWrittenLab(String gradingPeriod){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Assignment" + "' or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Seatwork" + "'",null);
        if (cursor.moveToFirst()){
            writtenTotalCount = Integer.parseInt(cursor.getString(0));
        }cursor.close();
    }

    // count total performance tasks and multiply by 100 for getting average
    private void countTotalPerformanceLab(String gradingPeriod){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Activity" + "'or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Project" + "' or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Report" + "'",null);
        if (cursor.moveToFirst()){
            performanceTasksCount = cursor.getInt(0);
        }cursor.close();
    }

    // total all recitation of a student for getting average
    private void computeRecitationLab(@NonNull Cursor cursor, String gradingPeriod){
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

    // total all recitation of a student for getting average
    private void countRecitationLab(String gradingPeriod){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Recitation" + "'", null);
        if (cursor2.moveToNext()) {
                recitationCountFinals = recitationCountFinals + cursor2.getInt(8);
        }
        cursor2.close();
    }

    private void computeReportLab(@NonNull Cursor cursor, String gradingPeriod){
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

    private void getFinalsExamScore(@NonNull Cursor cursor, String gradingPeriod){
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

    private void getPresentLab(@NonNull Cursor cursor, String gradingPeriod){
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

    private void getAbsentLab(@NonNull Cursor cursor, String gradingPeriod){
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

    private void getLateLab(@NonNull Cursor cursor, String gradingPeriod){
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
                    Log.d(TAG,"" + cursor.getString(3) + " " + finalRating );
                    db.updateFinalRatingGrade(cursor.getString(1),
                            parentId,
                            finalRating);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}