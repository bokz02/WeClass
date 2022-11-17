package com.example.weclass.taskGrade;

import com.example.weclass.studentlist.StudentItems;

import java.util.Comparator;

public class TaskGradeItems {

    String lastName, firstName, taskType, gradingPeriod;
    int studentID, subjectID, taskNumber, taskId;


    public TaskGradeItems(int studentID, int subjectID, String lastName, String firstName, String taskType,
                          int taskNumber, String gradingPeriod, int taskId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentID = studentID;
        this.subjectID = subjectID;
        this.taskType = taskType;
        this.taskNumber = taskNumber;
        this.gradingPeriod = gradingPeriod;
        this.taskId = taskId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public String getTaskType() {
        return taskType;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public String getGradingPeriod() {
        return gradingPeriod;
    }

    public int getTaskId() {
        return taskId;
    }

    public static Comparator<TaskGradeItems> sortAtoZComparator = new Comparator<TaskGradeItems>() {
        @Override
        public int compare(TaskGradeItems taskGradeItems, TaskGradeItems t1) {
            return taskGradeItems.getLastName().compareTo(t1.getLastName());
        }
    };
}
