package com.example.addressbook;

import java.util.Dictionary;
import java.util.Hashtable;

public class User {
    private String username;
    private String password;
    private String email;
    private int id;
    private Dictionary<String, String> appUsage;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.appUsage = new Hashtable<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
    public String getTableName() {
        return "user" + id;
    }
    public Dictionary<String, String> getAppUsage() {
        return appUsage;
    }
    public void addAppUsage(String appname, String seconds) {
        appUsage.put(appname, seconds);
    }
}
