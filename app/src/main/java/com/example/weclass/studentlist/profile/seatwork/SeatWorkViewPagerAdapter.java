package com.example.weclass.studentlist.profile.seatwork;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weclass.studentlist.profile.activities.fragments.ActivitiesFinalsFragment;
import com.example.weclass.studentlist.profile.activities.fragments.ActivitiesMidtermFragment;
import com.example.weclass.studentlist.profile.seatwork.fragments.SeatWorkFinals;
import com.example.weclass.studentlist.profile.seatwork.fragments.SeatWorkMidterm;

public class SeatWorkViewPagerAdapter extends FragmentStateAdapter {

    private final String studentNumber, parentId;

    public SeatWorkViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
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
            SeatWorkMidterm fragment= new SeatWorkMidterm();
            fragment.setArguments(bundle);
            return fragment;

        }else {
            Bundle bundle = new Bundle();
            bundle.putString("studentNumber", studentNumber);
            bundle.putString("parentId", parentId);
            SeatWorkFinals fragment= new SeatWorkFinals();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
