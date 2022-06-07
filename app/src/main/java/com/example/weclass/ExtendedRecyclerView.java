package com.example.weclass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


// THIS EXTENDS RECYCLERVIEW FUNCTION IS ABOUT SHOWING TEXTVIEW AND VIEW WHEN RECYCLERVIEW IS EMPTY
public class ExtendedRecyclerView extends RecyclerView {

    private View emptyView1;
    private TextView emptyTextView1;

    private final AdapterDataObserver adapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateEmptyView();
        }
    };

    public ExtendedRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ExtendedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public  void setEmptyView(View emptyView, TextView textView){
        emptyView1 = emptyView;
        emptyTextView1 = textView;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter){
        if (getAdapter() != null){
            getAdapter().unregisterAdapterDataObserver(adapterDataObserver);
        }
        if (adapter != null){
            adapter.registerAdapterDataObserver(adapterDataObserver);
        }
        super.setAdapter(adapter);
        updateEmptyView();
    }

    private void updateEmptyView(){
        if (emptyView1 != null && emptyTextView1 != null && getAdapter() != null){
            boolean showEmptyView = getAdapter().getItemCount() == 0;
            emptyView1.setVisibility(showEmptyView ? VISIBLE : GONE);
            emptyTextView1.setVisibility(showEmptyView ? VISIBLE : GONE);
            setVisibility(showEmptyView ? GONE : VISIBLE);
        }
    }
}
