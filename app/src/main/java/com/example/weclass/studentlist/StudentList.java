package com.example.weclass.studentlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.security.identity.ResultData;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weclass.BottomNavi;
import com.example.weclass.CSVWriter;
import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.LockScreen;
import com.example.weclass.MyProgressBar;
import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.SpinnerAdapter;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;


public class StudentList extends Fragment implements StudentAdapter.OnNoteListener, StudentAdapter.ItemCallback, StudentAdapter.UpdateStudentList {

    ExtendedRecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    View view;
    ImageButton optionButton;
    TextView parentID, _subjectCode, _sort, _courseTitle,
            noStudentTextView, _id, _studentSum, _archive
            ,_schoolYear;
    DataBaseHelper dataBaseHelper;
    ArrayList<StudentItems> studentItems;
    StudentAdapter studentAdapter;
    EditText searchStudent;
    View noFile_;
    ProgressBar progressBar;
    Spinner spinner;
    String[] gradingPeriod = {"Midterm", "Finals"};
    SharedPreferences sharedPreferences;
    int spinnerPosition;
    String spinnerGradingPeriod, notArchive;
    PassData passData;
    private final String tag = "Student list";

    int lastFirstVisiblePosition;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_student_list, container, false);

        initialize();       // INITIALIZE ALL VIEWS
        selectGradingPeriod();
        loadSpinnerPosition();
        addStudent();       // ADD STUDENT BUTTON
        getDataFromBottomNaviActivity(); // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
        display();              // DATA TO BE DISPLAY IN RECYCLERVIEW
        initializeAdapter();     // INITIALIZE ADAPTER FOR RECYCLERVIEW
        textListener();         // SEARCH FUNCTION FOR LIST OF STUDENTS
        showHideFloatingActionButton();     // HIDE FLOATING ACTION BUTTON WHEN RECYCLERVIEW IS SCROLLING
        sortList();     // SORT STUDENT LIST
        getSumOfStudents(); // GET SUM OF ALL STUDENTS BASED ON THEIR SUBJECT ID
        automaticSort();    //AUTOMATIC SORT
        optionButton();


        return view;
    }



    // RESUME ALL FUNCTION FROM BcEING HIDE
    @Override
    public void onResume() {
        super.onResume();

        initialize();       // INITIALIZE ALL VIEWS
        addStudent();       // ADD STUDENT BUTTON
        getDataFromBottomNaviActivity(); // GET PARENT ID FROM SUBJECT ACTIVITY
        display();              // DATA TO BE DISPLAY IN RECYCLERVIEW
        initializeAdapter();     // INITIALIZE ADAPTER FOR RECYCLERVIEW
        textListener();         // SEARCH BAR FOR LIST OF STUDENTS
        getSumOfStudents();     // GET SUM OF ALL STUDENTS BASED ON THEIR SUBJECT ID
        automaticSort();    //AUTOMATIC SORT
        hideOptionButton();


        // SAVE RECYCLERVIEW SCROLL POSITION
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
    }

    @Override
    public void onPause() {
        super.onPause();

        // RESUME RECYCLERVIEW SCROLL POSITION
        lastFirstVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    // interface that pass data from this fragment to bottom navigation
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passData = (PassData) context;
    }

    public void hideOptionButton(){
        if (notArchive.equals("Archive")){
            if (optionButton.getVisibility() == View.VISIBLE){
                optionButton.setVisibility(View.GONE);
            }
        }
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

        if (notArchive.equals("Archive")){
            floatingActionButton.hide();
        }else {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (dy > 0) {
                        floatingActionButton.hide();
//                }else if (dy < 0){
//                    floatingActionButton.show();
                    }
                }
            });
        }
    }

    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        studentAdapter = new StudentAdapter(getContext(), studentItems, this, this, spinnerGradingPeriod, this, notArchive);
        recyclerView.setAdapter(studentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEmptyView(noFile_, noStudentTextView);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display(){
        studentItems = new ArrayList<>();
        dataBaseHelper = DataBaseHelper.getInstance(getContext());
        studentItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<StudentItems> displayData(){

        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID + " = "
                + parentID.getText().toString(), null);

        ArrayList<StudentItems> studentItems = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                studentItems.add(new StudentItems(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8),
                        cursor.getBlob(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return studentItems;
    }

    // INITIALIZE ALL VIEWS
    public void initialize(){
        _sort = view.findViewById(R.id.sortStudentList);
        recyclerView = view.findViewById(R.id.recyclerViewStudentList);
        floatingActionButton = view.findViewById(R.id.fabAddStudent);
        parentID = view.findViewById(R.id.parentID);
        searchStudent = view.findViewById(R.id.searchEditTextStudent);
        _subjectCode = view.findViewById(R.id.studentListSubjectCode);
        _courseTitle = view.findViewById(R.id.courseTitleStudentList);
        noFile_ = view.findViewById(R.id.noStudentTaskView);
        noStudentTextView = view.findViewById(R.id.noStudentTextView);
        _studentSum = view.findViewById(R.id.summaryOfStudent);
        _archive = view.findViewById(R.id.archiveTextViewStudentList);
        optionButton = view.findViewById(R.id.optionButtonStudentList);
        _schoolYear = view.findViewById(R.id.schoolYearStudentList);
        progressBar = view.findViewById(R.id.progressBarStudentList);
        spinner = view.findViewById(R.id.spinnerStudentList);

    }

    public void optionButton(){
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), optionButton);
                popupMenu.getMenuInflater().inflate(R.menu.option_student_list, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.exportCSV:
                                if (studentAdapter.getItemCount() == 0){
                                    Toast.makeText(getContext(),"Student list is empty", Toast.LENGTH_SHORT).show();
                                }else {
                                    askForPermissions();
                                }
                                break;
                            case R.id.importCSV:
                                showHelp();
                                //askForPermissionsBeforeImport();
                                break;
                        }
                        return false;
                    }
                });


                popupMenu.show();
            }
        });
    }

    public void askForPermissionsBeforeImport() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            openStorage();
        }
    }

    public void openStorage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        intent.setDataAndType(uri,"*/*");
        startActivityForResult(intent,1);

    }

    public void showHelp(){

        if (getActivity()!=null) {
            ImageView image = new ImageView(getActivity());

            image.setImageResource(R.drawable.importhelp);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.ic_baseline_warning_24);
            builder.setTitle("Import");
            builder.setMessage("Before you import, make sure the file is CSV format and contains 4 columns only,\n\nstudent number, last name, first name,\nand middle name.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    askForPermissionsBeforeImport();
                }
            }).setView(image);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }
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
            _subjectCode.setText(bundle.getString("SubjectCode"));
            _courseTitle.setText(bundle.getString("CourseCode"));
            _archive.setText(bundle.getString("archive_text"));
            _schoolYear.setText(bundle.getString("sy"));
            notArchive = bundle.getString("NotArchive");
            Log.d(tag, " "+ notArchive);

        }
    }

    // GET SUM OF ALL STUDENTS BASED ON THEIR SUBJECT ID
    public void getSumOfStudents(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT COUNT(*) FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID + " = "
                + parentID.getText().toString(), null);

        if (cursor.moveToFirst() ){
            _studentSum.setText(String.valueOf(cursor.getInt(0)));
            cursor.close();
        }
    }


    // THIS INTERFACE KNOWS WHAT ITEM NUMBER USER CLICKS THEN PASS THE DATA TO PROFILE ACTIVITY
    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getContext(), StudentProfile.class);
        intent.putExtra("Student", studentItems.get(position));

        intent.putExtra("course", _courseTitle.getText().toString());
        intent.putExtra("subject", _subjectCode.getText().toString());
        intent.putExtra("gradingPeriod", spinnerGradingPeriod);
        startActivity(intent);
    }

    // UPDATE SUM OF STUDENTS TEXTVIEW WHEN ITEM IS DELETED FROM RECYCLERVIEW
    @Override
    public void updateTextView() {
        _studentSum.setText(String.valueOf(studentAdapter.getItemCount()));
    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort(){
        Collections.sort(studentItems, StudentItems.sortAtoZComparator);
        initializeAdapter();
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
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

    // download student list as csv file
    private void exportDB() {

        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()){
            boolean mkdir = exportDir.mkdirs();

            if (!mkdir) {
                Toast.makeText(getContext(), "failed" , Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Success" , Toast.LENGTH_SHORT).show();
            }
        }

        File file = new File(exportDir,  ""+_courseTitle.getText().toString()+"_"+ _subjectCode.getText().toString() +
                "_Student_list_" + _schoolYear.getText().toString() +""+".csv");
        try
        {
            boolean createFile = file.createNewFile();
            if (!createFile){

            }

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = {"Student number","Last name","First name", "Middle name","Gender", "Present",
                    "Late", "Absences", "Midterm grade", "Finals grade", "Final rating"};

            Cursor cursor = db.rawQuery("SELECT * FROM "
                    + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                    + DataBaseHelper.COLUMN_PARENT_ID + "="
                    + parentID.getText().toString(),null);

            csvWrite.writeNext(columns);
            while(cursor.moveToNext())
            {
                //Which column you want to export
                String[] arrStr ={cursor.getString(1),cursor.getString(3),cursor.getString(4), cursor.getString(5), cursor.getString(6)
                        ,cursor.getString(7), cursor.getString(13), cursor.getString(8), cursor.getString(10), cursor.getString(11),
                        cursor.getString(12)};
                csvWrite.writeNext(arrStr);
            }
            Toast.makeText(getContext(), "Downloaded to storage/downloads" , Toast.LENGTH_SHORT).show();
            csvWrite.close();
            cursor.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
            Toast.makeText(getContext(), "Error occurred" , Toast.LENGTH_SHORT).show();
        }
    }

    // method for spinner
    private void selectGradingPeriod(){

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, gradingPeriod);
        spinner.setAdapter(spinnerAdapter);
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setDropDownVerticalOffset(spinner.getDropDownVerticalOffset() + spinner.getHeight());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // when item in dropdown selected
                spinnerGradingPeriod = adapterView.getItemAtPosition(position).toString();
                spinnerPosition = spinner.getSelectedItemPosition();
                saveSpinnerPosition();
                OnPassData(spinnerGradingPeriod);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void saveSpinnerPosition(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("position", spinnerPosition);
        editor.apply();
    }

    public void loadSpinnerPosition(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int position= sharedPreferences .getInt("position", 0);
        spinner.setSelection(position);
        if(position == 0) {
            spinnerGradingPeriod = "Midterm";
        }else {
            spinnerGradingPeriod = "Finals";
        }
        OnPassData(spinnerGradingPeriod);
    }

    @Override
    public void updateRecView() {
        if (studentAdapter.getItemCount()==0){
            initializeAdapter();
        }
    }

    // interface that pass data from this fragment to bottom navigation
    public interface PassData {
        void onPassData(String data);
    }

    public void OnPassData(String data){
        passData.onPassData(data);
    }

}
