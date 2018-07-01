package com.shelly.Models;

import java.util.HashMap;
import java.util.List;

public class UserMember {
    private int Points;
    private HashMap<String, Integer> TestResults;
    private HashMap<String, ActivityStatus> Activities;

    public UserMember() {}

    public UserMember(int points, HashMap<String, Integer> testResults, HashMap<String, ActivityStatus> activities) {
        Points = points;
        TestResults = testResults;
        Activities = activities;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public HashMap<String, Integer> getTestResults() {
        return TestResults;
    }

    public void setTestResults(HashMap<String, Integer> testResults) {
        TestResults = testResults;
    }

    public HashMap<String, ActivityStatus> getActivities() {
        return Activities;
    }

    public void setActivities(HashMap<String, ActivityStatus> activities) {
        Activities = activities;
    }

    @Override
    public String toString() {
        return "UserMember{" +
                "Points=" + Points +
                ", TestResults=" + TestResults +
                ", Activities=" + Activities +
                '}';
    }
}
