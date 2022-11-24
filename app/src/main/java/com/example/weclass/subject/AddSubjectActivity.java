package com.example.weclass.subject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AddSubjectActivity extends AppCompatActivity {

    Button cancelButton, createButton;
    EditText courseEditText, subjectNameEditText, subjectCodeEditText, roomEditText;
    TextView dayTextView, timeTextView, timeEndTextView, schoolYearTextView, semesterTextView, colorTextView, _classType, _section;
    int t1Hour, t1Minute;
    ImageButton backButton;
    String selectedDay, selectedSem,selectedSchoolYear, selectedSection, selectedClassType;
    int index;
    List<String> colors = new ArrayList<>();
    private List<String> daysSelected;
    SharedPreferences sharedPreferences = null;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);



        initialize(); // INITIALIZE ALL VIEWS
        pickTime();     // TIME PICKER POP AFTER BUTTON CLICKED
        pickEndTime();  // TIME PICKER POP AFTER BUTTON CLICKED
        createButton(); // CREATE SUBJECT FUNCTION
        cancelButton(); // CANCEL BUTTON FUNCTION
        backButton();   // BACK BUTTON FUNCTION OF THE PHONE
        //pickDate();     // DATE PICKER POP UP AFTER BUTTON CLICKED
        pickDay();
        pickSemester();
        pickSchoolYear();
        randomColor();
        pickSection();
        pickClassType();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.67));
        getWindow().setBackgroundDrawableResource(R.drawable.dialogbox_bg);
        getWindow().setElevation(20);

    }



    //DAY PICKER
    public void pickDay(){
        dayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                daysSelected = new ArrayList<>();
                String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select day");
                builder.setMultiChoiceItems(daysOfWeek, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {

                        if (isChecked)
                        {
                            daysSelected.add(daysOfWeek[i]);
                        }
                        else daysSelected.remove(daysOfWeek[i]);
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder data = new StringBuilder();

                        for (String a:daysSelected){
                            data.append(a).append("/");
                        }
                        String b = data.deleteCharAt(data.length() - 1 ).toString();
                        dayTextView.setText(b);

//                        Toast.makeText(AddSubjectActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("Cancel",null);

                builder.create();
                builder.show();

            }
        });

    }


    // SEMESTER PICKER WILL OPEN WHEN PRESSED
    public void pickSemester() {
        final String[] semester = new String[]{
                "1st Semester",
                "2nd Semester",

        };

        selectedSem = semester[0];
        semesterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select day");
                builder.setSingleChoiceItems(semester, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSem = semester[i];
                        semesterTextView.setText(selectedSem);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    // SEMESTER PICKER WILL OPEN WHEN PRESSED
    public void pickSchoolYear() {
        final String[] schoolYear = new String[]{
                "SY 2022 - 2023",
                "SY 2023 - 2024",
                "SY 2024 - 2025",
                "SY 2025 - 2026",
                "SY 2026 - 2027",
                "SY 2027 - 2028",
                "SY 2028 - 2029",
                "SY 2029 - 2030",

        };

        selectedSchoolYear = schoolYear[0];
        schoolYearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select school year");
                builder.setSingleChoiceItems(schoolYear, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSchoolYear = schoolYear[i];
                        schoolYearTextView.setText(selectedSchoolYear);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    // Open time picker when PRESSED
    public void pickTime() {
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddSubjectActivity.this, R.style.Theme_TimeDialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        t1Hour = hourOfDay;
                        t1Minute = minutes;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, t1Hour, t1Minute);
                        SimpleDateFormat format = new SimpleDateFormat("h:mm aa");
                        String time = format.format(calendar.getTime());
                        timeTextView.setText(time);

                    }
                }, 12, 0, false
                );
                timePickerDialog.updateTime(t1Hour, t1Minute);
                timePickerDialog.show();
            }
        });
    }

    // Open time picker when PRESSED
    public void pickEndTime() {
        timeEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddSubjectActivity.this, R.style.Theme_TimeDialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        t1Hour = hourOfDay;
                        t1Minute = minutes;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, t1Hour, t1Minute);
                        SimpleDateFormat format = new SimpleDateFormat("h:mm aa");
                        String time = format.format(calendar.getTime());
                        timeEndTextView.setText(time);

                    }
                }, 12, 0, false
                );
                timePickerDialog.updateTime(t1Hour, t1Minute);
                timePickerDialog.show();
            }
        });
    }

    // BACK BUTTON FUNCTION OF THE PHONE
    public void backButton() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }       // Back button


    // BACK BUTTON OF THE PHONE
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddSubjectActivity.this, Subject.class);
        startActivity(intent);
    }

    //Initialize all the textview and button
    public void initialize() {
        createButton = findViewById(R.id.createButtonSubject);
        courseEditText = findViewById(R.id.courseNameText);
        subjectCodeEditText = findViewById(R.id.subjectCodeText);
        subjectNameEditText = findViewById(R.id.subjectNameText);
        dayTextView = findViewById(R.id.dayAddSubject);
        timeTextView = findViewById(R.id.timeAddSubject);
        cancelButton = findViewById(R.id.cancelButtonSubject);
        backButton = findViewById(R.id.backButtonSubject);
        timeEndTextView = findViewById(R.id.timeEndAddSubject);
        semesterTextView = findViewById(R.id.semesterAddSubject);
        schoolYearTextView = findViewById(R.id.schoolYearAddSubject);
        colorTextView = findViewById(R.id.colorHexTextView);
        roomEditText = findViewById(R.id.roomNumberEditTextAddSubject);
        _section = findViewById(R.id.sectionTextViewAddSubject);
        _classType = findViewById(R.id.classTypeTextViewAddSubject);

    }


    // Show dialog box when create button is pressed
    public void createButton() {

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseEditText.getText().toString().isEmpty() || subjectNameEditText.getText().toString().isEmpty() || subjectCodeEditText.getText().toString().isEmpty()
                        || dayTextView.getText().toString().isEmpty() || timeTextView.getText().toString().isEmpty() || timeEndTextView.getText().toString().isEmpty()
                        || semesterTextView.getText().toString().isEmpty() || schoolYearTextView.getText().toString().isEmpty())  {

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Don't leave empty fields!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }

                    });
                    builder.show();
                }

                //IF FIELDS ARE FILLED, IT WILL ADD TO DATABASE
                else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                    builder.setTitle("Please confirm");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Are you sure all the information are correct?");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DataBaseHelper dbh = new DataBaseHelper(AddSubjectActivity.this);
                            SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();

                            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                    + DataBaseHelper.TABLE_MY_SUBJECTS + " WHERE "
                                    + DataBaseHelper.COLUMN_SUBJECT_CODE + " = '"
                                    + subjectCodeEditText.getText().toString().trim() + "'" + " collate nocase " + " AND "
                                    + DataBaseHelper.COLUMN_SUBJECT_NAME + " = '"
                                    + subjectNameEditText.getText().toString().trim() + "'" + " collate nocase " + " AND "
                                    + DataBaseHelper.COLUMN_COURSE + " = '"
                                    + courseEditText.getText().toString().trim() + "' AND "
                                    + DataBaseHelper.COLUMN_SUBJECT_SECTION + " ='"
                                    + _section.getText().toString() + "'",  null);


                            if(cursor.moveToFirst()){
                                Snackbar.make(createButton, "" + courseEditText.getText().toString() + ""+ _section.getText().toString() +", " + subjectCodeEditText.getText().toString() + " is already in your subject list!", Snackbar.LENGTH_LONG).show();
                                cursor.close();

                            }else {

                                Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM "
                                        + DataBaseHelper.TABLE_MY_SUBJECTS + " WHERE "
                                        + DataBaseHelper.COLUMN_TIME + " = '"
                                        + timeTextView.getText().toString().trim() + "' AND "
                                        + DataBaseHelper.COLUMN_TIME_END + " AND '"
                                        + timeEndTextView.getText().toString().trim() + "' AND "
                                        + DataBaseHelper.COLUMN_DAY + " = '"
                                        + dayTextView.getText().toString().trim() + "'",  null);

                                if(c.moveToFirst()){
                                    Snackbar.make(createButton, "You already created a subject with that schedule.", Snackbar.LENGTH_LONG).show();
                                    c.close();

                                }else {

                                    dbh.addSubject(courseEditText.getText().toString().trim(),
                                            subjectCodeEditText.getText().toString().trim(),
                                            subjectNameEditText.getText().toString().trim(),
                                            dayTextView.getText().toString().trim(),
                                            timeTextView.getText().toString().trim(),
                                            timeEndTextView.getText().toString().trim(),
                                            semesterTextView.getText().toString().trim(),
                                            schoolYearTextView.getText().toString().trim(),
                                            colorTextView.getText().toString().trim(),
                                            roomEditText.getText().toString().trim(),
                                            _section.getText().toString().trim(),
                                            _classType.getText().toString().trim());

                                    Snackbar.make(createButton, "" + subjectCodeEditText.getText().toString() + " successfully created!", Snackbar.LENGTH_LONG).show();

                                    courseEditText.setText("");
                                    subjectCodeEditText.setText("");
                                    subjectNameEditText.setText("");
                                    dayTextView.setText("");
                                    timeTextView.setText("");
                                    timeEndTextView.setText("");
                                    semesterTextView.setText("");
                                    schoolYearTextView.setText("");
                                    roomEditText.setText("");
                                    _section.setText("");
                                    _classType.setText("");

                                    index = new Random().nextInt(colors.size());
                                    colorTextView.setText(String.valueOf(colors.get(index)));

                                }
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
    }


    // Show dialog box when cancel button is pressed
    public void cancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Confirm exit");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("All the fields will not be saved!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();

                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });
    }


    public void randomColor(){
        colors = new ArrayList<>();



        colors.add("#FF968A");
        colors.add("#FFC9A2");
        colors.add("#97C1A9");
        colors.add("#55CBCD");
        colors.add("#FFC8A2");
        colors.add("#E0A096");
        colors.add("#B2CFA5");
        colors.add("#99A399");
        colors.add("#BA94D1");
        colors.add("#90A17D");
        colors.add("#9ED2C6");
        colors.add("#9A86A4");
        colors.add("#886F6F");

        index = new Random().nextInt(colors.size());
        colorTextView.setText(String.valueOf(colors.get(index)));
    }

    // SECTION PICKER WILL OPEN WHEN PRESSED
    public void pickSection() {
        final String[] section = new String[]{
                "A",
                "B",
                "C",
                "D",
                "E",

        };

        selectedSection = section[0];
        _section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select section");
                builder.setSingleChoiceItems(section, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSection = section[i];
                        _section.setText(selectedSection);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    // CLASS PICKER WILL OPEN WHEN PRESSED
    public void pickClassType() {
        final String[] classType = new String[]{
                "Lecture",
                "Laboratory",


        };

        selectedClassType = classType[0];
        _classType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select class type");
                builder.setSingleChoiceItems(classType, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedClassType = classType[i];
                        _classType.setText(selectedClassType);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}