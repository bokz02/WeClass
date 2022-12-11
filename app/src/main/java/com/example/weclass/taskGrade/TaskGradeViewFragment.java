package com.example.weclass.taskGrade;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.EditTextSetMinMax;
import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.Collections;


public class TaskGradeViewFragment extends Fragment {


    TextView _taskType, _taskNumber, _gradingPeriod, _subjectId, _noStudentToGradeTextViewFragment;
    View view, _noStudentTGradeViewFragment;
    Bundle bundle;
    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<TaskGradeViewItems> taskGradeViewItems, studentID, subjectID, idTaskGrade;
    TaskGradeViewFragmentAdapter taskGradeViewFragmentAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_grade_view, container, false);


        initialize();
        getDataFromTaskGradeActivity();
        display();
        initializeAdapter();
        automaticSort();

        return view;
    }

    // run all methods of this fragment when fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        initialize();
        getDataFromTaskGradeActivity();
        display();
        initializeAdapter();
        automaticSort();
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

    // initialize all views
    public void initialize(){
        extendedRecyclerView = view.findViewById(R.id.extendedRecViewTaskGradeView);
        _taskType = view.findViewById(R.id.taskTypeGradeView);
        _taskNumber = view.findViewById(R.id.taskNumberGradeView);
        _gradingPeriod = view.findViewById(R.id.gradingPeriodGradeView);
        _subjectId = view.findViewById(R.id.subjectIdGradeViewFragment);
        _noStudentTGradeViewFragment = view.findViewById(R.id.noStudentViewGradeFragmentView);
        _noStudentToGradeTextViewFragment = view.findViewById(R.id.noStudentTextViewGradeFragmentView);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        taskGradeViewFragmentAdapter = new TaskGradeViewFragmentAdapter(taskGradeViewItems, getContext());
        extendedRecyclerView.setAdapter(taskGradeViewFragmentAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(_noStudentTGradeViewFragment, _noStudentToGradeTextViewFragment);
        linearLayoutManager.setStackFromEnd(true);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){
        idTaskGrade = new ArrayList<>();
        studentID = new ArrayList<>();
        subjectID = new ArrayList<>();
        taskGradeViewItems = new ArrayList<>();
        dataBaseHelper = DataBaseHelper.getInstance(getContext());
        taskGradeViewItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<TaskGradeViewItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = "
                + _subjectId.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + _taskType.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_TASK_NUMBER_MY_GRADE + " = "
                + _taskNumber.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + _gradingPeriod.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + "!='" + "" + "'", null);

        ArrayList<TaskGradeViewItems> taskGradeViewItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                taskGradeViewItems.add(new TaskGradeViewItems(
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8),
                        cursor.getString(1),
                        cursor.getString(9),
                        cursor.getInt(3),
                        cursor.getInt(10)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return taskGradeViewItems;
    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort(){
        Collections.sort(taskGradeViewItems, TaskGradeViewItems.sortAtoZComparator);
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
            _subjectId.setText(subjectId);

        }
    }

}