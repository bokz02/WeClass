package com.example.weclass.studentlist.profile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.StudentItems;
import com.example.weclass.studentlist.StudentProfile;
import com.example.weclass.studentlist.profile.activities.fragments.ActivitiesViewPagerAdapter;
import com.example.weclass.studentlist.profile.attendance.AttendanceViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class Activities extends AppCompatActivity {


    ImageButton _backButton;
    SharedPref sharedPref;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ActivitiesViewPagerAdapter activitiesViewPagerAdapter;
    TextView type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //This will set the theme depends on save state of switch button
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.titleBar));
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.red2));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        initialize();
        backToStudentProfile();
        fragmentManager();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initialize(){

        viewPager2 = findViewById(R.id.viewPagerActivities);
        tabLayout = findViewById(R.id.tabLayoutActivities);
        _backButton = findViewById(R.id.backButtonActivity);
        type = findViewById(R.id.activityTypeTitle);

        String activityType = "Activities";
        type.setText(activityType);

    }

    public void backToStudentProfile(){
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
            }
        });
    }

    public void fragmentManager() {
        Intent intent = getIntent();
        String studentNumber = intent.getStringExtra("studentID");
        String parentId = intent.getStringExtra("subjectID");
        FragmentManager fragmentManager = getSupportFragmentManager();
        activitiesViewPagerAdapter = new ActivitiesViewPagerAdapter(fragmentManager, getLifecycle(), studentNumber, parentId);

        viewPager2.setAdapter(activitiesViewPagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}