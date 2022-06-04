package com.example.weclass.studentlist;

public class StudentItems {
    String lastname, middleName, firstname, gender;
    private int id;
    private int parent_id;

    public StudentItems (int id, int parent_ID, String lastname, String firstname, String middleName, String gender){
        this.id = id;
        this.parent_id = parent_ID;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middleName = middleName;
        this.gender = gender;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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
