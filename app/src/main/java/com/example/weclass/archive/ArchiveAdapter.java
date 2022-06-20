package com.example.weclass.archive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.subject.SubjectItems;

import java.util.ArrayList;
import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<ArchiveItems> archiveItems;
    private final ArrayList<ArchiveItems> archiveItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;

    public ArchiveAdapter(ArrayList<ArchiveItems> archiveItems, Context context, OnNoteListener onNoteListener) {
        this.archiveItems = archiveItems;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
        archiveItemsFull = new ArrayList<>(archiveItems);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView idParent,courseNameTxt, subjectCodeTxt, subjectTitleTxt, dateTxt, timeTxt, timeEnd, sem, sy;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            idParent = itemView.findViewById(R.id.subjectIDArchive);
            courseNameTxt = itemView.findViewById(R.id.courseRecViewArchive);
            subjectCodeTxt = itemView.findViewById(R.id.subjectCodeRecViewArchive);
            subjectTitleTxt = itemView.findViewById(R.id.subjectTitleRecViewArchive);
            dateTxt = itemView.findViewById(R.id.dateTextViewRecViewArchive);
            timeTxt = itemView.findViewById(R.id.timeTextViewRecViewArchive);
            timeEnd = itemView.findViewById(R.id.timeEndTextViewRecViewArchive);
            sem = itemView.findViewById(R.id.semesterRecViewArchive);
            sy = itemView.findViewById(R.id.schoolYearRecViewArchive);

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
        View view = inflater.inflate(R.layout.archive_recyclerview_style, parent, false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveAdapter.MyViewHolder holder, int position) {

        holder.idParent.setText(String.valueOf(archiveItems.get(position).getId_subject()));
        holder.courseNameTxt.setText(String.valueOf(archiveItems.get(position).getCourse()));
        holder.subjectCodeTxt.setText(String.valueOf(archiveItems.get(position).getSubjectCode()));
        holder.subjectTitleTxt.setText(String.valueOf(archiveItems.get(position).getSubjectName()));
        holder.dateTxt.setText(String.valueOf(archiveItems.get(position).getDaySubject()));
        holder.timeTxt.setText(String.valueOf(archiveItems.get(position).getTimeSubject()));
        holder.timeEnd.setText(String.valueOf(archiveItems.get(position).getTimeEndSubject()));
        holder.sem.setText(String.valueOf(archiveItems.get(position).getSem()));
        holder.sy.setText(String.valueOf(archiveItems.get(position).getSchoolYear()));
    }

    @Override
    public int getItemCount() {
        return archiveItems.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);

    }

    @Override
    public Filter getFilter() {
        return subjectFilter;
    }

    private final Filter subjectFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ArchiveItems> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(archiveItemsFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ArchiveItems archiveItems: archiveItemsFull){
                    if (archiveItems.getCourse().toLowerCase().contains(filterPattern) ||
                            archiveItems.getSubjectCode().toLowerCase().contains(filterPattern) ||
                            archiveItems.getSubjectName().toLowerCase().contains(filterPattern)){
                        filteredList.add(archiveItems);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            archiveItems.clear();
            archiveItems.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };


}
