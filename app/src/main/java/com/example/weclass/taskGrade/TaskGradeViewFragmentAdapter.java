package com.example.weclass.taskGrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;

public class TaskGradeViewFragmentAdapter extends RecyclerView.Adapter<TaskGradeViewFragmentAdapter.MyViewHolder> {

    private final ArrayList<TaskGradeViewItems> taskGradeViewItems;
    private final android.content.Context context;

    public TaskGradeViewFragmentAdapter(ArrayList<TaskGradeViewItems> taskGradeViewItems, Context context){
        this.taskGradeViewItems = taskGradeViewItems;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lastName, firstName, grade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameRecViewGrade);
            firstName = itemView.findViewById(R.id.firstNameRecViewGrade);
            grade = itemView.findViewById(R.id.gradeTextViewGrade);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_grade_view_recyclerview_style, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.lastName.setText(String.valueOf(taskGradeViewItems.get(position).getLastName()));
        holder.firstName.setText(String.valueOf(taskGradeViewItems.get(position).getFirstName()));
        holder.grade.setText(String.valueOf(taskGradeViewItems.get(position).getGrade()));

    }

    @Override
    public int getItemCount() {
        return taskGradeViewItems.size();
    }


}
