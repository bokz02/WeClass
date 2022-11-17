package com.example.weclass.taskGrade;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.sql.Ref;
import java.util.ArrayList;

public class TaskGradeFragmentAdapter extends FragmentStateAdapter {

    private final String string;
    private final String taskNumber;
    private final String gradingPeriod;
    private final String subjectId;
    private final String taskId;

    public TaskGradeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String string,
                                    String taskNumber, String gradingPeriod, String subjectId, String taskId) {
        super(fragmentManager, lifecycle);
        this.string = string;
        this.taskNumber = taskNumber;
        this.gradingPeriod = gradingPeriod;
        this.subjectId =subjectId;
        this.taskId =taskId;
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
            bundle.putString("TaskId", taskId);
            TaskGradeViewFragment fragment= new TaskGradeViewFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        Bundle bundle = new Bundle();
        bundle.putString("TaskType", string);
        bundle.putString("TaskNumber", taskNumber);
        bundle.putString("GradingPeriod", gradingPeriod);
        bundle.putString("SubjectId", subjectId);
        bundle.putString("TaskId", taskId);
        TaskGradeFragment taskGradeFragment= new TaskGradeFragment();
        taskGradeFragment.setArguments(bundle);


        return taskGradeFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }



}
