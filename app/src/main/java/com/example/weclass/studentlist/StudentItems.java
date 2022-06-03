package com.example.weclass.studentlist;

public class StudentItems {
    String lastname, middleName, firstname, gender;
    private int id;

    public StudentItems (int id, String lastname, String middleName, String firstname, String gender){
        this.id = id;
        this.lastname = lastname;
        this.middleName = middleName;
        this.firstname = firstname;
        this.gender = gender;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddleName(){
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
