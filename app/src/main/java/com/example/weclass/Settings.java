package com.example.weclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.archive.Archive;
import com.example.weclass.dashboard.MainActivity;
import com.example.weclass.login.LoginActivity;
import com.example.weclass.login.UserAccount;
import com.example.weclass.schedule.WeekViewActivity;
import com.example.weclass.subject.Subject;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView button, button1, changeProfile, button2;
    TextView userFullname, userEmail;

    SwipeRefreshLayout refreshLayout;
    DatabaseReference referenceUsers;
    FirebaseAuth auth;

    private StorageReference storageReference;
    FirebaseAuth fauth;
    ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
        navigationOpen();
        terms();
        aboutUs();
        refreshlayout();
        userProfile();
        getData();
        getPicture();

        //Refresh


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen
    }

    @Override
    protected void onResume() {
        super.onResume();

        initialize();
        refreshlayout();
        getData();
        getPicture();
    }

    private void getPicture() {
        fauth = FirebaseAuth.getInstance();
        profilepic = findViewById(R.id.userProfile);
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
                String user_email = firebaseUser.getEmail();

                //Displaying data from firebase
                userFullname.setText(user_name);
                userEmail.setText(user_email); }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Settings.this, "Something wrong happend!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void userProfile() {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, UserAccount.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
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


    public void terms (){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, TermsAndCondition.class);
                startActivity(intent);
            }
        });
    }

    public void initialize(){
        toolbar = findViewById(R.id.toolbarSettings);
        navigationView = findViewById(R.id.navigationViewSettings);
        drawerLayout = findViewById(R.id.drawerSettings);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        userFullname = findViewById(R.id.accountName);
        userEmail = findViewById(R.id.accountEmail);
        refreshLayout = findViewById(R.id.refreshLayout);
        button2 = findViewById(R.id.accButton);
    }

    public void navigationOpen() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();     // Show navigation drawer when clicked
        navigationView.setNavigationItemSelectedListener(this); //navigation drawer item clickable
    }

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawerHome:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSched:
                intent = new Intent(this, WeekViewActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerSubject:
                intent = new Intent(this, Subject.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerArchive:
                intent = new Intent(this, Archive.class);
                startActivity(intent);
                finish();
                break;
            case R.id.drawerLogout:

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Settings.this);
                builder.setTitle("Confirm logout");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("Do you really want to logout?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.this, LoginActivity.class);
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