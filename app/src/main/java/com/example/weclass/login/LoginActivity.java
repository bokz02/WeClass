package com.example.weclass.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weclass.R;
import com.example.weclass.schedule.ScheduleActivity;

public class LoginActivity extends AppCompatActivity {

    TextView signup, forgotpass;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Initialized();
        setSignup();
    }

    private void Initialized(){
        signup = findViewById(R.id.signUp);
        forgotpass = findViewById(R.id.forgotPassword);
        login = findViewById(R.id.btnLogin);
    }

    public void setSignup(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));
            }
        });
    }
}