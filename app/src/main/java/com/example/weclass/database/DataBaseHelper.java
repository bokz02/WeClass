package com.example.weclass.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.weclass.SubjectAdapter;
import com.google.android.material.snackbar.Snackbar;


public class DataBaseHelper extends SQLiteOpenHelper {


    private Context context;
    private static final String DATABASE_NAME = "weClass.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "my_subjects";
    public static final String COLUMN_ID = "id_number";
    public static final String COLUMN_COURSE = "course";
    private static final String COLUMN_SUBJECT_CODE = "subject_code";
    private static final String COLUMN_SUBJECT_NAME = "subject_name";
    private static final String COLUMN_TIME = "subject_time";
    private static final String COLUMN_DAY = "subject_day";


    public static final String TABLE_NAME2 = "my_students";
    public static final String COLUMN_ID2 = "id_number";
    public static final String COLUMN_PARENT_ID = "parent_id";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_MIDDLE_NAME = "middle_name";
    private static final String COLUMN_GENDER = "gender";



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
                COLUMN_GENDER + " TEXT);";

        db.execSQL(query2);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }


    public void addStudent(String parentID, String lastName, String firstName, String middleName, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PARENT_ID, parentID);
        cv.put(COLUMN_LAST_NAME, lastName);
        cv.put(COLUMN_FIRST_NAME, firstName);
        cv.put(COLUMN_MIDDLE_NAME, middleName);
        cv.put(COLUMN_GENDER, gender);

        long result = db.insert(TABLE_NAME2, null, cv);
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

    // UPDATE DATE OF SUBJECT DATABASE
    public void updateData(String id,String course, String subjectCode, String subjectName, String day, String time){
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



}
