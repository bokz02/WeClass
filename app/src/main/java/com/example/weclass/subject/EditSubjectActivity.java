package com.example.weclass.subject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import java.util.List;

public class EditSubjectActivity extends AppCompatActivity {

    EditText _course, _subjectCode, _subjectName, _room;
    TextView  _id, dayTextView, timeTextView, timeEndTextView, semesterTextView, schoolYearTextView, _section, _classType;
    Button updateButton, cancelEditButton;
    int t1Hour, t1Minute;
    ImageButton backButton;
    String selectedSem, selectedSy, selectedSection, selectedClassType;
    private List<String> daysSelected;
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
        setContentView(R.layout.activity_edit_subject);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.67));
        getWindow().setBackgroundDrawableResource(R.drawable.dialogbox_bg);
        getWindow().setElevation(20);

        initialize();
        displayData();
        updateData();
        pickTime();
        pickDate();
        pickEndTime();
        cancelButton();
        backButton();
        pickSemester();
        pickSchoolYear();
        pickSection();
        pickClassType();
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


    public void backButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }       // Back button

    public void updateData(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dbh = new DataBaseHelper(EditSubjectActivity.this);
                dbh.updateSubject(
                        _id.getText().toString().trim(),
                        _course.getText().toString().trim(),
                        _subjectCode.getText().toString().trim(),
                        _subjectName.getText().toString().trim(),
                        dayTextView.getText().toString().trim(),
                        timeTextView.getText().toString().trim(),
                        timeEndTextView.getText().toString().trim(),
                        semesterTextView.getText().toString().trim(),
                        schoolYearTextView.getText().toString().trim(),
                        _room.getText().toString().trim(),
                        _section.getText().toString().trim(),
                        _classType.getText().toString().trim());

                Snackbar.make(updateButton, "Subject successfully updated!", Snackbar.LENGTH_LONG).show();


            }
        });
    }

    // Open time picker when PRESSED
    public void pickEndTime() {
        timeEndTextView.setOnClickListener(new View.OnClickListener() {
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
                        timeEndTextView.setText(time);

                    }
                }, 12, 0, false
                );
                timePickerDialog.updateTime(t1Hour, t1Minute);
                timePickerDialog.show();
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this, R.style.CustomMaterialDialog);
                builder.setTitle("Select day");
                builder.setSingleChoiceItems(semester, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSem = semester[i];

                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
        };

        selectedSy = schoolYear[0];
        schoolYearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this, R.style.CustomMaterialDialog);
                builder.setTitle("Select school year");
                builder.setSingleChoiceItems(schoolYear, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSy = schoolYear[i];

                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        schoolYearTextView.setText(selectedSy);
                        dialogInterface.dismiss();
                    }
                });

                // add custom school year
                builder.setNegativeButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MaterialAlertDialogBuilder b = new MaterialAlertDialogBuilder(EditSubjectActivity.this, R.style.CustomMaterialDialog);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.add_sy_edit_text, null);
                        EditText editText= (EditText) dialogView.findViewById(R.id.editTextAddSy);
                        b.setView(dialogView);
                        b.setTitle("Enter school year");
                        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        // override the positive button to not dismiss the dialog if edit text is empty
                        AlertDialog alertDialog = b.create();
                        alertDialog.show();
                        Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String sy = editText.getText().toString();
                                if (sy.length()==0){
                                    Toast.makeText(EditSubjectActivity.this, "Do not leave the field blank" , Toast.LENGTH_SHORT).show();

                                }else {
                                    schoolYearTextView.setText(sy);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                builder.show();
            }
        });
    }

    // DAY PICKER WILL OPEN WHEN PRESSED
    public void pickDate() {
        dayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                daysSelected = new ArrayList<>();
                String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this, R.style.CustomMaterialDialog);
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

                        // "/" is place as separator when multiple day is selected
                        for (String a:daysSelected){
                            data.append(a).append("/");
                        }
                        String b = data.deleteCharAt(data.length() - 1 ).toString();
                        dayTextView.setText(b);

                    }
                });

                builder.setNegativeButton("Cancel",null);

                builder.create();
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
        timeEndTextView = findViewById(R.id.timeEndTextViewEditSubject);
        semesterTextView = findViewById(R.id.semesterTextViewEditSubject);
        schoolYearTextView = findViewById(R.id.schoolYearTextViewEditSubject);
        _room = findViewById(R.id.roomNumberEditTextEditSubject);
        _section = findViewById(R.id.sectionTextViewEditSubject);
        _classType = findViewById(R.id.classTypeTextViewEditSubject);

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
            timeEndTextView.setText(bundle.getString("timeEnd"));
            semesterTextView.setText(bundle.getString("sem"));
            schoolYearTextView.setText(bundle.getString("sy"));
            _room.setText(bundle.getString("room"));
            _section.setText(bundle.getString("section"));
            _classType.setText(bundle.getString("class_type"));
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

    // SECTION PICKER WILL OPEN WHEN PRESSED
    public void pickSection() {
        final String[] section = new String[]{
                "A",
                "B",
                "C",
                "None",
        };

        selectedSection = section[0];
        _section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this, R.style.CustomMaterialDialog);
                builder.setTitle("Select section");
                builder.setSingleChoiceItems(section, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSection = section[i];
                        _section.setText(selectedSection);
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        _section.setText(selectedSection);
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditSubjectActivity.this, R.style.CustomMaterialDialog);
                builder.setTitle("Select class type");
                builder.setSingleChoiceItems(classType, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedClassType = classType[i];
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        _classType.setText(selectedClassType);
                    }
                });

                builder.show();
            }
        });
    }
}