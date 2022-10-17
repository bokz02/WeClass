package com.example.weclass.taskGrade;

import java.util.Comparator;

public class TaskGradeViewItems {

    String lastName, firstName, taskType;
    int taskNumber, grade;


    public TaskGradeViewItems(String lastName, String firstName, String taskType, int taskNumber, int grade) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.taskType = taskType;
        this.taskNumber = taskNumber;
        this.grade = grade;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getTaskType() {
        return taskType;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public int getGrade() {
        return grade;
    }

    public static Comparator<TaskGradeViewItems> sortAtoZComparator = new Comparator<TaskGradeViewItems>() {
        @Override
        public int compare(TaskGradeViewItems taskGradeViewItems, TaskGradeViewItems t1) {
            return taskGradeViewItems.getLastName().compareTo(t1.getLastName());
        }
    };
}
