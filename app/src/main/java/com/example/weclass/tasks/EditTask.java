package com.example.weclass.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.example.weclass.studentlist.EditStudent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Calendar;

public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText _score, _description, _taskNumber;
    TextView _progress, _taskType, _dueDate, _idTask;
    String selectedTask, selectedProgress;
    Button _cancel, _update;
    ImageButton _backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();
        pickTask();
        pickProgress();
        setDate();
        displayData();
        updateData();
        backButton();
        cancelButton();
    }

    public void initialize(){
        _score = findViewById(R.id.scoreEditTextEdit);
        _description = findViewById(R.id.descriptionEditTextEdit);
        _taskNumber = findViewById(R.id.taskNumberEditTextEdit);
        _progress = findViewById(R.id.progressEditTextEdit);
        _taskType = findViewById(R.id.taskTypeTextViewEdit);
        _dueDate = findViewById(R.id.dateTextViewTaskEdit);
        _idTask = findViewById(R.id.idTaskEdit);
        _cancel = findViewById(R.id.cancelButtonTaskEdit);
        _update = findViewById(R.id.updateButtonTaskEdit);
        _backButton = findViewById(R.id.backButtonAddTasksEdit);
    }

    // GET ALL DATA IN VIES AND INSERT TO DATABASE
    public void updateData(){
        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dbh = new DataBaseHelper(EditTask.this);
                dbh.updateTask(
                        _idTask.getText().toString().trim(),
                        _taskType.getText().toString().trim(),
                        _dueDate.getText().toString().trim(),
                        _score.getText().toString().trim(),
                        _description.getText().toString().trim(),
                        _progress.getText().toString().trim(),
                        _taskNumber.getText().toString().trim());

                Snackbar.make(_update, "Task information successfully updated!", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    //  GET THE DATA THAT PASS TO HERE, FROM THE ADAPTER
    public void displayData(){
        if (getIntent().getBundleExtra("Task") != null){
            Bundle bundle = getIntent().getBundleExtra("Task");

            _idTask.setText(bundle.getString("task_id"));
            _score.setText(bundle.getString("task_score"));
            _description.setText(bundle.getString("task_description"));
            _taskNumber.setText(bundle.getString("task_number"));
            _taskType.setText(bundle.getString("task_type"));
            _dueDate.setText(bundle.getString("task_date"));
            _progress.setText(bundle.getString("task_progress"));

        }
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
        _taskType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditTask.this);
                builder.setTitle("Choose task");
                builder.setSingleChoiceItems(tasks, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedTask = tasks[i];
                        _taskType.setText(selectedTask);
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
                "To-Do",
                "Done",
        };

        selectedProgress = progress[0];
        _progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditTask.this);
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
        _dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    // IMPLEMENTS ON DATE PICKER DIALOG
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        _dueDate.setText(currentDate);
    }

    public void cancelButton() {
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditTask.this);
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

    public void backButton(){
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}