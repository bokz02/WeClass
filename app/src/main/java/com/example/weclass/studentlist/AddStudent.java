package com.example.weclass.studentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class AddStudent extends AppCompatActivity {

    Button cancelButton, createButton;
    ImageButton backButton;
    TextView genderTextview, parentID, _present, _absent, _date;
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
        backToStudentList();    // BACK TO STUDENT LIST ACTIVITY
        getDataFromStudentListFragment();
    }

    public void getDataFromStudentListFragment(){
        Intent intent = getIntent();
        String a = intent.getStringExtra("id");
        parentID.setText(a);
    }

    public void initialized (){
        lastName = findViewById(R.id.studLastnameProfile);
        middleName = findViewById(R.id.studMidname);
        firstName = findViewById(R.id.studFirstnameProfile);
        genderTextview = findViewById(R.id.studGender);
        createButton = findViewById(R.id.createButtonStudent);
        cancelButton = findViewById(R.id.cancelButtonStudent);
        backButton = findViewById(R.id.backButtonAddStudent);
        parentID = findViewById(R.id.parentIDAddStudent);
        _absent = findViewById(R.id.absentIDAddStudent);
        _present = findViewById(R.id.presentIDAddStudent);

    }

    public void backToStudentList(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

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

                            DataBaseHelper db = new DataBaseHelper(AddStudent.this);
                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                            Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                                    + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                                    + DataBaseHelper.COLUMN_PARENT_ID + " = "
                                    + parentID.getText().toString().trim() + " AND "
                                    + DataBaseHelper.COLUMN_LAST_NAME + " = '"
                                    + lastName.getText().toString().trim() + "' AND "
                                    + DataBaseHelper.COLUMN_FIRST_NAME + " = '"
                                    + firstName.getText().toString().trim() + "'", null);


                            if(cursor.moveToFirst()){
                                Snackbar.make(createButton, "" + lastName.getText().toString() + ", "
                                        + firstName.getText().toString() + " is already added in this subject.", Snackbar.LENGTH_SHORT).show();
                                cursor.close();


                            }else {

                                DataBaseHelper dbh = new DataBaseHelper(AddStudent.this);
                                dbh.addStudent(parentID.getText().toString().trim(),
                                        lastName.getText().toString().trim(),
                                        firstName.getText().toString().trim(),
                                        middleName.getText().toString().trim(),
                                        genderTextview.getText().toString().trim(),
                                        _present.getText().toString().trim(),
                                        _absent.getText().toString().trim());

                                Snackbar.make(createButton, "" + lastName.getText().toString() + ", " + firstName.getText().toString() + " successfully added!", Snackbar.LENGTH_LONG).show();
                                lastName.setText("");
                                firstName.setText("");
                                middleName.setText("");
                                genderTextview.setText("");

                            }
                        }
                    });
                    builder.show();
            };

        };
    }
    );

}}