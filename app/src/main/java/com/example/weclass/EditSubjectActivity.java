package com.example.weclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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

    EditText _course, _subjectCode, _subjectName, _dayEditText, _timeEditText;
    TextView  _id;
    Button updateButton, cancelEditButton;
    int t1Hour, t1Minute;
    ImageButton _time, _day, backButton;
    String selectedDay;
    SubjectAdapter subjectAdapter;
    ArrayList<SubjectItems> subjectItemsA = new ArrayList<>();
    RecyclerView recyclerView;

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
        _time.setOnClickListener(new View.OnClickListener() {
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
                        _timeEditText.setText(time);

                    }
                }, 12, 0, false
                );
                timePickerDialog.updateTime(t1Hour, t1Minute);
                timePickerDialog.show();
            }
        });
    }



    // BACK BUTTON OF THE PHONE
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditSubjectActivity.this, Subject.class);
        startActivity(intent);
    }

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
                        _dayEditText.getText().toString().trim(),
                        _timeEditText.getText().toString().trim());

                Snackbar.make(updateButton, "Subject successfully created!", Snackbar.LENGTH_LONG).show();

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
        _day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this);
                builder.setTitle("Select day");
                builder.setSingleChoiceItems(dayOfWeek, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedDay = dayOfWeek[i];
                        _dayEditText.setText(selectedDay);
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
        _dayEditText = findViewById(R.id.dayTextViewEditSubject);
        _timeEditText = findViewById(R.id.timeTextViewEditSubject);
        _day = findViewById(R.id.dateImageButton2);
        _time = findViewById(R.id.timeImageButton2);
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
            _dayEditText.setText(bundle.getString("day"));
            _timeEditText.setText(bundle.getString("time"));
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