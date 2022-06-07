package com.example.weclass.tasks;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Record extends Fragment implements TaskAdapter.OnNoteListener {

    DataBaseHelper dataBaseHelper;
    ArrayList<TaskItems> taskItems, id, _parentID;
    FloatingActionButton floatingActionButton;
    ExtendedRecyclerView extendedRecyclerView;
    TaskAdapter taskAdapter;
    TextView parentID, subjectCode, noFileTextView;
    EditText searchEditText;
    View view;
    View _noFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);

        initialize(); // INITIALIZE VIEWS
        showHideFloatingActionButton(); // HIDE FLOATING ACTION BUTTON WHEN RECYCLERVIEW IS SCROLLING
        getDataFromBottomNaviActivity(); // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH REPORTS
        moveToAddTask(); // MOVE TO ADD TASK ACTIVITY
        display();
        initializeAdapter();


        return view;
    }

    // INITIALIZE VIEWS
    public void initialize(){
        parentID = view.findViewById(R.id.parentIDRecord);
        subjectCode = view.findViewById(R.id.subjectCodeRecords);
        floatingActionButton = view.findViewById(R.id.fabAddTask);
        extendedRecyclerView = view.findViewById(R.id.recyclerViewTaskList);
        noFileTextView = view.findViewById(R.id.noTaskTextView);
        _noFile = view.findViewById(R.id.noFileTaskView);
        extendedRecyclerView.setEmptyView(_noFile,noFileTextView);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        taskAdapter = new TaskAdapter(getContext(), taskItems, this);
        extendedRecyclerView.setAdapter(taskAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(_noFile,noFileTextView);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){
        id = new ArrayList<>();
        _parentID = new ArrayList<>();
        taskItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(getContext());
        taskItems = displayData();
    }

    // HIDE FLOATING ACTION BUTTON WHEN RECYCLERVIEW IS SCROLLING
    public void showHideFloatingActionButton(){

        extendedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0 || dy < 0 && floatingActionButton.isShown()){
                    floatingActionButton.hide();
                }
            }
        });
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<TaskItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHelper.TABLE_NAME4 + " WHERE " + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = " + parentID.getText().toString(), null);
        ArrayList<TaskItems> taskItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                taskItems.add(new TaskItems(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return taskItems;
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID.setText(bundle.getString("IDParent"));

        }
    }

    // MOVE TO ADD TASK ACTIVITY
    public void moveToAddTask(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddTask.class);
                intent.putExtra("id", parentID.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onNoteClick(int position) {

    }
}