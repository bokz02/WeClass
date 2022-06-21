package com.example.weclass.studentlist.profile.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;

public class ActivitiesFinalsAdapter extends RecyclerView.Adapter<ActivitiesFinalsAdapter.MyViewHolder> {

    private final ArrayList<ActivitiesItems> activitiesItems;
    Context context;

    public ActivitiesFinalsAdapter(ArrayList<ActivitiesItems> activitiesItems, Context context) {
        this.activitiesItems = activitiesItems;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _taskType, _taskNumber, _taskScore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            _taskType = itemView.findViewById(R.id.activityTextViewRecView2);
            _taskNumber = itemView.findViewById(R.id.activityNumberTextViewRecView2);
            _taskScore = itemView.findViewById(R.id.activityScoreTextViewRecView2);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activities_finals_recyclerview_style, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder._taskType.setText(String.valueOf(activitiesItems.get(position).getTaskType()));
        holder._taskNumber.setText(String.valueOf(activitiesItems.get(position).getTaskNumber()));
        holder._taskScore.setText(String.valueOf(activitiesItems.get(position).getTaskScore()));

    }

    @Override
    public int getItemCount() {
        return activitiesItems.size();
    }


}
