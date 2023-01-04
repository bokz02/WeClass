package com.example.weclass.ratings;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingsModel implements Parcelable {

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

    protected RatingsModel(Parcel in) {
        image = in.createByteArray();
        lastName = in.readString();
        firstName = in.readString();
        studentNumber = in.readString();
        parentId = in.readString();
        grade = in.readDouble();
    }

    public static final Creator<RatingsModel> CREATOR = new Creator<RatingsModel>() {
        @Override
        public RatingsModel createFromParcel(Parcel in) {
            return new RatingsModel(in);
        }

        @Override
        public RatingsModel[] newArray(int size) {
            return new RatingsModel[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(image);
        parcel.writeString(lastName);
        parcel.writeString(firstName);
        parcel.writeString(studentNumber);
        parcel.writeString(parentId);
        parcel.writeDouble(grade);
    }
}
