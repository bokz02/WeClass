package com.example.weclass.calendar;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;
import java.util.Calendar;

import uk.co.deanwild.flowtextview.FlowTextView;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    Context context;
    ArrayList<CalendarItems> calendarItems;

    public CalendarAdapter(Context context, ArrayList<CalendarItems> calendarItems) {
        this.context =context;
        this.calendarItems = calendarItems;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView task,taskNumber,course, instruction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.taskTextViewCalendarRecView);
            taskNumber = itemView.findViewById(R.id.taskNumberTextViewCalendarRecView);
            course = itemView.findViewById(R.id.courseTextViewCalendarRecView);
            instruction = itemView.findViewById(R.id.instructionTextViewRecView);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.calendar_recyclerview_style, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CalendarItems items = calendarItems.get(position);

        String a = String.valueOf(calendarItems.get(position).getInstruction());
        String b = "                        " + a;
        holder.task.setText(String.valueOf(calendarItems.get(position).getTask()));
        holder.taskNumber.setText(String.valueOf(calendarItems.get(position).getTaskNumber()));
        holder.course.setText(String.valueOf(calendarItems.get(position).getCourse()));
        holder.instruction.setText(b);

    }

    @Override
    public int getItemCount() {
        return calendarItems.size();
    }
}
