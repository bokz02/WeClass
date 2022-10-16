package com.example.weclass.taskGrade;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TaskGradeFragmentAdapter extends FragmentStateAdapter {

    private final String string;
    private final String taskNumber;
    private final String gradingPeriod;
    private final String subjectId;

    public TaskGradeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String string, String taskNumber, String gradingPeriod, String subjectId) {
        super(fragmentManager, lifecycle);
        this.string = string;
        this.taskNumber = taskNumber;
        this.gradingPeriod = gradingPeriod;
        this.subjectId =subjectId;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1 ){
            Bundle bundle = new Bundle();
            bundle.putString("TaskType", string);
            bundle.putString("TaskNumber", taskNumber);
            bundle.putString("GradingPeriod", gradingPeriod);
            bundle.putString("SubjectId", subjectId);
            TaskGradeViewFragment fragment= new TaskGradeViewFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        Bundle bundle = new Bundle();
        bundle.putString("TaskType", string);
        bundle.putString("TaskNumber", taskNumber);
        bundle.putString("GradingPeriod", gradingPeriod);
        bundle.putString("SubjectId", subjectId);
        TaskGradeFragment taskGradeFragment= new TaskGradeFragment();
        taskGradeFragment.setArguments(bundle);

        return taskGradeFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
