package com.example.weclass.ratings.fragments;

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
import com.example.weclass.ratings.RatingsAdapter;
import com.example.weclass.ratings.RatingsModel;

import java.util.ArrayList;


public class Midterm extends Fragment {

    View view, noView;
    TextView noTextView;
    RatingsAdapter ratingsAdapter;
    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<RatingsModel> ratingsModel;
    String parentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_midterm, container, false);

        initialize();
        getBundleData();
        initializeRecView();
        initializeAdapter();


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
        }
    }

    public void initializeAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ratingsAdapter = new RatingsAdapter(ratingsModel, getContext());
        extendedRecyclerView.setAdapter(ratingsAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(noView, noTextView);
        linearLayoutManager.setStackFromEnd(true);
    }

    private void initializeRecView(){

        ratingsModel = new ArrayList<>();
        ratingsModel = getData();
    }

    private ArrayList<RatingsModel> getData(){
        DataBaseHelper dbh = new DataBaseHelper(getContext());
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
                        cursor.getString(10)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return ratingsModels;
    }


}