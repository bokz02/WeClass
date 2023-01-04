package com.example.weclass.ratings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.database.DataBaseHelper;
import com.example.weclass.studentlist.StudentItems;

import java.util.ArrayList;
import java.util.Locale;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.MyViewHolder> {

    private final ArrayList<RatingsModel> ratingsModel;
    private final Context context;
    OnStudentClick onStudentClick;

    public RatingsAdapter(ArrayList<RatingsModel> ratingsModel, Context context, String classType, OnStudentClick onStudentClick) {
        this.ratingsModel = ratingsModel;
        this.context = context;
        this.onStudentClick = onStudentClick;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView _lastName, _firstName, _rating, studentNumber, parentId;
        ImageView _profilePicture;
        OnStudentClick onStudentClick;

        public MyViewHolder(@NonNull View itemView, OnStudentClick onStudentClick) {
            super(itemView);

            _lastName = itemView.findViewById(R.id.lastNameRatingsRecView);
            _firstName = itemView.findViewById(R.id.firstNameRatingsRecView);
            _rating = itemView.findViewById(R.id.finalRatingRatingsRecView);
            _profilePicture = itemView.findViewById(R.id.imageViewRatingsRecView);
            studentNumber = itemView.findViewById(R.id.studentNumberRatings);
            parentId = itemView.findViewById(R.id.parentIdRatings);

            this.onStudentClick = onStudentClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onStudentClick.onStudentClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ratings_recyclerview_style, parent, false);

        return new MyViewHolder(view, onStudentClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RatingsModel model = ratingsModel.get(position);

        byte[] image = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);

        holder._profilePicture.setImageBitmap(bitmap);
        holder._lastName.setText(String.valueOf(ratingsModel.get(position).getLastName()));
        holder._firstName.setText(String.valueOf(ratingsModel.get(position).getFirstName()));
        holder._rating.setText(String.format( Locale.US,"%.2f", ratingsModel.get(position).getGrade()));
        holder.studentNumber.setText(String.valueOf(ratingsModel.get(position).getStudentNumber()));
        holder.parentId.setText(String.valueOf(ratingsModel.get(position).getParentId()));


        String a = holder._rating.getText().toString();
        if (a.equals("5.00")){
            holder._rating.setTextColor(holder._rating.getContext().getResources().getColor(R.color.red2));
        }
    }

    @Override
    public int getItemCount() {
        return ratingsModel.size();
    }

    public interface OnStudentClick{
        void onStudentClick(int position);
    }

}
