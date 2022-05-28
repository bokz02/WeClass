package com.example.weclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.database.DataBaseHelper;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {
    private Context context;
    private ArrayList courseName, subjectCode, subjectName, date, time;

    SubjectAdapter(Context context, ArrayList courseName, ArrayList subjectCode,
                   ArrayList subjectName, ArrayList date, ArrayList time){
        this.context = context;
        this.courseName = courseName;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.date = date;
        this.time = time;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subject_recyclerview_style, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.courseNameTxt.setText(String.valueOf(courseName.get(position)));
        holder.subjectCodeTxt.setText(String.valueOf(subjectCode.get(position)));
        holder.subjectTitleTxt.setText(String.valueOf(subjectName.get(position)));
        holder.dateTxt.setText(String.valueOf(date.get(position)));
        holder.timeTxt.setText(String.valueOf(time.get(position)));
    }

    @Override
    public int getItemCount() {
        return courseName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView courseNameTxt, subjectCodeTxt, subjectTitleTxt, dateTxt, timeTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTxt = itemView.findViewById(R.id.courseNameRecView);
            subjectCodeTxt = itemView.findViewById(R.id.subjectCodeRecView);
            subjectTitleTxt = itemView.findViewById(R.id.subjectTitleRecView);
            dateTxt = itemView.findViewById(R.id.dateTextViewRecView);
            timeTxt = itemView.findViewById(R.id.timeTextViewRecView);

        }
    }


}
