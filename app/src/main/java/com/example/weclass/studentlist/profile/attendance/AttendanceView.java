package com.example.weclass.studentlist.profile.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.google.android.material.tabs.TabLayout;

public class AttendanceView extends AppCompatActivity {

    SharedPref sharedPreferences;
    ImageButton _backButton;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    AttendanceViewPagerAdapter attendanceViewPagerAdapter;
    TextView _studentNumber, _parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = new SharedPref(this);

        if (sharedPreferences.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(AttendanceView.this, R.color.titleBar));

        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(AttendanceView.this, R.color.red2));

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        initialize();
        backToProfile();
        fragmentManager();

    }

    public void initialize(){
        _backButton = findViewById(R.id.backButtonAttendanceView);
        tabLayout = findViewById(R.id.tabLayoutAttendanceView);
        viewPager2 = findViewById(R.id.viewPagerAttendanceView);
        _studentNumber = findViewById(R.id.studentNumberAttendanceView);
        _parentId = findViewById(R.id.parentIdAttendanceView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
    }

    public void backToProfile(){
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
            }
        });
    }



    public void fragmentManager(){
        Intent intent = getIntent();
        String studentNumber = intent.getStringExtra("studentNumber");
        String parentId = intent.getStringExtra("parentId");
        FragmentManager fragmentManager = getSupportFragmentManager();
        attendanceViewPagerAdapter = new AttendanceViewPagerAdapter(fragmentManager,getLifecycle(), studentNumber, parentId);

        viewPager2.setAdapter(attendanceViewPagerAdapter);

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