package com.example.addressbook;

import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.layout.HBox;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.event.EventHandler;
import java.io.File;
import java.io.FileWriter;
import javafx.geometry.Pos;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.ScrollPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SetupController {

    public Button exportDataButton;
    @FXML
    private TilePane tilePane;

    @FXML
    private Label nameLabel;

    @FXML
    private HBox myHBox;

    @FXML
    private HBox addButtonContainer;


    private User currentUser;

    public void getUser(User user) {
        currentUser = user;
        displayAppsInTilePattern();
        createAddButton();
    }

    // Add a Connection field to the class
    private Connection connection;

    // Put everything in a single connection
    private IUserDAO userDAO;

    // Initialize the connection in a method or constructor
    public SetupController() {
        this.userDAO = new sqlDAO();
        this.connection = userDAO.getConnection();
    }


    @FXML
    public void toSummary() throws IOException {
        Stage stage = (Stage) myHBox.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        // Pass the current user to next page
        DashboardController dashboardController = fxmlLoader.getController();
        dashboardController.getUser(currentUser);
        // Wrap the root layout in a ScrollPane
        ScrollPane scrollPane = SceneUtils.createScrollableContent(root);
        // to new scene
        Scene scene = SceneUtils.createStyledScene(scrollPane, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);

    }
    @FXML
    public void navigateToAddApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddApp-view.fxml"));
        Parent addAppParent = fxmlLoader.load();
        AddAppController addAppController = fxmlLoader.getController();
        addAppController.getUser(currentUser);
        // Wrap the root layout in a ScrollPane
        ScrollPane scrollPane = SceneUtils.createScrollableContent(addAppParent);
        Scene addAppScene = SceneUtils.createStyledScene(scrollPane, MainApplication.WIDTH, MainApplication.HEIGHT);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addAppScene);
        window.show();
    }

    @FXML
    private Button exportButton;

    @FXML
    private void exportToCSV() {
        // Set default value
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime now = LocalDateTime.now();
        String defaultFileName = "app_logs_" + dtf.format(now);

        TextInputDialog dialog = new TextInputDialog(defaultFileName);

        dialog.setTitle("Export Data");
        dialog.setHeaderText("Choose a name for the exported file:");
        dialog.setContentText("File name:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            // User pressed cancel or closed the dialog, return without creating the file
            return;
        }

        String fileName;
        if (!result.get().trim().isEmpty()) {
            fileName = result.get();
        } else {
            fileName = defaultFileName;
        }

        File csvFile = new File(fileName + ".csv"); // Use the fileName for the CSV file

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write the header line
            writer.append("User ID,App ID,Start Time,Stop Time,Duration\n");

            // Create an instance of sqlDAO
            sqlDAO dao = new sqlDAO();

            // Fetch the app_logs for the current user
            List<Map<String, Object>> appLogs = dao.getAppLogsForUser(currentUser);

            // Write a line for each log entry
            for (Map<String, Object> log : appLogs) {
                writer.append(log.get("user_id") + ",");
                writer.append(log.get("app_id") + ",");
                writer.append(log.get("start_time") + ",");
                writer.append(log.get("stop_time") + ",");
                writer.append(log.get("duration") + "\n");
            }

            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }


    private void displayAppsInTilePattern() {
        // Fetch the list of apps from the database
        List<App> apps = userDAO.fetchAppsFromDatabase(currentUser);

        // Set the preferred number of columns based on the number of apps
        int maxColumns = 5; // Set your desired maximum number of columns
        tilePane.setPrefColumns(maxColumns);

        // Set the preferred width of each tile
        double prefTileWidth = 120.0; // Set your desired tile width
        tilePane.setPrefTileWidth(prefTileWidth);

        // Center the tiles in the window
        tilePane.setAlignment(Pos.CENTER);

        // Create a tile for each app
        for (App app : apps) {
            // Create a new tile
            BorderPane tile = new BorderPane();
            tile.setPadding(new Insets(10));
            tile.setStyle("-fx-border-color: lightgray; -fx-background-color: black;");
            tile.setMinSize(prefTileWidth, 70); // Set your desired tile size
            tile.setMaxSize(prefTileWidth, 70); // Set your desired tile size
            ;

            // Add the app name to the tile
            Label appNameLabel = new Label(app.getName());
            appNameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;"); // Set the text color to white and make it bold
            BorderPane.setAlignment(appNameLabel, Pos.CENTER); // Center the label at the top of the BorderPane
            tile.setTop(appNameLabel);

            // Create a delete button with an image
            Image image = new Image(getClass().getResource("/com/example/addressbook/redX.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(15); // adjust the size as needed
            imageView.setFitWidth(15); // adjust the size as needed

            Button deleteButton = new Button();
            deleteButton.setGraphic(imageView);
            deleteButton.setStyle("-fx-border-width: 0;"); // Remove the border from the button
            deleteButton.setOnAction(e -> {
                // Delete the app from the database
                userDAO.deleteAppFromDatabase(app, currentUser);

                // Update the TilePane
                updateTilePane();
            });

            // Position the delete button at the bottom right of the tile
            BorderPane.setAlignment(deleteButton, Pos.BOTTOM_RIGHT);
            tile.setBottom(deleteButton);

            // Add the tile to the TilePane
            tilePane.getChildren().add(tile);

        }
    }
    private void updateTilePane() {
        // Clear the existing tiles in the TilePane
        tilePane.getChildren().clear();

        // Repopulate the TilePane with the updated list of apps
        displayAppsInTilePattern();
    }


    public void createAddButton() {
        // Load the image
        Image image = new Image(getClass().getResource("/com/example/addressbook/greenPlus.png").toExternalForm());

        // Create an ImageView and set the image to it
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);

        // Create a Button and set the ImageView as its graphic
        Button addButton = new Button();
        addButton.setGraphic(imageView);
        addButton.setText("Add APP");
        addButton.setOnAction(e -> {
            // Add your action here
            try {
                navigateToAddApp(e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });


        // Add the button to the addButtonContainer
        addButtonContainer.getChildren().add(addButton);

        // Center the addButtonContainer
        addButtonContainer.setAlignment(Pos.CENTER);
    }


}

