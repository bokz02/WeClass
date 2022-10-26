package com.example.weclass.subject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddSubjectActivity extends AppCompatActivity {

    Button cancelButton, createButton;
    EditText courseEditText, subjectNameEditText, subjectCodeEditText, roomEditText;
    TextView dayTextView, timeTextView, timeEndTextView, schoolYearTextView, semesterTextView, colorTextView;
    int t1Hour, t1Minute;
    ImageButton backButton;
    String selectedDay, selectedSem, selectedSchoolYear;
    int index;
    List<String> colors = new ArrayList<>();
    private List<String> daysSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                String[] daysOfWeek = {"Monday ", "Tuesday", "Wednesday ", "Thursday ", "Friday "};

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select day");
                builder.setMultiChoiceItems(daysOfWeek, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        String selected[] = daysOfWeek;

                        if (isChecked)
                        {
                            daysSelected.add(selected[i]);
                        }
                        else if (daysSelected.contains(selected[i]))
                        {
                            daysSelected.remove(selected[i]);
                        }
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String data = "";
                        for (String item:daysSelected)
                        {
                            data = data +" "+ item;
                        }
//                        Toast.makeText(AddSubjectActivity.this, data, Toast.LENGTH_SHORT).show();
                        dayTextView.setText(data);
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
                builder.setTitle("Select day");
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
                                    + subjectCodeEditText.getText().toString().trim() + "' AND "
                                    + DataBaseHelper.COLUMN_SUBJECT_NAME + " = '"
                                    + subjectNameEditText.getText().toString().trim() + "' AND "
                                    + DataBaseHelper.COLUMN_COURSE + " = '"
                                    + courseEditText.getText().toString().trim() + "'",  null);


                            if(cursor.moveToFirst()){
                                Snackbar.make(createButton, "" + courseEditText.getText().toString() + "," + subjectCodeEditText.getText().toString() + " is already in your subject list!", Snackbar.LENGTH_LONG).show();
                                cursor.close();

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
                                        roomEditText.getText().toString().trim());

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

                                index = new Random().nextInt(colors.size());
                                colorTextView.setText(String.valueOf(colors.get(index)));

                                //cursor.close();

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
        colors.add("#DADADA");

        colors.add("#E0A096");
        colors.add("#B2CFA5");
        colors.add("#99A399");



        index = new Random().nextInt(colors.size());
        colorTextView.setText(String.valueOf(colors.get(index)));
    }
}