package com.example.weclass.studentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weclass.AddSubjectActivity;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AddStudent extends AppCompatActivity {

    Button cancelButton, createButton;
    TextView genderTextview;
    EditText lastName, firstName, middleName;
    String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialized();          //INITIALIZED ALL VIEWS
        createStudent();    // CREATE STUDENT BUTTON
        chooseGender();     // GENDER BUTTON
        cancelButton();     // CANCEL BUTTON, BACK TO STUDENT LIST

    }
    public void initialized (){
        lastName = findViewById(R.id.studLastname);
        middleName = findViewById(R.id.studMidname);
        firstName = findViewById(R.id.studFirstname);
        genderTextview = findViewById(R.id.studGender);
        createButton = findViewById(R.id.createButtonStudent);
        cancelButton = findViewById(R.id.cancelButtonStudent); }

    public void chooseGender(){
        final String[] gender = new String[]{
                "Male",
                "Female"
        };

        selectedGender = gender[0];
        genderTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
                builder.setTitle("Select gender");
                builder.setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedGender = gender[i];
                        genderTextview.setText(selectedGender);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public void cancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
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

    public void createStudent (){
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastName.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || genderTextview.getText().toString().isEmpty()){
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
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
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
                    builder.setTitle("Please confirm");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Are you sure all the information are correct?");
                    builder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DataBaseHelper dbh = new DataBaseHelper(AddStudent.this);



                        }
                    });
                    builder.show();
            };

        };
    }
    );

}}