package com.example.weclass.taskGrade;

public class TaskGradeItems {

    String lastName, firstName;
    int studentID, subjectID;

    public TaskGradeItems(String lastName, String firstName, int studentID, int subjectID) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentID = studentID;
        this.subjectID = subjectID;
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
}
