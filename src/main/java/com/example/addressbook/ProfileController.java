package com.example.addressbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {
    @FXML
    private Label username;
    @FXML
    private Label email;
    @FXML
    private Button updateButton;

    private User currentUser;
    public void showProfile(User user) {
        username.setText(user.getUsername());
        email.setText(user.getEmail());
    }

    public void getUser(User user) {
        currentUser = user;
        showProfile(currentUser);
    }

    @FXML
    protected void setUpdateButton() throws IOException {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("update-view.fxml"));
        Parent root = fxmlLoader.load();
        UpdateController updateController = fxmlLoader.getController();
        updateController.getUser(currentUser);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    protected void backToDashboard() throws IOException {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        DashboardController dashboardController = fxmlLoader.getController();
        dashboardController.getUser(currentUser);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }


}
