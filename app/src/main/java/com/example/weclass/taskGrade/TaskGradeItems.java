package com.example.weclass.taskGrade;

import com.example.weclass.studentlist.StudentItems;

import java.util.Comparator;

public class TaskGradeItems {

    String lastName, firstName, taskType, gradingPeriod, studentNumber;
    int studentID, subjectID, taskNumber, taskId, totalItem;


    public TaskGradeItems(String studentNumber, int subjectID, String lastName, String firstName, String taskType,
                          int taskNumber, String gradingPeriod, int taskId, int totalItem) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentNumber = studentNumber;
        this.subjectID = subjectID;
        this.taskType = taskType;
        this.taskNumber = taskNumber;
        this.gradingPeriod = gradingPeriod;
        this.taskId = taskId;
        this.totalItem = totalItem;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getStudentID() {
        return studentNumber;
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

    public int getTotalItem(){
        return totalItem;
    }

    public static Comparator<TaskGradeItems> sortAtoZComparator = new Comparator<TaskGradeItems>() {
        @Override
        public int compare(TaskGradeItems taskGradeItems, TaskGradeItems t1) {
            return taskGradeItems.getLastName().compareTo(t1.getLastName());
        }
    };
}
