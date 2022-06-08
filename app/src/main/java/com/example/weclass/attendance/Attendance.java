package com.example.weclass.attendance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;

public class Attendance extends Fragment {

    ExtendedRecyclerView extendedRecyclerView;
    AttendanceAdapter attendanceAdapter;
    AttendanceItems attendanceItems;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        return view;
    }

    public void initialize(){
        extendedRecyclerView = view.findViewById(R.id.attendanceRecyclerView);
    }
}