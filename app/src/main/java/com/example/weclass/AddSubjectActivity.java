package com.example.weclass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class AddSubjectActivity extends AppCompatActivity {

    Button cancelButton, createButton;
    TextView course, subjectName, subjectCode, dateTextView, timeTextView;
    int t1Hour, t1Minute;
    ImageButton timeButton, dateButton;
    String selectedDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();
        pickTime();
        createButton();
        cancelButton();
        backButton();
        pickDate();
    }

    // DAY PICKER WILL OPEN WHEN PRESSED
    public void pickDate() {
        final String[] dayOfWeek = new String[]{
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
        };

        selectedDay = dayOfWeek[0];
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select day");
                builder.setSingleChoiceItems(dayOfWeek, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedDay = dayOfWeek[i];
                        dateTextView.setText(selectedDay);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    // Open time picker when PRESSED
    public void pickTime() {
        timeButton.setOnClickListener(new View.OnClickListener() {
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


    public void backButton() {
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButtonSubject);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSubjectActivity.this, Subject.class);
                startActivity(intent);
            }
        });
    }       // Back button

    //Initialize all the textview and button
    public void initialize() {
        dateButton = findViewById(R.id.dateImageButton);
        timeButton = findViewById(R.id.timeImageButton);
        createButton = findViewById(R.id.createButtonSubject);
        course = findViewById(R.id.courseNameText);
        subjectCode = findViewById(R.id.subjectCodeText);
        subjectName = findViewById(R.id.subjectNameText);
        dateTextView = findViewById(R.id.dayAddSubject);
        timeTextView = findViewById(R.id.timeAddSubject);
        cancelButton = findViewById(R.id.cancelButtonSubject);
    }


    // Show dialog box when create button is pressed
    public void createButton() {

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (course.getText().toString().isEmpty() || subjectName.getText().toString().isEmpty() || subjectCode.getText().toString().isEmpty()
                        || dateTextView.getText().toString().isEmpty() || timeTextView.getText().toString().isEmpty())  {
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
                            dbh.addSubject(course.getText().toString().trim(),
                                    subjectCode.getText().toString().trim(),
                                    subjectName.getText().toString().trim(),
                                    dateTextView.getText().toString().trim(),
                                    timeTextView.getText().toString().trim());

                            Snackbar.make(createButton, "Subject successfully created!", Snackbar.LENGTH_LONG).show();

                            course.setText("");
                            subjectCode.setText("");
                            subjectName.setText("");
                            dateTextView.setText("");
                            timeTextView.setText("");
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
}