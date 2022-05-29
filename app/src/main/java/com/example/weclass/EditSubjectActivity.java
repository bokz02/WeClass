package com.example.weclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EditSubjectActivity extends AppCompatActivity implements SubjectAdapter.OnNoteListener{

    EditText _course, _subjectCode, _subjectName, _day, _time;
    TextView  _id;
    Button updateButton, backButton;
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
        initializeAdapter();
        backButton();
    }

    public void initializeAdapter(){



    }

    public void backButton(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditSubjectActivity.this, Subject.class);
                startActivity(intent);
            }
        });

    }

    // BACK BUTTON OF THE PHONE
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditSubjectActivity.this, Subject.class);
        startActivity(intent);
    }

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
                        _day.getText().toString().trim(),
                        _time.getText().toString().trim());

                Snackbar.make(updateButton, "Subject successfully created!", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    public void initialize(){

        _id  = findViewById(R.id.idNumberEditSubject);
        _course = findViewById(R.id.courseNameText2);
        _subjectCode = findViewById(R.id.subjectCodeText2);
        _subjectName = findViewById(R.id.subjectNameText2);
        _day = findViewById(R.id.dayAddSubject2);
        _time = findViewById(R.id.timeAddSubject2);
        updateButton = findViewById(R.id.updateButtonSubject);
        backButton = findViewById(R.id.backButtonEditSubject);
    }

    public void displayData(){
        if (getIntent().getBundleExtra("Userdata") != null){
            Bundle bundle = getIntent().getBundleExtra("Userdata");


            _id.setText(bundle.getString("id"));
            _course.setText(bundle.getString("course"));
            _subjectCode.setText(bundle.getString("subject_code"));
            _subjectName.setText(bundle.getString("subject_name"));
            _day.setText(bundle.getString("day"));
            _time.setText(bundle.getString("time"));
        }
    }


    @Override
    public void onNoteClick(int position) {

    }
}