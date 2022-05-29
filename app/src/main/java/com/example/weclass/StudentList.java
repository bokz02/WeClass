package com.example.weclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


public class StudentList extends Fragment {

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    v = inflater.inflate(R.layout.fragment_student_list, container, false);

    initialize();
    //moveFromFragmentToActivity();

        return v;
    }

    public void initialize(){

    }

}
