package com.example.weclass.studentlist.profile.attendance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weclass.studentlist.profile.attendance.attendanceviewpager.AbsentFragment;
import com.example.weclass.studentlist.profile.attendance.attendanceviewpager.LateFragment;
import com.example.weclass.studentlist.profile.attendance.attendanceviewpager.PresentFragment;

public class AttendanceViewPagerAdapter extends FragmentStateAdapter {

    private final String studentNumber;
    private final String parentId;

    public AttendanceViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
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
            PresentFragment fragment= new PresentFragment();
            fragment.setArguments(bundle);

            return fragment;
        }else if (position == 1){
            Bundle bundle = new Bundle();
            bundle.putString("studentNumber", studentNumber);
            bundle.putString("parentId", parentId);
            LateFragment fragment= new LateFragment();
            fragment.setArguments(bundle);

            return fragment;
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("studentNumber", studentNumber);
            bundle.putString("parentId", parentId);
            AbsentFragment fragment= new AbsentFragment();
            fragment.setArguments(bundle);

            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
