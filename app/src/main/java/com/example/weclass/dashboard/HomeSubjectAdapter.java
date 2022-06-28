package com.example.weclass.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.subject.EditSubjectActivity;
import com.example.weclass.subject.SubjectAdapter;
import com.example.weclass.subject.SubjectItems;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeSubjectAdapter extends RecyclerView.Adapter<HomeSubjectAdapter.MyViewHolder> implements Filterable {
    private final ArrayList<SubjectItems> subjectItems;
    private final ArrayList<SubjectItems> subjectItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;


    public HomeSubjectAdapter(Context context, ArrayList<SubjectItems> subjectItems, OnNoteListener onNoteListener){
        this.context = context;
        this.subjectItems = subjectItems;
        this.mOnNoteListener = onNoteListener;
        subjectItemsFull = new ArrayList<>(subjectItems);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView id,courseNameTxt, subjectCodeTxt, subjectTitleTxt, dateTxt, timeTxt, timeEnd, semester, schoolYear;
        OnNoteListener onNoteListener;
        CardView subjectClick;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            id = itemView.findViewById(R.id.positionNumberHome);
            courseNameTxt = itemView.findViewById(R.id.taskTypeRecViewHome);
            subjectCodeTxt = itemView.findViewById(R.id.subjectCodeRecViewHome);
            subjectTitleTxt = itemView.findViewById(R.id.subjectTitleRecViewHome);

            subjectClick = itemView.findViewById(R.id.cardViewRecViewHome);

            constraintLayout = itemView.findViewById(R.id.constraintBackgroundSubjectRecViewHome);


            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subject_recyclerview_home_style, parent,false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubjectItems item = subjectItems.get(position);
        holder.id.setText(String.valueOf(subjectItems.get(position).getId()));
        holder.courseNameTxt.setText(String.valueOf(subjectItems.get(position).getCourse()));
        holder.subjectCodeTxt.setText(String.valueOf(subjectItems.get(position).getSubjectCode()));
        holder.subjectTitleTxt.setText(String.valueOf(subjectItems.get(position).getSubjectName()));

        holder.constraintLayout.setBackgroundColor(Color.parseColor(subjectItems.get(position).getColor()));

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



    public interface OnNoteListener{
        void onNoteClick(int position);

    }
}
