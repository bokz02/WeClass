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


public class TaskGradeFragment extends Fragment implements TaskGradeAdapter.OnNoteListener{

    View view, _noStudentTGradeViewFragment;
    TextView _taskType,_subjectID, _taskNumber, _gradingPeriod, _noStudentToGradeTextViewFragment;
    Bundle bundle;
    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<TaskGradeItems> taskGradeItems, studentID, subjectID;
    TaskGradeAdapter taskGradeAdapter;
    DataBaseHelper dataBaseHelper;
    TaskGradeViewFragment taskGradeViewFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_grade, container, false);

        initialize();
        getDataFromTaskGradeActivity();
        display();
        initializeAdapter();
        automaticSort();

        return view;
    }

    public void initialize(){

        extendedRecyclerView = view.findViewById(R.id.extendedRecViewTaskGrade);
        _taskType = view.findViewById(R.id.taskTypeTaskGradeFragment);
        _taskNumber = view.findViewById(R.id.taskNumberTaskGradeFragment);
        _gradingPeriod = view.findViewById(R.id.gradingPeriodTaskGradeFragment);
        _subjectID = view.findViewById(R.id.subjectIDTaskGradeFragment);
        _noStudentToGradeTextViewFragment = view.findViewById(R.id.noStudentTextViewGradeFragment);
        _noStudentTGradeViewFragment = view.findViewById(R.id.noStudentViewGradeFragment);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        taskGradeAdapter = new TaskGradeAdapter(taskGradeItems, getContext(), this);
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

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort(){
        Collections.sort(taskGradeItems, TaskGradeItems.sortAtoZComparator);
        initializeAdapter();
    }

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
        }
    }


    @Override
    public void OnNoteClick(int position) {

    }

}