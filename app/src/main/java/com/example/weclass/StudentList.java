package com.example.weclass;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.AddStudent;
import com.example.weclass.studentlist.StudentAdapter;
import com.example.weclass.studentlist.StudentItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;


public class StudentList extends Fragment implements StudentAdapter.OnNoteListener{

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    View view;
    TextView parentID, _studentCode, _sort, _courseTitle;
    DataBaseHelper dataBaseHelper;
    ArrayList<StudentItems> studentItems, id, parent_id;
    StudentAdapter studentAdapter;
    EditText searchStudent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_student_list, container, false);

    initialize();       // INITIALIZE ALL VIEWS
    addStudent();       // ADD STUDENT BUTTON
    getDataFromBottomNaviActivity(); // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    display();              // DATA TO BE DISPLAY IN RECYCLERVIEW
    initializeAdapter();     // INITIALIZE ADAPTER FOR RECYCLERVIEW
    textListener();         // SEARCH FUNCTION FOR LIST OF STUDENTS
    showHideFloatingActionButton();     // HIDE FLOATING ACTION BUTTON WHEN RECYCLERVIEW IS SCROLLING
    sortList();     // SORT STUDENT LIST


        return view;
    }

    // RESUME ALL FUNCTION FROM BEING HIDE
    @Override
    public void onResume() {
        initialize();       // INITIALIZE ALL VIEWS
        addStudent();       // ADD STUDENT BUTTON
        getDataFromBottomNaviActivity(); // GET PARENT ID FROM SUBJECT ACTIVITY
        display();              // DATA TO BE DISPLAY IN RECYCLERVIEW
        initializeAdapter();     // INITIALIZE ADAPTER FOR RECYCLERVIEW
        textListener();         // SEARCH BAR FOR LIST OF STUDENTS
        super.onResume();
    }

    // SORT STUDENT LIST
    public void sortList(){
        _sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(),view);
                popupMenu.inflate(R.menu.sort_student);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.sortAtoZ:
                                Collections.sort(studentItems, StudentItems.sortAtoZComparator);
                                initializeAdapter();
                            break;

                            case R.id.sortZtoA:
                                Collections.sort(studentItems, StudentItems.sortZtoAComparator);
                                initializeAdapter();
                            break;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    // HIDE FLOATING ACTION BUTTON WHEN RECYCLERVIEW IS SCROLLING
    public void showHideFloatingActionButton(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0 || dy < 0 && floatingActionButton.isShown()){
                    floatingActionButton.hide();
                }
            }
        });
    }


    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        studentAdapter = new StudentAdapter(getContext(), studentItems, this);
        recyclerView.setAdapter(studentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){
        id = new ArrayList<>();
        parent_id = new ArrayList<>();
        studentItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(getContext());
        studentItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<StudentItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHelper.TABLE_NAME2 + " WHERE " + DataBaseHelper.COLUMN_PARENT_ID + " = " + parentID.getText().toString(), null);
        ArrayList<StudentItems> studentItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                studentItems.add(new StudentItems(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return studentItems;
    }

    // INITIALIZE ALL VIEWS
    public void initialize(){
        _sort = view.findViewById(R.id.sortStudentList);
        recyclerView = view.findViewById(R.id.recyclerViewStudentList);
        floatingActionButton = view.findViewById(R.id.fabAddStudent);
        parentID = view.findViewById(R.id.parentID);
        searchStudent = view.findViewById(R.id.searchEditTextStudent);
        _studentCode = view.findViewById(R.id.studentListSubjectCode);
        _courseTitle = view.findViewById(R.id.courseTitleStudentList);
    }

    // SEARCH FUNCTION FOR LIST OF STUDENTS
    public void textListener(){
        searchStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                studentAdapter.getFilter().filter(editable);

            }
        });
    }

    // ADD STUDENT BUTTON
    public void addStudent(){

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddStudent.class);
                intent.putExtra("id", parentID.getText().toString());
                startActivity(intent);
            }
        });
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID.setText(bundle.getString("IDParent"));
            _studentCode.setText(bundle.getString("SubjectCode"));
            _courseTitle.setText(bundle.getString("CourseCode"));
        }

    }

    @Override
    public void onNoteClick(int position) {

    }
}
