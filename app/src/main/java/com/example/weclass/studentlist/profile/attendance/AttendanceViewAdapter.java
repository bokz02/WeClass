package com.example.weclass.studentlist.profile.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;

public class AttendanceViewAdapter extends RecyclerView.Adapter<AttendanceViewAdapter.MyViewHolder> {

    private final ArrayList<PresentAndAbsentItems> presentAndAbsentItems;
    Context context;

    public AttendanceViewAdapter(Context context, ArrayList<PresentAndAbsentItems> presentAndAbsentItems) {
        this.presentAndAbsentItems = presentAndAbsentItems;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            _date = itemView.findViewById(R.id.datePresentAndAbsentTextViewRecView);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.present_absent_recyclerview_style, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder._date.setText(String.valueOf(presentAndAbsentItems.get(position).getDatePresent()));
    }

    @Override
    public int getItemCount() {
        return presentAndAbsentItems.size();
    }


}
