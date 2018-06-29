package com.shelly.Models;

import java.util.List;

public class ActivityResource {
    private String imageURL;
    private List<ActivityTask> tasks;

    public ActivityResource() {}

    public ActivityResource(String imageURL, List<ActivityTask> tasks) {
        this.imageURL = imageURL;
        this.tasks = tasks;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<ActivityTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ActivityTask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "ActivityResource{" +
                "imageURL='" + imageURL + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
