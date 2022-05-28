package com.example.weclass.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Subjects.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "my_subjects";
    public static final String COLUMN_ID = "id_number";
    public static final String COLUMN_COURSE = "course";
    private static final String COLUMN_SUBJECT_CODE = "subject_code";
    private static final String COLUMN_SUBJECT_NAME = "subject_name";
    private static final String COLUMN_TIME = "subject_time";
    private static final String COLUMN_DAY = "subject_day";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    //THIS IS SQLITE DABATASE SCHEME
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

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

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
        } else {
            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
