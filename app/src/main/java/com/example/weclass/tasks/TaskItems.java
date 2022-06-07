package com.example.weclass.tasks;

public class TaskItems {

    String taskType, dueDate, score, taskDescription;
    private int taskID, parentID;

    public TaskItems(int taskID,int parentID, String taskType, String dueDate, String score, String taskDescription) {
        this.taskType = taskType;
        this.dueDate = dueDate;
        this.score = score;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.parentID = parentID;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
}
