package com.example.weclass.taskGrade;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;

import java.util.ArrayList;

public class TaskGradeAdapter extends RecyclerView.Adapter<TaskGradeAdapter.MyViewHolder>{

    private final ArrayList<TaskGradeItems> taskGradeItems;
    private final Context context;
    private final ItemCallBack itemCallBack;

    int a;
    int c;


    public TaskGradeAdapter(ArrayList<TaskGradeItems> taskGradeItems, Context context, ItemCallBack itemCallBack) {
        this.taskGradeItems = taskGradeItems;
        this.context = context;
        this.itemCallBack = itemCallBack;

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lastName, firstName, studentID, subjectID, taskType,
                taskNumber, gradingPeriod, taskId;
        ImageButton submitButtonGrade;
        EditText gradeEditText;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, ItemCallBack itemCallBack) {
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
            
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_grade_recycler_style, parent, false);
        return new MyViewHolder(view, itemCallBack);
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

        holder.submitButtonGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper db = new DataBaseHelper(context);
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                int b;

                if(!holder.gradeEditText.getText().toString().equals("")) {
                    a = Integer.parseInt(holder.gradeEditText.getText().toString());
                }

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

                if(holder.gradeEditText.getText().toString().equals("")) {
                    itemCallBack.updateStudentGrades();
                    Toast.makeText(context, "Do not submit empty grade" , Toast.LENGTH_SHORT).show();

                }else if (a < 0 ||a > 100){
                    Toast.makeText(context, "Grades must be within range" , Toast.LENGTH_SHORT).show();

                }else if (cursor.moveToFirst()){


                    Toast.makeText(context, "" + holder.lastName.getText().toString() + ", " + holder.firstName.getText().toString() + " already graded!" , Toast.LENGTH_SHORT).show();
                    cursor.close();

                    b = holder.getAdapterPosition();
                    taskGradeItems.remove(b);
                    notifyItemRemoved(b);

                }
                else {

                    db.updateGrade(holder.studentID.getText().toString().trim(),
                            holder.taskType.getText().toString().trim(),
                            holder.taskNumber.getText().toString().trim(),
                            holder.gradingPeriod.getText().toString().trim(),
                            holder.gradeEditText.getText().toString().trim(),
                            holder.subjectID.getText().toString().trim());

                    Toast.makeText(context, "" + holder.lastName.getText().toString() + ", " + holder.firstName.getText().toString() + " successfully graded" , Toast.LENGTH_SHORT).show();



                    b = holder.getAdapterPosition();
                    taskGradeItems.remove(b);
                    notifyItemRemoved(b);



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

    public interface ItemCallBack{
        void updateStudentGrades();
    }


}
