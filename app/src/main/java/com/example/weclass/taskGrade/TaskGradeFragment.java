package com.example.weclass.taskGrade;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.Collections;


public class TaskGradeFragment extends Fragment{

    View view, _noStudentTGradeViewFragment;
    TextView _taskType,_subjectID, _taskNumber, _gradingPeriod,
            _noStudentToGradeTextViewFragment, _taskId;
    Bundle bundle;
    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<TaskGradeItems> taskGradeItems, studentID, subjectID;
    TaskGradeAdapter taskGradeAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_grade, container, false);

        initialize();   // initialize views
        getDataFromTaskGradeActivity(); // get data via bundles
        display();      // get data via arraylist
        initializeAdapter();    // initialize adapter of recyclerView
        automaticSort();    // automatic sort arraylist of students when activity opens

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        initialize();
        getDataFromTaskGradeActivity();
        display();
        initializeAdapter();
        automaticSort();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
        getDataFromTaskGradeActivity();
        display();
        initializeAdapter();
        automaticSort();
    }

    // initialize all views
    public void initialize(){
        extendedRecyclerView = view.findViewById(R.id.extendedRecViewTaskGrade);
        _taskType = view.findViewById(R.id.taskTypeTaskGradeFragment);
        _taskNumber = view.findViewById(R.id.taskNumberTaskGradeFragment);
        _gradingPeriod = view.findViewById(R.id.gradingPeriodTaskGradeFragment);
        _subjectID = view.findViewById(R.id.subjectIDTaskGradeFragment);
        _noStudentToGradeTextViewFragment = view.findViewById(R.id.noStudentTextViewGradeFragment);
        _noStudentTGradeViewFragment = view.findViewById(R.id.noStudentViewGradeFragment);
        _taskId = view.findViewById(R.id.taskIdTaskGradeFragment);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        taskGradeAdapter = new TaskGradeAdapter(taskGradeItems, getContext());
        extendedRecyclerView.setAdapter(taskGradeAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(_noStudentTGradeViewFragment, _noStudentToGradeTextViewFragment);
        linearLayoutManager.setStackFromEnd(true);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){
        studentID = new ArrayList<>();
        subjectID = new ArrayList<>();
        taskGradeItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(getContext());
        taskGradeItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<TaskGradeItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();


        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + _taskType.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_TASK_NUMBER_MY_GRADE + " = "
                + _taskNumber.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " ='"
                + _gradingPeriod.getText().toString() + "'AND "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + "="
                + _subjectID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " = '"
                + "" + "'", null);

        ArrayList<TaskGradeItems> taskGradeItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                taskGradeItems.add(new TaskGradeItems(
                        cursor.getString(1),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(9),
                        cursor.getInt(2),
                        cursor.getInt(10)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return taskGradeItems;
    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort(){
        taskGradeItems.sort(TaskGradeItems.sortAtoZComparator);
        initializeAdapter();
    }

    // get data from task grade activity to this fragment
    public void getDataFromTaskGradeActivity(){

        bundle = this.getArguments();
        if (getArguments() != null){
            String taskType = bundle.getString("TaskType");
            _taskType.setText(taskType);

            String taskNumber = bundle.getString("TaskNumber");
            _taskNumber.setText(taskNumber);

            String gradingPeriod = bundle.getString("GradingPeriod");
            _gradingPeriod.setText(gradingPeriod);

            String subjectId = bundle.getString("SubjectId");
            _subjectID.setText(subjectId);

            String taskId = bundle.getString("TaskId");
            _taskId.setText(taskId);
        }
    }

}