package com.example.weclass.attendance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<AttendanceItems> attendanceItems;
    private final ArrayList<AttendanceItems> attendanceItemsFull;
    private final Context context;
    private final OnNoteListener mOnNoteListener;
    private final UpdateRecView updateRecView;
    private final String spinnerGradingPeriod, notArchive;
    private int c;

    public AttendanceAdapter(Context context, ArrayList<AttendanceItems> attendanceItems,
                             OnNoteListener mOnNoteListener, UpdateRecView updateRecView,
                             String spinnerGradingPeriod, String notArchive) {
        this.attendanceItems = attendanceItems;
        this.context = context;
        this.mOnNoteListener = mOnNoteListener;
        attendanceItemsFull = new ArrayList<>(attendanceItems);
        this.updateRecView = updateRecView;
        this.spinnerGradingPeriod = spinnerGradingPeriod;
        this.notArchive = notArchive;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView lastName, firstName, gender, id, _present,
                _absent, _subjectID, _date, late;
        ImageButton absentButton, presentButton, lateButton;
        OnNoteListener onNoteListener;
        ImageView image;
        ConstraintLayout background;


        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            lastName = itemView.findViewById(R.id.lastNameAttendanceRecView);
            firstName = itemView.findViewById(R.id.firstNameAttendanceRecView);
            gender = itemView.findViewById(R.id.genderAttendanceRecView);
            id = itemView.findViewById(R.id.idAttendanceRecView);
            absentButton = itemView.findViewById(R.id.absentAttendanceRecView);
            presentButton = itemView.findViewById(R.id.presentAttendanceRecView);
            _present = itemView.findViewById(R.id.presentTextViewAttendanceRecView);
            _absent = itemView.findViewById(R.id.absentTextViewAttendanceRecView);
            _subjectID = itemView.findViewById(R.id.parentIdAttendanceRecView);
            _date = itemView.findViewById(R.id.dateTextViewAttendanceRecView);
            image = itemView.findViewById(R.id.pictureAttendanceRecView);
            background =itemView.findViewById(R.id.attendanceBackgroundRecView);
            late = itemView.findViewById(R.id.lateTextViewAttendanceRecView);
            lateButton = itemView.findViewById(R.id.lateButtonAttendanceRecView);

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

        byte[] image = itemsAttendance.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);

        holder.id.setText(String.valueOf(attendanceItems.get(position).getStudentNumber()));
        holder.lastName.setText(String.valueOf(attendanceItems.get(position).getLastName()));
        holder.firstName.setText(String.valueOf(attendanceItems.get(position).getFirstName()));
        holder._present.setText(String.valueOf(attendanceItems.get(position).getPresent()));
        holder._absent.setText(String.valueOf(attendanceItems.get(position).getAbsent()));
        holder._subjectID.setText(String.valueOf(attendanceItems.get(position).getParentID()));
        holder.image.setImageBitmap(bitmap);

        // hide buttons in archive
        hideOptionButton(holder);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - MMM d, yyyy");
        String date = dateFormat.format(calendar.getTime());
        holder._date.setText(date);
        DataBaseHelper db = DataBaseHelper.getInstance(context);

        // BACKGROUND COLOR WILL CHANGE IF IT HITS THE SPECIFIC COUNT
        int d = Integer.parseInt(holder._absent.getText().toString());
        if(d == 4) {
            holder.background.setBackgroundResource(R.color.absentWarning1);
        }else if (d >= 5){
            holder.background.setBackgroundResource(R.color.absentWarning2);
        }

        holder.lateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo button in snack bar
                Snackbar snackbar = Snackbar.make(holder.lateButton, "" + holder.lastName.getText().toString() + ", "
                        + holder.firstName.getText().toString() + " is late", Snackbar.LENGTH_LONG)
                        .setActionTextColor(context.getColor(R.color.red2))
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                db.undoAttendance(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString(),
                                        date);

                                db.undoStudentLate(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString());

                                db.updateAttendanceToday(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString(),
                                        "date");

                                attendanceItems.add(c, itemsAttendance);
                                notifyItemInserted(c);

                                updateRecView.updateAttendanceRecView();
                            }
                        });
                snackbar.show();

                // ADD ATTENDANCE TO ATTENDANCE DATABASE
                db.addAttendance(holder.id.getText().toString(),
                        holder._subjectID.getText().toString(),
                        holder.lastName.getText().toString(),
                        holder._date.getText().toString(),
                        "0",
                        "0",
                        "1",
                        spinnerGradingPeriod);

                // update student late count
                db.updateStudentLate(holder.id.getText().toString(),
                        holder._subjectID.getText().toString());

                // update the attendance today db
                db.updateAttendanceToday(holder.id.getText().toString(),
                        holder._subjectID.getText().toString(),
                        holder._date.getText().toString());

                c = holder.getAdapterPosition();
                attendanceItems.remove(c);
                notifyItemRemoved(c);

                updateRecView.updateAttendanceRecView();

            }
        });


        // PRESENT BUTTON
        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // undo button in snack bar
                Snackbar snackbar = Snackbar.make(holder.presentButton, "" + holder.lastName.getText().toString() + ", "
                                + holder.firstName.getText().toString() + " is present", Snackbar.LENGTH_LONG)
                        .setActionTextColor(context.getColor(R.color.red2))
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                db.undoAttendance(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString(),
                                        date);

                                db.undoStudentPresent(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString());

                                db.updateAttendanceToday(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString(),
                                        "date");

                                 attendanceItems.add(c, itemsAttendance);
                                 notifyItemInserted(c);

                                updateRecView.updateAttendanceRecView();
                            }
                        });
                snackbar.show();

                    // ADD ATTENDANCE TO ATTENDANCE DATABASE
                    db.addAttendance(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder.lastName.getText().toString(),
                            holder._date.getText().toString(),
                            "1",
                            "0",
                            "0",
                            spinnerGradingPeriod);


                    db.updateStudentPresent(holder.id.getText().toString(),
                            holder._subjectID.getText().toString());

                    db.updateAttendanceToday(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._date.getText().toString());



                    c = holder.getAdapterPosition();
                    attendanceItems.remove(c);
                    notifyItemRemoved(c);

                updateRecView.updateAttendanceRecView();
                }
        });

        // ABSENT BUTTON
        holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo button in snack bar
                Snackbar snackbar = Snackbar.make(holder.absentButton, "" + holder.lastName.getText().toString() + ", "
                                + holder.firstName.getText().toString() + " is absent", Snackbar.LENGTH_LONG)
                        .setActionTextColor(context.getColor(R.color.red2))
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                db.undoAttendance(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString(),
                                        date);

                                db.updateAttendanceToday(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString(),
                                        "date");

                                db.undoStudentAbsent(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString());

                                db.undoStudentAbsentToday(holder.id.getText().toString(),
                                        holder._subjectID.getText().toString());

                                attendanceItems.add(c, itemsAttendance);
                                notifyItemInserted(c);

                                updateRecView.updateAttendanceRecView();
                            }
                        });
                snackbar.show();

                    // ADD ATTENDANCE TO ATTENDANCE DATABASE
                    db.addAttendance(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder.lastName.getText().toString(),
                            holder._date.getText().toString(),
                            "0",
                            "1",
                            "0",
                            spinnerGradingPeriod);

                    // UPDATE STUDENT'S ATTENDANCE COUNT
                    db.updateStudentAbsent(holder.id.getText().toString(),
                            holder._subjectID.getText().toString());

                    db.updateAttendanceToday(holder.id.getText().toString(),
                            holder._subjectID.getText().toString(),
                            holder._date.getText().toString());

                    db.updateStudentAbsentToday(holder.id.getText().toString(),
                            holder._subjectID.getText().toString());


                    c = holder.getAdapterPosition();
                    attendanceItems.remove(c);
                    notifyItemRemoved(c);

                updateRecView.updateAttendanceRecView();
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
                    String a = attendanceItems.getFirstName() + " " + attendanceItems.getLastName();
                    String b = attendanceItems.getLastName() + " " + attendanceItems.getFirstName();
                    if (attendanceItems.getLastName().toLowerCase().contains(filterPattern) ||
                            attendanceItems.getFirstName().toLowerCase().contains(filterPattern) ||
                            a.contains(filterPattern) ||
                            b.contains(filterPattern)){
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

    public interface UpdateRecView{
        void updateAttendanceRecView();
    }

    // hide buttons in archive
    public void hideOptionButton(MyViewHolder holder){
        if (notArchive.equals("Archive")){
            if (holder.presentButton.getVisibility() == View.VISIBLE && holder.lateButton.getVisibility() == View.VISIBLE &&
            holder.absentButton.getVisibility() == View.VISIBLE){
                holder.presentButton.setVisibility(View.GONE);
                holder.absentButton.setVisibility(View.GONE);
                holder.lateButton.setVisibility(View.GONE);
            }
        }

    }

}
