package com.example.weclass.attendance.summary;

import android.content.Context;
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
import com.example.weclass.ratings.RatingsModel;

import java.util.ArrayList;

public class AttendanceSummaryAdapter extends RecyclerView.Adapter<AttendanceSummaryAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<AttendanceSummaryModel> attendanceSummaryModel;
    String date, gradingPeriod, studentId;


    public AttendanceSummaryAdapter(Context context, ArrayList<AttendanceSummaryModel> attendanceSummaryModel, String date, String gradingPeriod) {
        this.context = context;
        this.attendanceSummaryModel = attendanceSummaryModel;
        this.date = date;
        this.gradingPeriod = gradingPeriod;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lastName, firstName;
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
        studentId = attendanceSummaryModel.get(position).getStudentNumber();

    }

    @Override
    public int getItemCount() {
        return attendanceSummaryModel.size();
    }

}
