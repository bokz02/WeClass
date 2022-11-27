package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.weclass.archive.Archive;
import com.example.weclass.calendar.CalendarEvents;
import com.example.weclass.setting.FAQs;
import com.example.weclass.subject.Subject;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Set;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView button, button1,button2;
    SwipeRefreshLayout refreshLayout;
    SwitchMaterial toggleButton;
    SharedPref sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //This will set the theme depends on save state of switch button
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(Settings.this, R.color.titleBar));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
        navigationOpen();
        terms();
        aboutUs();
        refreshlayout();
        faqs();
        setNightModeTheme(); // Button toggle for night mode


    }

    @Override
    protected void onResume() {
        super.onResume();

        initialize();
        refreshlayout();
    }

    // Button toggle for night mode
    public void setNightModeTheme(){
        if (sharedPref.loadNightModeState()){
            toggleButton.setChecked(true);
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sharedPref.setNightModeState(true);
                    Intent intent = new Intent(Settings.this, Settings.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.transition.fade_in_out,R.transition.fade_in_out);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }else {
                    sharedPref.setNightModeState(false);
                    Intent intent = new Intent(Settings.this, Settings.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.transition.fade_in_out,R.transition.fade_in_out);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

            }
        });
    }





    private void refreshlayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void aboutUs() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, AboutUs.class);
                startActivity(intent);
            }
        });
    }


    public void terms() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, TermsAndCondition.class);
                startActivity(intent);
            }
        });
    }

    public void faqs() {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, FAQs.class);
                startActivity(intent);
            }
        });
    }

    public void initialize() {
        toolbar = findViewById(R.id.toolbarSettings);
        navigationView = findViewById(R.id.navViewSettings);
        drawerLayout = findViewById(R.id.drawerSettings);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        refreshLayout = findViewById(R.id.refreshLayout);
        toggleButton = findViewById(R.id.darkModeButton);

    }

    public void navigationOpen() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();     // Show navigation drawer when clicked

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.mm1);

        navigationView.setNavigationItemSelectedListener(this); //navigation drawer item clickable
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);  // When back button is pressed while navigation drawer is open, it will close the navigation drawer.
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> finish())
                    .setNegativeButton("No", null)
                    .show();                    // Exit pop up when back button is pressed if navigation drawer is not open
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawerSubject:
                Intent intent = new Intent(this, Subject.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerArchive:
                intent = new Intent(this, Archive.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerCalendar:
                intent = new Intent(this, CalendarEvents.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerLogout:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Settings.this);
                builder.setTitle("Confirm Exit");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("Are you sure you want to exit?");
                builder.setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> finishAffinity())
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
        return true;
    }
}