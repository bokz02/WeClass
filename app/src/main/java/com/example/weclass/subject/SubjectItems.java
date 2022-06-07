package com.example.weclass.subject;

public class SubjectItems {
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
}
