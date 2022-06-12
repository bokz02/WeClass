package com.example.weclass.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private final ArrayList<EventItem> eventItems;
    private ArrayList<EventItem> eventItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;


    public EventAdapter(Context context, ArrayList<EventItem> eventItems, OnNoteListener mOnNoteListener) {
        this.context = context;
        this.eventItems = eventItems;
        this.mOnNoteListener = mOnNoteListener;

    }

    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_cell, parent, false);
        return  new EventAdapter.MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        
        holder.eventTitle.setText(String.valueOf(eventItems.get(position).getName()));
        holder.eventTime.setText(String.valueOf(eventItems.get(position).getTime()));
        holder.eventDay.setText(String.valueOf(eventItems.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
//        return (eventItems == null) ? 0: eventItems.size();

        return eventItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, eventTitle, eventTime, eventDay;
        OnNoteListener onNoteListener;

        public MyViewHolder(View itemView, OnNoteListener OnNoteListener) {
            super(itemView);

            eventTitle = itemView.findViewById(R.id.eventCellTV);
            eventDay = itemView.findViewById(R.id.eventCellDay);
            eventTime = itemView.findViewById(R.id.eventCellTime);
            this.onNoteListener = OnNoteListener;

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

