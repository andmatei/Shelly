package com.shelly.Models;

public class ActivityTask {
    private String Title;
    private String Description;
    private int Points;

    public ActivityTask(String title, String description, int points) {
        Title = title;
        Description = description;
        Points = points;
    }

    public ActivityTask() {}

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

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    @Override
    public String toString() {
        return "ActivityTask{" +
                "Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Points=" + Points +
                '}';
    }
}
