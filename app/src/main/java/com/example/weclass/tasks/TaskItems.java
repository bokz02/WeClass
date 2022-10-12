package com.example.weclass.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.weclass.studentlist.StudentItems;
import com.example.weclass.studentlist.profile.activities.ActivitiesItems;

import java.util.Comparator;

public class TaskItems implements Parcelable {

    String taskType, dueDate, score, taskDescription, progress, gradingPeriod;
    private int taskID;
    private int parentID;
    private final int taskNumber;


    public TaskItems(int taskID,
                     int parentID,
                     String taskType,
                     String dueDate,
                     String score,
                     String taskDescription,
                     String progress,
                     int taskNumber,
                     String gradingPeriod) {


        this.taskType = taskType;
        this.dueDate = dueDate;
        this.score = score;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.parentID = parentID;
        this.progress = progress;
        this.taskNumber = taskNumber;
        this.gradingPeriod = gradingPeriod;
    }

    protected TaskItems(Parcel in) {
        taskType = in.readString();
        dueDate = in.readString();
        score = in.readString();
        taskDescription = in.readString();
        progress = in.readString();
        taskID = in.readInt();
        parentID = in.readInt();
        taskNumber = in.readInt();
        gradingPeriod = in.readString();
    }

    public static final Creator<TaskItems> CREATOR = new Creator<TaskItems>() {
        @Override
        public TaskItems createFromParcel(Parcel in) {
            return new TaskItems(in);
        }

        @Override
        public TaskItems[] newArray(int size) {
            return new TaskItems[size];
        }
    };

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public String getGradingPeriod() {
        return gradingPeriod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(taskType);
        parcel.writeString(dueDate);
        parcel.writeString(score);
        parcel.writeString(taskDescription);
        parcel.writeString(progress);
        parcel.writeInt(taskID);
        parcel.writeInt(parentID);
        parcel.writeInt(taskNumber);
        parcel.writeString(gradingPeriod);
    }

    public static Comparator<TaskItems> sortAtoZComparator = new Comparator<TaskItems>() {
        @Override
        public int compare(TaskItems t1, TaskItems t2) {
            return t1.getTaskType().compareTo(t2.getTaskType());
        }
    };

    public static Comparator<TaskItems> sortZtoAComparator = new Comparator<TaskItems>() {
        @Override
        public int compare(TaskItems t2, TaskItems t1) {
            return t1.getGradingPeriod().compareTo(t2.getGradingPeriod());
        }
    };
}
