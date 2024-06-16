package com.example.addressbook;

import com.example.addressbook.schedule.Setup.Date;
import javafx.scene.paint.Color;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class sqlDAO implements IUserDAO, IGoalDAO {

    private Connection connection;
    private final String[] ListOfDay = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};


    public sqlDAO() {
        connection = sqlConnection.getInstance();
        createTable();
        createUserAppsTable(); // Create the user_apps table
        createAppLogsTable(); // Create the app_logs table
        createScheduleTable();
        createUserColorTable();
        createGoalsTable(); // Create the goals table
    }

    public Connection getConnection() {
        return connection;
    }

    private void createUserAppsTable() {
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
    public Dictionary<String, Integer> getUsageSummary(User user, String option, String purpose) {
        Dictionary<String, Integer> weeklyUsage = new Hashtable<>();
        try {
            String query = "SELECT app_logs.app_id, app_logs.duration " +
                    "FROM app_logs ";

            if (!Objects.equals(purpose, "All Kinds")) {
                query += "JOIN schedule " +
                        "ON strftime('%H', app_logs.start_time) = schedule.hour AND " +
                        "CASE strftime('%w', app_logs.start_time)\n" +
                        "           WHEN '0' THEN 'Sunday'\n" +
                        "           WHEN '1' THEN 'Monday'\n" +
                        "           WHEN '2' THEN 'Tuesday'\n" +
                        "           WHEN '3' THEN 'Wednesday'\n" +
                        "           WHEN '4' THEN 'Thursday'\n" +
                        "           WHEN '5' THEN 'Friday'\n" +
                        "           WHEN '6' THEN 'Saturday'\n" +
                        "       END = schedule.day " +
                        "AND schedule.ID = " + user.getId();
                if (Objects.equals(purpose, "Work")) {
                    query += " AND schedule.purpose = 'Work' ";
                } else if (Objects.equals(purpose, "Personal")) {
                    query += " AND schedule.purpose = 'Personal' ";
                }
            }

            if (Objects.equals(option, "Weekly")) {
                query += "WHERE stop_time BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')";
            } else if (Objects.equals(option, "Monthly")) {
                query += "WHERE stop_time BETWEEN datetime('now', '-1 months') AND datetime('now', 'localtime')";
            } else if (Objects.equals(option, "Yearly")) {
                query += "WHERE stop_time BETWEEN datetime('now', '-1 years') AND datetime('now', 'localtime')";
            }
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int app_id = resultSet.getInt("app_id");
                int duration = resultSet.getInt("duration");
                PreparedStatement getAppName = connection.prepareStatement(
                        "SELECT app_name " +
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
                if (!result.contains(storedApps.getString("app_name"))) {
                    result.add(storedApps.getString("app_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void saveStartTimeToDatabase(Connection connection, int userID, String appName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println(formattedNow);
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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        System.out.println(formattedNow);
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
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT app_name " +
                    "FROM user_apps " +
                    "WHERE user_id = " + user.getId());
            while (resultSet.next()) {
                String name = resultSet.getString("app_name");
                App app = new App(name);
                apps.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apps;
    }

    @Override
    public void deleteAppFromDatabase(App app, User user) {
        try {
            Statement statement = connection.createStatement();
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
                    + "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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

    private void createScheduleTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS schedule ("
                    + "ID INTEGER,"
                    + "day VARCHAR,"
                    + "hour INTEGER,"
                    + "purpose VARCHAR NOT NULL,"
                    + "PRIMARY KEY(ID, day, hour)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createGoalsTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS goals ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "goalName VARCHAR NOT NULL,"
                    + "goalDesire VARCHAR NOT NULL,"
                    + "goalTime VARCHAR NOT NULL,"
                    + "user_id INTEGER NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES users(id)"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createStartingData(User user) {
        for (int i = 0; i < 7; i++) {
            for (int y = 0; y < 24; y++) {
                Date date = new Date(ListOfDay[i], y, "None");
                InitialSchedule(user, date);
            }
        }
    }

    public void InitialSchedule(User user, Date date) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO schedule(ID, day, hour, purpose) VALUES (?,?,?,?)");
            statement.setInt(1, user.getId());
            statement.setString(2, date.getDay());
            statement.setInt(3, date.getHour());
            statement.setString(4, date.getPurpose());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSchedule(User user, Date date) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE schedule SET purpose = ? WHERE ID = ? AND day = ? AND hour = ?");
            statement.setString(1, date.getPurpose());
            statement.setInt(2, user.getId());
            statement.setString(3, date.getDay());
            statement.setInt(4, date.getHour());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addGoal(Goal goal, User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO goals " +
                    "(goalName, goalDesire, goalTime, user_id) "
                    + "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, goal.getGoalName());
            statement.setString(2, goal.getGoalDesire());
            statement.setString(3, goal.getGoalTime());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                goal.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPurpose(User user, String date, int hour) {
        String purpose = "None";
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM schedule WHERE ID = ? AND day = ? AND hour = ?");
            statement.setInt(1, user.getId());
            statement.setString(2, date);
            statement.setInt(3, hour);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                purpose = resultSet.getString("purpose");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purpose;
    }

    private void createUserColorTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS userColor ("
                    + "ID INTEGER,"
                    + "purpose STRING,"
                    + "color STRING NOT NULL,"
                    + "PRIMARY KEY(ID, purpose)"
                    + ")";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String[] purposeList = new String[]{"Work", "Personal"};
    Color[] defaultColorList = new Color[]{Color.DODGERBLUE, Color.RED};

    @Override
    public void DefaultColor(User user) {
        for (int i = 0; i < 2; i++) {
            QueryDefaultColor(user, purposeList[i], defaultColorList[i]);
        }
    }

    public void QueryDefaultColor(User user, String purpose, Color color) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO userColor(ID, purpose, color) VALUES (?,?,?)");
            statement.setInt(1, user.getId());
            statement.setString(2, purpose);
            statement.setString(3, color.toString());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateColor(User user, String purpose, Color color) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE userColor SET color = ? WHERE ID = ? AND purpose = ?");
            statement.setString(1, color.toString());
            statement.setInt(2, user.getId());
            statement.setString(3, purpose);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Color getColor(User user, String purpose) {
        Color color = Color.WHITE;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM userColor WHERE ID = ? AND purpose = ?");
            statement.setInt(1, user.getId());
            statement.setString(2, purpose);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                color = Color.valueOf(resultSet.getString("color"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return color;
    }

    @Override
    public void updateGoal(Goal goal) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE goals " +
                    "SET goalName = ?, goalDesire = ?, goalTime = ? " +
                    "WHERE id = ?");
            statement.setString(1, goal.getGoalName());
            statement.setString(2, goal.getGoalDesire());
            statement.setString(3, goal.getGoalTime());
            statement.setInt(4, goal.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteGoal(Goal goal) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM goals WHERE id = ?");
            statement.setInt(1, goal.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Goal GetGoal(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goals WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String goalName = resultSet.getString("goalName");
                String goalDesire = resultSet.getString("goalDesire");
                String goalTime = resultSet.getString("goalTime");
                Goal goal = new Goal(goalName, goalDesire, goalTime, resultSet.getInt("user_id"));
                goal.setId(id);
                return goal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Goal> fetchGoalsForUser(User user) {
        List<Goal> goals = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM goals WHERE user_id = ?");
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String goalName = resultSet.getString("goalName");
                String goalDesire = resultSet.getString("goalDesire");
                String goalTime = resultSet.getString("goalTime");
                Goal goal = new Goal(goalName, goalDesire, goalTime, user.getId());
                goal.setId(id);
                goals.add(goal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goals;
    }
}
