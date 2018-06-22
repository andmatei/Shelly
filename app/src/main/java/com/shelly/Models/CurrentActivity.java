package com.shelly.Models;

import java.util.Arrays;
import java.util.List;

public class CurrentActivity {
    private int Number;
    private String Factor;
    private int Progress;
    private boolean Locked;
    private boolean[] TaskStatusList;

    public CurrentActivity(int number, String factor, int progress, boolean locked, boolean[] taskStatusList) {
        Number = number;
        Factor = factor;
        Progress = progress;
        Locked = locked;
        TaskStatusList = taskStatusList;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getFactor() {
        return Factor;
    }

    public void setFactor(String factor) {
        Factor = factor;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public boolean isLocked() {
        return Locked;
    }

    public void setLocked(boolean locked) {
        Locked = locked;
    }

    public boolean[] getTaskStatusList() {
        return TaskStatusList;
    }

    public void setTaskStatusList(boolean[] taskStatusList) {
        TaskStatusList = taskStatusList;
    }

    @Override
    public String toString() {
        return "CurrentActivity{" +
                "Number=" + Number +
                ", Factor='" + Factor + '\'' +
                ", Progress=" + Progress +
                ", Locked=" + Locked +
                ", TaskStatusList=" + Arrays.toString(TaskStatusList) +
                '}';
    }
}
