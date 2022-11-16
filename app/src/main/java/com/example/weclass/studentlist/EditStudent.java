package com.example.weclass.studentlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.image.DrawableUtils;
import com.example.weclass.studentlist.profile.image.ImageUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;

public class EditStudent extends AppCompatActivity {

    EditText _firstName, _lastName, _middleName;
    Button updateButton, cancelButton;
    ImageButton _backButton;
    ImageView profilePic;
    TextView _gender, _id;
    String selectedGender;
    Uri uri = null;
    SharedPreferences sharedPreferences = null;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        initialize();   // INITIALIZE ALL VIEWS
        chooseGender(); // GENDER PICKER
        displayData();  // RECEIVE THA DATA FROM ADAPTER THAT PASS HERE, AND DISPLAY IN ALL VIEWS
        backToStudentList();    // BACK BUTTON
        cancelButton();     // CANCEL BUTTON
        updateData();   // GET ALL DATA IN VIES AND INSERT TO DATABASE
        addPhoto(); // Add profile pic method
        displayImage(); // Get profile pic of the student

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.67));
        getWindow().setBackgroundDrawableResource(R.drawable.dialogbox_bg);
        getWindow().setElevation(20);
    }

    // GET ALL DATA IN VIES AND INSERT TO DATABASE
    public void updateData(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_lastName.getText().toString().isEmpty() || _firstName.getText().toString().isEmpty() || _gender.getText().toString().isEmpty()){
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditStudent.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Don't leave empty fields!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();

                }else {

                    if(uri==null) {

                        byte[] image = DrawableUtils.getBytes(BitmapFactory.decodeResource(getResources(), R.drawable.prof1));

                            DataBaseHelper dbh = new DataBaseHelper(EditStudent.this);
                            dbh.updateStudent(
                                    _id.getText().toString().trim(),
                                    _lastName.getText().toString().trim(),
                                    _firstName.getText().toString().trim(),
                                    _middleName.getText().toString().trim(),
                                    _gender.getText().toString().trim(),
                                    image);

                            Snackbar.make(updateButton, "Student information successfully updated!", Snackbar.LENGTH_LONG).show();
                        }else {
                            try {


                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                byte[] inputData = ImageUtils.getBytes(inputStream);
                                DataBaseHelper dbh = new DataBaseHelper(EditStudent.this);
                                dbh.updateStudent(
                                        _id.getText().toString().trim(),
                                        _lastName.getText().toString().trim(),
                                        _firstName.getText().toString().trim(),
                                        _middleName.getText().toString().trim(),
                                        _gender.getText().toString().trim(),
                                        inputData);

                                Snackbar.make(updateButton, "Student information successfully updated!", Snackbar.LENGTH_LONG).show();
                            }catch (Exception e){
                                DataBaseHelper dbh = new DataBaseHelper(EditStudent.this);
                                dbh.close();

                            }

                      }

                   }
            }
        });
    }

    // INITIALIZE ALL VIEWS
    public void initialize(){
        _gender = findViewById(R.id.editStudGender);
        _id = findViewById(R.id.idEditStudent);
        _lastName = findViewById(R.id.editStudentLastname);
        _firstName = findViewById(R.id.editStudFirstname);
        _middleName = findViewById(R.id.editStudMiddleName);
        _backButton = findViewById(R.id.backButtonEditStudent);
        cancelButton = findViewById(R.id.editCancelButtonStudent);
        updateButton = findViewById(R.id.editUpdateButton);
        profilePic = findViewById(R.id.editStudentProfilePicture);
    }

    public void backToStudentList(){
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void cancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditStudent.this);
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

    //  GET THE DATA THAT PASS TO HERE, FROM THE ADAPTER
    public void displayData(){
        if (getIntent().getBundleExtra("Student") != null){
            Bundle bundle = getIntent().getBundleExtra("Student");
            _id.setText(bundle.getString("id"));
            _lastName.setText(bundle.getString("last_name"));
            _firstName.setText(bundle.getString("first_name"));
            _middleName.setText(bundle.getString("middle_name"));
            _gender.setText(bundle.getString("gender"));

        }
    }

    // GENDER PICKER
    public void chooseGender(){
        final String[] gender = new String[]{
                "Male",
                "Female"
        };

        selectedGender = gender[0];
        _gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditStudent.this);
                builder.setTitle("Select gender");
                builder.setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedGender = gender[i];
                        _gender.setText(selectedGender);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    //IMAGE PICKER THAT SELECT PHOTO FROM CAMERA OR GALLERY
    public void addPhoto(){
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditStudent.this)
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

        if(data != null){
            uri = data.getData();
            profilePic.setImageURI(uri);
        }
    }

    // DISPLAY IMAGE IN PROFILE FROM DATABASE
    private void displayImage(){
        DataBaseHelper db = new DataBaseHelper(EditStudent.this);
        SQLiteDatabase sqL = db.getWritableDatabase();
        Cursor cursor = sqL.rawQuery("SELECT * FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_ID2 + " = "
                + _id.getText().toString().trim(), null);

        if (cursor.moveToFirst()){
            byte[] image = cursor.getBlob(8);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            profilePic.setImageBitmap(bitmap);
            cursor.close();
        }

    }
}