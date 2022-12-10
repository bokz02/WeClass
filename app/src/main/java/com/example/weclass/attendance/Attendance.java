package com.example.weclass.attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.CSVWriter;
import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.SpinnerAdapter;
import com.example.weclass.database.DataBaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

public class Attendance extends Fragment implements AttendanceAdapter.OnNoteListener, AttendanceAdapter.UpdateRecView{

    ExtendedRecyclerView extendedRecyclerView;
    ArrayList<AttendanceItems> attendanceItems, id, parentID;
    AttendanceAdapter attendanceAdapter;
    ImageButton optionButton, helpButton;
    DataBaseHelper dataBaseHelper;
    TextView _noStudentsTextView, _id, _parentID,
            dateTimeDisplay, _sortAttendance, _always0, _subjectCode,
            _sy, _course, present, absent, _late;
    View view;
    View _noStudentsView;
    EditText _search;
    Spinner spinner;
    String[] gradingPeriod = {"Midterm", "Finals"};
    SharedPreferences sharedPreferences;
    int spinnerPosition;
    String spinnerGradingPeriod;
    private static final String tag = "Attendance";

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance, container, false);

        initialize();
        selectGradingPeriod();
        loadSpinnerPosition();
        getDataFromBottomNaviActivity();
        display();
        initializeAdapter();
        textListener();
        displayDate();
        sortAttendance();
        automaticSort();
        optionButton();
        presentToday();
        absentToday();
        updateAttendanceToday();
        help();
        lateToday();


        return view;


    }

    @Override
    public void onResume() {

        initialize();
        getDataFromBottomNaviActivity();
        display();
        initializeAdapter();
        automaticSort();
        super.onResume();
    }

    public void initialize() {
        dateTimeDisplay = view.findViewById(R.id.currentDateTextView);
        extendedRecyclerView = view.findViewById(R.id.attendanceRecyclerView);
        _noStudentsView = view.findViewById(R.id.noAttendanceView);
        _noStudentsTextView = view.findViewById(R.id.noAttendanceTextView);
        _id = view.findViewById(R.id.idAttendanceRecView);
        _parentID = view.findViewById(R.id.parentIDAttendance);
        _search = view.findViewById(R.id.searchAttendanceEditText);
        _sortAttendance = view.findViewById(R.id.sortAttendance);
        _always0 = view.findViewById(R.id.always0Attendance);
        optionButton = view.findViewById(R.id.optionButtonAttendance);
        _subjectCode = view.findViewById(R.id.subjectCodeAttendance);
        _sy = view.findViewById(R.id.schoolYearAttendance);
        _course = view.findViewById(R.id.courseAttendance);
        present = view.findViewById(R.id.presentTodayAttendance);
        absent = view.findViewById(R.id.absentTodayAttendance);
        helpButton = view.findViewById(R.id.helpButtonAttendance);
        _late = view.findViewById(R.id.lateTodayAttendance);
        spinner = view.findViewById(R.id.spinnerAttendance);

    }

    public void help (){

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null) {
                    ImageView image = new ImageView(getActivity());


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setTitle("Notice");
                    builder.setMessage("Start taking attendance by tapping buttons. \n\nL stands for \"Late\"\nA stands for \"Absent\"\nP stands for \"Present\"\n");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            }
        });


    }


    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter() {
        attendanceAdapter = new AttendanceAdapter(getContext(), attendanceItems, this, this, spinnerGradingPeriod);
        extendedRecyclerView.setAdapter(attendanceAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        extendedRecyclerView.setEmptyView(_noStudentsView, _noStudentsTextView);
    }

    // DATA TO BE DISPLAY IN RECYCLERVIEW
    public void display() {
        id = new ArrayList<>();
        parentID = new ArrayList<>();
        attendanceItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(getContext());
        attendanceItems = displayData();
    }

    // GET DATA FROM DATABASE DEPEND ON THE PARENT'S ID
    private ArrayList<AttendanceItems> displayData() {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                + DataBaseHelper.TABLE_ATTENDANCE_TODAY + " WHERE "
                + DataBaseHelper.COLUMN_PARENT_ID + " = "
                + _parentID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_DATE_TODAY + " != '"
                + dateTimeDisplay.getText().toString() + "'", null);

        ArrayList<AttendanceItems> attendanceItems = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                attendanceItems.add(new AttendanceItems(
                        cursor.getString(8),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(7),
                        cursor.getInt(5),
                        cursor.getInt(6),
                        cursor.getInt(9)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return attendanceItems;
    }

    public void displayDate() {
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("EEEE - MMM d, yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);
    }

    // GET DATA FROM BOTTOM NAVI THE NEEDS to DISPLAY SPECIFIC DATA FROM EACH SUBJECT
    public void getDataFromBottomNaviActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            _parentID.setText(bundle.getString("IDParent"));
            _subjectCode.setText(bundle.getString("SubjectCode"));
            _sy.setText(bundle.getString("sy"));
            _course.setText(bundle.getString("CourseCode"));
        }
    }

    public void sortAttendance() {
        _sortAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.sort_student);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.sortAtoZ:
                                Collections.sort(attendanceItems, AttendanceItems.sortAtoZComparator);
                                initializeAdapter();
                                break;

                            case R.id.sortZtoA:
                                Collections.sort(attendanceItems, AttendanceItems.sortZtoAComparator);
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


    // SEARCH FUNCTION FOR LIST OF STUDENTS
    public void textListener() {

        _search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                attendanceAdapter.getFilter().filter(editable);

            }
        });
    }

    // AUTOMATIC SORT WHEN ACTIVITY OPEN
    public void automaticSort() {
        Collections.sort(attendanceItems, AttendanceItems.sortAtoZComparator);
        initializeAdapter();
    }

    @Override
    public void onNoteClick(int position) {

    }

    public void optionButton() {
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), optionButton);
                popupMenu.getMenuInflater().inflate(R.menu.option_attendance, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.exportAllCSV:
                                askForPermissions();
                                break;
                            case R.id.exportCSV:
                                askForPermissionsToday();
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

    public void askForPermissionsToday() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            downloadTodayAttendance();
        }
    }

    private void downloadTodayAttendance(){
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) {
            boolean mkdir = exportDir.mkdirs();

            if (!mkdir) {
                Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        }

        File file = new File(exportDir, _subjectCode.getText().toString() + "_Attendance_" + _course.getText().toString() + "_" + dateTimeDisplay.getText().toString() + ".csv");
        try {
            boolean createFile = file.createNewFile();
            if (!createFile) {

            }

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = {"Last name", "Date", "Present", "Absent"};

            Cursor cursor = db.rawQuery("SELECT * FROM "
                    + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                    + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "="
                    + _parentID.getText().toString() + " AND "
                    + DataBaseHelper.COLUMN_DATE_TODAY + " = '"
                    + dateTimeDisplay.getText().toString() + "'", null);


            csvWrite.writeNext(columns);
            while (cursor.moveToNext()) {
                //Which column you want to export
                String[] arrStr = {cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)};
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

        File file = new File(exportDir, _subjectCode.getText().toString() + "_Attendance_" + _course.getText().toString() + "_" + _sy.getText().toString() + ".csv");
        try {
            boolean createFile = file.createNewFile();
            if (!createFile) {

            }

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            String[] columns = {"Student number","Last name", "Date", "Present", "Absent", "late"};

            Cursor cursor = db.rawQuery("SELECT * FROM "
                    + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                    + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "="
                    + _parentID.getText().toString(), null);


            csvWrite.writeNext(columns);
            while (cursor.moveToNext()) {
                //Which column you want to export
                String[] arrStr = {cursor.getString(1),cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7)};
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

    public void presentToday() {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT COUNT(*) FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + _parentID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + " = "
                + "1" + " AND "
                + DataBaseHelper.COLUMN_DATE_ATTENDANCE + " ='"
                + dateTimeDisplay.getText().toString() + "'", null);

        if (cursor.moveToFirst()) {
            present.setText(String.valueOf(cursor.getInt(0)));
            cursor.close();
        }
    }

    public void absentToday() {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT COUNT(*) FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + _parentID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + " = "
                + "1" + " AND "
                + DataBaseHelper.COLUMN_DATE_ATTENDANCE + " ='"
                + dateTimeDisplay.getText().toString() + "'", null);

        if (cursor.moveToFirst()) {
            absent.setText(String.valueOf(cursor.getInt(0)));
            cursor.close();
        }
    }

    public void lateToday() {
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT COUNT(*) FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + _parentID.getText().toString() + " AND "
                + DataBaseHelper.COLUMN_LATE_ATTENDANCE + " = "
                + "1" + " AND "
                + DataBaseHelper.COLUMN_DATE_ATTENDANCE + " ='"
                + dateTimeDisplay.getText().toString() + "'", null);

        if (cursor.moveToFirst()) {
            _late.setText(String.valueOf(cursor.getInt(0)));
            cursor.close();
        }
    }

    public void updateAttendanceToday() {
        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        if(getActivity() == null)
                            return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                presentToday();
                                absentToday();
                                lateToday();
                            }
                        });
                    }
                } catch (InterruptedException ignored){
                }
            }
        };
        thread.start();
    }

    @Override
    public void updateAttendanceRecView() {
        initialize();
        getDataFromBottomNaviActivity();
        display();
        initializeAdapter();
        automaticSort();
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
                initializeAdapter();
                Log.d(tag, "" + spinnerGradingPeriod);
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
            spinnerGradingPeriod = "FInals";
        }
    }

}