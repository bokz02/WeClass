package com.example.weclass.studentlist.profile.projects;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weclass.studentlist.profile.exams.fragments.ExamFinals;
import com.example.weclass.studentlist.profile.exams.fragments.ExamMidterm;
import com.example.weclass.studentlist.profile.projects.fragments.ProjectFinals;
import com.example.weclass.studentlist.profile.projects.fragments.ProjectMidterm;

public class ProjectsViewPagerAdapter extends FragmentStateAdapter {
    private final String studentNumber, parentId;
    public ProjectsViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
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
            ProjectMidterm fragment= new ProjectMidterm();
            fragment.setArguments(bundle);
            return fragment;

        }else {
            Bundle bundle = new Bundle();
            bundle.putString("studentNumber", studentNumber);
            bundle.putString("parentId", parentId);
            ProjectFinals fragment= new ProjectFinals();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
