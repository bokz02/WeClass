package com.example.weclass.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.BottomNavi;
import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.Settings;
import com.example.weclass.archive.Archive;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.login.LoginActivity;
import com.example.weclass.schedule.EventAdapter;
import com.example.weclass.schedule.EventItem;
import com.example.weclass.schedule.WeekViewActivity;
import com.example.weclass.subject.Subject;
import com.example.weclass.subject.SubjectItems;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeSubjectAdapter.OnNoteListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ExtendedRecyclerView extendedRecyclerView, extendedRecyclerView2;
    ArrayList<SubjectItems> subjectItems;
    ArrayList<EventItem> eventItems;
    DataBaseHelper dataBaseHelper;
    HomeSubjectAdapter homeSubjectAdapter;
    HomeScheduleAdapter homeScheduleAdapter;
    View noFile, noFile2;
    TextView noSubject, noSchedule, userFullname;
    TextView search;



    SwipeRefreshLayout refreshLayout;
    DatabaseReference referenceUsers;
    FirebaseAuth auth;

    private StorageReference storageReference;
    FirebaseAuth fauth;
    ImageView profilepic;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen


        initialize();
        navigationOpen(); // open navigation drawer method
        display();
        display2();
        initializeAdapter();
        initializeAdapter2();
        textListener();
        getData();
        getPicture();
        //viewPagerView();

    }

    public void initialize(){

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);
        extendedRecyclerView = findViewById(R.id.homeSubjectRecView);
        noSubject = findViewById(R.id.noSubjectTextViewHome);
        noFile = findViewById(R.id.noViewViewSubjectHome);
        search = findViewById(R.id.searchEditTextSubjectHome);
        userFullname = findViewById(R.id.homeName);
        extendedRecyclerView2 = findViewById(R.id.homeScheduleRecView);
        noSchedule = findViewById(R.id.noSubjectTextViewHome2);
        noFile2 = findViewById(R.id.noViewViewSubjectHome2);

    }

    @Override
    protected void onResume() {
        super.onResume();

        initialize();
        navigationOpen(); // open navigation drawer method
        display();
        display2();
        initializeAdapter();
        initializeAdapter2();
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
            toggle.syncState();// Show navigation drawer when clicked

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_iconsort1_svg);

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


    // INITIALIZE ADAPTER FOR VIEWPAGER
    public void initializeAdapter(){
        homeSubjectAdapter = new HomeSubjectAdapter(this, subjectItems, this);
        extendedRecyclerView.setAdapter(homeSubjectAdapter);
        extendedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        extendedRecyclerView.setEmptyView(noFile, noSubject);
    }

    // DISPLAY DATA FROM DATABASE TO RECYCLERVIEW
    public void display(){
        subjectItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(this);
        subjectItems = displayData();

    }


    // GET THE DATA IN THE DATABASE
    private ArrayList<SubjectItems> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHelper.TABLE_MY_SUBJECTS, null);
        ArrayList<SubjectItems> subjectItems = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                subjectItems.add(new SubjectItems (
                        cursor.getInt(0),
                        cursor.getString(1),
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
        return subjectItems;
    }


    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, BottomNavi.class);
        intent.putExtra("Subject", subjectItems.get(position));
        startActivity(intent);
        overridePendingTransition(R.transition.slide_right,R.transition.slide_left);

    }

    // FILTER SEARCH IN SUBJECT ACTIVITY
    public void textListener(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                homeSubjectAdapter.getFilter().filter(editable);

            }
        });
    }



    public void initializeAdapter2(){
        homeScheduleAdapter = new HomeScheduleAdapter(this, eventItems);
        extendedRecyclerView2.setAdapter(homeScheduleAdapter);
        extendedRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        extendedRecyclerView2.setEmptyView(noFile2,noSchedule);
    }

    // DISPLAY DATA FROM DATABASE TO RECYCLERVIEW
    public void display2(){
        eventItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(this);
        eventItems = displayData2();

    }


    // GET THE DATA IN THE DATABASE
    private ArrayList<EventItem> displayData2(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHelper.TABLE_MY_SCHEDULE, null);
        ArrayList<EventItem> eventItems= new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                eventItems.add(new EventItem (
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return eventItems;
    }

    private void getPicture() {
        fauth = FirebaseAuth.getInstance();
        profilepic = findViewById(R.id.homePic);
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilepic);
            }
        });
    }


    //Get data to firebase
    private void getData() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        referenceUsers = FirebaseDatabase.getInstance().getReference().child("UserItem");
        referenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String user_name = dataSnapshot.child(user.getUid()).child("fullname").getValue(String.class);

                //Displaying data from firebase
                userFullname.setText(user_name);}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Something wrong happend!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}