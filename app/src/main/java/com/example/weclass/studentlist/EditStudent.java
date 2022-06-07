package com.example.weclass.studentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class EditStudent extends AppCompatActivity {

    EditText _firstName, _lastName, _middleName;
    Button updateButton, cancelButton;
    ImageButton _backButton;
    TextView _gender, _id;
    String selectedGender;
    StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();
        chooseGender();
        displayData();
        backToStudentList();
        cancelButton();
        updateData();
    }

    public void updateData(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dbh = new DataBaseHelper(EditStudent.this);
                dbh.updateData(
                        _id.getText().toString().trim(),
                        _lastName.getText().toString().trim(),
                        _firstName.getText().toString().trim(),
                        _middleName.getText().toString().trim(),
                        _gender.getText().toString().trim());

                Snackbar.make(updateButton, "Student information successfully updated!", Snackbar.LENGTH_LONG).show();



            }
        });
    }

    public void initialize(){
        _gender = findViewById(R.id.editStudGender);
        _id = findViewById(R.id.idEditStudent);
        _lastName = findViewById(R.id.editStudentLastname);
        _firstName = findViewById(R.id.editStudFirstname);
        _middleName = findViewById(R.id.editStudMiddleName);
        _backButton = findViewById(R.id.backButtonEditStudent);
        cancelButton = findViewById(R.id.editCancelButtonStudent);
        updateButton = findViewById(R.id.editUpdateButton);
    }

    public void backToStudentList(){
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void cancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditStudent.this);
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

    public void displayData(){
        if (getIntent().getBundleExtra("Student") != null){
            Bundle bundle = getIntent().getBundleExtra("Student");


            _id.setText(bundle.getString("id"));
            _lastName.setText(bundle.getString("last_name"));
            _firstName.setText(bundle.getString("first_name"));
            _middleName.setText(bundle.getString("middle_name"));
            _gender.setText(bundle.getString("gender"));

        }
    }

    public void chooseGender(){
        final String[] gender = new String[]{
                "Male",
                "Female"
        };

        selectedGender = gender[0];
        _gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditStudent.this);
                builder.setTitle("Select gender");
                builder.setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedGender = gender[i];
                        _gender.setText(selectedGender);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}