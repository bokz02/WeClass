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

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {

    private final ArrayList<ActivitiesItems> activities;
    Context context;

    public ActivitiesAdapter(Context context, ArrayList<ActivitiesItems> activities) {
        this.context = context;
        this.activities = activities;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _taskType, _taskNumber, _taskScore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            _taskType = itemView.findViewById(R.id.activityTextViewRecView);
            _taskNumber = itemView.findViewById(R.id.activityNumberTextViewRecView);
            _taskScore = itemView.findViewById(R.id.activityScoreTextViewRecView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activities_recyclerview_layout, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder._taskType.setText(String.valueOf(activities.get(position).getTaskType()));
        holder._taskNumber.setText(String.valueOf(activities.get(position).getTaskNumber()));
        holder._taskScore.setText(String.valueOf(activities.get(position).getTaskScore()));

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }


}
