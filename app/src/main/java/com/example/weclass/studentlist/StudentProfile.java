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
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.activities.Activities;
import com.example.weclass.studentlist.profile.assignments.Assignments;
import com.example.weclass.studentlist.profile.attendance.Absent;
import com.example.weclass.studentlist.profile.attendance.Present;
import com.example.weclass.studentlist.profile.exams.Exams;
import com.example.weclass.studentlist.profile.image.ImageUtils;
import com.example.weclass.studentlist.profile.projects.Projects;
import com.example.weclass.studentlist.profile.quiz.Quiz;
import com.example.weclass.studentlist.profile.seatwork.SeatWork;
import com.example.weclass.subject.AddSubjectActivity;
import com.example.weclass.tasks.AddTask;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class StudentProfile extends AppCompatActivity {

    ImageButton backButton, finalGradeButton, midtermGradeButton, finalRatingButton;
    TextView _id, _subjectID, _lastName,
            _firstName, _presentTextview, _absentTextView,
            _courseTextView, _subjectTextView, _finalGrade,
            _midtermGrade, _finalRating;
    ImageView _activities, _quiz, _assignments, _seatWork, _present, _absent, _exams , _projects, _profileImage;
    String selectedFinalGrade, selectedMidtermGrade, selectedFinalRating;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //enable full screen

        initialize();   // INITIALIZE ALL VIEWS
        backToStudentListActivity(); // BACK BUTTON
        getDataFromStudentListRecView(); //  GET DATA FROM BUNDLE FROM ACTIVITY AND PARCELABLE FROM RECYCLERVIEW
        goToActivities();   // GO TO ACTIVITIES ACTIVITY
        goToAssignments();  // GO TO ASSIGNMENTS ACTIVITY
        goToQuiz(); // GO TO QUIZZES ACTIVITY
        goToSeatWork(); // GO TO SEATWORK ACTIVITY
        goToPresent();  // GO TO PRESENT ACTIVITY
        goToAbsent(); // GO TO ABSENT ACTIVITY
        goToProjects(); // GO TO PROJECTS ACTIVITY
        goToExams();    // GO TO EXAMS ACTIVITY
        addPhoto();     // ADD PHOTO FEATURE
        displayImage(); // DISPLAY IMAGE FROM DATABASE
        pickMidtermGrade();
        pickFinalGrade();
        pickFinalRating();


    }

    public void initialize(){
        backButton = findViewById(R.id.backButtonProfile);
        _id = findViewById(R.id.studentIDProfile);
        _subjectID = findViewById(R.id.subjectIDProfile);
        _activities = findViewById(R.id.activityButtonProfile);
        _firstName = findViewById(R.id.studFirstnameProfile);
        _lastName = findViewById(R.id.studLastnameProfile);
        _assignments = findViewById(R.id.assignmentButtonProfile);
        _quiz = findViewById(R.id.quizButtonProfile);
        _seatWork = findViewById(R.id.seatWorkButtonProfile);
        _presentTextview = findViewById(R.id.presentTextViewProfile);
        _absentTextView = findViewById(R.id.absentTextViewProfile);
        _present = findViewById(R.id.presentButtonProfile);
        _absent = findViewById(R.id.absentButtonProfile);
        _courseTextView = findViewById(R.id.courseTextViewProfile);
        _subjectTextView = findViewById(R.id.subjectTextViewProfile);
        _exams = findViewById(R.id.examButtonProfile);
        _projects = findViewById(R.id.projectButtonProfile);
        _profileImage = findViewById(R.id.studentProfilePicture);
        _midtermGrade = findViewById(R.id.midtermGradeTextViewProfile);
        _finalGrade = findViewById(R.id.finalGradeTextViewProfile);
        _finalRating = findViewById(R.id.finalRatingTextViewProfile);
        midtermGradeButton = findViewById(R.id.midtermGradeButtonProfile);
        finalGradeButton = findViewById(R.id.finalGradeButtonProfile);
        finalRatingButton = findViewById(R.id.finalRatingButtonProfile);


    }

    public void goToActivities(){
        _activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Activities.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToAssignments(){
        _assignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Assignments.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToQuiz(){
        _quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Quiz.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToSeatWork(){
        _seatWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, SeatWork.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToProjects(){
        _projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Projects.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToExams(){
        _exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Exams.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToPresent(){
        _present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Present.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void goToAbsent(){
        _absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Absent.class);
                intent.putExtra("studentID", _id.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right,R.transition.slide_left);
            }
        });
    }

    public void backToStudentListActivity(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getDataFromStudentListRecView(){
        Intent intent = getIntent();
        StudentItems studentItems = intent.getParcelableExtra("Student");

        String subjectCode = intent.getStringExtra("subject");
        String course = intent.getStringExtra("course");

        int studentID = studentItems.getId();
        int subjectID = studentItems.getParent_id();

        String lastName = studentItems.getLastname();
        String firstName = studentItems.getFirstname();
        String midtermGrade_ = studentItems.getMidtermGrade();
        String finalGrade_ = studentItems.getFinalGrade();
        String finalRating_ = studentItems.getFinalRating();
        int present = studentItems.getPresent();
        int absent = studentItems.getAbsent();

        _id.setText(String.valueOf(studentID));
        _subjectID.setText(String.valueOf(subjectID));
        _lastName.setText(lastName);
        _firstName.setText(firstName);
        _presentTextview.setText(String.valueOf(present));
        _absentTextView.setText(String.valueOf(absent));
        _courseTextView.setText(course);
        _subjectTextView.setText(subjectCode);
        _midtermGrade.setText(midtermGrade_);
        _finalGrade.setText(finalGrade_);
        _finalRating.setText(finalRating_);

    }


    //IMAGE PICKER THAT SELECT PHOTO FROM CAMERA OR GALLERY
    public void addPhoto(){
        _profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(StudentProfile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    // SET IMAGE FROM CAMERA OR GALLERY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            uri = data.getData();
            if(saveImageInDB(uri)){
                _profileImage.setImageURI(uri);
            }

        }
    }


    // CONVERT IMAGE TO BLOB AND SAVE TO DATABASE
    private boolean saveImageInDB(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] inputData = ImageUtils.getBytes(inputStream);
            DataBaseHelper dbh = new DataBaseHelper(StudentProfile.this);
            dbh.updateProfilePicture(_id.getText().toString(),
                    _subjectID.getText().toString(),
                    inputData);

            return true;


        }catch (Exception e){
            DataBaseHelper dbh = new DataBaseHelper(StudentProfile.this);
            dbh.close();
            return false;
        }
    }

    // DISPLAY IMAGE IN PROFILE FROM DATABASE
    private void displayImage(){
        DataBaseHelper db = new DataBaseHelper(StudentProfile.this);
        SQLiteDatabase sqL = db.getWritableDatabase();
        Cursor cursor = sqL.rawQuery("SELECT * FROM "
                        + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                        + DataBaseHelper.COLUMN_ID2 + " = "
                        + _id.getText().toString().trim() + " AND "
                        + DataBaseHelper.COLUMN_PARENT_ID + " = "
                        + _subjectID.getText().toString(), null);

        if (cursor.moveToFirst()){
            byte[] image = cursor.getBlob(8);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            _profileImage.setImageBitmap(bitmap);
            cursor.close();
        }

    }


    // MIDTERM GRADE PICKER
    public void pickMidtermGrade() {
        final String[] grade = new String[]{
                "1",
                "1.25",
                "1.5",
                "1.75",
                "2",
                "2.25",
                "2.5",
                "2.75",
                "3",
                "5",
                "INC",
                "DRP",

        };

        selectedMidtermGrade = grade[0];
        midtermGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(StudentProfile.this);
                builder.setTitle("Select grade");
                builder.setSingleChoiceItems(grade, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedMidtermGrade = grade[i];
                        _midtermGrade.setText(selectedMidtermGrade);

                        DataBaseHelper dbh = new DataBaseHelper(StudentProfile.this);
                        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();

                        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                                + DataBaseHelper.COLUMN_ID2 + " = "
                                + _id.getText().toString() + " AND "
                                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                                + _subjectID.getText().toString() + "'", null);

                        dbh.updateMidtermGrade(_id.getText().toString().trim(),
                                _midtermGrade.getText().toString());

                        cursor.close();

                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    // FINAL GRADE PICKER
    public void pickFinalGrade() {
        final String[] grade = new String[]{
                "1",
                "1.25",
                "1.5",
                "1.75",
                "2",
                "2.25",
                "2.5",
                "2.75",
                "3",
                "5",
                "INC",
                "DRP",

        };

        selectedFinalGrade = grade[0];
        finalGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(StudentProfile.this);
                builder.setTitle("Select grade");
                builder.setSingleChoiceItems(grade, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedFinalGrade = grade[i];
                        _finalGrade.setText(selectedFinalGrade);

                        DataBaseHelper dbh = new DataBaseHelper(StudentProfile.this);
                        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();

                        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                                + DataBaseHelper.COLUMN_ID2 + " = "
                                + _id.getText().toString() + " AND "
                                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                                + _subjectID.getText().toString() + "'", null);

                        dbh.updateFinalGrade(_id.getText().toString().trim(),
                                _finalGrade.getText().toString());

                        cursor.close();

                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    // FINAL RATING PICKER
    public void pickFinalRating() {
        final String[] grade = new String[]{
                "1",
                "1.25",
                "1.5",
                "1.75",
                "2",
                "2.25",
                "2.5",
                "2.75",
                "3",
                "5",
                "INC",
                "DRP",

        };

        selectedFinalRating = grade[0];
        finalRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(StudentProfile.this);
                builder.setTitle("Select grade");
                builder.setSingleChoiceItems(grade, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedFinalRating = grade[i];
                        _finalRating.setText(selectedFinalRating);

                        DataBaseHelper dbh = new DataBaseHelper(StudentProfile.this);
                        SQLiteDatabase sqLiteDatabase = dbh.getWritableDatabase();

                        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                                + DataBaseHelper.COLUMN_ID2 + " = "
                                + _id.getText().toString() + " AND "
                                + DataBaseHelper.COLUMN_PARENT_ID + " = '"
                                + _subjectID.getText().toString() + "'", null);

                        dbh.updateFinalRating(_id.getText().toString().trim(),
                                _finalRating.getText().toString());

                        cursor.close();

                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

}