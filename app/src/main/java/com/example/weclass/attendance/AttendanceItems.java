package com.example.weclass.attendance;

public class AttendanceItems {

    String lastName, firstName, gender;
    int id, parentID;


    public AttendanceItems(int id, int parentID, String lastName, String firstName, String gender) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.id = id;
        this.parentID = parentID;
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

    public int getParentID() {
        return parentID;
    }
}
