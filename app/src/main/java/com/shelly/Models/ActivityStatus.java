package com.shelly.Models;

import java.util.Arrays;
import java.util.List;

public class ActivityStatus {

    private String factor;
    private long level;
    private long number;
    private int progress;
    private boolean locked;
    private List<Boolean> taskStatusList;
    private String changeDate;

    public ActivityStatus() {}

    public ActivityStatus(String factor, long level, long number, int progress, boolean locked, List<Boolean> taskStatusList, String changeDate) {
        this.factor = factor;
        this.level = level;
        this.number = number;
        this.progress = progress;
        this.locked = locked;
        this.taskStatusList = taskStatusList;
        this.changeDate = changeDate;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Boolean> getTaskStatusList() {
        return taskStatusList;
    }

    public void setTaskStatusList(List<Boolean> taskStatusList) {
        this.taskStatusList = taskStatusList;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public String toString() {
        return "ActivityStatus{" +
                "factor='" + factor + '\'' +
                ", level=" + level +
                ", number=" + number +
                ", progress=" + progress +
                ", locked=" + locked +
                ", taskStatusList=" + taskStatusList +
                ", changeDate='" + changeDate + '\'' +
                '}';
    }
}
