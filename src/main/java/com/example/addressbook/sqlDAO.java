package com.example.addressbook;

import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class sqlDAO implements IUserDAO{

    private Connection connection;

    public sqlDAO() {
        connection = sqlConnection.getInstance();
        createTable();
        createUserAppsTable(); // Create the user_apps table
        createAppLogsTable(); // Create the app_logs table
    }

    public Connection getConnection() {
        return connection;
    }

    private void createUserAppsTable() {
        // Create user_apps table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS user_apps ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "app_name VARCHAR NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES users(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAppLogsTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS app_logs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL," +
                    "app_id INTEGER, " +
                    "start_time TEXT, " +
                    "stop_time TEXT, " +
                    "duration INTEGER, " +
                    "FOREIGN KEY(app_id) REFERENCES user_apps(id))";
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL,"
                    + "password VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dictionary<String, Integer> getWeeklyUsage(User user) {
        Dictionary<String, Integer> weeklyUsage = new Hashtable<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT app_id, duration " +
                    "FROM app_logs " +
                    "WHERE stop_time " +
                    "BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime')"
            );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int app_id = resultSet.getInt("app_id");
                int duration = resultSet.getInt("duration");
                PreparedStatement getAppName = connection.prepareStatement("SELECT app_name " +
                        "FROM user_apps " +
                        "WHERE user_id = ? AND id = ?");
                getAppName.setInt(1, user.getId());
                getAppName.setInt(2, app_id);
                ResultSet names = getAppName.executeQuery();
                if (names.next()) {
                    String appName = names.getString("app_name");
                    weeklyUsage.put(appName, duration);
                }
                names.close();
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyUsage;
    }

    @Override
    public ArrayList<String> getStoredApps(User user) {
        ArrayList<String> result = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT app_name " +
                    "FROM user_apps " +
                    "WHERE user_id = ?");
            statement.setInt(1, user.getId());
            ResultSet storedApps = statement.executeQuery();
            while (storedApps.next()) {
                if (!result.contains(storedApps.getString("app_name")))
                {
                    result.add(storedApps.getString("app_name"));
                }
                //storedApps.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveStartTimeToDatabase(Connection connection, int userID, String appName) {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println(formattedNow);
        // Save the start time to the database
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO " +
                    "app_logs (user_id, app_id, start_time) " +
                    "SELECT ?, id, ? " +
                    "FROM user_apps " +
                    "WHERE app_name = ?");
            pstmt.setInt(1, userID);
            pstmt.setString(2, formattedNow);
            pstmt.setString(3, appName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTimeToDatabase(Connection connection, String appName, int time, User user) {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println(formattedNow);
        // Save the stop time and duration to the database
        try {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE app_logs " +
                    "SET stop_time = ?, duration = ? " +
                    "WHERE app_id = (SELECT id FROM user_apps WHERE app_name = ? AND user_id = ?) " +
                    "AND stop_time IS NULL");
            pstmt.setString(1, formattedNow);
            pstmt.setInt(2, time);
            pstmt.setString(3, appName);
            pstmt.setInt(4, user.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<App> fetchAppsFromDatabase(User user) {
        List<App> apps = new ArrayList<>();

        try {
            // Connect to the database
            //Connection connection = DriverManager.getConnection("jdbc:sqlite:users.db");

            // Create a Statement
            Statement statement = connection.createStatement();

            // Execute a query to fetch the apps linked to the current user
            ResultSet resultSet = statement.executeQuery("SELECT app_name " +
                    "FROM user_apps " +
                    "WHERE user_id = " + user.getId());

            // Convert the result into a list of App objects
            while (resultSet.next()) {
                String name = resultSet.getString("app_name");
                App app = new App(name);
                apps.add(app);
            }

            // Close the resources
            //resultSet.close();
            //statement.close();
            //connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return apps;
    }

    @Override
    public void deleteAppFromDatabase(App app, User user) {
        try {
            // Create a Statement
            Statement statement = connection.createStatement();

            // Execute a query to delete the app from the user_apps table
            statement.executeUpdate("DELETE FROM user_apps " +
                    "WHERE user_id = " + user.getId() +
                    " AND app_name = '" + app.getName() + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users " +
                    "(username, email, password) "
                    + "VALUES (?, ?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users " +
                    "SET username = ?, email = ?, password = ? " +
                    "WHERE id = ?");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User GetUser(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(username, email, password);
                user.setId(id);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(username, email, password);
                user.setId(id);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


    public List<Map<String, Object>> getAppLogsForUser(User user) {
        List<Map<String, Object>> appLogs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM app_logs WHERE user_id = ?");
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> columns = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnName(i), resultSet.getObject(i));
                }

                appLogs.add(columns);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appLogs;
    }





//    /**
//     * Update the correlated table when the user is using the app is already recorded
//     * @param tableName the combination of id and name of current user, also the name of the table
//     * @param appName the name of app the needed to update usage time
//     * @param seconds the time spent on this app, the add operation should take place before using this method
//     */
//    @Override
//    public void updateAppUsage(String tableName, String appName, int seconds) {
//        try {
//            PreparedStatement pstmt = connection.prepareStatement("UPDATE "+ tableName +
//                    " SET duration = ? WHERE appName = ?");
//            pstmt.setInt(1, seconds);
//            pstmt.setString(2, appName);
//            pstmt.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Insert new app if it doesn't exist in database, the usage time starts as 0
//     * @param tableName the id or current user, also the name of the table
//     * @param appName the name of app the needed to be inserted*/
//    @Override
//    public void insertAppUsage(String tableName, String appName) {
//        try {
//            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO " + tableName +
//                    " (appName,duration) VALUES (?,?)");
//            pstmt.setString(1, appName);
//            pstmt.setInt(2, 0);
//            pstmt.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
