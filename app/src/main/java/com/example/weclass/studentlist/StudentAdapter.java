package com.example.weclass.studentlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<StudentItems> studentItems;
    private final ArrayList<StudentItems> studentItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;
    private final ItemCallback itemCallback;
    private final String gradingPeriod, notArchive;
    private final UpdateStudentList update;
    int absentCount;

    public StudentAdapter(Context context, ArrayList<StudentItems> studentItems, OnNoteListener onNoteListener, ItemCallback itemCallback,
                          String gradingPeriod, UpdateStudentList update, String notArchive){
        this.context = context;
        this.studentItems = studentItems;
        this.mOnNoteListener = onNoteListener;
        this.itemCallback = itemCallback;
        studentItemsFull = new ArrayList<>(studentItems);
        this.gradingPeriod = gradingPeriod;
        this.update = update;
        this.notArchive = notArchive;

    }

    @Override
    public Filter getFilter() {
        return studentFilter;
    }

    private final Filter studentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<StudentItems> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(studentItemsFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (StudentItems studentItems: studentItemsFull){
                    String a = studentItems.getLastname() + " " + studentItems.getFirstname();
                    String b = studentItems.getFirstname() + " " + studentItems.getLastname();
                    if (studentItems.getLastname().toLowerCase().contains(filterPattern) ||
                            studentItems.getFirstname().toLowerCase().contains(filterPattern) ||
                            studentItems.getMiddleName().toLowerCase().contains(filterPattern) ||
                            studentItems.getGender().toLowerCase().contains(filterPattern) ||
                            studentItems.getStudentNumber().contains(filterPattern) ||
                            a.contains(filterPattern) ||
                            b.contains(filterPattern)){

                        filteredList.add(studentItems);

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            studentItems.clear();
            studentItems.addAll((List)filterResults.values);

            if(studentItems.size()==0){
                Toast.makeText(context, "Student doesn't exist" , Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();

        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.studentlist_recyclerview_style, parent,false);
        return new MyViewHolder(view, mOnNoteListener, itemCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentItems item = studentItems.get(position);
        DataBaseHelper db = DataBaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        byte[] image = item.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);


        holder.parent_id.setText(String.valueOf(studentItems.get(position).getParent_id()));
        holder.lastNameText.setText(String.valueOf(studentItems.get(position).getLastname()));
        holder.firstNameText.setText(String.valueOf(studentItems.get(position).getFirstname()));
        holder.middleNameText.setText(String.valueOf(studentItems.get(position).getMiddleName()));
        holder.genderText.setText(String.valueOf(studentItems.get(position).getGender()));
        holder.absences.setText(String.valueOf(studentItems.get(position).getAbsent()));
        holder.studentNumber.setText(String.valueOf(studentItems.get(position).getStudentNumber()));
        holder.studentImage.setImageBitmap(bitmap);

        // hide option button in archive
        hideOptionButton(holder);

        Cursor cursor = sqLiteDatabase.rawQuery("select count(*) from " + DataBaseHelper.TABLE_ATTENDANCE + " where "
                + DataBaseHelper.COLUMN_ID_STUDENT_ATTENDANCE + "='"
                + holder.studentNumber.getText().toString() + "' and "
                + DataBaseHelper.COLUMN_SUBJECT_ID_ATTENDANCE + "='"
                + holder.parent_id.getText().toString() + "' and "
                + DataBaseHelper.COLUMN_ABSENT_ATTENDANCE + "="
                + 1 + " and "
                + DataBaseHelper.COLUMN_GRADING_PERIOD_ATTENDANCE + "='"
                + gradingPeriod + "'", null);

        if (cursor.moveToFirst()){
            absentCount = cursor.getInt(0);
        }cursor.close();

        if(absentCount == 4){
            holder.background.setBackgroundResource(R.color.absentWarning1);
        }else if (absentCount == 5){
            holder.background.setBackgroundResource(R.color.absentWarning2);
        }

        // NAVIGATE TO EDIT ACTIVITY, OR DELETE A STUDENT
        holder.optionStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, holder.optionStudent);
                popupMenu.inflate(R.menu.option_subject_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit_subject:

//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                                byte[] dp = stream.toByteArray();

                                Intent intent = new Intent(context, EditStudent.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("parent_id", String.valueOf(item.getParent_id()));
                                bundle.putString("last_name", String.valueOf(item.getLastname()));
                                bundle.putString("first_name", String.valueOf(item.getFirstname()));
                                bundle.putString("middle_name", String.valueOf(item.getMiddleName()));
                                bundle.putString("gender", String.valueOf(item.getGender()));
                                bundle.putString("studentNumber", String.valueOf(item.getStudentNumber()));

                                //intent.putExtra("image",dp);
                                intent.putExtra("Student", bundle);
                                context.startActivity(intent);


                                break;
                            case R.id.delete_subject:
                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                                builder.setTitle("Delete");
                                builder.setIcon(R.drawable.ic_baseline_warning_24);
                                builder.setMessage("Are you sure do you want to delete this?");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        db.deleteStudent(item.getStudentNumber(),
                                                item.getParent_id());
                                        db.deleteAttendanceToday(item.getStudentNumber(),
                                                item.getParent_id());
                                        db.deleteAttendance(item.getStudentNumber(),
                                                item.getParent_id());
                                        db.deleteTotalGrades(item.getStudentNumber(),
                                                item.getParent_id());

                                        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                                + DataBaseHelper.TABLE_MY_GRADE + " WHERE "
                                                + DataBaseHelper.COLUMN_STUDENT_ID_MY_GRADE + "='"
                                                + item.getStudentNumber() + "'",null);

                                        if (cursor.moveToNext()){
                                            db.deleteStudentGrade(item.getStudentNumber(),
                                                    item.getParent_id());
                                        }

                                        int a = holder.getAdapterPosition();
                                        studentItems.remove(a);
                                        notifyItemRemoved(a);
                                        itemCallback.updateTextView();
                                        update.updateRecView();
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                builder.show();

                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id,parent_id, lastNameText, firstNameText, middleNameText, genderText, absences, studentNumber;
        ImageButton button, optionStudent;
        OnNoteListener onNoteListener;
        ImageView studentImage;
        ConstraintLayout background;

        public MyViewHolder(@NonNull View itemView, OnNoteListener mOnNoteListener, ItemCallback itemCallback) {
            super(itemView);

            parent_id = itemView.findViewById(R.id.parentIDStudentList);
            lastNameText = itemView.findViewById(R.id.studentLastnameRecView);
            middleNameText = itemView.findViewById(R.id.studentMiddleRecView);
            firstNameText = itemView.findViewById(R.id.studentFirstnameRecView);
            genderText = itemView.findViewById(R.id.studentSexRecView);
            optionStudent = itemView.findViewById(R.id.optionButtonSubject);
            studentImage = itemView.findViewById(R.id.studentImageRecView);
            absences =itemView.findViewById(R.id.studentListAbsentWarning);
            background = itemView.findViewById(R.id.studentListBackgroundRecView);
            studentNumber = itemView.findViewById(R.id.studentNumberStudentListRecView);

            this.onNoteListener = mOnNoteListener;

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    // UPDATE SUM OF STUDENTS IN STUDENT LIST FRAGMENT
    public interface ItemCallback{
        void updateTextView();
    }

    public interface UpdateStudentList{
        void updateRecView();
    }

    // hide option button in archive
    public void hideOptionButton(MyViewHolder holder){
        if (notArchive.equals("Archive")){
            if(holder.optionStudent.getVisibility() == View.VISIBLE){
                holder.optionStudent.setVisibility(View.GONE);
            }
        }
    }

}



