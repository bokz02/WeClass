package com.example.weclass.studentlist.profile.attendance.attendanceviewpager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;

public class AbsentFragment extends Fragment {

    View view;
    ExtendedRecyclerView extendedRecyclerView;
    TextView studentNumber, parentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_absent, container, false);

        initialize();
        return view;
    }

    public void initialize(){
        extendedRecyclerView = view.findViewById(R.id.extendedRecViewPresentFragment);
        studentNumber = view.findViewById(R.id.studentNumberPresentFragment);
        parentId = view.findViewById(R.id.parentIdPresentFragment);
    }
}