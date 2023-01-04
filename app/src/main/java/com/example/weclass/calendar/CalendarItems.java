package com.example.weclass.calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class CalendarItems implements Parcelable {

    String task;
    String taskNumber;
    String course;
    String instruction;
    String gradingPeriod;
    String progress;
    int parentId, taskId;

    public CalendarItems(int parentId, String task, String taskNumber, String course, String instruction,
                         int taskId, String gradingPeriod, String progress){
        this.parentId = parentId;
        this.task = task;
        this.taskNumber=taskNumber;
        this.course=course;
        this.instruction = instruction;
        this.taskId = taskId;
        this.gradingPeriod = gradingPeriod;
        this.progress = progress;

    }

    protected CalendarItems(Parcel in) {
        task = in.readString();
        taskNumber = in.readString();
        course = in.readString();
        instruction = in.readString();
        parentId = in.readInt();
        taskId = in.readInt();
        gradingPeriod = in.readString();
        progress = in.readString();
    }

    public static final Creator<CalendarItems> CREATOR = new Creator<CalendarItems>() {
        @Override
        public CalendarItems createFromParcel(Parcel in) {
            return new CalendarItems(in);
        }

        @Override
        public CalendarItems[] newArray(int size) {
            return new CalendarItems[size];
        }
    };

    public String getTask() {
        return task;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public String getCourse() {
        return course;
    }

    public int getParentId() {
        return parentId;
    }

    public String getInstruction() {
        return instruction;
    }

    public int getTaskId() {
        return taskId;
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
        parcel.writeString(task);
        parcel.writeString(taskNumber);
        parcel.writeString(course);
        parcel.writeString(instruction);
        parcel.writeInt(parentId);
        parcel.writeInt(taskId);
        parcel.writeString(gradingPeriod);
        parcel.writeString(progress);
    }
}
