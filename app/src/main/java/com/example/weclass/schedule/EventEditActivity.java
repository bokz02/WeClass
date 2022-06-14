package com.example.weclass.schedule;


import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import java.time.LocalTime;
import java.util.Calendar;

public class EventEditActivity extends AppCompatActivity implements EventAdapter.OnNoteListener {
    private Button createEvent, cancelEvent;
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    int t1Hour, t1Minute;
    private LocalTime time;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen


        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
//        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
        pickTime();
        createEvent();
        cancelEvent();
        backButton();

    }

    // BACK BUTTON
    public void backButton(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    public void pickTime() {
        eventTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditActivity.this, R.style.Theme_TimeDialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        t1Hour = hourOfDay;
                        t1Minute = minutes;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, t1Hour, t1Minute);
                        SimpleDateFormat format = new SimpleDateFormat("h:mm aa");
                        String time = format.format(calendar.getTime());
                        eventTimeTV.setText(time);

                    }
                }, 12, 0, false
                );
                timePickerDialog.updateTime(t1Hour, t1Minute);
                timePickerDialog.show();
            }
        });
    }


    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameText);
        eventDateTV = findViewById(R.id.eventDateText);
        eventTimeTV = findViewById(R.id.eventTimeText);
        createEvent = findViewById(R.id.createEvent);
        cancelEvent = findViewById(R.id.cancelButtonEvent);
        backButton = findViewById(R.id.backButtonEventEdit);

    }


    public void createEvent() {

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventNameET.getText().toString().isEmpty() || eventDateTV.getText().toString().isEmpty() || eventTimeTV.getText().toString().isEmpty()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EventEditActivity.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Don't leave empty fields!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }

                    });
                    builder.show();
                } else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EventEditActivity.this);
                    builder.setTitle("Please confirm");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Are you sure all the information are correct?");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            DataBaseHelper dbh = new DataBaseHelper(EventEditActivity.this);
                            dbh.addSchedule(eventNameET.getText().toString().trim(),
                                    eventTimeTV.getText().toString().trim(),
                                    eventDateTV.getText().toString().trim());
                            Snackbar.make(createEvent, "Subject successfully created!", Snackbar.LENGTH_LONG).show();
                            eventNameET.setText("");
                            eventTimeTV.setText("");
                            eventDateTV.setText("");

                        }
                    });
                    builder.show();
                }
            }


        });

    }

    public void cancelEvent()
    {
        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EventEditActivity.this);
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
