package com.example.weclass.studentlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<StudentItems> studentItems;
    private final ArrayList<StudentItems> studentItemsFull;
    private final Context context;
    private final StudentAdapter.OnNoteListener mOnNoteListener;


    public StudentAdapter(Context context, ArrayList<StudentItems> studentItems, StudentAdapter.OnNoteListener onNoteListener){
        this.context = context;
        this.studentItems = studentItems;
        this.mOnNoteListener = onNoteListener;
        studentItemsFull = new ArrayList<>(studentItems);

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
                filteredList.addAll(studentItems);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (StudentItems studentItems: studentItemsFull){
                    if (studentItems.getLastname().toLowerCase().contains(filterPattern) ||
                            studentItems.getFirstname().toLowerCase().contains(filterPattern) ||
                            studentItems.getMiddleName().toLowerCase().contains(filterPattern) ||
                            studentItems.getGender().toLowerCase().contains(filterPattern)
                    ){
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
            notifyDataSetChanged();

        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.studentlist_recyclerview_style, parent,false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText(String.valueOf(studentItems.get(position).getId()));
        holder.lastNameText.setText(String.valueOf(studentItems.get(position).getLastname()));
        holder.middleNameText.setText(String.valueOf(studentItems.get(position).getMiddleName()));
        holder.firstNameText.setText(String.valueOf(studentItems.get(position).getFirstname()));
        holder.genderText.setText(String.valueOf(studentItems.get(position).getGender()));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id,parent_id, lastNameText, middleNameText, firstNameText, genderText;
        ImageButton button;
        StudentAdapter.OnNoteListener onNoteListener;
        public MyViewHolder(@NonNull View itemView, OnNoteListener mOnNoteListener) {
            super(itemView);

            id = itemView.findViewById(R.id.positionNUmberStudentList);
            lastNameText = itemView.findViewById(R.id.studentLastnameRecView);
            middleNameText = itemView.findViewById(R.id.studentMiddleRecView);
            firstNameText = itemView.findViewById(R.id.studentFirstnameRecView);
            genderText = itemView.findViewById(R.id.studentSexRecView);
            button = itemView.findViewById(R.id.studentBtnRecView);

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

}



