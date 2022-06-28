package com.example.weclass.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.ratings.RatingAdapter;
import com.example.weclass.schedule.EventItem;

import java.util.ArrayList;

public class HomeScheduleAdapter extends RecyclerView.Adapter<HomeScheduleAdapter.MyViewHolder> {

    private final ArrayList<EventItem> eventItems;
    private final Context context;

    public HomeScheduleAdapter(Context context, ArrayList<EventItem> eventItems) {
        this.context = context;
        this.eventItems = eventItems;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, time, date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.scheduleTitleTextViewRecView);
            time = itemView.findViewById(R.id.scheduleTimeTextViewRecView);
            date = itemView.findViewById(R.id.scheduleDateTextViewRecView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.schedule_recyclerview_home_style, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(String.valueOf(eventItems.get(position).getName()));
        holder.time.setText(String.valueOf(eventItems.get(position).getTime()));
        holder.date.setText(String.valueOf(eventItems.get(position).getDate()));

    }

    @Override
    public int getItemCount() {
        return eventItems.size();
    }


}
