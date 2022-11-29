package com.example.weclass.subject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.dashboard.MainActivity;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> implements Filterable {
    private final ArrayList<SubjectItems> subjectItems;
    private final ArrayList<SubjectItems> subjectItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;


    public SubjectAdapter(Context context, ArrayList<SubjectItems> subjectItems, OnNoteListener onNoteListener){
        this.context = context;
        this.subjectItems = subjectItems;
        this.mOnNoteListener = onNoteListener;
        subjectItemsFull = new ArrayList<>(subjectItems);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subject_recyclerview_style, parent,false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubjectItems item = subjectItems.get(position);
        holder.id.setText(String.valueOf(subjectItems.get(position).getId()));
        holder.courseNameTxt.setText(String.valueOf(subjectItems.get(position).getCourse()));
        holder.subjectCodeTxt.setText(String.valueOf(subjectItems.get(position).getSubjectCode()));
        holder.subjectTitleTxt.setText(String.valueOf(subjectItems.get(position).getSubjectName()));
        holder.dateTxt.setText(String.valueOf(subjectItems.get(position).getDaySubject()));
        holder.timeTxt.setText(String.valueOf(subjectItems.get(position).getTimeSubject()));
        holder.timeEnd.setText(String.valueOf(subjectItems.get(position).getTimeEndSubject()));
        holder.semester.setText(String.valueOf(subjectItems.get(position).getSemesterSubject()));
        holder.schoolYear.setText(String.valueOf(subjectItems.get(position).getSchoolYearSubject()));
        holder._room.setText(String.valueOf(subjectItems.get(position).get_room()));
        holder.constraintLayout.setBackgroundColor(Color.parseColor(subjectItems.get(position).getColor()));
        holder._section.setText(String.valueOf(subjectItems.get(position).getSection()));
        holder._classType.setText(String.valueOf(subjectItems.get(position).getClassType()));


        // NAVIGATE TO EDIT ACTIVITY, OR DELETE A SUBJECT
        holder.optionSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, holder.optionSubject);
                popupMenu.inflate(R.menu.option_subject_menu2);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit_subject:
                                Intent intent = new Intent(context, EditSubjectActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("id", String.valueOf(item.getId()));
                                bundle.putString("course", item.getCourse());
                                bundle.putString("subject_code", item.getSubjectCode());
                                bundle.putString("subject_name", item.getSubjectName());
                                bundle.putString("day", item.getDaySubject());
                                bundle.putString("time", item.getTimeSubject());
                                bundle.putString("timeEnd", item.getTimeEndSubject());
                                bundle.putString("sem", item.getSemesterSubject());
                                bundle.putString("sy", item.getSchoolYearSubject());
                                bundle.putString("room", item.get_room());
                                bundle.putString("section", item.getSection());
                                bundle.putString("class_type", item.getClassType());



                                intent.putExtra("Userdata", bundle);
                                context.startActivity(intent);


                                break;
                            case R.id.delete_subject:
                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                                builder.setTitle("Delete");
                                builder.setIcon(R.drawable.ic_baseline_warning_24);
                                builder.setMessage("Are you sure you want to delete this? You can't undo this action.");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        DataBaseHelper db = new DataBaseHelper(context);
                                        db.deleteSubject(item.getId());

                                        int a = holder.getAdapterPosition();
                                        subjectItems.remove(a);
                                        notifyItemRemoved(a);
                                        Snackbar.make(holder.optionSubject, "" + holder.subjectCodeTxt.getText().toString() + " successfully deleted!", Snackbar.LENGTH_LONG).show();
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                builder.show();
                                break;
                            case R.id.archive_subject:
                                builder = new MaterialAlertDialogBuilder(context);
                                builder.setTitle("Archive");
                                builder.setIcon(R.drawable.ic_baseline_warning_24);
                                builder.setMessage("Are you sure do you want to put this in archive? You can't undo this action.");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        DataBaseHelper db = new DataBaseHelper(context);

                                        // ADD ATTENDANCE TO ATTENDANCE DATABASE
                                        db.addToArchive(holder.id.getText().toString(),
                                                holder.courseNameTxt.getText().toString(),
                                                holder.subjectCodeTxt.getText().toString(),
                                                holder.subjectTitleTxt.getText().toString(),
                                                holder.dateTxt.getText().toString(),
                                                holder.timeTxt.getText().toString(),
                                                holder.timeEnd.getText().toString(),
                                                holder.semester.getText().toString(),
                                                holder.schoolYear.getText().toString());

                                        db = new DataBaseHelper(context);
                                        db.deleteSubject(item.getId());

                                        Snackbar.make(holder.optionSubject, "" + holder.subjectCodeTxt.getText().toString() + " moved to archive.", Snackbar.LENGTH_LONG).show();

                                        int a = holder.getAdapterPosition();
                                        subjectItems.remove(a);
                                        notifyItemRemoved(a);
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                builder.show();
                                break;

                            case R.id.edit_info:
                                Intent intent1 = new Intent(context, MainActivity.class);

                                Bundle bundle1 = new Bundle();
                                bundle1.putString("id", String.valueOf(item.getId()));
                                bundle1.putString("course", item.getCourse());
                                bundle1.putString("subject_code", item.getSubjectCode());
                                bundle1.putString("subject_name", item.getSubjectName());
                                bundle1.putString("day", item.getDaySubject());
                                bundle1.putString("time", item.getTimeSubject());
                                bundle1.putString("timeEnd", item.getTimeEndSubject());
                                bundle1.putString("sem", item.getSemesterSubject());
                                bundle1.putString("sy", item.getSchoolYearSubject());
                                bundle1.putString("room", item.get_room());
                                bundle1.putString("section", item.getSection());
                                bundle1.putString("class_type", item.getClassType());


                                intent1.putExtra("Userdata", bundle1);
                                context.startActivity(intent1);
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
        return subjectItems.size();
    }


    // FILTER WHEN SEARCHING SUBJECTS
    @Override
    public Filter getFilter() {
        return subjectFilter;
    }

    private final Filter subjectFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SubjectItems> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(subjectItemsFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (SubjectItems subjectItems: subjectItemsFull){
                    String a = subjectItems.getCourse() + " " + subjectItems.getSection();
                    if (subjectItems.getCourse().toLowerCase().contains(filterPattern) ||
                            subjectItems.getSubjectCode().toLowerCase().contains(filterPattern) ||
                            subjectItems.getSubjectName().toLowerCase().contains(filterPattern) ||
                            subjectItems.getDaySubject().toLowerCase().contains(filterPattern) ||
                            subjectItems.getSchoolYearSubject().toLowerCase().contains(filterPattern) ||
                            subjectItems.getSemesterSubject().toLowerCase().contains(filterPattern) ||
                            subjectItems.getClassType().toLowerCase().contains(filterPattern) ||
                            subjectItems.get_room().toLowerCase().contains(filterPattern) ||
                            subjectItems.getSection().toLowerCase().contains(filterPattern) ||
                            subjectItems.getTimeSubject().toLowerCase().contains(filterPattern) ||
                            a.contains(filterPattern)){
                        filteredList.add(subjectItems);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            subjectItems.clear();
            subjectItems.addAll((List)filterResults.values);

            if(subjectItems.size()==0){
                Toast.makeText(context, "Subject doesn't exist" , Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();

        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView id,courseNameTxt, subjectCodeTxt, subjectTitleTxt, dateTxt, timeTxt,
                timeEnd, semester, schoolYear, _room, _section, _classType;
        OnNoteListener onNoteListener;
        ImageButton optionSubject;
        CardView subjectClick;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            id = itemView.findViewById(R.id.positionNumber);
            courseNameTxt = itemView.findViewById(R.id.courseTypeRecView);
            subjectCodeTxt = itemView.findViewById(R.id.subjectCodeRecView);
            subjectTitleTxt = itemView.findViewById(R.id.subjectTitleRecView);
            dateTxt = itemView.findViewById(R.id.dateTextViewRecView);
            timeTxt = itemView.findViewById(R.id.timeTextViewRecView);
            optionSubject = itemView.findViewById(R.id.optionButtonSubject);
            subjectClick = itemView.findViewById(R.id.cardViewRecView);
            timeEnd = itemView.findViewById(R.id.timeEndTextViewRecView);
            semester = itemView.findViewById(R.id.semesterSubjectRecView);
            schoolYear = itemView.findViewById(R.id.schoolYearSubjectRecView);
            _room = itemView.findViewById(R.id.roomTextViewRecView);
            constraintLayout = itemView.findViewById(R.id.constraintBackgroundSubjectRecView);
            _section = itemView.findViewById(R.id.sectionTextViewRecView);
            _classType = itemView.findViewById(R.id.classTypeTextViewRecView);


            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }

    }

    public interface OnNoteListener{
        void onNoteClick(int position);

    }



}