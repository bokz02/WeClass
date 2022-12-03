package com.example.weclass.studentlist.profile.assignments.fragments;

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
import com.example.weclass.studentlist.profile.activities.ActivitiesAdapter;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;

import java.util.ArrayList;


public class AssignmentFinals extends Fragment {

    View view, noView;
    TextView noTextView;
    DataBaseHelper dbh;
    ArrayList<ActivitiesItems> activitiesItems;
    ActivitiesAdapter activitiesAdapter;
    ExtendedRecyclerView extendedRecyclerView;
    String studentNumber, parentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_for_all_activities_profile, container, false);
        initialize();
        getDataFromProfile();
        initializeRecView();
        initializeAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeAdapter();
    }

    public void initialize(){
        extendedRecyclerView = view.findViewById(R.id.extendedRecViewActivities);
        noTextView = view.findViewById(R.id.noStudentTextViewActivities);
        noView = view.findViewById(R.id.noStudentViewActivities);
    }

    public void getDataFromProfile(){
        Bundle bundle = this.getArguments();
        if (getArguments() != null) {
            studentNumber = bundle.getString("studentNumber");
            parentId = bundle.getString("parentId");
        }
    }

    public void initializeAdapter(){
        activitiesAdapter = new ActivitiesAdapter(getContext(), activitiesItems);
        extendedRecyclerView.setAdapter(activitiesAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(noView,noTextView);
    }

    public void initializeRecView(){

        activitiesItems = new ArrayList<>();
        dbh = new DataBaseHelper(getContext());
        activitiesItems = getData();
    }

    private ArrayList<ActivitiesItems> getData(){
        SQLiteDatabase sqLiteDatabase = dbh.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = '"
                + studentNumber + "' AND "
                + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = '"
                + parentId + "' AND "
                + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                + "Assignment" + "' and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                + "Finals" + "' and "
                + DataBaseHelper.COLUMN_GRADE_MY_GRADE + " != '"
                + "" + "'", null);

        ArrayList<ActivitiesItems> activitiesItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                activitiesItems.add(new ActivitiesItems(
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return activitiesItems;
    }
}
