package com.example.weclass.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.weclass.R;
import com.example.weclass.Settings;
import com.example.weclass.SharedPref;


public class FAQs extends AppCompatActivity {
    SharedPreferences sharedPreferences = null;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO); Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.titleBar));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);


    }

    public void ClassManage(View view) {
        startActivity(new Intent(FAQs.this, ClassManagement.class));
    }

    public void StudentManage(View view) {
        startActivity(new Intent(this, StudentManagement.class));
    }

    public void AttendanceManage(View view) {
        startActivity(new Intent(this, AttendanceManagement.class));
    }

    public void report(View view) {
        startActivity(new Intent(this, Report.class));
    }

    public void Classwork(View view) {
        startActivity(new Intent(this, TaskManagement.class));
    }

    public void backToFaqs(View view) {
        startActivity(new Intent(this, Settings.class));
    }
}