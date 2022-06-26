package com.example.weclass.ratings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weclass.R;
import com.example.weclass.studentlist.StudentItems;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private final ArrayList<StudentItems> studentItems;
    private final Context context;

    public RatingAdapter(Context context, ArrayList<StudentItems> studentItems) {
        this.context = context;
        this.studentItems = studentItems;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _lastName, _firstName, _finalRating;
        ImageView _profilePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            _lastName = itemView.findViewById(R.id.lastNameRatingsRecView);
            _firstName = itemView.findViewById(R.id.firstNameRatingsRecView);
            _finalRating = itemView.findViewById(R.id.finalRatingRatingsRecView);
            _profilePicture = itemView.findViewById(R.id.imageViewRatingsRecView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ratings_recyclerview_style, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentItems items = studentItems.get(position);

        byte[] image = items.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);

        holder._lastName.setText(String.valueOf(studentItems.get(position).getLastname()));
        holder._firstName.setText(String.valueOf(studentItems.get(position).getFirstname()));
        holder._finalRating.setText(String.valueOf(studentItems.get(position).getFinalRating()));
        holder._profilePicture.setImageBitmap(bitmap);

        if(holder._finalRating.getText().toString().equals("5")){
            holder._finalRating.setTextColor(Color.parseColor("#EF5350"));
        }

        if(holder._finalRating.getText().toString().equals("INC")){
            holder._finalRating.setTextColor(Color.parseColor("#26A69A"));
        }

        if(holder._finalRating.getText().toString().equals("DRP")){
            holder._finalRating.setTextColor(Color.parseColor("#BDBDBD"));
        }

    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }

}
