package com.example.weclass.taskGrade;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.EditTextSetMinMax;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;

import java.util.ArrayList;

public class TaskGradeViewFragmentAdapter extends RecyclerView.Adapter<TaskGradeViewFragmentAdapter.MyViewHolder> {

    private final ArrayList<TaskGradeViewItems> taskGradeViewItems;
    private final android.content.Context context;
    private final UpdateTaskGradeView update;


    public TaskGradeViewFragmentAdapter(ArrayList<TaskGradeViewItems> taskGradeViewItems, Context context,
                                        UpdateTaskGradeView update){
        this.taskGradeViewItems = taskGradeViewItems;
        this.context = context;
        this.update = update;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lastName, firstName, studentId, gradingPeriod,
                parentId, taskType, taskNumber, totalItems;
        EditText grade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameRecViewGrade);
            firstName = itemView.findViewById(R.id.firstNameRecViewGrade);
            grade = itemView.findViewById(R.id.gradeEditTextViewGrade);
            studentId = itemView.findViewById(R.id.studentIdTextViewTaskGrade);
            gradingPeriod = itemView.findViewById(R.id.gradingPeriodTextViewRecViewGrade);
            parentId = itemView.findViewById(R.id.subjectIDTextViewRecViewGrade);
            taskType = itemView.findViewById(R.id.taskTypeTextViewRecViewGrade);
            taskNumber = itemView.findViewById(R.id.taskNumberRecViewGrade);
            totalItems = itemView.findViewById(R.id.totalItemTaskGradeViewRecView);
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
        holder.taskType.setText(String.valueOf(taskGradeViewItems.get(position).getTaskType()));
        holder.taskNumber.setText(String.valueOf(taskGradeViewItems.get(position).getTaskNumber()));
        holder.gradingPeriod.setText(String.valueOf(taskGradeViewItems.get(position).getGradingPeriod()));
        holder.parentId.setText(String.valueOf(taskGradeViewItems.get(position).getParentId()));
        holder.studentId.setText(String.valueOf(taskGradeViewItems.get(position).getId()));
        holder.totalItems.setText(String.valueOf(taskGradeViewItems.get(position).getTotalItems()));

        String taskType = holder.taskType.getText().toString();
        int toTalItem = Integer.parseInt(holder.totalItems.getText().toString());

        // if task is quiz, total score is based to its total item
        if (taskType.equals("Quiz")){
            holder.grade.setFilters(new InputFilter[]{ new EditTextSetMinMax(0,toTalItem)});
        }else {
            // set min and max value of edit text
            holder.grade.setFilters(new InputFilter[]{ new EditTextSetMinMax(0,100)});
        }


        DataBaseHelper dbh = DataBaseHelper.getInstance(context);

        // automatic save the value in edit text
        holder.grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dbh.updateGrade(holder.studentId.getText().toString(),
                        holder.taskType.getText().toString(),
                        holder.taskNumber.getText().toString(),
                        holder.gradingPeriod.getText().toString(),
                        holder.grade.getText().toString(),
                        holder.parentId.getText().toString());

                update.updateRecView();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return taskGradeViewItems.size();
    }

    public interface UpdateTaskGradeView{
        void updateRecView();
    }
}
