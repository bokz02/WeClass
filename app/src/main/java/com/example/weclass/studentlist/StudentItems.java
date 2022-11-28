package com.example.weclass.studentlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class StudentItems implements Parcelable {
    String lastname, middleName, firstname, gender, finalGrade,
            midtermGrade, finalRating, studentNumber;
    private final int present;
    private final int absent;
    private int parent_id;
    private byte[] image;



    public StudentItems (String studentNumber, int parent_ID, String lastname,
                         String firstname, String middleName, String gender,
                         int present, int absent, byte[] image,
                         String midtermGrade,String finalGrade, String finalRating){


        this.parent_id = parent_ID;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middleName = middleName;
        this.gender = gender;
        this.present = present;
        this.absent = absent;
        this.image = image;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
        this.finalRating = finalRating;
        this.studentNumber = studentNumber;
    }

    protected StudentItems(Parcel in) {
        lastname = in.readString();
        middleName = in.readString();
        firstname = in.readString();
        gender = in.readString();
        present = in.readInt();
        absent = in.readInt();
        parent_id = in.readInt();
        midtermGrade = in.readString();
        finalGrade = in.readString();
        finalRating = in.readString();
        studentNumber = in.readString();
    }

    public static final Creator<StudentItems> CREATOR = new Creator<StudentItems>() {
        @Override
        public StudentItems createFromParcel(Parcel in) {
            return new StudentItems(in);
        }

        @Override
        public StudentItems[] newArray(int size) {
            return new StudentItems[size];
        }
    };

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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

    public String getGender() {
        return gender;
    }

    public int getPresent() {
        return present;
    }

    public int getAbsent() {
        return absent;
    }

    public byte[] getImage(){
        return image;
    }

    public String getMidtermGrade() {
        return midtermGrade;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public String getFinalRating() {
        return finalRating;
    }

    public String getStudentNumber(){
        return studentNumber;
    }

    public static Comparator<StudentItems> sortAtoZComparator = new Comparator<StudentItems>() {
        @Override
        public int compare(StudentItems studentItems, StudentItems t1) {
            return studentItems.getLastname().compareTo(t1.getLastname());
        }
    };

    public static Comparator<StudentItems> sortZtoAComparator = new Comparator<StudentItems>() {
        @Override
        public int compare(StudentItems studentItems, StudentItems t1) {
            return t1.getLastname().compareTo(studentItems.getLastname());
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lastname);
        parcel.writeString(middleName);
        parcel.writeString(firstname);
        parcel.writeString(gender);
        parcel.writeInt(present);
        parcel.writeInt(absent);
        parcel.writeInt(parent_id);
        parcel.writeString(midtermGrade);
        parcel.writeString(finalGrade);
        parcel.writeString(finalRating);
        parcel.writeString(studentNumber);
    }
}
