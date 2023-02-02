package com.example.weclass.ratings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;

import java.util.ArrayList;
import java.util.Locale;

public class FinalRatingsAdapter extends RecyclerView.Adapter<FinalRatingsAdapter.MyViewHolder> {

    private final ArrayList<RatingsModel> ratingsModel;
    private final Context context;
    OnStudentClick onStudentClick;
    Double finalGrade;
    String notGraded;

    public FinalRatingsAdapter(ArrayList<RatingsModel> ratingsModel, Context context, OnStudentClick onStudentClick) {
        this.ratingsModel = ratingsModel;
        this.context = context;
        this.onStudentClick = onStudentClick;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView _lastName, _firstName, _rating, studentNumber, parentId, remarks;
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
            remarks = itemView.findViewById(R.id.remarksTextViewRatings);

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
        View view = inflater.inflate(R.layout.ratings_finals_recyclerview_style, parent, false);

        return new MyViewHolder(view, onStudentClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RatingsModel model = ratingsModel.get(position);

        byte[] image = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);

        notGraded = String.valueOf(ratingsModel.get(position).getGrade());
        if (notGraded.equals("-") || notGraded.equals("INC") || notGraded.equals("DRP")){

            holder._profilePicture.setImageBitmap(bitmap);
            holder._lastName.setText(String.valueOf(ratingsModel.get(position).getLastName()));
            holder._firstName.setText(String.valueOf(ratingsModel.get(position).getFirstName()));
            holder._rating.setText(ratingsModel.get(position).getGrade());
            holder.studentNumber.setText(String.valueOf(ratingsModel.get(position).getStudentNumber()));
            holder.parentId.setText(String.valueOf(ratingsModel.get(position).getParentId()));

        } else {

            finalGrade = Double.parseDouble(ratingsModel.get(position).getGrade());

            holder._profilePicture.setImageBitmap(bitmap);
            holder._lastName.setText(String.valueOf(ratingsModel.get(position).getLastName()));
            holder._firstName.setText(String.valueOf(ratingsModel.get(position).getFirstName()));
            holder._rating.setText(String.format(Locale.US, "%.2f", finalGrade));
            holder.studentNumber.setText(String.valueOf(ratingsModel.get(position).getStudentNumber()));
            holder.parentId.setText(String.valueOf(ratingsModel.get(position).getParentId()));

            String a = holder._rating.getText().toString();
            if (a.equals("5.00")) {
                holder._rating.setTextColor(holder._rating.getContext().getResources().getColor(R.color.red2));
            }

            if (finalGrade >= 1.00 && finalGrade <= 3.00) {
                holder.remarks.setText("Passed");
                holder.remarks.setTextColor(holder.remarks.getContext().getResources().getColor(R.color.remarksPassed));
            } else if (finalGrade >= 3.10 && finalGrade <= 5.00) {
                holder.remarks.setText("Failed");
                holder.remarks.setTextColor(holder.remarks.getContext().getResources().getColor(R.color.remarksFailed));
            }
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
