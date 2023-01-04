package com.example.weclass.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    Context context;
    ArrayList<CalendarItems> calendarItems;
    OnClickListener onClickListener;

    public CalendarAdapter(Context context, ArrayList<CalendarItems> calendarItems, OnClickListener onClickListener) {
        this.context =context;
        this.calendarItems = calendarItems;
        this.onClickListener = onClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView task,taskNumber,course, instruction, progress;
        OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            task = itemView.findViewById(R.id.taskTextViewCalendarRecView);
            taskNumber = itemView.findViewById(R.id.taskNumberTextViewCalendarRecView);
            course = itemView.findViewById(R.id.courseTextViewCalendarRecView);
            instruction = itemView.findViewById(R.id.instructionTextViewRecView);
            progress = itemView.findViewById(R.id.progressTextViewCalendar);

            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onNoteClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.calendar_recyclerview_style, parent,false);
        return new MyViewHolder(view, onClickListener);
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

        if(holder.progress.getText().toString().equals("Completed")){
            holder.progress.setTextColor(holder.progress.getContext().getResources().getColor(R.color.lightText));
            holder.progress.setBackgroundResource(R.drawable.rounded_stroke);
        }else{
            holder.progress.setTextColor(holder.progress.getContext().getResources().getColor(R.color.progressColorToDo));
            holder.progress.setBackgroundResource(R.drawable.rounded_stroke_red);
        }

    }

    @Override
    public int getItemCount() {
        return calendarItems.size();
    }

    public interface OnClickListener{
        void onNoteClick(int position);
    }
}
