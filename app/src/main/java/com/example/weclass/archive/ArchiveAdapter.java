package com.example.weclass.archive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.subject.SubjectItems;

import java.util.ArrayList;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.MyViewHolder> {

    private final ArrayList<ArchiveItems> archiveItems;
    private final Context context;
    private final OnNoteListener mOnNoteListener;

    public ArchiveAdapter(ArrayList<ArchiveItems> archiveItems, Context context, OnNoteListener onNoteListener) {
        this.archiveItems = archiveItems;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView idParent,courseNameTxt, subjectCodeTxt, subjectTitleTxt, dateTxt, timeTxt;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            idParent = itemView.findViewById(R.id.subjectIDArchive);
            courseNameTxt = itemView.findViewById(R.id.courseRecViewArchive);
            subjectCodeTxt = itemView.findViewById(R.id.subjectCodeRecViewArchive);
            subjectTitleTxt = itemView.findViewById(R.id.subjectTitleRecViewArchive);
            dateTxt = itemView.findViewById(R.id.dateTextViewRecViewArchive);
            timeTxt = itemView.findViewById(R.id.timeTextViewRecViewArchive);

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

    }

    @Override
    public int getItemCount() {
        return archiveItems.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);

    }



}
