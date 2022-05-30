package com.example.weclass.schedule;

import static com.example.weclass.schedule.CalendarUtils.formattedTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weclass.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        TextView eventCelltime = convertView.findViewById(R.id.eventCellTime);
        TextView eventCellDay = convertView.findViewById(R.id.eventCellDay);

        String eventTime = formattedTime(event.getTime());
        String eventTitle = event.getName();
        String eventDay = CalendarUtils.formattedDate(event.getDate());
        eventCellTV.setText(eventTitle);
        eventCelltime.setText(eventTime);
        eventCellDay.setText(eventDay);
        return convertView;
    }
}
