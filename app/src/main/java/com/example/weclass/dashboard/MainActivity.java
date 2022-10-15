package com.example.weclass.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.BottomNavi;
import com.example.weclass.ExtendedRecyclerView;
import com.example.weclass.R;
import com.example.weclass.Settings;
import com.example.weclass.archive.Archive;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.login.LoginActivity;
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

public class MainActivity extends AppCompatActivity {
    TextView _course, _subjectCode, _subjectName;
    TextView  _id, dayTextView, timeTextView, timeEndTextView, semesterTextView, schoolYearTextView;
    private Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.32));
        getWindow().setBackgroundDrawableResource(R.drawable.dialogbox_bg);
        getWindow().setElevation(10);


        initialize();
        displayData();





    }



    public void initialize() {
        _id  = findViewById(R.id.positionNumber);
        _course = findViewById(R.id.courseTypeRecView);
        _subjectCode = findViewById(R.id.subjectCodeRecView);
        _subjectName = findViewById(R.id.subjectTitleRecView);
        dayTextView = findViewById(R.id.dateTextViewRecView);
        timeTextView = findViewById(R.id.timeTextViewRecView);
        timeEndTextView = findViewById(R.id.timeEndTextViewRecView);
        semesterTextView = findViewById(R.id.semesterSubjectRecView);
        schoolYearTextView = findViewById(R.id.schoolYearSubjectRecView);
    }

    public void displayData(){
        if (getIntent().getBundleExtra("Userdata") != null){
            Bundle bundle = getIntent().getBundleExtra("Userdata");

            _id.setText(bundle.getString("id"));
            _course.setText(bundle.getString("course"));
            _subjectCode.setText(bundle.getString("subject_code"));
            _subjectName.setText(bundle.getString("subject_name"));
            dayTextView.setText(bundle.getString("day"));
            timeTextView.setText(bundle.getString("time"));
            timeEndTextView.setText(bundle.getString("timeEnd"));
            semesterTextView.setText(bundle.getString("sem"));
            schoolYearTextView.setText(bundle.getString("sy"));

        }
    }


}