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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.AddStudent;
import com.example.weclass.studentlist.StudentAdapter;
import com.example.weclass.studentlist.StudentItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class StudentList extends Fragment implements StudentAdapter.OnNoteListener{

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    View view;
    TextView parentID;
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
    getDataFromActivity(); // GET PARENT ID FROM SUBJECT ACTIVITY
    display();              // DATA TO BE DISPLAY IN RECYCLERVIEW
    initializeAdapter();     // INITIALIZE ADAPTER FOR RECYCLERVIEW
    textListener();         // SEARCH BAR FOR LIST OF STUDENTS

        return view;
    }

    public void initializeAdapter(){
        studentAdapter = new StudentAdapter(getContext(), studentItems, this);
        recyclerView.setAdapter(studentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void display(){
        id = new ArrayList<>();
        parent_id = new ArrayList<>();
        studentItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(getContext());
        studentItems = displayData();
    }

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


    public void initialize(){
        recyclerView = view.findViewById(R.id.recyclerViewStudentList);
        floatingActionButton = view.findViewById(R.id.fabAddStudent);
        parentID = view.findViewById(R.id.parentID);
        searchStudent = view.findViewById(R.id.searchEditTextStudent);
    }

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

    public void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID.setText(bundle.getString("IDParent"));
        }

    }

    @Override
    public void onNoteClick(int position) {

    }
}
