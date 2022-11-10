package com.example.weclass.archive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weclass.BottomNavi;
import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.Settings;
import com.example.weclass.dashboard.MainActivity;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.login.LoginActivity;
import com.example.weclass.studentlist.StudentList;
import com.example.weclass.subject.Subject;
import com.example.weclass.subject.SubjectAdapter;
import com.example.weclass.subject.SubjectItems;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Archive extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ArchiveAdapter.OnNoteListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ExtendedRecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    ArrayList<ArchiveItems> archiveItems;
    ArchiveAdapter archiveAdapter;
    EditText searchEditText;
    View noFile;
    TextView noSubject;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);



        //status bar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.WHITE);


        mAuth = FirebaseAuth.getInstance();
        initialize();
        display();
        initializeAdapter();
        navigationOpen();
    }

    private void initialize() {
        drawerLayout = findViewById(R.id.drawerArchive);
        navigationView = findViewById(R.id.navViewArchive);
        toolbar = findViewById(R.id.toolbarArchive);
        recyclerView = findViewById(R.id.recyclerViewArchive);
        noFile = findViewById(R.id.noViewViewArchive);
        noSubject = findViewById(R.id.noSubjectTextViewArchive);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initialize();
        display();
        initializeAdapter();
        navigationOpen();
    }


    // INITIALIZE ADAPTER FOR RECYCLERVIEW
    public void initializeAdapter(){
        archiveAdapter = new ArchiveAdapter( archiveItems,Archive.this, this);
        recyclerView.setAdapter(archiveAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Archive.this));
        recyclerView.setEmptyView(noFile, noSubject);
    }

    // DISPLAY DATA FROM DATABASE TO RECYCLERVIEW
    public void display(){
        archiveItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(this);
        archiveItems = displayData();

    }

    // GET THE DATA IN THE DATABASE
    private ArrayList<ArchiveItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHelper.TABLE_MY_ARCHIVE, null);
        ArrayList<ArchiveItems> archiveItems = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                archiveItems.add(new ArchiveItems (
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return archiveItems;
    }

    // BACK BUTTON FUNCTION OF THE PHONE
    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);  // When back button is pressed while navigation drawer is open, it will close the navigation drawer.
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> finish())
                    .setNegativeButton("No", null)
                    .show();                    // Exit pop up when back button is pressed if navigation drawer is not open
        }
    }


    //NAVIGATION DRAWER
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawerSubject:
                Intent intent = new Intent(this, Subject.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSettings:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerArchive:
                intent = new Intent(this, Archive.class);
                startActivity(intent);
                finish();
                break;

            case R.id.drawerLogout:

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Archive.this);
                builder.setTitle("Confirm Exit");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("Are you sure you want to exit?");
                builder.setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> finishAffinity())
                        .setNegativeButton("No", null)
                        .show();
                break;

        }

        return false;
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, BottomNavi.class);
        intent.putExtra("archive_text", "archive");
        intent.putExtra("Archive", archiveItems.get(position));
        startActivity(intent);
        overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
    }
}