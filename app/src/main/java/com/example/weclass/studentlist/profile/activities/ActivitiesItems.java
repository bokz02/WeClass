package com.example.weclass.studentlist.profile.activities;

import com.example.weclass.studentlist.StudentItems;

import java.util.Comparator;

public class ActivitiesItems {

    private final String taskType;
    private final int taskNumber;
    private final int taskScore;

    public ActivitiesItems(String taskType, int taskNumber, int taskScore) {
        this.taskType = taskType;
        this.taskNumber = taskNumber;
        this.taskScore = taskScore;
    }

    public String getTaskType() {
        return taskType;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public int getTaskScore() {
        return taskScore;
    }


    public static Comparator<ActivitiesItems> sortAtoZComparator = new Comparator<ActivitiesItems>() {
        @Override
        public int compare(ActivitiesItems t1, ActivitiesItems t2) {
            return t1.getTaskType().compareTo(t2.getTaskType());
        }
    };

}
