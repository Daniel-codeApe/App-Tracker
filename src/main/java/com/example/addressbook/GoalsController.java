package com.example.addressbook;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GoalsController implements Initializable {

    @FXML
    private TilePane tilePane;

    @FXML
    private Label nameLabel;

    @FXML
    private Pane goalAddMenu;

    @FXML
    private HBox myHBox;

    @FXML
    private ChoiceBox<String> goalDesireEntry;

    @FXML
    private ChoiceBox<String> goalTimeEntry;

    @FXML
    private ListView<String> appListView;

    private ObservableList<String> appList;

    @FXML
    private TextField filterField;

    private IGoalDAO goalDAO;

    private IUserDAO userDAO;
    private User currentUser;

    private Connection connection;

    public GoalsController() {
        goalDAO = new sqlDAO();
        this.userDAO = new sqlDAO();
        this.connection = userDAO.getConnection();
    }

    public void getUser(User user) {
        if (user == null) {
            System.err.println("User is null in getUser method");
            return;
        }
        currentUser = user;
        displayStoredApp();  // Ensure currentUser is set before calling this
        displayGoalsInTilePattern();
        updateTilePane();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goalDesireEntry.getItems().addAll("LessTime", "MoreTime");
        goalTimeEntry.getItems().addAll("Daily", "Weekly");
        tilePane.setVisible(true);
        appList = FXCollections.observableArrayList();
        appListView.setItems(appList);

        // Initialize the ListView
        double maxListViewHeight = 126.0; // Adjust this value as needed
        appListView.setMaxHeight(maxListViewHeight);

        double cellHeight = 24.0;
        FilteredList<String> filteredList = new FilteredList<>(appList, s -> true);
        appListView.prefHeightProperty().bind(Bindings.size(filteredList).multiply(cellHeight));
        appListView.setMinHeight(cellHeight + 2);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(s -> s.toLowerCase().contains(newValue.toLowerCase()));
            appListView.setItems(filteredList);
        });

        appListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedItem = appListView.getSelectionModel().getSelectedItem();
                filterField.setText(selectedItem);
            }
        });
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
    public void toSummary() throws IOException {
        Stage stage = (Stage) myHBox.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        DashboardController dashboardController = fxmlLoader.getController();
        dashboardController.getUser(currentUser);
        ScrollPane scrollPane = SceneUtils.createScrollableContent(root);
        Scene scene = SceneUtils.createStyledScene(scrollPane, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    private void toSchedule(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ScheduleUI.fxml"));
        Parent scheduleParent = fxmlLoader.load();
        ScrollPane scrollPane = SceneUtils.createScrollableContent(scheduleParent);
        Scene scheduleScene = SceneUtils.createStyledScene(scrollPane, ScheduleControl.WIDTH, ScheduleControl.HEIGHT);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scheduleScene);
        stage.show();
    }

    public void addNewGoal(ActionEvent event) throws IOException {
        goalAddMenu.setVisible(true);
        tilePane.setVisible(false);
        populateListView();
    }

    public void goalAdded(ActionEvent event) throws IOException {
        String desireSelected = goalDesireEntry.getValue();
        String goalNameSelected = filterField.getText();
        String goalTimeSelected = goalTimeEntry.getValue();

        if (!desireSelected.isEmpty() && !goalNameSelected.isEmpty() && !goalTimeSelected.isEmpty()) {
            Goal newGoal = new Goal(goalNameSelected, desireSelected, goalTimeSelected, currentUser.getId());
            goalDAO.addGoal(newGoal, currentUser);
            goalAddMenu.setVisible(false);
            tilePane.setVisible(true);
            updateTilePane();
        } else {
            // Provide error information and don't close the goal-Add Menu yet
        }
    }

    private void displayGoalsInTilePattern() {
        List<Goal> goals = fetchGoalsFromDatabase();
        tilePane.setPrefColumns(goals.size());

        for (Goal goal : goals) {
            VBox tile = new VBox();
            tile.setPadding(new Insets(10));
            tile.setStyle("-fx-border-color: white; -fx-background-color: black;");

            Label goalNameLabel = new Label(goal.getGoalName());
            tile.getChildren().add(goalNameLabel);

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> {
                goalDAO.DeleteGoal(goal);
                updateTilePane();
            });
            tile.getChildren().add(deleteButton);

            tilePane.getChildren().add(tile);
        }
    }

    private List<Goal> fetchGoalsFromDatabase() {
        return goalDAO.fetchGoalsForUser(currentUser);
    }

    private void updateTilePane() {
        tilePane.getChildren().clear();
        displayGoalsInTilePattern();
    }

    @FXML
    public void displayStoredApp() {
        if (currentUser == null) {
            System.err.println("currentUser is null in displayStoredApp");
            return;
        }
        appList.clear();
        ArrayList<String> appNames = userDAO.getStoredApps(currentUser);
        for (String name : appNames) {
            appList.add(name);
        }
    }

    private void populateListView() {
        if (currentUser == null) {
            System.err.println("currentUser is null in populateListView");
            return;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT app_name FROM user_apps WHERE user_id = ?");
            statement.setInt(1, currentUser.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String appName = resultSet.getString("app_name");
                if (!appList.contains(appName)) {
                    appList.add(appName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
