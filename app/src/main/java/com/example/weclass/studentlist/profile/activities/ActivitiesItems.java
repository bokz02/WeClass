package com.example.weclass.studentlist.profile.activities;

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



}
