package com.example.weclass.studentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.weclass.R;

public class StudentInformation extends AppCompatActivity {

    ImageView _backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);

        initialize();
        backToStudentList();
    }

    private void backToStudentList() {
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialize() {

        _backButton = findViewById(R.id.backButtonStudentInformation);
    }
}