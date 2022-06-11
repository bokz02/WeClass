package com.example.weclass.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DataBaseHelper extends SQLiteOpenHelper {


    private final Context context;
    private static final String DATABASE_NAME = "weClass.db";
    private static final int DATABASE_VERSION = 10;  // JUST INCREMENT DATABASE IF YOU YOU WANT UPDATED DB
    public static final String TABLE_NAME = "my_subjects";
    public static final String COLUMN_ID = "id_number";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_SUBJECT_CODE = "subject_code";
    public static final String COLUMN_SUBJECT_NAME = "subject_name";
    public static final String COLUMN_TIME = "subject_time";
    public static final String COLUMN_DAY = "subject_day";


    public static final String TABLE_NAME2 = "my_students";
    public static final String COLUMN_ID2 = "id_number";
    public static final String COLUMN_PARENT_ID = "parent_id";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_MIDDLE_NAME = "middle_name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PRESENT = "present";
    public static final String COLUMN_ABSENT = "absent";


    public static final String TABLE_NAME3 = "my_schedule";
    public static final String COLUMN_ID3 = "id_number";
    public static final String COLUMN_EVENT_TITLE = "event_title";
    public static final String COLUMN_SCHED_TIME= "sched_ime";
    public static final String COLUMN_SCHED_DAY = "sched_date";

    public static final String TABLE_NAME4 = "my_tasks";
    public static final String COLUMN_ID4 = "id_number";
    public static final String COLUMN_PARENT_ID_SUBJECT = "parent_id";
    public static final String COLUMN_TASK_TYPE = "task_type";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_SCORE = "task_score";
    public static final String COLUMN_DESCRIPTION = "task_description";
    public static final String COLUMN_PROGRESS = "task_progress";
    public static final String COLUMN_TASK_NUMBER = "task_number";

    public static final String TABLE_MY_GRADE = "my_grades";
    public static final String COLUMN_ID_MY_GRADE = "id_number";
    public static final String COLUMN_STUDENT_ID_MY_GRADE = "id_student";
    public static final String COLUMN_PARENT_ID_MY_GRADE = "id_parent";
    public static final String COLUMN_LAST_NAME_MY_GRADE = "last_name";
    public static final String COLUMN_FIRST_NAME_MY_GRADE = "first_name";
    public static final String COLUMN_TASK_TYPE_MY_GRADE = "task_type";
    public static final String COLUMN_TASK_NUMBER_MY_GRADE = "task_number";
    public static final String COLUMN_GRADE_MY_GRADE = "score";

    public static final String TABLE_ATTENDANCE = "my_attendance";
    public static final String COLUMN_ID_ATTENDANCE = "id_number";
    public static final String COLUMN_ID_STUDENT_ATTENDANCE = "id_student";
    public static final String COLUMN_SUBJECT_ID_ATTENDANCE = "id_subject";
    public static final String COLUMN_LAST_NAME_ATTENDANCE = "last_name";
    public static final String COLUMN_PRESENT_ATTENDANCE = "present";
    public static final String COLUMN_ABSENT_ATTENDANCE = "absent";
    public static final String COLUMN_DATE_ATTENDANCE = "date";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    //THIS IS SQLITE DATABASE SCHEME
    // FATA EXCEPTION WILL OCCUR IF SPACES IS NOT WELL ARRANGED
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_COURSE + " TEXT, " +
                        COLUMN_SUBJECT_CODE + " TEXT, " +
                        COLUMN_SUBJECT_NAME + " TEXT, " +
                        COLUMN_DAY + " TEXT, " +
                        COLUMN_TIME + " TEXT);";

        String query2 = "CREATE TABLE " + TABLE_NAME2 +
                " (" + COLUMN_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PARENT_ID + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_MIDDLE_NAME + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_PRESENT + " TEXT, " +
                COLUMN_ABSENT + " TEXT);";

        String query3 = "CREATE TABLE " + TABLE_NAME3 +
                " (" + COLUMN_ID3 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_TITLE + " TEXT, " +
                COLUMN_SCHED_TIME + " TEXT, " +
                COLUMN_SCHED_DAY + " TEXT);";

        String query4 = "CREATE TABLE " + TABLE_NAME4 +
                " (" + COLUMN_ID4 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PARENT_ID_SUBJECT + " TEXT, " +
                COLUMN_TASK_TYPE + " TEXT, " +
                COLUMN_DUE_DATE + " TEXT, " +
                COLUMN_SCORE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PROGRESS + " TEXT, " +
                COLUMN_TASK_NUMBER + " TEXT);";

        String query5 = "CREATE TABLE " + TABLE_MY_GRADE +
                " (" + COLUMN_ID_MY_GRADE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_ID_MY_GRADE + " TEXT, " +
                COLUMN_PARENT_ID_MY_GRADE + " TEXT, " +
                COLUMN_LAST_NAME_MY_GRADE + " TEXT, " +
                COLUMN_FIRST_NAME_MY_GRADE + " TEXT, " +
                COLUMN_TASK_TYPE_MY_GRADE + " TEXT, " +
                COLUMN_TASK_NUMBER_MY_GRADE + " TEXT," +
                COLUMN_GRADE_MY_GRADE + " TEXT);";

        String query6 = "CREATE TABLE " + TABLE_ATTENDANCE +
                " (" + COLUMN_ID_ATTENDANCE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_STUDENT_ATTENDANCE + " TEXT, " +
                COLUMN_SUBJECT_ID_ATTENDANCE + " TEXT, " +
                COLUMN_LAST_NAME_ATTENDANCE + " TEXT, " +
                COLUMN_DATE_ATTENDANCE + " TEXT, " +
                COLUMN_PRESENT_ATTENDANCE + " TEXT, " +
                COLUMN_ABSENT_ATTENDANCE + " TEXT);";

        db.execSQL(query4);
        db.execSQL(query3);
        db.execSQL(query2);
        db.execSQL(query);
        db.execSQL(query5);
        db.execSQL(query6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_GRADE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
    }


    public void addSchedule(String eventTitle, String eventTime, String eventDay){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EVENT_TITLE, eventTitle);
        cv.put(COLUMN_SCHED_TIME, eventTime);
        cv.put(COLUMN_SCHED_DAY, eventDay);

        long result = db.insert(TABLE_NAME3, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }


    public void addStudent(String parentID, String lastName, String firstName, String middleName, String gender, String present, String absent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PARENT_ID, parentID);
        cv.put(COLUMN_LAST_NAME, lastName);
        cv.put(COLUMN_FIRST_NAME, firstName);
        cv.put(COLUMN_MIDDLE_NAME, middleName);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_PRESENT, present);
        cv.put(COLUMN_ABSENT, absent);

        long result = db.insert(TABLE_NAME2, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // ADD QUERY TO MY_TASKS DATABASE
    public void addTask(String parentID, String taskType, String dueDate, String score, String description, String progress, String taskNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PARENT_ID, parentID);
        cv.put(COLUMN_TASK_TYPE, taskType);
        cv.put(COLUMN_DUE_DATE, dueDate);
        cv.put(COLUMN_SCORE, score);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_PROGRESS, progress);
        cv.put(COLUMN_TASK_NUMBER, taskNumber);

        long result = db.insert(TABLE_NAME4, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // ADD QUERY TO SUBJECT DATABASE
    public void addSubject(String course, String subjectCode, String subjectName, String day, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_COURSE, course);
        contentValues.put(COLUMN_SUBJECT_CODE, subjectCode);
        contentValues.put(COLUMN_SUBJECT_NAME, subjectName);
        contentValues.put(COLUMN_DAY, day);
        contentValues.put(COLUMN_TIME, time);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // ADD QUERY TO my_grades DATABASE
    public void addGrade(String idStudent, String idSubject, String lastName, String firstName, String taskType, String taskNumber, String grade){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(COLUMN_STUDENT_ID_MY_GRADE, idStudent);
        contentValues.put(COLUMN_PARENT_ID_MY_GRADE, idSubject);
        contentValues.put(COLUMN_LAST_NAME_MY_GRADE, lastName);
        contentValues.put(COLUMN_FIRST_NAME_MY_GRADE, firstName);
        contentValues.put(COLUMN_TASK_TYPE_MY_GRADE, taskType);
        contentValues.put(COLUMN_TASK_NUMBER_MY_GRADE, taskNumber);
        contentValues.put(COLUMN_GRADE_MY_GRADE, grade);

        long result = db.insert(TABLE_MY_GRADE, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // ADD QUERY TO SUBJECT DATABASE
    public void addAttendance(String idStudent, String idSubject, String lastName, String date, String present, String absent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID_STUDENT_ATTENDANCE, idStudent);
        contentValues.put(COLUMN_SUBJECT_ID_ATTENDANCE, idSubject);
        contentValues.put(COLUMN_LAST_NAME_ATTENDANCE, lastName);
        contentValues.put(COLUMN_DATE_ATTENDANCE, date);
        contentValues.put(COLUMN_PRESENT_ATTENDANCE, present);
        contentValues.put(COLUMN_ABSENT_ATTENDANCE, absent);

        long result = db.insert(TABLE_ATTENDANCE, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // UPDATE DATA OF SUBJECT DATABASE
    public void updateSubject(String id, String course, String subjectCode, String subjectName, String day, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_COURSE, course);
        contentValues.put(COLUMN_SUBJECT_CODE, subjectCode);
        contentValues.put(COLUMN_SUBJECT_NAME, subjectName);
        contentValues.put(COLUMN_DAY, day);
        contentValues.put(COLUMN_TIME, time);
        long result = db.update(TABLE_NAME, contentValues, "id_number=" + id, null);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // UPDATE DATA OF STUDENT DATABASE
    public void updateStudent(String id, String lastName, String firstName, String middleName, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_LAST_NAME, lastName);
        contentValues.put(COLUMN_FIRST_NAME, firstName);
        contentValues.put(COLUMN_MIDDLE_NAME, middleName);
        contentValues.put(COLUMN_GENDER, gender);

        long result = db.update(TABLE_NAME2, contentValues, "id_number=" + id, null);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // UPDATE PRESENT COLUMN OF STUDENT DATABASE
    public void updateStudentPresent(String id, String idSubject,String present){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID2, id);
        contentValues.put(COLUMN_PARENT_ID_SUBJECT, idSubject);
        contentValues.put(COLUMN_PRESENT, present);

        long result = db.update(TABLE_NAME2, contentValues, "id_number=" + id, null);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // UPDATE PRESENT COLUMN OF STUDENT DATABASE
    public void updateStudentAbsent(String id, String idSubject,String absent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID2, id);
        contentValues.put(COLUMN_PARENT_ID_SUBJECT, idSubject);
        contentValues.put(COLUMN_ABSENT, absent);

        long result = db.update(TABLE_NAME2, contentValues, "id_number=" + id, null);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // UPDATE DATA OF STUDENT DATABASE
    public void updateTask(String id, String taskType, String dueDate, String score, String description, String progress, String taskNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TASK_TYPE, taskType);
        contentValues.put(COLUMN_DUE_DATE, dueDate);
        contentValues.put(COLUMN_SCORE, score);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_PROGRESS, progress);
        contentValues.put(COLUMN_TASK_NUMBER, taskNumber);

        long result = db.update(TABLE_NAME4, contentValues, "id_number=" + id, null);
        if(result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // DELETE A SUBJECT
    public void deleteSubject(int row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result =  db.delete(TABLE_NAME, "id_number=?", new String[]{ String.valueOf(row_id)});

        if (result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();

        }
    }

    // DELETE A SUBJECT
    public void deleteStudent(int row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result =  db.delete(TABLE_NAME2, "id_number=?", new String[]{ String.valueOf(row_id)});

        if (result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();

        }
    }

    // DELETE A TASK
    public void deleteTask(int row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result =  db.delete(TABLE_NAME4, "id_number=?", new String[]{ String.valueOf(row_id)});

        if (result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();

        }
    }




}
