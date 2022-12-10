package com.example.weclass.ratings;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    String classType;
    DataBaseHelper db;
    SQLiteDatabase sql;
    int activityTotal=0, assignmentTotal=0, seatWorkTotal=0,quizTotal=0, reportTotal=0
            , presentCount=0, absentCount=0, lateCount=0, totalAttendance=0, quizTotalItems=0
            , totalQuiz=0, project;
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
                Midterm.getInstance().initializeRecView();
                Midterm.getInstance().initializeAdapter();
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
                            case R.id.generateFinals:
                                if (getActivity() instanceof MyProgressBar) {
                                    wait3seconds();
                                    ((BottomNavi) getActivity()).showProgressBAr();
                                    ((BottomNavi) getActivity()).hideProgressBAr();
                                    compute();

                                }
                                break;
                            case R.id.generateFInalRating:

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

    private void compute(){
        if (!classType.equals("Laboratory")){
            computeMidtermGrades();
        }else {
            computeFinalsGrades();
        }
    }

    private void computeMidtermGrades(){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        // count all tasks
        countTotalWritten();
        countTotalPerformance();

        // total all attendance

        Cursor cursor = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_STUDENTS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                + parentId + "'",null);

            if (cursor.moveToFirst()) {

                countAttendance(cursor);

                // total all attendance
                totalAttendance = presentCount + absentCount + lateCount;

                do {

                    computeActivity(cursor);
                    computeAssignment(cursor);
                    computeSeatWork(cursor);

                    computeQuiz(cursor);
                    computeReport(cursor);
                    computeRecitation(cursor);
                    getPresent(cursor);
                    getLate(cursor);
                    getAbsent(cursor);

                    getMidtermExamScore(cursor);

                    // get 10% of a student attendance
                    attendanceTotal = presentCount + lateCount;
                    attendanceTotal = attendanceTotal / totalAttendance;
                    attendanceTotal = attendanceTotal * 50;
                    attendanceTotal = attendanceTotal + 50;
                    attendanceTotal = attendanceTotal * .10;

                    /*get 30% midterm exam of a student */
                    exam = exam *.3;

                    /*compute 40% of total performance task of a student*/
                    performanceTotal1 = performanceTasksCount * 100;
                    performanceTotal1 = performanceTotal1 + quizTotalItems;
                    performanceTotalScore = quizTotal + recitationTotal + reportTotal;
                    performanceTotalScore = performanceTotalScore / performanceTotal1;
                    performanceTotalScore = performanceTotalScore * 50;
                    performanceTotalScore = performanceTotalScore + 50;
                    performanceTotalScore = performanceTotalScore * .4;

                    /*compute 20% of total written task of a student*/
                    writtenTotal = writtenTotalCount * 100;
                    writtenTotalScore = activityTotal + assignmentTotal + seatWorkTotal;
                    writtenTotalScore = writtenTotalScore / writtenTotal;
                    writtenTotalScore = writtenTotalScore * 50;
                    writtenTotalScore = writtenTotalScore + 50;
                    writtenTotalScore = writtenTotalScore * .2;

                    // get the midterm grade
                    grade = attendanceTotal + exam + performanceTotalScore + writtenTotalScore;

                    Log.d(TAG, ""+ writtenTotalCount);

                    // insert midterm grade
                    db.updateMidtermGrade(cursor.getString(1),
                            parentId,
                            grade);

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

    private void computeAssignment(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
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

    private void computeSeatWork(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
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

    private void computeActivity(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
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
    private void countTotalWritten(){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + "Midterm" + "' and "
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
    private void countTotalPerformance(){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Recitation" + "' or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Report" + "'",null);
        if (cursor.moveToFirst()){
            performanceTasksCount = cursor.getInt(0);
        }cursor.close();
    }

    private void computeQuiz(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
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

    private void computeRecitation(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Recitation" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                recitationTotal = recitationTotal + cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void computeReport(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Report" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                reportTotal = reportTotal + cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void getMidtermExamScore(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Exam" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                exam = cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void getPresent(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
                 presentCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void getAbsent(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
            absentCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void getLate(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Midterm" + "' and "
                + DataBaseHelper.COLUMN_LATE_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
            lateCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void countAttendance(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Midterm" + "'", null);

        if (cursor2.moveToNext()) {
            presentCount = cursor2.getInt(0);
            absentCount = cursor2.getInt(0);
            lateCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void computeFinalsGrades() {
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        // count all tasks
        countTotalWrittenFinals();
        countTotalPerformanceFinals();
        countRecitationFinals();

        Cursor cursor = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_STUDENTS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                + parentId + "'",null);

        if (cursor.moveToFirst()) {

            countAttendanceFinals(cursor);

            // total all attendance
            totalAttendance = presentCount + absentCount + lateCount;

            do {

                computeActivityFinals(cursor);
                computeAssignmentFinals(cursor);
                computeSeatWorkFinals(cursor);
                computeQuizFinals(cursor);
                computeReportFinals(cursor);
                computeRecitationFinals(cursor);
                computeProjectFinals(cursor);
                getPresentFinals(cursor);
                getLateFinals(cursor);
                getAbsentFinals(cursor);

                getFinalsExamScore(cursor);

                // get 10% of a student attendance
                attendanceTotal = presentCount + lateCount;
                attendanceTotal = attendanceTotal / totalAttendance;
                attendanceTotal = attendanceTotal * 50;
                attendanceTotal = attendanceTotal + 50;
                attendanceTotal = attendanceTotal * .10;

                /*compute 10% of total written task of a student*/
                writtenTotalCount = writtenTotalCount * 100;
                writtenTotalCount = writtenTotalCount + quizTotalItems;
                writtenTotalScore = quizTotal + assignmentTotal + seatWorkTotal;
                writtenTotalScore = writtenTotalScore / writtenTotalCount;
                writtenTotalScore = writtenTotalScore * 50;
                writtenTotalScore = writtenTotalScore + 50;
                writtenTotalScore = writtenTotalScore * .1;

                /*get 30% midterm exam of a student */
                computeExam = exam *.2;

                /*compute 40% of total performance task of a student*/
                performanceTotal1 = performanceTasksCount * 100;
                performanceTotalScore = project + reportTotal + activityTotal;
                performanceTotalScore = performanceTotalScore / performanceTotal1;
                performanceTotalScore = performanceTotalScore * 50;
                performanceTotalScore = performanceTotalScore + 50;
                performanceTotalScore = performanceTotalScore * .5;

                // formula for getting 10% recitation
                recitationCountFinals = recitationCountFinals * 100;
                recitationTotal = recitationTotal / recitationCountFinals;
                recitationTotal = recitationTotal * 50;
                recitationTotal = recitationTotal + 50;
                recitationTotal = recitationTotal * .10;

                // get the midterm grade
                grade = attendanceTotal + computeExam + performanceTotalScore + recitationTotal + writtenTotalScore;

                // insert midterm grade
                db.updateMidtermGrade(cursor.getString(1),
                        parentId,
                        grade);

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

    private void computeAssignmentFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
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

    private void computeQuizFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
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

    private void computeSeatWorkFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
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

    private void computeActivityFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
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

    private void computeProjectFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
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
    private void countTotalWrittenFinals(){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Assignment" + "' or "
                + DataBaseHelper.COLUMN_TASK_TYPE + " ='"
                + "Seatwork" + "'",null);
        if (cursor.moveToFirst()){
            writtenTotalCount = Integer.parseInt(cursor.getString(0));
        }cursor.close();
    }

    // count total performance tasks and multiply by 100 for getting average
    private void countTotalPerformanceFinals(){
        db = new DataBaseHelper(getContext());
        sql = db.getReadableDatabase();

        Cursor cursor = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + "Finals" + "' and "
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
    private void computeRecitationFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
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
    private void countRecitationFinals(){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_MY_TASKS + " where "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                + "Recitation" + "'", null);
        if (cursor2.moveToNext()) {
                recitationCountFinals = recitationCountFinals + cursor2.getInt(8);
        }
        cursor2.close();
    }

    private void computeReportFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Report" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                reportTotal = reportTotal + cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void getFinalsExamScore(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select * from " + DataBaseHelper.TABLE_MY_GRADE + " where "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Exam" + "'", null);
        if (cursor2.moveToNext()) {

            do {
                exam = cursor2.getInt(8);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
    }

    private void getPresentFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
            presentCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void getAbsentFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
            absentCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void getLateFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_LATE_ATTENDANCE + "="
                + 1, null);
        if (cursor2.moveToNext()) {
            lateCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

    private void countAttendanceFinals(@NonNull Cursor cursor){
        Cursor cursor2 = sql.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + parentId + "' and "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + cursor.getString(1) + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + "Finals" + "'", null);

        if (cursor2.moveToNext()) {
            presentCount = cursor2.getInt(0);
            absentCount = cursor2.getInt(0);
            lateCount = cursor2.getInt(0);
        }
        cursor2.close();
    }

}