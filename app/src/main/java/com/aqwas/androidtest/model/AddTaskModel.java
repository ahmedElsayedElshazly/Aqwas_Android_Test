package com.aqwas.androidtest.model;

public class AddTaskModel {
    private String Title;
    private String DueDate;
    private String Priority;
    private String Description;

    public AddTaskModel(String title, String dueDate, String priority, String description) {
        Title = title;
        DueDate = dueDate;
        Priority = priority;
        Description = description;
    }

    public AddTaskModel(String title, String dueDate, String priority) {
        Title = title;
        DueDate = dueDate;
        Priority = priority;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }
}
