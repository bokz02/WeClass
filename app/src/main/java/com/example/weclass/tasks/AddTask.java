package com.example.weclass.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.DatePickerFragment;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageButton backButton;
    TextView taskType, _date, parentID, _progress;
    EditText _score, _description, _taskNumber;
    Button _cancel, _create;
    String selectedTask, selectedProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();   // INITIALIZE ALL VIEWS
        backToTasks();  //  BACK TO RECORD ACTIVITY
        pickTask();     // TASK PICKER BUTTON
        setDate();      // DATE PICKER
        pickProgress(); // CHOOSE PROGRESS
        cancelButton(); // CANCEL BUTTON
        getDataFromStudentListFragment(); // SET THE ID FOR PARENT ID TEXT
        createTask();
    }


    // INITIALIZE ALL VIEWS
    public void initialize() {
        backButton = findViewById(R.id.backButtonAddTasks);
        taskType = findViewById(R.id.taskTypeTextViewEdit);
        _score = findViewById(R.id.scoreEditTextEdit);
        _description = findViewById(R.id.descriptionEditTextEdit);
        _cancel = findViewById(R.id.cancelButtonTaskEdit);
        _create = findViewById(R.id.updateButtonTaskEdit);
        _date = findViewById(R.id.dateTextViewTaskEdit);
        parentID = findViewById(R.id.parentIDTask);
        _progress = findViewById(R.id.progressTaskEditText);
        _taskNumber = findViewById(R.id.taskNumberEditText);
    }

    public void createTask() {
        _create.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (taskType.getText().toString().isEmpty() || _date.getText().toString().isEmpty() || _score.getText().toString().isEmpty()
                                                   || _description.getText().toString().isEmpty()) {
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
                                               MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddTask.this);
                                               builder.setTitle("Please confirm");
                                               builder.setIcon(R.drawable.ic_baseline_warning_24);
                                               builder.setMessage("Are you sure all the information are correct?");
                                               builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {

                                                       DataBaseHelper dbh = new DataBaseHelper(AddTask.this);
                                                       dbh.addTask(parentID.getText().toString().trim(),
                                                               taskType.getText().toString().trim(),
                                                               _date.getText().toString().trim(),
                                                               _score.getText().toString().trim(),
                                                               _description.getText().toString().trim(),
                                                               _progress.getText().toString().trim(),
                                                               _taskNumber.getText().toString().trim());

                                                       Snackbar.make(_create, "Task successfully created!", Snackbar.LENGTH_LONG).show();
                                                       taskType.setText("");
                                                       _date.setText("");
                                                       _score.setText("");
                                                       _description.setText("");
                                                       _progress.setText("");
                                                       _taskNumber.setText("");

                                                   }
                                               });
                                               builder.show();
                                           }
                                           ;

                                       }

            ;
                                   }
        );

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
        _date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
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

        _date.setText(currentDate);
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

    // TO MOVE VIEWS DYNAMICALLY
    public void moveEditText() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintParent);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.taskNumberEditText, ConstraintSet.LEFT, R.id.taskTypeTextViewEdit, ConstraintSet.RIGHT, 5);
        constraintSet.connect(R.id.taskNumberEditText, ConstraintSet.TOP, R.id.taskTypeTextViewEdit, ConstraintSet.TOP, 5);
        constraintSet.applyTo(constraintLayout);
    }
}