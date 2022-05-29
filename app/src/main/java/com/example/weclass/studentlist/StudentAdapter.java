package com.example.weclass.studentlist;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.EditSubjectActivity;
import com.example.weclass.R;
import com.example.weclass.SubjectAdapter;
import com.example.weclass.SubjectItems;
import com.example.weclass.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> implements Filterable {

    private ArrayList<StudentItems> studentItems;
    private ArrayList<StudentItems> studentItemsFull;
    private ArrayList idNumber;
    private Context context;
    private StudentAdapter.OnNoteListener mOnNoteListener;
    SQLiteDatabase sqLiteDatabase;
    String id;

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
                            studentItems.getMiddlename().toLowerCase().contains(filterPattern) ||
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
        View view = inflater.inflate(R.layout.studenlist_recyclerview_style, parent,false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText(String.valueOf(studentItems.get(position).getId()));
        holder.LastnameTxt.setText(String.valueOf(studentItems.get(position).getLastname()));
        holder.MiddlenameTxt.setText(String.valueOf(studentItems.get(position).getMiddlename()));
        holder.FirstnameTxt.setText(String.valueOf(studentItems.get(position).getFirstname()));
        holder.GenderTxt.setText(String.valueOf(studentItems.get(position).getGender()));
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id,LastnameTxt, MiddlenameTxt, FirstnameTxt, GenderTxt;
        ImageButton button;
        StudentAdapter.OnNoteListener onNoteListener;
        public MyViewHolder(@NonNull View itemView, OnNoteListener mOnNoteListener) {
            super(itemView);

            id = itemView.findViewById(R.id.StudPosNumber);
            LastnameTxt = itemView.findViewById(R.id.studentLastnameRecView);
            MiddlenameTxt = itemView.findViewById(R.id.studentMiddleRecView);
            FirstnameTxt = itemView.findViewById(R.id.studentFirstnameRecView);
            GenderTxt = itemView.findViewById(R.id.studentSexRecView);
            button = itemView.findViewById(R.id.studentBtnRecView);

            this.onNoteListener = onNoteListener;

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



