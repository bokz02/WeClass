package com.example.weclass.attendance.summary;

import com.example.weclass.attendance.AttendanceItems;

import java.util.Comparator;

public class AttendanceSummaryModel {

    String studentNumber, lastName, firstName, status;
    int parentId;
    byte[] picture;

    public AttendanceSummaryModel(String studentNumber, String lastName, String firstName, int parentId, byte[] picture, String status){
        this.studentNumber = studentNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.parentId = parentId;
        this.picture = picture;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public static Comparator<AttendanceSummaryModel> sortAtoZComparator = new Comparator<AttendanceSummaryModel>() {
        @Override
        public int compare(AttendanceSummaryModel attendanceSummaryModel, AttendanceSummaryModel t1) {
            return attendanceSummaryModel.getLastName().compareTo(t1.getLastName());
        }
    };

}
