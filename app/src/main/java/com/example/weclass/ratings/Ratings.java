package com.example.weclass.ratings;

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
import com.example.weclass.ratings.RatingAdapter;
import com.example.weclass.studentlist.StudentItems;

import java.util.ArrayList;
import java.util.Collections;

public class Ratings extends Fragment {

    TextView _course, _subjectCode, _noViewTextView, _subjectID;
    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<StudentItems> studentItems;
    RatingAdapter ratingAdapter;
    DataBaseHelper dataBaseHelper;
    View view, _noView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ratings, container, false);

        initialize();
        getDataFromBottomNaviActivity();
        display();
        initializeAdapter();
        automaticSort();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeAdapter();
    }

    public void initialize(){
        extendedRecyclerView = view.findViewById(R.id.extendedRecViewRatings);
        _noViewTextView = view.findViewById(R.id.noStudentTextViewRatings);
        _noView = view.findViewById(R.id.noStudentViewRatings);
        _subjectID = view.findViewById(R.id.subjectIDRatings);
        _subjectCode = view.findViewById(R.id.subjectCodeTextViewRatings);
        _course = view.findViewById(R.id.courseTextViewRatings);
    }


    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){

        ratingAdapter = new RatingAdapter(getContext(), studentItems);
        extendedRecyclerView.setAdapter(ratingAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(_noView,_noViewTextView);


    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){
        studentItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(getContext());
        studentItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<StudentItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID + " = "
                + _subjectID.getText().toString(), null);

        ArrayList<StudentItems> studentItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                studentItems.add(new StudentItems(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getInt(7),
                        cursor.getBlob(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return studentItems;
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            _subjectID.setText(bundle.getString("IDParent"));
            _subjectCode.setText(bundle.getString("SubjectCode"));
            _course.setText(bundle.getString("CourseCode"));
        }
    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort(){
        Collections.sort(studentItems, StudentItems.sortAtoZComparator);
        initializeAdapter();
    }

}