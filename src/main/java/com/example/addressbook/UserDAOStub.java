package com.example.addressbook;

import com.example.addressbook.schedule.Setup.Date;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.util.*;

public class UserDAOStub implements IUserDAO {
    private List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public Connection getConnection() {
        // As this is a stub, we don't have a real connection to return
        return null;
    }

    @Override
    public void updateUser(User user) {
        // For simplicity, we just add the user to the list
        users.add(user);
    }

    @Override
    public void DeleteUser(User user) {
        users.remove(user);
    }

    @Override
    public User GetUser(int id) {
        // Return a hard-coded user for simplicity
        return new User("username", "email", "password");
    }

    @Override
    public List<User> getAllUsers() {
        // Return the list of users
        return users;
    }

    @Override
    public Dictionary<String, Integer> getUsageSummary(User user, String option, String purpose) {
        // Return a hard-coded dictionary for simplicity
        Dictionary<String, Integer> usage = new Hashtable<>();
        usage.put("app1", 10);
        usage.put("app2", 20);
        return usage;
    }

    @Override
    public ArrayList<String> getStoredApps(User user) {
        return null;
    }

    public void createStartingData(User user) {

    }

    @Override
    public void updateSchedule(User user, Date date) {

    }

    @Override
    public String getPurpose(User user, String date, int time) {
        return null;
    }

    @Override
    public void saveStartTimeToDatabase(Connection connection, int userID, String appName) {

    }

    public void DefaultColor(User user) {

    }

    @Override
    public void deleteAppFromDatabase(App app, User user) {
        // Since this is a stub, we don't need to do anything here
    }

    @Override
    public List<App> fetchAppsFromDatabase(User user) {
        // Since this is a stub, we don't need to do anything here
        // Return an empty list
        return new ArrayList<>();
    }

    @Override
    public void saveTimeToDatabase(Connection connection, String appName, int time, User user) {
        // Since this is a stub, we don't need to do anything here
    }

    public void UpdateColor(User user, String purpose, Color color) {

    }

    @Override
    public Color getColor(User user, String purpose) {
        return null;
    }
}