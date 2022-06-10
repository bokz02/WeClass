package com.example.weclass.taskGrade;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.tasks.TaskAdapter;

import java.util.ArrayList;

public class TaskGradeAdapter extends RecyclerView.Adapter<TaskGradeAdapter.MyViewHolder>{

    private final ArrayList<TaskGradeItems> taskGradeItems;
    private final Context context;
    private final OnNoteListener onNoteListener;

    public TaskGradeAdapter(ArrayList<TaskGradeItems> taskGradeItems, Context context, OnNoteListener onNoteListener) {
        this.taskGradeItems = taskGradeItems;
        this.context = context;
        this.onNoteListener = onNoteListener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lastName, firstName, studentID, subjectID;
        ImageButton submitButtonGrade;
        EditText gradeEditText;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameRecViewGrade);
            firstName = itemView.findViewById(R.id.firstNameRecViewGrade);
            submitButtonGrade = itemView.findViewById(R.id.submitButtonRecViewGrade);
            gradeEditText = itemView.findViewById(R.id.gradeEditTextRecViewGrade);
            studentID = itemView.findViewById(R.id.studentIDGrade);
            subjectID = itemView.findViewById(R.id.subjectIDTextViewRecViewGrade);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getAdapterPosition());

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_grade_recycler_style, parent, false);
        return new MyViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaskGradeItems itemsTaskGrade = taskGradeItems.get(position);
        holder.lastName.setText(String.valueOf(taskGradeItems.get(position).getLastName()));
        holder.firstName.setText(String.valueOf(taskGradeItems.get(position).getFirstName()));
        holder.studentID.setText(String.valueOf(taskGradeItems.get(position).getStudentID()));
        holder.subjectID.setText(String.valueOf(taskGradeItems.get(position).getSubjectID()));

    }

    @Override
    public int getItemCount() {
        return taskGradeItems.size();
    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
    }

}
