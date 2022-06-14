package com.example.weclass.archive;

import android.os.Parcel;
import android.os.Parcelable;

public class ArchiveItems implements Parcelable {

    String course, subjectCode, subjectName, daySubject, timeSubject;
    private final int id_subject;

    public ArchiveItems(int id_subject, String course, String subjectCode, String subjectName, String daySubject, String timeSubject) {
        this.course = course;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.daySubject = daySubject;
        this.timeSubject = timeSubject;
        this.id_subject = id_subject;
    }

    protected ArchiveItems(Parcel in) {
        course = in.readString();
        subjectCode = in.readString();
        subjectName = in.readString();
        daySubject = in.readString();
        timeSubject = in.readString();
        id_subject = in.readInt();
    }

    public static final Creator<ArchiveItems> CREATOR = new Creator<ArchiveItems>() {
        @Override
        public ArchiveItems createFromParcel(Parcel in) {
            return new ArchiveItems(in);
        }

        @Override
        public ArchiveItems[] newArray(int size) {
            return new ArchiveItems[size];
        }
    };

    public String getCourse() {
        return course;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDaySubject() {
        return daySubject;
    }

    public String getTimeSubject() {
        return timeSubject;
    }

    public int getId_subject() {
        return id_subject;
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
        parcel.writeInt(id_subject);
    }
}
