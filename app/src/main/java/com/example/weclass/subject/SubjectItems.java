package com.example.weclass.subject;

import android.os.Parcel;
import android.os.Parcelable;

public class SubjectItems implements Parcelable {
    String course, subjectCode, subjectName, daySubject, timeSubject;
    private int id;

    public SubjectItems(int id ,String course, String subjectCode, String subjectName, String daySubject, String timeSubject){
        this.id = id;
        this.course = course;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.daySubject = daySubject;
        this.timeSubject = timeSubject;
    }

    protected SubjectItems(Parcel in) {
        course = in.readString();
        subjectCode = in.readString();
        subjectName = in.readString();
        daySubject = in.readString();
        timeSubject = in.readString();
        id = in.readInt();
    }

    public static final Creator<SubjectItems> CREATOR = new Creator<SubjectItems>() {
        @Override
        public SubjectItems createFromParcel(Parcel in) {
            return new SubjectItems(in);
        }

        @Override
        public SubjectItems[] newArray(int size) {
            return new SubjectItems[size];
        }
    };

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDaySubject() {
        return daySubject;
    }

    public void setDaySubject(String daySubject) {
        this.daySubject = daySubject;
    }

    public String getTimeSubject() {
        return timeSubject;
    }

    public void setTimeSubject(String timeSubject) {
        this.timeSubject = timeSubject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(course);
        parcel.writeString(subjectCode);
        parcel.writeString(subjectName);
        parcel.writeString(daySubject);
        parcel.writeString(timeSubject);
        parcel.writeInt(id);
    }
}
