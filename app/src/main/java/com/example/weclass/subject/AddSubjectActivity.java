package com.example.weclass.subject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import java.util.Calendar;

public class AddSubjectActivity extends AppCompatActivity {

    Button cancelButton, createButton;
    EditText courseEditText, subjectNameEditText, subjectCodeEditText;
    TextView dayTextView, timeTextView;
    int t1Hour, t1Minute;
    ImageButton backButton;
    String selectedDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize(); // INITIALIZE ALL VIEWS
        pickTime();     // TIME PICKER POP AFTER BUTTON CLICKED
        createButton(); // CREATE SUBJECT FUNCTION
        cancelButton(); // CANCEL BUTTON FUNCTION
        backButton();   // BACK BUTTON FUNCTION OF THE PHONE
        pickDate();     // DATE PICKER POP UP AFTER BUTTON CLICKED
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
        dayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddSubjectActivity.this);
                builder.setTitle("Select day");
                builder.setSingleChoiceItems(dayOfWeek, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedDay = dayOfWeek[i];
                        dayTextView.setText(selectedDay);
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
    }


    // Show dialog box when create button is pressed
    public void createButton() {

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseEditText.getText().toString().isEmpty() || subjectNameEditText.getText().toString().isEmpty() || subjectCodeEditText.getText().toString().isEmpty()
                        || dayTextView.getText().toString().isEmpty() || timeTextView.getText().toString().isEmpty())  {
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
                            dbh.addSubject(courseEditText.getText().toString().trim(),
                                    subjectCodeEditText.getText().toString().trim(),
                                    subjectNameEditText.getText().toString().trim(),
                                    dayTextView.getText().toString().trim(),
                                    timeTextView.getText().toString().trim());

                            Snackbar.make(createButton, "Subject successfully created!", Snackbar.LENGTH_LONG).show();

                            courseEditText.setText("");
                            subjectCodeEditText.setText("");
                            subjectNameEditText.setText("");
                            dayTextView.setText("");
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