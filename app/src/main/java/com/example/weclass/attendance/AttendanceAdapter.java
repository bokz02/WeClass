package com.example.weclass.attendance;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.tasks.TaskAdapter;
import com.example.weclass.tasks.TaskItems;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<AttendanceItems> attendanceItems;
    private final ArrayList<AttendanceItems> attendanceItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;

    public AttendanceAdapter(Context context, ArrayList<AttendanceItems> attendanceItems, OnNoteListener mOnNoteListener) {
        this.attendanceItems = attendanceItems;
        this.context = context;
        this.mOnNoteListener = mOnNoteListener;
        attendanceItemsFull = new ArrayList<>(attendanceItems);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView lastName, firstName, gender, id;
        ImageButton absentButton, presentButton;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameAttendanceRecView);
            firstName = itemView.findViewById(R.id.firstNameAttendanceRecView);
            gender = itemView.findViewById(R.id.genderAttendanceRecView);
            id = itemView.findViewById(R.id.idAttendanceRecView);
            absentButton = itemView.findViewById(R.id.absentAttendanceRecView);
            presentButton = itemView.findViewById(R.id.presentAttendanceRecView);


            this.onNoteListener = onNoteListener;
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
        View view = inflater.inflate(R.layout.attendance_recyclerview_style, parent, false);
        return new  MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AttendanceItems itemsAttendance = attendanceItems.get(position);

        holder.id.setText(String.valueOf(attendanceItems.get(position).getId()));
        holder.lastName.setText(String.valueOf(attendanceItems.get(position).getLastName()));
        holder.firstName.setText(String.valueOf(attendanceItems.get(position).getFirstName()));
        holder.gender.setText(String.valueOf(attendanceItems.get(position).getGender()));

        // PRESENT BUTTON
        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.presentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                holder.absentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                holder.absentButton.setEnabled(false);
                Snackbar.make(holder.presentButton, "" + holder.lastName.getText().toString() + " " + holder.firstName.getText().toString() + " is present!", Snackbar.LENGTH_SHORT).show();
            }
        });

        // ABSENT BUTTON
        holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.presentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                holder.absentButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.buttonDisabled));
                holder.absentButton.setEnabled(false);
                Snackbar.make(holder.presentButton, "" + holder.lastName.getText().toString() + " " + holder.firstName.getText().toString() + " is absent!", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return attendanceItems.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    private final Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AttendanceItems> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(attendanceItemsFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (AttendanceItems attendanceItems: attendanceItemsFull){
                    if (attendanceItems.getLastName().toLowerCase().contains(filterPattern) ||
                            attendanceItems.getFirstName().toLowerCase().contains(filterPattern) ||
                            attendanceItems.getGender().toLowerCase().contains(filterPattern)){
                        filteredList.add(attendanceItems);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            attendanceItems.clear();
            attendanceItems.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };

}
