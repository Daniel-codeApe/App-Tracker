package com.example.addressbook;

import com.example.addressbook.schedule.Setup.Date;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public interface IUserDAO {
    void addUser(User user);
    Connection getConnection();
    public void updateUser(User user);
    public void DeleteUser(User user);
    public User GetUser(int id);
    public List<User> getAllUsers();
    Dictionary<String, Integer> getUsageSummary(User user, String option, String purpose);
    ArrayList<String> getStoredApps(User user);
    void saveStartTimeToDatabase(Connection connection, int userID, String appName);
    void saveTimeToDatabase(Connection connection, String appName, int time, User user);
    List<App> fetchAppsFromDatabase(User user);
    void deleteAppFromDatabase(App app, User user);
    void createStartingData(User user);
    void updateSchedule(User user, Date date);
    String getPurpose(User user, String date, int time);
    void DefaultColor(User user);
    void UpdateColor(User user, String purpose, Color color);
    Color getColor(User user, String purpose); // Ensure this method is declared here
}
