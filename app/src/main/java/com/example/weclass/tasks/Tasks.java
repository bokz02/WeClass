package com.example.weclass.tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.CSVWriter;
import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.StudentItems;
import com.example.weclass.subject.AddSubjectActivity;
import com.example.weclass.taskGrade.TaskGrade;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Tasks extends Fragment implements TaskAdapter.OnNoteListener {

    DataBaseHelper dataBaseHelper;
    ArrayList<TaskItems> taskItems, id, _parentID;
    FloatingActionButton floatingActionButton;
    ExtendedRecyclerView extendedRecyclerView;
    ImageButton optionButton;
    TaskAdapter taskAdapter;
    TextView parentID, subjectCode, noFileTextView, _taskSubjectCode, _course, _sy;
    EditText searchEditText;
    View view;
    View _noFile;
    int lastFirstVisiblePosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);

        initialize(); // INITIALIZE VIEWS
        showHideFloatingActionButton(); // HIDE FLOATING ACTION BUTTON WHEN RECYCLERVIEW IS SCROLLING
        getDataFromBottomNaviActivity(); // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH REPORTS
        moveToAddTask(); // MOVE TO ADD TASK ACTIVITY
        display();      // DATA TO BE DISPLAY IN RECYCLERVIEW
        initializeAdapter();        // INITIALIZE ADAPTER FOR RECYCLERVIEW
        textListener();     // SEARCH FUNCTION FOR LIST OF STUDENTS
        automaticSort(); // SORT LISTS WHEN ACTIVITY OPENS
        optionButton();

        return view;
    }


    // INITIALIZE VIEWS
    public void initialize() {
        parentID = view.findViewById(R.id.parentIDRecord);
        subjectCode = view.findViewById(R.id.subjectCodeRecords);
        floatingActionButton = view.findViewById(R.id.fabAddTask);
        extendedRecyclerView = view.findViewById(R.id.recyclerViewTaskList);
        noFileTextView = view.findViewById(R.id.noTaskTextView);
        _noFile = view.findViewById(R.id.noAttendanceView);
        _taskSubjectCode = view.findViewById(R.id.taskSubjectTextView);
        _course = view.findViewById(R.id.courseTextViewTask);
        searchEditText = view.findViewById(R.id.searchEditTextTask);
        optionButton = view.findViewById(R.id.optionButtonTasks);
        _sy = view.findViewById(R.id.schoolYearTasks);
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter() {

        taskAdapter = new TaskAdapter(getContext(), taskItems, this);
        extendedRecyclerView.setAdapter(taskAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(_noFile, noFileTextView);


    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display() {
        id = new ArrayList<>();
        _parentID = new ArrayList<>();
        taskItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(getContext());
        taskItems = displayData();
    }

    @Override
    public void onResume() {
        initialize();       // INITIALIZE ALL VIEWS
        moveToAddTask();       // ADD STUDENT BUTTON
        getDataFromBottomNaviActivity(); // GET PARENT ID FROM SUBJECT ACTIVITY
        display();              // DATA TO BE DISPLAY IN RECYCLERVIEW
        initializeAdapter();     // INITIALIZE ADAPTER FOR RECYCLERVIEW
        textListener();     // FILTER DATA WHEN SEARCHING
        automaticSort(); // SORT LISTS WHEN ACTIVITY OPENS

        // SAVE RECYCLERVIEW SCROLL POSITION
        ((LinearLayoutManager) extendedRecyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
        super.onResume();
    }

    @Override
    public void onPause() {

        // RESUME RECYCLERVIEW SCROLL POSITION
        lastFirstVisiblePosition = ((LinearLayoutManager) extendedRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        super.onPause();
    }


    // HIDE FLOATING ACTION BUTTON WHEN RECYCLERVIEW IS SCROLLING
    public void showHideFloatingActionButton() {

        extendedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    floatingActionButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            floatingActionButton.show();
                        }
                    }, 2000);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                if(dy > 0 || dy < 0 && floatingActionButton.isShown()){
//                    floatingActionButton.hide();
//                }

                if (dy > 0) {
                    floatingActionButton.hide();
//                }else if (dy < 0){
//                    floatingActionButton.show();
                }
            }
        });
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<TaskItems> displayData() {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_TASKS + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = "
                + parentID.getText().toString(), null);

        ArrayList<TaskItems> taskItems = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                taskItems.add(new TaskItems(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(3)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return taskItems;
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID.setText(bundle.getString("IDParent"));
            _taskSubjectCode.setText(bundle.getString("SubjectCode"));
            _course.setText(bundle.getString("CourseCode"));
            _sy.setText(bundle.getString("sy"));
        }
    }

    // MOVE TO ADD TASK ACTIVITY
    public void moveToAddTask() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddTask.class);
                intent.putExtra("id", parentID.getText().toString());
                startActivity(intent);
            }
        });
    }

    // SEARCH FUNCTION FOR LIST OF STUDENTS
    public void textListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                taskAdapter.getFilter().filter(editable);

            }
        });
    }

    @Override
    public void onNoteClick(int position) {



        Intent intent = new Intent(getContext(), TaskGrade.class);
        intent.putExtra("Task", taskItems.get(position));
        intent.putExtra("id", parentID.getText().toString());

        startActivity(intent);
    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort() {
        taskItems.sort(TaskItems.sortAtoZComparator);
        taskItems.sort(TaskItems.sortZtoAComparator);
        taskItems.sort(TaskItems.sortByTaskNumber);
        initializeAdapter();
    }

    public void optionButton() {
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), optionButton);
                popupMenu.getMenuInflater().inflate(R.menu.option_tasks, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.exportCSV:
                                askForPermissions();
                                break;
                        }
                        return false;
                    }
                });


                popupMenu.show();
            }
        });
    }

    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            exportDB();
        }
    }

    private void exportDB() {
        DataBaseHelper dbhelper = new DataBaseHelper(getContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) {
            boolean mkdir = exportDir.mkdirs();

            if (!mkdir) {
                Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        }

        File file = new File(exportDir, _taskSubjectCode.getText().toString() + "_Task_Grades_"+ _course.getText().toString()+"_" +_sy.getText().toString() + ".csv");
        try {
            boolean createFile = file.createNewFile();
            if (!createFile) {

            }

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String[] columns = {"Last name", "First name", "Task type", "Task number", "Grade", "Grading period"};

            Cursor cursor = db.rawQuery("SELECT * FROM "
                    + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                    + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + "="
                    + parentID.getText().toString(), null);


            csvWrite.writeNext(columns);
            while (cursor.moveToNext()) {
                //Which column you want to export
                String[] arrStr = {cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)
                        , cursor.getString(7), cursor.getString(8)};
                csvWrite.writeNext(arrStr);
            }
            Toast.makeText(getContext(), "Downloaded to storage/downloads", Toast.LENGTH_SHORT).show();
            csvWrite.close();
            cursor.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
            Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}