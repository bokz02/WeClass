package com.example.weclass.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.weclass.schedule.CalendarUtils.daysInWeekArray;
import static com.example.weclass.schedule.CalendarUtils.weeklyYearFromDate;

import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.Settings;
import com.example.weclass.subject.Subject;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.navigation.NavigationView;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, NavigationView.OnNavigationItemSelectedListener, EventAdapter.OnNoteListener {
    private TextView monthYearText, weeklyYearText, eventDay, eventTime;
    private EditText eventTitle;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Button createEvent;
    ArrayList<EventItem> eventItems, id;
    EventAdapter eventAdapter;
    ExtendedRecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    View _noScheduleView;
    TextView _noScheduleTextView;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();
        navigationOpen();
        display();
        initializeAdapter();
    }

    public void initializeAdapter(){
        eventAdapter = new EventAdapter(WeekViewActivity.this, eventItems, this);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(WeekViewActivity.this));
        recyclerView.setEmptyView(_noScheduleView,_noScheduleTextView);
    }

    // DISPLAY DATA FROM DATABASE TO RECYCLERVIEW
    public void display(){
        id = new ArrayList<>();
        eventItems = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(this);
        eventItems = displayData();

    }


    // GET THE DATA IN THE DATABASE
    private ArrayList<EventItem> displayData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHelper.TABLE_NAME3, null);
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




    //OnBackPressed
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


    //Navigation Open
    public void navigationOpen() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();     // Show navigation drawer when clicked
        navigationView.setNavigationItemSelectedListener(this); //navigation drawer item clickable
    }


    //Navigation Item Selected
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
                intent = new Intent(WeekViewActivity.this, WeekViewActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSettings:
                intent = new Intent(WeekViewActivity.this, Settings.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }


    //Initialization
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        eventListView = findViewById(R.id.eventListView);
        weeklyYearText = findViewById(R.id.weeklyYearTV);
        drawerLayout = findViewById(R.id.drawerLayout1);
        navigationView = findViewById(R.id.navView1);
        toolbar = findViewById(R.id.toolbarSchedule);
        recyclerView = findViewById(R.id.eventRecycler);
        _noScheduleTextView = findViewById(R.id.noScheduleTextView);
        _noScheduleView = findViewById(R.id.noScheduleView);

    }

    //SetWeekView Calendar
    private void setWeekView()
    {
        weeklyYearText.setText(weeklyYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
//        setEventAdpater();
    }


    //Calendar Back Button
    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }


    //Calendar Next Button
    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    //Selected Date Click
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();
        navigationOpen();
        display();
        initializeAdapter();
    }

    //Add New Button
    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    //Month View Button
    public void monthView(View view) {
        startActivity(new Intent(this, ScheduleActivity.class));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onNoteClick(int position) {

    }
}