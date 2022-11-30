package com.example.weclass.ratings;

public class RatingsModel {

    byte[] image;
    String lastName, firstName, grade;

    public RatingsModel(byte[] image, String lastName, String firstName, String grade) {
        this.image = image;
        this.lastName = lastName;
        this.firstName = firstName;
        this.grade = grade;
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

    public String getGrade() {
        return grade;
    }


}
