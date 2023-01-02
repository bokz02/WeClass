package com.example.weclass;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class SpinnerAdapter extends ArrayAdapter<String> {

    Typeface font = ResourcesCompat.getFont(getContext(), R.font.poppins_regular);

    public SpinnerAdapter(@NonNull Context context, int resource, String[] period) {
         super(context, resource, period);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ((TextView) view).setTypeface(font);
        ((TextView) view).setTextSize(14);
        ((TextView) view).getResources().getColor(R.color.black1);
        view.setPadding(10,0,10,0);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.SpinnerBgColor));
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        ((TextView) view).setTypeface(font);
        ((TextView) view).setTextSize(16);
        ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.spinnerDropDownText));

        return view;
    }
}
