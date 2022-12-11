package com.example.weclass.studentlist.profile.attendance.attendanceviewpager;

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
import com.example.weclass.studentlist.profile.attendance.AttendanceViewAdapter;
import com.example.weclass.studentlist.profile.attendance.PresentAndAbsentItems;

import java.util.ArrayList;


public class PresentFragment extends Fragment {

    View view, noView;
    ExtendedRecyclerView extendedRecyclerView;
    TextView studentNumber, parentId, noTextView;
    AttendanceViewAdapter attendanceViewAdapter;
    ArrayList<PresentAndAbsentItems> presentAndAbsentItems;
    DataBaseHelper dbh;
    String studentNumber_, parentId_;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_attendance, container, false);

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
        extendedRecyclerView = view.findViewById(R.id.extendedRecViewProfileAttendance);
        studentNumber = view.findViewById(R.id.studentNumberProfileAttendance);
        parentId = view.findViewById(R.id.parentIdProfileAttendance);
        noView = view.findViewById(R.id.noStudentViewProfileAttendance);
        noTextView = view.findViewById(R.id.noStudentTextViewProfileAttendance);
    }

    public void initializeAdapter(){

        attendanceViewAdapter = new AttendanceViewAdapter(presentAndAbsentItems, getContext());
        extendedRecyclerView.setAdapter(attendanceViewAdapter);
        extendedRecyclerView.setLayoutManager(new  LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(noView,noTextView);


    }

    public void getDataFromProfile(){
        Bundle bundle = this.getArguments();
        if (getArguments() != null) {
            studentNumber_ = bundle.getString("studentNumber");
            parentId_ = bundle.getString("parentId");
        }
    }

    public void initializeRecView(){

        presentAndAbsentItems = new ArrayList<>();
        dbh = DataBaseHelper.getInstance(getContext());
        presentAndAbsentItems = getData();
    }

    private ArrayList<PresentAndAbsentItems> getData(){
        SQLiteDatabase sqLiteDatabase = dbh.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = '"
                + studentNumber_ + "' AND "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + parentId_ + " AND "
                + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + " = "
                + 1, null);

        ArrayList<PresentAndAbsentItems> presentAndAbsentItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                presentAndAbsentItems.add(new PresentAndAbsentItems(
                        cursor.getString(4)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return presentAndAbsentItems;
    }




}