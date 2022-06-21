package com.example.weclass.taskGrade;

public class TaskGradeItems {

    String lastName, firstName, taskType, gradingPeriod;
    int studentID, subjectID, taskNumber;


    public TaskGradeItems(int studentID, int subjectID, String lastName, String firstName, String taskType, int taskNumber, String gradingPeriod) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentID = studentID;
        this.subjectID = subjectID;
        this.taskType = taskType;
        this.taskNumber = taskNumber;
        this.gradingPeriod = gradingPeriod;
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
}
