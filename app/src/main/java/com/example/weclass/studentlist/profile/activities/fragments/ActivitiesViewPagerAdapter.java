package com.example.weclass.studentlist.profile.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ActivitiesViewPagerAdapter extends FragmentStateAdapter {

    private final String studentNumber, parentId;

    public ActivitiesViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
                                      String studentNumber, String parentId) {
        super(fragmentManager, lifecycle);
        this.studentNumber = studentNumber;
        this.parentId = parentId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0 ){
            Bundle bundle = new Bundle();
            bundle.putString("studentNumber", studentNumber);
            bundle.putString("parentId", parentId);
            ActivitiesMidtermFragment fragment= new ActivitiesMidtermFragment();
            fragment.setArguments(bundle);
            return fragment;

        }else {
            Bundle bundle = new Bundle();
            bundle.putString("studentNumber", studentNumber);
            bundle.putString("parentId", parentId);
            ActivitiesFinalsFragment fragment= new ActivitiesFinalsFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
