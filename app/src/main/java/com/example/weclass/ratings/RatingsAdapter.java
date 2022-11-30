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

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.MyViewHolder> {

    private final ArrayList<RatingsModel> ratingsModel;
    private final Context context;

    public RatingsAdapter(ArrayList<RatingsModel> ratingsModel, Context context) {
        this.ratingsModel = ratingsModel;
        this.context = context;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _lastName, _firstName, _rating;
        ImageView _profilePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            _lastName = itemView.findViewById(R.id.lastNameRatingsRecView);
            _firstName = itemView.findViewById(R.id.firstNameRatingsRecView);
            _rating = itemView.findViewById(R.id.finalRatingRatingsRecView);
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
        RatingsModel model = ratingsModel.get(position);

        byte[] image = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);

        holder._profilePicture.setImageBitmap(bitmap);
        holder._lastName.setText(String.valueOf(ratingsModel.get(position).getLastName()));
        holder._firstName.setText(String.valueOf(ratingsModel.get(position).getFirstName()));
        holder._rating.setText(String.valueOf(ratingsModel.get(position).getGrade()));

    }

    @Override
    public int getItemCount() {
        return ratingsModel.size();
    }

}
