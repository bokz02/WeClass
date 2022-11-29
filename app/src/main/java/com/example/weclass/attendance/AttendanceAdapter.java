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
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private int c;
    private final int a = 1;

    public AttendanceAdapter(Context context, ArrayList<AttendanceItems> attendanceItems, OnNoteListener mOnNoteListener) {
        this.attendanceItems = attendanceItems;
        this.context = context;
        this.mOnNoteListener = mOnNoteListener;
        attendanceItemsFull = new ArrayList<>(attendanceItems);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView lastName, firstName, gender, id, _present,
                _absent, _subjectID, _date, _always1, _always0, late;
        ImageButton absentButton, presentButton, lateButton;
        OnNoteListener onNoteListener;
        ImageView image;
        ConstraintLayout background;


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
            image = itemView.findViewById(R.id.pictureAttendanceRecView);
            background =itemView.findViewById(R.id.attendanceBackgroundRecView);
            late = itemView.findViewById(R.id.lateTextViewAttendanceRecView);
            lateButton = itemView.findViewById(R.id.lateButtonAttendanceRecView);

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

        holder.id.setText(String.valueOf(attendanceItems.get(position).getStudentNumber()));
        holder.lastName.setText(String.valueOf(attendanceItems.get(position).getLastName()));
        holder.firstName.setText(String.valueOf(attendanceItems.get(position).getFirstName()));
        holder._present.setText(String.valueOf(attendanceItems.get(position).getPresent()));
        holder._absent.setText(String.valueOf(attendanceItems.get(position).getAbsent()));
        holder._subjectID.setText(String.valueOf(attendanceItems.get(position).getParentID()));
        holder.image.setImageBitmap(bitmap);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - MMM d, yyyy");
        String date = dateFormat.format(calendar.getTime());
        holder._date.setText(date);
        DataBaseHelper db = new DataBaseHelper(context);

        // BACKGROUND COLOR WILL CHANGE IF IT HITS THE SPECIFIC COUNT
        int d = Integer.parseInt(holder._absent.getText().toString());
        if(d == 4) {
            holder.background.setBackgroundResource(R.color.absentWarning1);
        }else if (d == 5){
            holder.background.setBackgroundResource(R.color.absentWarning2);
        }

        holder.lateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int late = Integer.parseInt(holder.late.getText().toString());
                holder.late.setText(String.valueOf(a + late));

                Snackbar.make(holder.presentButton, "" + holder.lastName.getText().toString() + ", "
                        + holder.firstName.getText().toString() + " is late.", Snackbar.LENGTH_SHORT).show();

                // ADD ATTENDANCE TO ATTENDANCE DATABASE
                db.addAttendance(holder.id.getText().toString(),
                        holder._subjectID.getText().toString(),
                        holder.lastName.getText().toString(),
                        holder._date.getText().toString(),
                        "0",
                        "0",
                        "1");

                db.updateStudentLate(holder.id.getText().toString(),
                        holder._subjectID.getText().toString(),
                        holder.late.getText().toString());

                db.updateAttendanceToday(holder.id.getText().toString(),
                        holder._subjectID.getText().toString(),
                        holder._date.getText().toString());

                c = holder.getAdapterPosition();
                attendanceItems.remove(c);
                notifyItemRemoved(c);
            }
        });


        // PRESENT BUTTON
        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // INCREMENT PRESENT COUNTS OF A STUDENT WHEN PRESENT BUTTON IS PRESSED
                int b = Integer.parseInt(holder._present.getText().toString());
                holder._present.setText(String.valueOf(a + b));


                    Snackbar.make(holder.presentButton, "" + holder.lastName.getText().toString() + ", "
                            + holder.firstName.getText().toString() + " is present.", Snackbar.LENGTH_SHORT).show();

                    // ADD ATTENDANCE TO ATTENDANCE DATABASE
                    db.addAttendance(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder.lastName.getText().toString(),
                            holder._date.getText().toString(),
                            "1",
                            "0",
                            "0");

                    // UPDATE STUDENT'S ATTENDANCE COUNT
                    db.updateStudentPresent(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._present.getText().toString());

                    db.updateAttendanceToday(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._date.getText().toString());

                    c = holder.getAdapterPosition();
                    attendanceItems.remove(c);
                    notifyItemRemoved(c);

                }


        });

        // ABSENT BUTTON
        holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // INCREMENT PRESENT COUNTS OF A STUDENT WHEN PRESENT BUTTON IS PRESSED
                int b = Integer.parseInt(holder._absent.getText().toString());
                holder._absent.setText(String.valueOf(a + b));

                    Snackbar.make(holder.absentButton, "" + holder.lastName.getText().toString() + ", "
                            + holder.firstName.getText().toString() + " is absent.", Snackbar.LENGTH_SHORT).show();

                    // ADD ATTENDANCE TO ATTENDANCE DATABASE
                    db.addAttendance(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder.lastName.getText().toString(),
                            holder._date.getText().toString(),
                            "0",
                            "1",
                            "0");

                    // UPDATE STUDENT'S ATTENDANCE COUNT
                    db.updateStudentAbsent(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._absent.getText().toString());

                    db.updateAttendanceToday(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._date.getText().toString());

                    c = holder.getAdapterPosition();
                    attendanceItems.remove(c);
                    notifyItemRemoved(c);
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
                    String a = attendanceItems.getFirstName() + " " + attendanceItems.getLastName();
                    String b = attendanceItems.getLastName() + " " + attendanceItems.getFirstName();
                    if (attendanceItems.getLastName().toLowerCase().contains(filterPattern) ||
                            attendanceItems.getFirstName().toLowerCase().contains(filterPattern) ||
                            a.contains(filterPattern) ||
                            b.contains(filterPattern)){
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
