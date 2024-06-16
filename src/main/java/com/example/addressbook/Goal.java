package com.example.addressbook;

import java.util.Dictionary;

public class Goal {

    private String goalName;
    private String goalDesire;

    private String goalTime;

    private int user_id;
    private int id;
    //private Dictionary<String, String> appUsage;

    public Goal(String goalName, String goalDesire, String goalTime, int user_id) {
        this.goalName = goalName;
        this.goalDesire = goalDesire;
        this.goalTime = goalTime;
        this.user_id = user_id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getGoalDesire() {
        return goalDesire;
    }

    public String getGoalTime() {
        return goalTime;
    }

    public String getTableName() {
        return "user" + id;
    }

}
