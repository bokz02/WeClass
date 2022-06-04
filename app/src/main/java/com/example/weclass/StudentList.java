package com.example.weclass;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.weclass.studentlist.AddStudent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class StudentList extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    View view;
    String a;
    TextView parentID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_student_list, container, false);

    initialize();
    addStudent();
    getDataFromActivity();


        return view;
    }

    public void initialize(){
        recyclerView = view.findViewById(R.id.recyclerViewAddSubject);
        floatingActionButton = view.findViewById(R.id.fabAddStudent);
        parentID = view.findViewById(R.id.parentID);
    }

    public void addStudent(){

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddStudent.class);
                startActivity(intent);
            }
        });
    }

    public void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID.setText(bundle.getString("IDParent"));
        }

    }
}
