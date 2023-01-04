package com.example.weclass.studentlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.weclass.R;
import com.example.weclass.SharedPref;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.profile.activities.Activities;
import com.example.weclass.studentlist.profile.assignments.Assignments;
import com.example.weclass.studentlist.profile.attendance.AttendanceView;
import com.example.weclass.studentlist.profile.exams.Exams;
import com.example.weclass.studentlist.profile.image.ImageUtils;
import com.example.weclass.studentlist.profile.projects.Projects;
import com.example.weclass.studentlist.profile.quiz.Quiz;
import com.example.weclass.studentlist.profile.recitation.Recitation;
import com.example.weclass.studentlist.profile.reports.Report;
import com.example.weclass.studentlist.profile.seatwork.SeatWork;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.core.Repo;

import java.io.InputStream;

public class StudentProfile extends AppCompatActivity {

    ImageButton backButton, _finalGradeButton, _midtermGradeButton, _finalRatingButton;
    TextView _id, _subjectID, _lastName,
            _firstName, _presentTextview, _absentTextView,
            _courseTextView, _subjectTextView, _finalGrade,
            _midtermGrade, _finalRating, studentNumber, _middleName, _late;
    ImageView _activities, _quiz, _assignments, _seatWork, _present, _absent, _exams, _projects, _profileImage;
    String gradingPeriod;
    Uri uri = null;
    CardView absentCardView, activities, quiz,
            assignment, seatWork, exams, projects, recitation,
            reportButton;
    SharedPref sharedPref;
    ProgressBar progressBar;
    ConstraintLayout constraintButton;
    int absentCount = 0, presentCount = 0, lateCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(StudentProfile.this, R.color.titleBar));

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(StudentProfile.this, R.color.red2));

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initialize();   // INITIALIZE ALL VIEWS
        backToStudentListActivity(); // BACK BUTTON
        getDataFromStudentListRecView(); //  GET DATA FROM BUNDLE FROM ACTIVITY AND PARCELABLE FROM RECYCLERVIEW
        goToActivities();   // GO TO ACTIVITIES ACTIVITY
        goToAssignments();  // GO TO ASSIGNMENTS ACTIVITY
        goToQuiz(); // GO TO QUIZZES ACTIVITY
        goToSeatWork(); // GO TO SEATWORK ACTIVITY
        goToProjects(); // GO TO PROJECTS ACTIVITY
        goToExams();    // GO TO EXAMS ACTIVITY
        goAttendance(); // GO TO late ACTIVITY
        addPhoto();     // ADD PHOTO FEATURE
        displayImage(); // DISPLAY IMAGE FROM DATABASE
        countAbsent();
        countPresent();
        countLate();
        goToRecitations();
        goToReports();
        displayGrade(); // display grade of the student


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void initialize() {
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
        _finalGradeButton = findViewById(R.id.finalGradeButton);
        exams = findViewById(R.id._materialExams);
        projects = findViewById(R.id._materialProject);
        quiz = findViewById(R.id._materialQuiz);
        seatWork = findViewById(R.id._materialSeatwork);
        assignment = findViewById(R.id._materialAssignment);
        activities = findViewById(R.id._materialActivity);
        studentNumber = findViewById(R.id.studentNumberProfile);
        progressBar = findViewById(R.id.progressBarStudentProfile);
        _middleName = findViewById(R.id.studentMiddleNameProfile);
        _late = findViewById(R.id.lateTextViewProfile);
        constraintButton = findViewById(R.id.constraintButtonToAttendance);
        recitation = findViewById(R.id._materialRecitation);
        absentCardView = findViewById(R.id._materialAbsent);
        reportButton = findViewById(R.id._materialReport);


    }

    public void countAbsent() {
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT count(*) FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = '"
                + studentNumber.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + _subjectID.getText().toString() + " and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "'and "
                + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + "="
                + 1, null);

        if (cursor.moveToFirst()) {
            absentCount = cursor.getInt(0);
            cursor.close();
        }

        if (absentCount == 4) {
            absentCardView.setCardBackgroundColor(getResources().getColor(R.color.absentWarning1));
        } else if (absentCount >= 5) {
            absentCardView.setCardBackgroundColor(getResources().getColor(R.color.absentWarning2));
        }
        _absentTextView.setText(String.valueOf(absentCount));
    }

    public void countPresent() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT count(*) FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = '"
                + studentNumber.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + _subjectID.getText().toString() + " and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "' and "
                + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + "="
                + 1, null);

        if (cursor.moveToFirst()) {
            presentCount = cursor.getInt(0);
            cursor.close();
        }
        _presentTextview.setText(String.valueOf(presentCount));
    }

    public void countLate() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT count(*) FROM "
                + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = '"
                + studentNumber.getText().toString() + "' AND "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                + _subjectID.getText().toString() + " and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "'and "
                + DataBaseHelper.COLUMN_LATE_ATTENDANCE + "="
                + 1, null);

        if (cursor.moveToFirst()) {
            lateCount = cursor.getInt(0);
            cursor.close();
        }
        _late.setText(String.valueOf(lateCount));
    }


    public void goToActivities() {
        activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Activities.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goToAssignments() {
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Assignments.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goToQuiz() {
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Quiz.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goToSeatWork() {
        seatWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, SeatWork.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goToProjects() {
        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Projects.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goToExams() {
        exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Exams.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goToRecitations() {
        recitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Recitation.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goToReports() {
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, Report.class);
                intent.putExtra("studentID", studentNumber.getText().toString());
                intent.putExtra("subjectID", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void goAttendance() {
        constraintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, AttendanceView.class);
                intent.putExtra("studentNumber", studentNumber.getText().toString());
                intent.putExtra("parentId", _subjectID.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_right, R.transition.slide_left);
            }
        });
    }

    public void backToStudentListActivity() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getDataFromStudentListRecView() {
        Intent intent = getIntent();
        StudentItems studentItems = intent.getParcelableExtra("Student");

        String subjectCode = intent.getStringExtra("subject");
        String course = intent.getStringExtra("course");
        gradingPeriod = intent.getStringExtra("gradingPeriod");

        //int studentID = studentItems.getId();
        int subjectID = studentItems.getParent_id();

        String lastName = studentItems.getLastname();
        String firstName = studentItems.getFirstname();
        String midtermGrade_ = studentItems.getMidtermGrade();
        String finalGrade_ = studentItems.getFinalGrade();
        String finalRating_ = studentItems.getFinalRating();
        int present = studentItems.getPresent();
        int absent = studentItems.getAbsent();
        String studNumber = studentItems.getStudentNumber();
        String middleName = studentItems.getMiddleName();

        //_id.setText(String.valueOf(studentID));
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
        studentNumber.setText(studNumber);
        _middleName.setText(middleName);

    }

    //IMAGE PICKER THAT SELECT PHOTO FROM CAMERA OR GALLERY
    public void addPhoto() {
        _profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(StudentProfile.this)
                        .galleryOnly()
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    // SET IMAGE FROM CAMERA OR GALLERY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            if (saveImageInDB(uri)) {
                _profileImage.setImageURI(uri);
                finish();
                startActivity(getIntent());
                overridePendingTransition(R.transition.fade_no_fade, R.transition.fade_no_fade);

                loadingAfterUpload();
            }
        } else {
            finish();
            startActivity(getIntent());
            overridePendingTransition(R.transition.fade_no_fade, R.transition.fade_no_fade);
        }


    }

    public void loadingAfterUpload() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(StudentProfile.this, "Upload complete", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }


    // CONVERT IMAGE TO BLOB AND SAVE TO DATABASE
    private boolean saveImageInDB(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] inputData = ImageUtils.getBytes(inputStream);
            DataBaseHelper dbh = new DataBaseHelper(StudentProfile.this);
            dbh.updateProfilePictureAttendanceToday(studentNumber.getText().toString(),
                    inputData);
            dbh.updateProfilePicture(studentNumber.getText().toString(),
                    inputData);


            return true;


        } catch (Exception e) {
            DataBaseHelper dbh = new DataBaseHelper(StudentProfile.this);
            dbh.close();
            return false;
        }
    }

    // DISPLAY IMAGE IN PROFILE FROM DATABASE
    private void displayImage() {
        DataBaseHelper db = new DataBaseHelper(StudentProfile.this);
        SQLiteDatabase sqL = db.getWritableDatabase();
        Cursor cursor = sqL.rawQuery("SELECT * FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_STUDENT_NUMBER_STUDENT + " = '"
                + studentNumber.getText().toString().trim() + "'", null);

        if (cursor.moveToFirst()) {
            byte[] image = cursor.getBlob(9);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            _profileImage.setImageBitmap(bitmap);
            cursor.close();
            db.close();
        }
    }

    private void displayGrade() {
        DataBaseHelper db = new DataBaseHelper(StudentProfile.this);
        SQLiteDatabase sqL = db.getWritableDatabase();
        Cursor cursor = sqL.rawQuery("SELECT * FROM "
                + DataBaseHelper.TABLE_MY_STUDENTS + " WHERE "
                + DataBaseHelper.COLUMN_STUDENT_NUMBER_STUDENT + " = '"
                + studentNumber.getText().toString().trim() + "'", null);

        if (cursor.moveToFirst()) {
            _midtermGrade.setText(cursor.getString(10));
            _finalGrade.setText(cursor.getString(11));
            _finalRating.setText(cursor.getString(12));
            cursor.close();
        }
    }
}