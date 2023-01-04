package com.example.weclass.ratings.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.calendar.CalendarEvents;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.ratings.RatingsAdapter;
import com.example.weclass.ratings.RatingsModel;
import com.example.weclass.studentlist.StudentProfile;
import com.example.weclass.taskGrade.TaskGrade;

import java.util.ArrayList;


public class Midterm extends Fragment implements RatingsAdapter.OnStudentClick {

    View view, noView;
    TextView noTextView;
    RatingsAdapter ratingsAdapter;
    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<RatingsModel> ratingsModel;
    String parentId, classType, gradingPeriod;
    @SuppressLint("StaticFieldLeak")
    private static Midterm instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_midterm, container, false);

        initialize();
        getBundleData();
        initializeRecView();
        initializeAdapter();

        instance = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
        getBundleData();
        initializeRecView();
        initializeAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
        initialize();
        getBundleData();
        initializeRecView();
        initializeAdapter();
    }

    public void initialize(){
        noView = view.findViewById(R.id.noStudentViewMidterm);
        noTextView = view.findViewById(R.id.noStudentTextViewMidterm);
        extendedRecyclerView = view.findViewById(R.id.extendedRecViewMidterm);

    }

    private void getBundleData(){
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            parentId = bundle.getString("parentId");
            classType = bundle.getString("classType");
            gradingPeriod = bundle.getString("gradingPeriod");

        }
    }

    public static Midterm getInstance() {
        return instance;
    }

    public void initializeAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ratingsAdapter = new RatingsAdapter(ratingsModel, getContext(), classType, this);
        extendedRecyclerView.setAdapter(ratingsAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(noView, noTextView);
        linearLayoutManager.setStackFromEnd(true);
    }

    public void initializeRecView(){

        ratingsModel = new ArrayList<>();
        ratingsModel = getData();
    }

    private ArrayList<RatingsModel> getData(){
        DataBaseHelper dbh = DataBaseHelper.getInstance(getContext());
        SQLiteDatabase sqLiteDatabase = dbh.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID + " = "
                + parentId, null);

        ArrayList<RatingsModel> ratingsModels = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                ratingsModels.add(new RatingsModel(
                        cursor.getBlob(9),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(10),
                        cursor.getString(1),
                        cursor.getString(2)));
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        cursor.close();
        return ratingsModels;
    }


    @Override
    public void onStudentClick(int position) {
        Intent intent = new Intent(getContext(), StudentProfile.class);
        intent.putExtra("Profile", ratingsModel.get(position));
        intent.putExtra("gradingPeriod", gradingPeriod);
        startActivity(intent);
    }
}