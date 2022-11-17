package com.example.weclass.calendar;

public class CalendarItems {

    String task;
    String taskNumber;
    String course;
    String instruction;
    int id;

    public CalendarItems(int id, String task, String taskNumber, String course, String instruction){
        this.id = id;
        this.task = task;
        this.taskNumber=taskNumber;
        this.course=course;
        this.instruction = instruction;

    }

    public String getTask() {
        return task;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public String getCourse() {
        return course;
    }

    public int getId() {
        return id;
    }

    public String getInstruction() {
        return instruction;
    }
}
