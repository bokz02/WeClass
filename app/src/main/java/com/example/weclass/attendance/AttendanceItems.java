package com.example.weclass.attendance;

public class AttendanceItems {

    String lastName, firstName, gender;
    int id;

    public AttendanceItems(String lastName, String firstName, String gender, int id) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }
}
