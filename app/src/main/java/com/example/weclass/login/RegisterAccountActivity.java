package com.example.weclass.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weclass.R;

public class RegisterAccountActivity extends AppCompatActivity {

    TextView login, terms, privacy;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        Initialized();
        setLogin();
    }

    private void Initialized(){
        signup = findViewById(R.id.btnsignUp);
        login = findViewById(R.id.signUpLogin);
        privacy = findViewById(R.id.privacyPolicy);
        terms = findViewById(R.id.termsOfServices);
    }

    private void setLogin(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
            }
        });
    }
}