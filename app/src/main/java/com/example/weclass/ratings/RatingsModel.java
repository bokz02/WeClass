package com.example.weclass.ratings;

public class RatingsModel {

    byte[] image;
    String lastName, firstName, studentNumber, parentId;
    double grade;

    public RatingsModel(byte[] image, String lastName, String firstName, double grade, String studentNumber, String parentId) {
        this.image = image;
        this.lastName = lastName;
        this.firstName = firstName;
        this.grade = grade;
        this.studentNumber = studentNumber;
        this.parentId = parentId;
    }

    public byte[] getImage() {
        return image;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public double getGrade() {
        return grade;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getParentId() {
        return parentId;
    }


}
