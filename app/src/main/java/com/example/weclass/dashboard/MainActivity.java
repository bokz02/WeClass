package com.example.weclass.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.weclass.R;
import com.example.weclass.Settings;
import com.example.weclass.archive.Archive;
import com.example.weclass.login.LoginActivity;
import com.example.weclass.schedule.WeekViewActivity;
import com.example.weclass.studentlist.AddStudent;
import com.example.weclass.subject.Subject;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ViewPager2 viewPager2;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // hide action bar title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        navigationOpen(); // open navigation drawer method
        initialize();


    }

    public void initialize(){

    }


    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);  // When back button is pressed while navigation drawer is open, it will close the navigation drawer.
        }
        else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
            builder.setTitle("Confirm exit");
            builder.setIcon(R.drawable.ic_baseline_warning_24);
            builder.setMessage("Do you really want to exit?");
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
    }

        public void navigationOpen() {
            setSupportActionBar(toolbar);
            navigationView.bringToFront();
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();     // Show navigation drawer when clicked
            navigationView.setNavigationItemSelectedListener(this); //navigation drawer item clickable
        }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawerSubject:
                Intent intent = new Intent(this, Subject.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSched:
                 intent = new Intent(MainActivity.this, WeekViewActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSettings:
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerArchive:
                intent = new Intent(MainActivity.this, Archive.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerLogout:

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("Confirm logout");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("Do you really want to logout?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.transition.animation_enter,R.transition.animation_leave);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

                break;

        }
        return true;
    }
}