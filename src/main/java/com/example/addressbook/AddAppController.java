package com.example.addressbook;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AddAppController {

    public Label messageLabel;
    @FXML
    private Label nameLabel;

    private User currentUser;

    public void getUser(User user) {
        currentUser = user;
    }

    @FXML
    private Button addButton;

    @FXML
    private TextField filterField;

    @FXML
    private ListView<String> appListView;

    private ObservableList<String> appList;

    private IUserDAO userDAO;

    private Connection connection;

    // Add setter methods
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public IUserDAO getUserDAO() {
        return this.userDAO;
    }



    @FXML
    public void initialize() {
        this.userDAO = new sqlDAO();
        this.connection = userDAO.getConnection();
        appList = FXCollections.observableArrayList();
        appListView.setItems(appList);
        refreshOpenApp();

// Set a maximum height for the ListView
        double maxListViewHeight = 200.0; // Adjust this value as needed
        appListView.setMaxHeight(maxListViewHeight);

// Assuming each cell in the ListView has a height of 24.0
        double cellHeight = 24.0;

// Create a FilteredList from the ObservableList
        FilteredList<String> filteredList = new FilteredList<>(appList, s -> true);

// Bind the height property of the ListView to the size of the FilteredList
        appListView.prefHeightProperty().bind(Bindings.size(filteredList).multiply(cellHeight));

// Add a little extra height for the ListView's header
        appListView.setMinHeight(cellHeight + 2);

// Update the filter whenever the text in the filterField changes
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(s -> s.toLowerCase().contains(newValue.toLowerCase()));
            appListView.setItems(filteredList);
        });
// Add a mouse click event handler to the ListView
        appListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Check if it's a single
                // Get the selected item and set it as the text in the TextField
                String selectedItem = appListView.getSelectionModel().getSelectedItem();
                filterField.setText(selectedItem);
            }
        });
// Add an action event to the "Add" button
        addButton.setOnAction(event -> {
            String text = filterField.getText();
            // Check if the TextField's text is null or blank
            if (text == null || text.trim().isEmpty()) {
                // Return from the method without adding the app
                return;
            }

            String appName = filterField.getText();
            User currentUser = this.currentUser;

            try  {
                //Connection connection = userDAO.getConnection();
                String checkSql = "SELECT * FROM user_apps WHERE user_id = ? AND app_name = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, currentUser.getId());
                checkStatement.setString(2, appName);

                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // App already exists, show a popup message
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("App already added!");
                    alert.showAndWait();
                } else {
                    // App does not exist, insert it into the database
                    String insertSql = "INSERT INTO user_apps (user_id, app_name) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                    insertStatement.setInt(1, currentUser.getId());
                    insertStatement.setString(2, appName);

                    insertStatement.executeUpdate();
                    // After successfully adding the app
                    messageLabel.setOpacity(1.0); // Make the label fully visible

                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), messageLabel); // Replace 3 with the number of seconds you want the fade out to last
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.play();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void populateListView() {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT name FROM process_info");

            while (resultSet.next()) {
                String appName = resultSet.getString("name");
                if (!appList.contains(appName)) {
                    appList.add(appName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void filterList(String filter) {
        if (filter == null || filter.length() == 0) {
            appListView.setItems(appList);
        } else {
            FilteredList<String> filteredList = new FilteredList<>(appList, s -> s.toLowerCase().contains(filter.toLowerCase()));
            appListView.setItems(filteredList);
        }
    }



    @FXML
    private void toSummary() throws IOException {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        // Wrap the root layout in a ScrollPane
        ScrollPane scrollPane = SceneUtils.createScrollableContent(root);
        // Pass the current user to next page
        DashboardController dashboardController = fxmlLoader.getController();
        dashboardController.getUser(currentUser);
        // to new scene
        Scene scene = SceneUtils.createStyledScene(scrollPane, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }
    @FXML
    private void toSetup() throws IOException {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Setup-view.fxml"));
        Parent root = fxmlLoader.load();
        // Wrap the root layout in a ScrollPane
        ScrollPane scrollPane = SceneUtils.createScrollableContent(root);
        SetupController setupController = fxmlLoader.getController();
        setupController.getUser(currentUser);
        Scene scene = SceneUtils.createStyledScene(scrollPane, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    private void toSchedule(ActionEvent event) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ScheduleUI.fxml"));
        Parent scheduleParent = fxmlLoader.load();

        // Wrap the root layout in a ScrollPane
        ScrollPane scrollPane = SceneUtils.createScrollableContent(scheduleParent);

        // Create a styled scene
        Scene scheduleScene = SceneUtils.createStyledScene(scrollPane, ScheduleControl.WIDTH, ScheduleControl.HEIGHT);

        // Get the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene on the current stage
        stage.setScene(scheduleScene);
        stage.show();
    }

    @FXML
    public void toGoals() throws IOException {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("goals-view.fxml"));
        Parent root = fxmlLoader.load();
        GoalsController goalsController = fxmlLoader.getController();
        goalsController.getUser(currentUser);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }


    @FXML
    public void refreshOpenApp() {
        String command = "powershell.exe  Get-Process | Where-Object {$_.mainWindowTitle} | Select-Object Id, Name, mainWindowtitle | Sort-Object Name | Format-Table -HideTableHeaders";
        Process powerShellProcess;
        try {
            powerShellProcess = Runtime.getRuntime().exec(command);
            powerShellProcess.getOutputStream().close();
            String line;
            BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
            //Connection connection = userDAO.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS process_info (id TEXT, name TEXT, mainWindowTitle TEXT, UNIQUE(id, name, mainWindowTitle))");
            statement.execute("DELETE FROM process_info"); // Clear the existing list of applications
            while ((line = stdout.readLine()) != null) {
                String[] parts = line.trim().split("\\s+", 3); // split the line into three parts
                if (parts.length >= 3) {
                    String id = parts[0];
                    String name = parts[1];
                    String mainWindowTitle = parts[2];
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR IGNORE INTO process_info (id, name, mainWindowTitle) VALUES (?, ?, ?)");
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, mainWindowTitle);
                    preparedStatement.executeUpdate();
                }
            }
            appList.clear(); // Clear the existing list of applications in the ListView
            populateListView(); // Update the ListView with the updated list of applications
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clearFilter() {
        filterField.clear();
    }
}