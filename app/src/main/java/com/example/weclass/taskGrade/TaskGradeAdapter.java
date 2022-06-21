package com.example.weclass.taskGrade;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.tasks.AddTask;
import com.example.weclass.tasks.TaskAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.EventListener;

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
        TextView lastName, firstName, studentID, subjectID, taskType, taskNumber, gradingPeriod;
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
            taskType = itemView.findViewById(R.id.taskTypeTextViewRecViewGrade);
            taskNumber = itemView.findViewById(R.id.taskNumberRecViewGrade);
            gradingPeriod = itemView.findViewById(R.id.gradingPeriodTextViewRecViewGrade);

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
        holder.taskType.setText(String.valueOf(taskGradeItems.get(position).getTaskType()));
        holder.taskNumber.setText(String.valueOf(taskGradeItems.get(position).getTaskNumber()));
        holder.gradingPeriod.setText(String.valueOf(taskGradeItems.get(position).getGradingPeriod()));

        holder.submitButtonGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper db = new DataBaseHelper(context);
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + " = "
                + holder.studentID.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_PARENT_ID_MY_GRADE + " = "
                        + holder.subjectID.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_TASK_TYPE_MY_GRADE + " = '"
                        + holder.taskType.getText().toString() + "' AND "
                        + DataBaseHelper.COLUMN_TASK_NUMBER_MY_GRADE + " = "
                        + holder.taskNumber.getText().toString() + " AND "
                        + DataBaseHelper.COLUMN_GRADING_PERIOD_MY_GRADE + " = '"
                        + holder.gradingPeriod.getText().toString() + "'", null);

                if(holder.gradeEditText.getText().toString().isEmpty()){
                    Snackbar.make(holder.submitButtonGrade, "Do not submit empty grade!", Snackbar.LENGTH_SHORT).show();

                }else if (cursor.moveToFirst()){

                    Snackbar.make(holder.submitButtonGrade, "You already graded this student!", Snackbar.LENGTH_SHORT).show();
                    cursor.close();
                }
                else {

                    DataBaseHelper dbh = new DataBaseHelper(context);
                    dbh.addGrade(holder.studentID.getText().toString().trim(),
                            holder.subjectID.getText().toString().trim(),
                            holder.lastName.getText().toString().trim(),
                            holder.firstName.getText().toString().trim(),
                            holder.taskType.getText().toString().trim(),
                            holder.taskNumber.getText().toString().trim(),
                            holder.gradeEditText.getText().toString().trim(),
                            holder.gradingPeriod.getText().toString().trim());

                    holder.submitButtonGrade.setEnabled(false);
                    holder.submitButtonGrade.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                    Snackbar.make(holder.submitButtonGrade, "" + holder.lastName.getText().toString() +
                            ", " + holder.firstName.getText().toString() +
                            " successfully graded!", Snackbar.LENGTH_SHORT).show();

                }



            }
        });
    }

    @Override
    public int getItemCount() {
        return taskGradeItems.size();
    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
    }

}
