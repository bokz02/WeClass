package com.example.weclass.studentlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.image.DrawableUtils;
import com.example.weclass.studentlist.profile.image.ImageUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddStudent extends AppCompatActivity {

    Button cancelButton, createButton;
    ImageButton backButton;
    ImageView profilePicture;
    TextView genderTextview, parentID, _present, _absent, _finalGrade, _midtermGrade, _finalRating;
    EditText lastName, firstName, middleName;
    String selectedGender;
    Uri uri = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialized();          //INITIALIZED ALL VIEWS
        createStudent();    // CREATE STUDENT BUTTON
        chooseGender();     // GENDER BUTTON
        cancelButton();     // CANCEL BUTTON, BACK TO STUDENT LIST
        backToStudentList();    // BACK TO STUDENT LIST ACTIVITY
        getDataFromStudentListFragment();
        addPhoto();     // BUTTON THAT PICK IMAGE FROM CAMERA OR GALLERY
        //displayImage();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.67));
        getWindow().setBackgroundDrawableResource(R.drawable.dialogbox_bg);
        getWindow().setElevation(20);


    }

    public void getDataFromStudentListFragment(){
        Intent intent = getIntent();
        String a = intent.getStringExtra("id");
        parentID.setText(a);
    }

    public void initialized (){
        lastName = findViewById(R.id.studLastnameProfile);
        middleName = findViewById(R.id.studMidname);
        firstName = findViewById(R.id.studFirstnameProfile);
        genderTextview = findViewById(R.id.studGender);
        createButton = findViewById(R.id.createButtonStudent);
        cancelButton = findViewById(R.id.cancelButtonStudent);
        backButton = findViewById(R.id.backButtonAddStudent);
        parentID = findViewById(R.id.parentIDAddStudent);
        _absent = findViewById(R.id.absentIDAddStudent);
        _present = findViewById(R.id.presentIDAddStudent);
        profilePicture = findViewById(R.id.studentProfilePicture);
        _midtermGrade = findViewById(R.id.midtermGradeTextViewAddStudent);
        _finalGrade = findViewById(R.id.finalGradeTextViewAddStudent);
        _finalRating = findViewById(R.id.finalRatingTextViewAddStudent);
        progressBar = findViewById(R.id.progressBar);
    }

    public void backToStudentList(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void chooseGender(){
        final String[] gender = new String[]{
                "Male",
                "Female"
        };

        selectedGender = gender[0];
        genderTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
                builder.setTitle("Select gender");
                builder.setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedGender = gender[i];
                        genderTextview.setText(selectedGender);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public void cancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
                builder.setTitle("Confirm exit");


                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("All the fields will not be saved!");
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
        });
    }

    public void createStudent (){
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (lastName.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || genderTextview.getText().toString().isEmpty()){
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Don't leave empty fields!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();

                }


                //IF FIELDS ARE FILLED, IT WILL ADD TO DATABASE
                else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddStudent.this);
                    builder.setTitle("Please confirm");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Are you sure all the information are correct?");
                    builder.setPositiveButton("ok",new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DataBaseHelper db = new DataBaseHelper(AddStudent.this);
                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                            Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM "
                                    + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                                    + DataBaseHelper.COLUMN_PARENT_ID + " = "
                                    + parentID.getText().toString().trim() + " AND "
                                    + DataBaseHelper.COLUMN_LAST_NAME + " = '"
                                    + lastName.getText().toString().trim() + "' AND "
                                    + DataBaseHelper.COLUMN_FIRST_NAME + " = '"
                                    + firstName.getText().toString().trim() + "'", null);


                            if(cursor.moveToFirst()){
                                Snackbar.make(createButton, "" + lastName.getText().toString() + ", "
                                        + firstName.getText().toString() + " is already added in this subject.", Snackbar.LENGTH_SHORT).show();
                                cursor.close();


                            }else {

                                // IF URI DON'T HAVE DATA, IT WILL SAVE WITH A DEFAULT IMAGE
                                if(uri == null) {

                                    byte[] image = DrawableUtils.getBytes(BitmapFactory.decodeResource(getResources(), R.drawable.prof1));

                                    DataBaseHelper dbh = new DataBaseHelper(AddStudent.this);
                                    dbh.addStudent(parentID.getText().toString().trim(),
                                            lastName.getText().toString().trim(),
                                            firstName.getText().toString().trim(),
                                            middleName.getText().toString().trim(),
                                            genderTextview.getText().toString().trim(),
                                            _present.getText().toString().trim(),
                                            _absent.getText().toString().trim(),
                                            image,
                                            _midtermGrade.getText().toString().trim(),
                                            _finalGrade.getText().toString().trim(),
                                            _finalRating.getText().toString().trim());


                                    Snackbar.make(createButton, "" + lastName.getText().toString() + ", " + firstName.getText().toString() + " successfully added!", Snackbar.LENGTH_LONG).show();
                                    lastName.setText("");
                                    firstName.setText("");
                                    middleName.setText("");
                                    genderTextview.setText("");

                                    // IF URI DON'T HAVE DATA, IT WILL SAVE WITH AN IMAGE PROVIDED BY USER
                                }else {
                                    try {
                                        InputStream inputStream = getContentResolver().openInputStream(uri);
                                        byte[] inputData = ImageUtils.getBytes(inputStream);
                                        DataBaseHelper dbh = new DataBaseHelper(AddStudent.this);
                                        dbh.addStudent(parentID.getText().toString().trim(),
                                                lastName.getText().toString().trim(),
                                                firstName.getText().toString().trim(),
                                                middleName.getText().toString().trim(),
                                                genderTextview.getText().toString().trim(),
                                                _present.getText().toString().trim(),
                                                _absent.getText().toString().trim(),
                                                inputData,
                                                _midtermGrade.getText().toString().trim(),
                                                _finalGrade.getText().toString().trim(),
                                                _finalRating.getText().toString().trim());


                                        Snackbar.make(createButton, "" + lastName.getText().toString() + ", " + firstName.getText().toString() + " successfully added!", Snackbar.LENGTH_LONG).show();
                                        lastName.setText("");
                                        firstName.setText("");
                                        middleName.setText("");
                                        genderTextview.setText("");
                                        profilePicture.setImageResource(R.drawable.prof1);


                                    }catch (Exception e){
                                        DataBaseHelper dbh = new DataBaseHelper(AddStudent.this);
                                        dbh.close();

                                    }
                                }
                            }
                        }
                    });
                    builder.show();
            };

        };
    }
    );
}

    //IMAGE PICKER THAT SELECT PHOTO FROM CAMERA OR GALLERY
    public void addPhoto(){
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(AddStudent.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(AddStudent.this, "Success!", Toast.LENGTH_SHORT).show();
            if (data != null) {
                uri = data.getData();
                profilePicture.setImageURI(uri);
                progressBar.setVisibility(View.GONE);
            }
        }else if(resultCode == RESULT_CANCELED){
            Toast.makeText(AddStudent.this, "Canceled!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

}