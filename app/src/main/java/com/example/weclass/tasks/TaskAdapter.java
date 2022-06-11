package com.example.weclass.tasks;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.EditStudent;
import com.example.weclass.subject.SubjectItems;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<TaskItems> taskItems;
    private final Context context;
    private final OnNoteListener mOnNoteListener;
    private final ArrayList<TaskItems> taskItemsFull;

    public TaskAdapter(Context context, ArrayList<TaskItems> taskItems,OnNoteListener onNoteListener) {
        this.context = context;
        this.taskItems = taskItems;
        this.mOnNoteListener = onNoteListener;
        taskItemsFull = new ArrayList<>(taskItems);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView _id, _parentTD, _taskType, _dueDate, _score, _description,_progress;
        ImageButton _optionTask, _expand;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            _id = itemView.findViewById(R.id.taskNumberRecView);
            _taskType = itemView.findViewById(R.id.taskTypeRecView);
            _dueDate = itemView.findViewById(R.id.deadLineRecView);
            _score = itemView.findViewById(R.id.scoreTextViewRecView);
            _description = itemView.findViewById(R.id.descriptionHiddenTextView);
            _optionTask = itemView.findViewById(R.id.optionButtonTaskRecView);
            _expand = itemView.findViewById(R.id.expandRecyclerView);
            _parentTD = itemView.findViewById(R.id.parentIDTaskRecView);
            _progress = itemView.findViewById(R.id.progressTextView);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

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
        View view = inflater.inflate(R.layout.task_recyclerview_style, parent, false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaskItems itemsTask = taskItems.get(position);

        holder._parentTD.setText(String.valueOf(taskItems.get(position).getParentID()));
        holder._taskType.setText(String.valueOf(taskItems.get(position).getTaskType()));
        holder._dueDate.setText(String.valueOf(taskItems.get(position).getDueDate()));
        holder._score.setText(String.valueOf(taskItems.get(position).getScore()));
        holder._description.setText(String.valueOf(taskItems.get(position).getTaskDescription()));
        holder._progress.setText(String.valueOf(taskItems.get(position).getProgress()));
        holder._id.setText(String.valueOf(taskItems.get(position).getTaskNumber()));

        if(holder._progress.getText().toString().equals("Completed")){
            holder._progress.setTextColor(holder._progress.getContext().getResources().getColor(R.color.progressColorDone));

        }else{
            holder._progress.setTextColor(holder._progress.getContext().getResources().getColor(R.color.progressColorToDo));
        }
        // EXPAND CARD VIEW WHEN CLICKED
        holder._expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConstraintLayout layout = holder.itemView.findViewById(R.id.hiddenDescription);
                TransitionManager.beginDelayedTransition(layout, new AutoTransition());
                layout.setVisibility( layout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                if(layout.getVisibility() == View.VISIBLE){
                    holder._expand.setImageResource(R.drawable.ic_up);

                }
                else {
                    holder._expand.setImageResource(R.drawable.ic_arrow_down1);
                }
                notifyDataSetChanged();
            }
        });

        // POPUP OPTION MENU WHEN CLICKED THE 3 DOT BUTTON
        holder._optionTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, holder._optionTask);
                popupMenu.inflate(R.menu.option_subject_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit_subject:
                                Intent intent = new Intent(context, EditTask.class);
                                Bundle bundle = new Bundle();

                                bundle.putString("task_id", String.valueOf(itemsTask.getTaskID()));
                                bundle.putString("task_number", String.valueOf(itemsTask.getTaskNumber()));
                                bundle.putString("task_type", String.valueOf(itemsTask.getTaskType()));
                                bundle.putString("task_progress", String.valueOf(itemsTask.getProgress()));
                                bundle.putString("task_date", String.valueOf(itemsTask.getDueDate()));
                                bundle.putString("task_score", String.valueOf(itemsTask.getScore()));
                                bundle.putString("task_description", String.valueOf(itemsTask.getTaskDescription()));

                                intent.putExtra("Task", bundle);
                                context.startActivity(intent);


                                break;
                            case R.id.delete_subject:
                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                                builder.setTitle("Delete");
                                builder.setIcon(R.drawable.ic_baseline_warning_24);
                                builder.setMessage("Are you sure do you want to delete this?");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        DataBaseHelper db = new DataBaseHelper(context);
                                        db.deleteTask(itemsTask.getTaskID());

                                        int a = holder.getAdapterPosition();
                                        taskItems.remove(a);
                                        notifyItemRemoved(a);
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                builder.show();

                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return taskItems.size();
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
            List<TaskItems> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(taskItemsFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (TaskItems taskItems: taskItemsFull){
                    if (taskItems.getTaskType().toLowerCase().contains(filterPattern) ||
                            taskItems.getDueDate().toLowerCase().contains(filterPattern) ||
                            taskItems.getTaskDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(taskItems);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            taskItems.clear();
            taskItems.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };

}
