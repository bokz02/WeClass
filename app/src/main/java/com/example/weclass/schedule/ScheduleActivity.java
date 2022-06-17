package com.example.weclass.schedule;

import static com.example.weclass.schedule.CalendarUtils.daysInMonthArray;
import static com.example.weclass.schedule.CalendarUtils.monthlyYearFromDate;
import static com.example.weclass.schedule.CalendarUtils.weeklyYearFromDate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.schedule.CalendarAdapter;
import com.example.weclass.schedule.CalendarUtils;
import com.example.weclass.schedule.WeekViewActivity;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{


    private TextView monthYearText, weeklyYearText, store;
    private RecyclerView calendarRecyclerView;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        //Schedule
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
        backButton();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen




    }//End of Oncreate

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        weeklyYearText = findViewById(R.id.weeklyYearTV);
        backButton = findViewById(R.id.backButtonAddSchedule);
        store = monthYearText;
    }

    private void setMonthView()
    {
        monthYearText.setText(monthlyYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
//        if(date != null)
//        {
//            CalendarUtils.selectedDate = date;
//            setMonthView();
//        }
        CalendarUtils.selectedDate = date;
        setMonthView();
    }

    public void weeklyAction(View view)
    {
        String str = monthYearText.getText().toString();
//        startActivity(new Intent(this, WeekViewActivity.class));
        Intent intent = new Intent(getApplicationContext(),WeekViewActivity.class);
        intent.putExtra("message_key", str);
        startActivity(intent);
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    public void backButton(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}