package com.example.weclass.tasks;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Record extends Fragment {

    FloatingActionButton floatingActionButton;
    ExtendedRecyclerView extendedRecyclerView;
    TextView parentID, subjectCode;
    EditText searchEditText;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);



        initialize(); // INITIALIZE VIEWS
        getDataFromBottomNaviActivity(); // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH REPORTS
        moveToAddTask(); // MOVE TO ADD TASK ACTIVITY

        return view;
    }

    // INITIALIZE VIEWS
    public void initialize(){
        parentID = view.findViewById(R.id.parentIDRecord);
        subjectCode = view.findViewById(R.id.subjectCodeRecords);
        floatingActionButton = view.findViewById(R.id.fabAddTask);
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID.setText(bundle.getString("IDParent"));
            subjectCode.setText(bundle.getString("SubjectCode"));
        }
    }

    // MOVE TO ADD TASK ACTIVITY
    public void moveToAddTask(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddTask.class);
                startActivity(intent);
            }
        });
    }
}