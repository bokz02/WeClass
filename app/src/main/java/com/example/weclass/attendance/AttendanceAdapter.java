package com.example.weclass.attendance;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<AttendanceItems> attendanceItems;
    private final ArrayList<AttendanceItems> attendanceItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;

    public AttendanceAdapter(Context context, ArrayList<AttendanceItems> attendanceItems, OnNoteListener mOnNoteListener) {
        this.attendanceItems = attendanceItems;
        this.context = context;
        this.mOnNoteListener = mOnNoteListener;
        attendanceItemsFull = new ArrayList<>(attendanceItems);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView lastName, firstName, gender, id, _present, _absent, _subjectID, _date, _always1, _always0;
        ImageButton absentButton, presentButton;
        OnNoteListener onNoteListener;
        ImageView image;


        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameAttendanceRecView);
            firstName = itemView.findViewById(R.id.firstNameAttendanceRecView);
            gender = itemView.findViewById(R.id.genderAttendanceRecView);
            id = itemView.findViewById(R.id.idAttendanceRecView);
            absentButton = itemView.findViewById(R.id.absentAttendanceRecView);
            presentButton = itemView.findViewById(R.id.presentAttendanceRecView);
            _present = itemView.findViewById(R.id.presentTextViewAttendanceRecView);
            _absent = itemView.findViewById(R.id.absentTextViewAttendanceRecView);
            _subjectID = itemView.findViewById(R.id.parentIdAttendanceRecView);
            _date = itemView.findViewById(R.id.dateTextViewAttendanceRecView);
            _always1 = itemView.findViewById(R.id.always1AttendanceRecView);
            _always0 = itemView.findViewById(R.id.always0AttendanceRecView);
            image = itemView.findViewById(R.id.pictureAttendanceRecView);

            this.onNoteListener = onNoteListener;
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.attendance_recyclerview_style, parent, false);
        return new  MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AttendanceItems itemsAttendance = attendanceItems.get(position);

        byte[] image = itemsAttendance.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);

        holder.id.setText(String.valueOf(attendanceItems.get(position).getId()));
        holder.lastName.setText(String.valueOf(attendanceItems.get(position).getLastName()));
        holder.firstName.setText(String.valueOf(attendanceItems.get(position).getFirstName()));
        holder.gender.setText(String.valueOf(attendanceItems.get(position).getGender()));
        holder._present.setText(String.valueOf(attendanceItems.get(position).getPresent()));
        holder._absent.setText(String.valueOf(attendanceItems.get(position).getAbsent()));
        holder._subjectID.setText(String.valueOf(attendanceItems.get(position).getParentID()));
        holder.image.setImageBitmap(bitmap);

        // PRESENT BUTTON
        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // SET THE CURRENT DATE OF A THE TEXTVIEW BEFORE STORING TO DATABASE
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - MMM d, yyyy");
                String date = dateFormat.format(calendar.getTime());
                holder._date.setText(date);

                // INCREMENT PRESENT COUNTS OF A STUDENT WHEN PRESENT BUTTON IS PRESSED
                int a = 1;
                int b = Integer.parseInt(holder._present.getText().toString());
                holder._present.setText(String.valueOf(a + b));

                DataBaseHelper db = new DataBaseHelper(context);
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                // CURSOR WILL CHECK DATABASE IF ALREADY HAVE DUPLICATE ENTRY
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                        + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                        + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = "
                        + holder.id.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                        + holder._subjectID.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_DATE_ATTENDANCE + " = '"
                        + holder._date.getText().toString() + "' AND "
                        + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + " = "
                        + holder._always1.getText().toString() + " OR "
                        + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + " = "
                        + holder._always1.getText().toString(), null);

                // IF DATABASE HAVE DUPLICATE ENTRY, IT WILL RUN THIS BLOCK
                if (cursor.moveToFirst()) {
                    holder.presentButton.setEnabled(false);
                    holder.absentButton.setEnabled(false);
                    holder.presentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    holder.absentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    Snackbar snackbar = Snackbar.make(holder.presentButton, "" + holder.lastName.getText().toString() + ", "
                            + holder.firstName.getText().toString() + " already have attendance today!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    cursor.close();

                    // ELSE IT WILL STORE TO DATABASE
                } else {
                    holder.presentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    holder.absentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    holder.absentButton.setEnabled(false);
                    Snackbar.make(holder.presentButton, "" + holder.lastName.getText().toString() + ", "
                            + holder.firstName.getText().toString() + " is present!", Snackbar.LENGTH_SHORT).show();

                    // ADD ATTENDANCE TO ATTENDANCE DATABASE
                    db.addAttendance(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder.lastName.getText().toString(),
                            holder._date.getText().toString(),
                            holder._always1.getText().toString(),
                            holder._always0.getText().toString());

                    // UPDATE STUDENT'S ATTENDANCE COUNT
                    db.updateStudentPresent(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._present.getText().toString());
                }
            }
        });

        // PRESENT BUTTON
        holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // SET THE CURRENT DATE OF A THE TEXTVIEW BEFORE STORING TO DATABASE
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - MMM d, yyyy");
                String date = dateFormat.format(calendar.getTime());
                holder._date.setText(date);

                // INCREMENT PRESENT COUNTS OF A STUDENT WHEN PRESENT BUTTON IS PRESSED
                int a = 1;
                int b = Integer.parseInt(holder._absent.getText().toString());
                holder._absent.setText(String.valueOf(a + b));


                DataBaseHelper db = new DataBaseHelper(context);
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                // CURSOR WILL CHECK DATABASE IF ALREADY HAVE DUPLICATE ENTRY
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                        + DataBaseHelper.TABLE_ATTENDANCE + " WHERE "
                        + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + " = "
                        + holder.id.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + " = "
                        + holder._subjectID.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_DATE_ATTENDANCE + " = '"
                        + holder._date.getText().toString() + "' AND "
                        + DataBaseHelper.COLUMN_PRESENT_ATTENDANCE + " = "
                        + holder._always1.getText().toString() + " OR "
                        + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + " = "
                        + holder._always1.getText().toString(), null);


                // IF DATABASE HAVE DUPLICATE ENTRY, IT WILL RUN THIS BLOCK
                if (cursor.moveToFirst()) {
                    holder.presentButton.setEnabled(false);
                    holder.absentButton.setEnabled(false);
                    holder.presentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    holder.absentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    Snackbar.make(holder.absentButton, "" + holder.lastName.getText().toString() + ", "
                            + holder.firstName.getText().toString() + " already have attendance today!", Snackbar.LENGTH_SHORT).show();
                    cursor.close();

                    // ELSE IT WILL STORE TO DATABASE
                } else {
                    holder.presentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    holder.absentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    holder.absentButton.setEnabled(false);
                    Snackbar.make(holder.absentButton, "" + holder.lastName.getText().toString() + ", "
                            + holder.firstName.getText().toString() + " is absent!", Snackbar.LENGTH_SHORT).show();

                    // ADD ATTENDANCE TO ATTENDANCE DATABASE
                    db.addAttendance(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder.lastName.getText().toString(),
                            holder._date.getText().toString(),
                            holder._always0.getText().toString(),
                            holder._always1.getText().toString());


                    // UPDATE STUDENT'S ATTENDANCE COUNT
                    db.updateStudentAbsent(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._absent.getText().toString());
                }
            }
        });






    }

    @Override
    public int getItemCount() {
        return attendanceItems.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    private final Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AttendanceItems> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(attendanceItemsFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (AttendanceItems attendanceItems: attendanceItemsFull){
                    if (attendanceItems.getLastName().toLowerCase().contains(filterPattern) ||
                            attendanceItems.getFirstName().toLowerCase().contains(filterPattern) ||
                            attendanceItems.getGender().toLowerCase().contains(filterPattern)){
                        filteredList.add(attendanceItems);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            attendanceItems.clear();
            attendanceItems.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };

}
