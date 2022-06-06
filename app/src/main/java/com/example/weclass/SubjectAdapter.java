package com.example.weclass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.schedule.EventAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> implements Filterable {
    private final ArrayList<SubjectItems> subjectItems;
    private ArrayList<SubjectItems> subjectItemsFull;
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

        // PASS DATA FROM RECYCLERVIEW CLICK TO BOTTOM NAVI ACTIVITY
        holder.subjectClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BottomNavi.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(item.getId()));
                bundle.putString("subject_code", String.valueOf(item.getSubjectCode()));
                bundle.putString("course", String.valueOf(item.getCourse()));

                intent.putExtra("ParentID", bundle);
                context.startActivity(intent);
            }
        });


        // NAVIGATE TO EDIT ACTIVITY, OR DELETE A SUBJECT
        holder.optionSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, holder.optionSubject);
                popupMenu.inflate(R.menu.option_subject_menu);
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


                                intent.putExtra("Userdata", bundle);
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

                                        DataBaseHelper db = new DataBaseHelper(context);
                                        db.deleteSubject(item.getId());

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
                    if (subjectItems.getCourse().toLowerCase().contains(filterPattern) ||
                    subjectItems.getSubjectCode().toLowerCase().contains(filterPattern) ||
                    subjectItems.getSubjectName().toLowerCase().contains(filterPattern)){
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
            notifyDataSetChanged();

        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView id,courseNameTxt, subjectCodeTxt, subjectTitleTxt, dateTxt, timeTxt;
        OnNoteListener onNoteListener;
        ImageButton optionSubject;
        CardView subjectClick;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            id = itemView.findViewById(R.id.positionNumber);
            courseNameTxt = itemView.findViewById(R.id.courseNameRecView);
            subjectCodeTxt = itemView.findViewById(R.id.subjectCodeRecView);
            subjectTitleTxt = itemView.findViewById(R.id.subjectTitleRecView);
            dateTxt = itemView.findViewById(R.id.dateTextViewRecView);
            timeTxt = itemView.findViewById(R.id.timeTextViewRecView);
            optionSubject = itemView.findViewById(R.id.optionButtonSubject);
            subjectClick = itemView.findViewById(R.id.cardViewRecView);
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
