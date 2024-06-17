package com.example.addressbook;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DashboardController {
    @FXML
    private Label nameLabel;
    @FXML
    private Button toProfileButton;
    private User currentUser;
    @FXML
    private PieChart pieChart;
    @FXML
    private TilePane tilePane;
    private ObservableList<PieChart.Data> pieChartData;
    private IUserDAO userDAO;

    private Connection connection;

    public DashboardController() {
        this.userDAO = new sqlDAO();
        this.connection = userDAO.getConnection();
        pieChartData = FXCollections.observableArrayList();
    }

    public void getUser(User user) {
        currentUser = user;
        nameLabel.setText(user.getUsername());
        updatePieChart();
        displayStoredApp();
    }

    private void updatePieChart() {
        Dictionary<String, Integer> weeklyUsage = userDAO.getWeeklyUsage(currentUser);
        Enumeration<String> apps = weeklyUsage.keys();
        while (apps.hasMoreElements()) {
            String app = apps.nextElement();
            int duration = weeklyUsage.get(app);
            System.out.println(app);
            pieChartData.add(new PieChart.Data(app, duration));
        }
        pieChart.setData(pieChartData);
    }

    private void clearPieChart() {
        pieChart.getData().clear();
    }

    @FXML
    private void toSetup() throws IOException {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Setup-view.fxml"));
        Parent root = fxmlLoader.load();
        SetupController setupController = fxmlLoader.getController();
        setupController.getUser(currentUser);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }


    @FXML
    private void toProfile() throws IOException {
        Stage stage = (Stage) toProfileButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.getController();
        profileController.getUser(currentUser);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    void displayStoredApp() {
        tilePane.getChildren().clear(); // Clear the tilePane
        ArrayList<String> AppNames = userDAO.getStoredApps(currentUser);
        for (String name:AppNames) {
            Button app = new Button(name);
            app.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            app.setOnAction(e -> {
                userDAO.saveStartTimeToDatabase(this.connection, currentUser.getId(), name);
                startTimer(name);
            });
            tilePane.getChildren().add(app);
        }
    }

    private void startTimer(String appName) {
        try {
            // Create a new Stage for the timer window
            Stage timerStage = new Stage();
            timerStage.setTitle(appName);

            // Create a VBox to hold the timer's controls
            VBox vbox = new VBox();
            vbox.setSpacing(10);

            // Create a Label to display the timer's value
            Label timerLabel = new Label("0");
            timerLabel.setFont(new Font("System", 24));
            vbox.getChildren().add(timerLabel);

            // Create a pause button
            Button pauseButton = new Button("Pause");
            vbox.getChildren().add(pauseButton);

            // Create a stop button
            Button stopButton = new Button("Stop");
            vbox.getChildren().add(stopButton);

            // Create a Scene with the vbox as its root node
            Scene timerScene = SceneUtils.createStyledScene(vbox, 400, 200);

            // Set the timerStage's scene to timerScene
            timerStage.setScene(timerScene);

            // Show the timerStage
            timerStage.show();
            // Create a Timeline that fires every second
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                // Get the current text of the timerLabel, convert it to an integer, and increment it
                int currentTime = Integer.parseInt(timerLabel.getText()) + 1;

                // Update the text of the timerLabel
                timerLabel.setText(String.valueOf(currentTime));
            }));

            // Set the timeline to repeat indefinitely
            timeline.setCycleCount(Timeline.INDEFINITE);

            // Start the timeline
            timeline.play();

            // Pause the timeline when the pause button is clicked
            pauseButton.setOnAction(e -> {
                if (timeline.getStatus() == Animation.Status.RUNNING) {
                    timeline.pause();
                    pauseButton.setText("Resume");
                } else {
                    timeline.play();
                    pauseButton.setText("Pause");
                }
            });

            // Stop the timeline and save the time to the database when the stop button is clicked
            stopButton.setOnAction(e -> {
                timeline.stop();
                userDAO.saveTimeToDatabase(this.connection, appName, Integer.parseInt(timerLabel.getText()), currentUser);
                clearPieChart();
                updatePieChart();
                timerStage.close();
            });

            // Removed the call to saveStartTimeToDatabase here
        }
        catch (Exception e) {
            System.out.println("Exception in startTimer method");
            e.printStackTrace();
        }
    }


//    @FXML
//    void initialize() {
//        pieChart.setData(pieChartData);
//    }
}
