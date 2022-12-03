package com.example.weclass.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageButton backButton;
    TextView taskType, parentID, _progress, _gradingPeriod, _examTextView, _dueTextView;
    EditText _score, _description, _taskNumber;
    Button _cancel, _create;
    String selectedTask, selectedProgress, selectedPeriod;
    SharedPreferences sharedPreferences = null;
    SharedPref sharedPref;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.67));
        getWindow().setBackgroundDrawableResource(R.drawable.dialogbox_bg);
        getWindow().setElevation(20);




        initialize();   // INITIALIZE ALL VIEWS
        backToTasks();  //  BACK TO RECORD ACTIVITY
        pickTask();     // TASK PICKER BUTTON
        setDate();      // DATE PICKER
        pickProgress(); // CHOOSE PROGRESS
        cancelButton(); // CANCEL BUTTON
        getDataFromStudentListFragment(); // SET THE ID FOR PARENT ID TEXT
        createTask();       // CREATE A TASK BUTTON
        pickGradingPeriod();
        examTrapping();
    }


    // INITIALIZE ALL VIEWS
    public void initialize() {
        backButton = findViewById(R.id.backButtonAddTasks);
        taskType = findViewById(R.id.taskTypeTextViewEdit);
        _score = findViewById(R.id.scoreEditTextEdit);
        _description = findViewById(R.id.descriptionEditTextEdit);
        _cancel = findViewById(R.id.cancelButtonTaskEdit);
        _create = findViewById(R.id.updateButtonTaskEdit);
        parentID = findViewById(R.id.parentIDTask);
        _progress = findViewById(R.id.progressTaskTextView);
        _taskNumber = findViewById(R.id.taskNumberEditText);
        _gradingPeriod = findViewById(R.id.gradingPeriodTextViewAddTask);
        _examTextView = findViewById(R.id.examTextView);
        _dueTextView = findViewById(R.id.dueTextViewAddTask);
    }


    // CREATE A TASK METHOD
    public void createTask() {
        _create.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           // IF ANY OF THE FIELDS IS EMPTY, AN ERROR WILL POP UP
                                           if (taskType.getText().toString().isEmpty() || _score.getText().toString().isEmpty()
                                                   || _taskNumber.getText().toString().isEmpty()
                                                   || _gradingPeriod.getText().toString().isEmpty() || _dueTextView.getText().toString().isEmpty()) {

                                               MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTask.this);
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
                                               DataBaseHelper dataBaseHelper = new DataBaseHelper(AddTask.this);
                                               SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

                                               Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                                       + DataBaseHelper.TABLE_MY_TASKS + " WHERE "
                                                       + DataBaseHelper.COLUMN_PARENT_ID_SUBJECT + " = "
                                                       + parentID.getText().toString() + " AND "
                                                       + DataBaseHelper.COLUMN_TASK_TYPE + " = '"
                                                       + taskType.getText().toString() + "' AND "
                                                       + DataBaseHelper.COLUMN_TASK_NUMBER + " = "
                                                       + _taskNumber.getText().toString() + " AND "
                                                       + DataBaseHelper.COLUMN_GRADING_PERIOD_TASK + " = '"
                                                       + _gradingPeriod.getText().toString() + "'", null);

                                               // DUPLICATE TASK TYPE AND TASK NUMBER IS NOT ALLOWED TO STORE
                                               if (cursor.moveToFirst()) {
                                                   Snackbar.make(_create, "" + taskType.getText().toString() + " "
                                                           + _taskNumber.getText().toString() + " is already in tasks list!", Snackbar.LENGTH_SHORT).show();
                                                   cursor.close ();

                                               } else {

                                                   // DATA WILL SAVE TO DATABASE IF ALL FIELDS ARE CORRECT
                                                   MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTask.this);
                                                   builder.setTitle("Please confirm");
                                                   builder.setIcon(R.drawable.ic_baseline_warning_24);
                                                   builder.setMessage("Are you sure all the information are correct?");
                                                   builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialog, int which) {


                                                           dataBaseHelper.addTask(parentID.getText().toString().trim(),
                                                                   taskType.getText().toString().trim(),
                                                                   _score.getText().toString().trim(),
                                                                   _description.getText().toString().trim(),
                                                                   _progress.getText().toString().trim(),
                                                                   _taskNumber.getText().toString().trim(),
                                                                   _gradingPeriod.getText().toString().trim(),
                                                                   _dueTextView.getText().toString().trim());

                                                           Snackbar.make(_create, "Task successfully created!", Snackbar.LENGTH_LONG).show();

                                                           taskType.setText("");
                                                           _score.setText("");
                                                           _description.setText("");
                                                           _taskNumber.setText("");
                                                           _gradingPeriod.setText("");
                                                           _dueTextView.setText("");
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

                                       }
                                   }
        );
    }


    // SEMESTER PICKER WILL OPEN WHEN PRESSED
    public void pickGradingPeriod() {
        final String[] gradingPeriod = new String[]{
                "Midterm",
                "Finals",

        };

        selectedPeriod = gradingPeriod[0];
        _gradingPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTask.this);
                builder.setTitle("Select grading period");
                builder.setSingleChoiceItems(gradingPeriod, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedPeriod = gradingPeriod[i];
                        _gradingPeriod.setText(selectedPeriod);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public void getDataFromStudentListFragment() {
        Intent intent = getIntent();
        String a = intent.getStringExtra("id");
        parentID.setText(a);
    }

    //  BACK TO RECORD ACTIVITY
    public void backToTasks() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // TASK PICKER
    public void pickTask() {
        final String[] tasks = new String[]{
                "Activity",
                "Assignment",
                "Seatwork",
                "Quiz",
                "Project",
                "Exam",
        };

        selectedTask = tasks[0];
        taskType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTask.this);
                builder.setTitle("Choose task");
                builder.setSingleChoiceItems(tasks, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedTask = tasks[i];
                        taskType.setText(selectedTask);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    // CHOOSE PROGRESS
    public void pickProgress() {
        final String[] progress = new String[]{
                "In-progress",
                "Completed",
        };

        selectedProgress = progress[0];
        _progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTask.this);
                builder.setTitle("Choose progress");
                builder.setSingleChoiceItems(progress, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedProgress = progress[i];
                        _progress.setText(selectedProgress);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    // DATE PICKER
    public void setDate() {
        _dueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DialogFragment datePicker = new DatePickerFragment();
//                datePicker.show(getSupportFragmentManager(), "date picker");

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                datePickerDialog = new DatePickerDialog(AddTask.this);

                datePickerDialog = new DatePickerDialog(AddTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Date date = calendar.getTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
                        String a = simpleDateFormat.format(date);
                        _dueTextView.setText(a);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        _dueTextView.setText(currentDate);



    }



    public void cancelButton() {
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTask.this);
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

    public void examTrapping(){
        if(taskType.getText().toString().equals("Exam")){
            _taskNumber.setText("1");
            _taskNumber.setEnabled(false);
        }else {
            _taskNumber.setEnabled(true);
        }
    }

}