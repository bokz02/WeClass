package com.example.weclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditSubjectActivity extends AppCompatActivity implements SubjectAdapter.OnNoteListener{

    EditText _course, _subjectCode, _subjectName;
    TextView  _id, dayTextView, timeTextView;
    Button updateButton, cancelEditButton;
    int t1Hour, t1Minute;
    ImageButton backButton;
    String selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();
        displayData();
        updateData();
        pickTime();
        pickDate();
        cancelButton();
        backButton();
    }

    // Open time picker when PRESSED
    public void pickTime() {
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditSubjectActivity.this, R.style.Theme_TimeDialog, new TimePickerDialog.OnTimeSetListener() {
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

    //Back button of the phone

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditSubjectActivity.this, Subject.class);
        startActivity(intent);
    }

    //ToDo: FIXED THE BACK BUTTON, IT SHOULDN'T BE INTENT. IT SHOULD BE startActivityForResult BASED ON STACKOVERFLOW
    public void backButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditSubjectActivity.this, Subject.class);
                startActivity(intent);
            }
        });
    }       // Back button

    public void updateData(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dbh = new DataBaseHelper(EditSubjectActivity.this);
                dbh.updateData(
                        _id.getText().toString().trim(),
                        _course.getText().toString().trim(),
                        _subjectCode.getText().toString().trim(),
                        _subjectName.getText().toString().trim(),
                        dayTextView.getText().toString().trim(),
                        timeTextView.getText().toString().trim());

                Snackbar.make(updateButton, "Subject successfully updated!", Snackbar.LENGTH_LONG).show();

            }
        });
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this);
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

    public void initialize(){

        _id  = findViewById(R.id.idNumber);
        _course = findViewById(R.id.courseNameText2);
        _subjectCode = findViewById(R.id.subjectCodeText2);
        _subjectName = findViewById(R.id.subjectNameText2);
        dayTextView = findViewById(R.id.dayTextViewEditSubject);
        timeTextView = findViewById(R.id.timeTextViewEditSubject);
        updateButton = findViewById(R.id.updateButtonSubject);
        cancelEditButton = findViewById(R.id.cancelButtonEditSubject);
        backButton = findViewById(R.id.backButtonEditSubject);
    }

    public void displayData(){
        if (getIntent().getBundleExtra("Userdata") != null){
            Bundle bundle = getIntent().getBundleExtra("Userdata");


            _id.setText(bundle.getString("id"));
            _course.setText(bundle.getString("course"));
            _subjectCode.setText(bundle.getString("subject_code"));
            _subjectName.setText(bundle.getString("subject_name"));
            dayTextView.setText(bundle.getString("day"));
            timeTextView.setText(bundle.getString("time"));
        }
    }

    public void cancelButton() {
        cancelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this);
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


    @Override
    public void onNoteClick(int position) {

    }
}