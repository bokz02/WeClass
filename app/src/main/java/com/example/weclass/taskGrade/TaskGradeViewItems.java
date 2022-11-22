package com.example.weclass.taskGrade;

import java.util.Comparator;

public class TaskGradeViewItems {

    String lastName, firstName, taskType, id, gradingPeriod;
    int taskNumber, grade, parentId;


    public TaskGradeViewItems(String lastName, String firstName, String taskType, int taskNumber, int grade, String id, String gradingPeriod, int parentId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.taskType = taskType;
        this.taskNumber = taskNumber;
        this.grade = grade;
        this.id = id;
        this.gradingPeriod = gradingPeriod;
        this.parentId = parentId;
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

    public String getId(){
        return id;
    }

    public String getGradingPeriod(){
        return gradingPeriod;
    }

    public int getParentId(){
        return parentId;
    }

    public static Comparator<TaskGradeViewItems> sortAtoZComparator = new Comparator<TaskGradeViewItems>() {
        @Override
        public int compare(TaskGradeViewItems taskGradeViewItems, TaskGradeViewItems t1) {
            return taskGradeViewItems.getLastName().compareTo(t1.getLastName());
        }
    };
}
