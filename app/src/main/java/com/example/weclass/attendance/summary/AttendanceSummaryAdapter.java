package com.example.weclass.attendance.summary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.ratings.RatingsModel;

import java.util.ArrayList;

public class AttendanceSummaryAdapter extends RecyclerView.Adapter<AttendanceSummaryAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<AttendanceSummaryModel> attendanceSummaryModel;
    String date, gradingPeriod, _studentId, status;
    String _parentId;


    public AttendanceSummaryAdapter(Context context, ArrayList<AttendanceSummaryModel> attendanceSummaryModel, String date, String gradingPeriod) {
        this.context = context;
        this.attendanceSummaryModel = attendanceSummaryModel;
        this.date = date;
        this.gradingPeriod = gradingPeriod;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lastName, firstName, parentId, studentNumber;
        ImageButton late, absent, present;
        ImageView picture;
        ConstraintLayout background;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameAttendanceSummary);
            firstName = itemView.findViewById(R.id.firstNameAttendanceSummary);
            late = itemView.findViewById(R.id.lateButtonAttendanceSummary);
            present = itemView.findViewById(R.id.presentAttendanceSummary);
            absent = itemView.findViewById(R.id.absentAttendanceSummary);
            picture = itemView.findViewById(R.id.pictureAttendanceSummary);
            background = itemView.findViewById(R.id.attendanceBackgroundSummary);
            parentId = itemView.findViewById(R.id.parentIdSummary);
            studentNumber = itemView.findViewById(R.id.studentNumberSummary);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.attendance_summary_recyclerview_style, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AttendanceSummaryModel model = attendanceSummaryModel.get(position);
        byte[] image = model.getPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
        holder.picture.setImageBitmap(bitmap);

        holder.lastName.setText(String.valueOf(attendanceSummaryModel.get(position).getLastName()));
        holder.firstName.setText(String.valueOf(attendanceSummaryModel.get(position).getLastName()));
        _studentId = attendanceSummaryModel.get(position).getStudentNumber();
        _parentId = String.valueOf(attendanceSummaryModel.get(position).getParentId());
        status = attendanceSummaryModel.get(position).getStatus();

        holder.studentNumber.setText(_studentId);
        holder.parentId.setText(_parentId);

        DataBaseHelper db = DataBaseHelper.getInstance(context);

        if (status.equals("Present")) {
            holder.background.setBackgroundResource(R.color.summaryPresent);
        } else if (status.equals("Late")) {
            holder.background.setBackgroundResource(R.color.summaryLate);
        } else {
            holder.background.setBackgroundResource(R.color.summaryAbsent);
        }

        holder.present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.background.setBackgroundResource(R.color.summaryPresent);

                db.updateAttendanceToday(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        date,
                        "Present");

                db.undoAttendance(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        date);

                db.addAttendance(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        holder.lastName.getText().toString(),
                        date,
                        "1",
                        "0",
                        "0",
                        gradingPeriod);

                Toast.makeText(context,"" + holder.lastName.getText().toString() + ", " +holder.firstName.getText().toString() + " is now present",Toast.LENGTH_SHORT).show();
            }
        });

       // Toast.makeText(context, "" + studentId + " " + parentId,Toast.LENGTH_SHORT).show();

        holder.late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.background.setBackgroundResource(R.color.summaryLate);

                db.updateAttendanceToday(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        date,
                        "Late");

                db.undoAttendance(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        date);

                db.addAttendance(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        holder.lastName.getText().toString(),
                        date,
                        "0",
                        "0",
                        "1",
                        gradingPeriod);

                Toast.makeText(context,"" + holder.lastName.getText().toString() + ", " +holder.firstName.getText().toString() + " is now late",Toast.LENGTH_SHORT).show();
            }
        });

        holder.absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.background.setBackgroundResource(R.color.summaryAbsent);

                db.updateAttendanceToday(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        date,
                        "Absent");

                db.undoAttendance(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        date);

                db.addAttendance(holder.studentNumber.getText().toString(),
                        holder.parentId.getText().toString(),
                        holder.lastName.getText().toString(),
                        date,
                        "0",
                        "1",
                        "0",
                        gradingPeriod);

                Toast.makeText(context,"" + holder.lastName.getText().toString() + ", " +holder.firstName.getText().toString() + " is now absent",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return attendanceSummaryModel.size();
    }

}
