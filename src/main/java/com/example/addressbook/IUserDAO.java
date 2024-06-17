package com.example.addressbook;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public interface IUserDAO {
//    public void createUserTable(User user);
    public void addUser(User user);
    Connection getConnection();
    public void updateUser(User user);
    public void DeleteUser(User user);
    public User GetUser(int id);
    public List<User> getAllUsers();
    Dictionary<String, Integer> getWeeklyUsage(User user);
    ArrayList<String> getStoredApps(User user);
    void saveStartTimeToDatabase(Connection connection, int userID, String appName);
    void saveTimeToDatabase(Connection connection, String appName, int time, User user);
    List<App> fetchAppsFromDatabase(User user);
    void deleteAppFromDatabase(App app, User user);
}
