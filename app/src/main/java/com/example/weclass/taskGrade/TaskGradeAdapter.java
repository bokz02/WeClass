package com.example.weclass.taskGrade;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.EditTextSetMinMax;
import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;

import java.util.ArrayList;

public class TaskGradeAdapter extends RecyclerView.Adapter<TaskGradeAdapter.MyViewHolder>{

    private final ArrayList<TaskGradeItems> taskGradeItems;
    private final Context context;
    int a;

    public TaskGradeAdapter(ArrayList<TaskGradeItems> taskGradeItems, Context context) {
        this.taskGradeItems = taskGradeItems;
        this.context = context;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lastName, firstName, studentID, subjectID, taskType,
                taskNumber, gradingPeriod, taskId, items, totalItems;
        ImageButton submitButtonGrade;
        EditText gradeEditText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameRecViewGrade);
            firstName = itemView.findViewById(R.id.firstNameRecViewGrade);
            submitButtonGrade = itemView.findViewById(R.id.submitButtonRecViewGrade);
            gradeEditText = itemView.findViewById(R.id.gradeEditTextViewGrade);
            studentID = itemView.findViewById(R.id.studentIDGrade);
            subjectID = itemView.findViewById(R.id.subjectIDTextViewRecViewGrade);
            taskType = itemView.findViewById(R.id.taskTypeTextViewRecViewGrade);
            taskNumber = itemView.findViewById(R.id.taskNumberRecViewGrade);
            gradingPeriod = itemView.findViewById(R.id.gradingPeriodTextViewRecViewGrade);
            taskId = itemView.findViewById(R.id.taskIdTaskGradeRecView);
            items = itemView.findViewById(R.id.itemsTaskGradeRecView);
            totalItems = itemView.findViewById(R.id.totalItemsTaskGradeRecView);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_grade_recycler_style, parent, false);
        return new MyViewHolder(view);
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
        holder.taskId.setText(String.valueOf(taskGradeItems.get(position).getTaskId()));
        holder.totalItems.setText(String.valueOf(taskGradeItems.get(position).getTotalItem()));

        String taskType = holder.taskType.getText().toString();
        String item = holder.totalItems.getText().toString();

        // if the task is quiz, the score is set to its total items
        if (taskType.equals("Quiz")){
            holder.items.setText(item);
        }
        // score of a task
        int totalItem = Integer.parseInt(holder.items.getText().toString());

        // filter the min and max value of edit text
        holder.gradeEditText.setFilters(new InputFilter[]{ new EditTextSetMinMax(0,totalItem)});

        // submit button
        holder.submitButtonGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper db = new DataBaseHelper(context);
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                int b;

                if(!holder.gradeEditText.getText().toString().equals("")) {
                    a = Integer.parseInt(holder.gradeEditText.getText().toString());
                }

                // query database
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

                if (cursor.moveToFirst()){

                    Toast.makeText(context, "" + holder.lastName.getText().toString() + ", " + holder.firstName.getText().toString() + " already graded!" , Toast.LENGTH_SHORT).show();
                    cursor.close();

                } else {
                    // update students grade in database
                    db.updateGrade(holder.studentID.getText().toString().trim(),
                            holder.taskType.getText().toString().trim(),
                            holder.taskNumber.getText().toString().trim(),
                            holder.gradingPeriod.getText().toString().trim(),
                            holder.gradeEditText.getText().toString().trim(),
                            holder.subjectID.getText().toString().trim());

                    Toast.makeText(context, "" + holder.lastName.getText().toString() + ", " + holder.firstName.getText().toString() + " successfully graded" , Toast.LENGTH_SHORT).show();

                }
                // remove item in recyclerview and database
                b = holder.getAdapterPosition();
                taskGradeItems.remove(b);
                notifyItemRemoved(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskGradeItems.size();
    }



}
