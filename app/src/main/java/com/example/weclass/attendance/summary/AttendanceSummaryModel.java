package com.example.weclass.attendance.summary;

public class AttendanceSummaryModel {

    String studentNumber, lastName, firstName;
    int parentId;
    byte[] picture;

    public AttendanceSummaryModel(String studentNumber, String lastName, String firstName, int parentId, byte[] picture){
        this.studentNumber = studentNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.parentId = parentId;
        this.picture = picture;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getParentId() {
        return parentId;
    }

    public byte[] getPicture() {
        return picture;
    }

}
