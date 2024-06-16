package com.example.addressbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private Button updateButton;
    private User currentUser;
    private IUserDAO userDAO;

    private void displayUser(User user) {
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
    }

    public void getUser(User user) {
        currentUser = user;
        userDAO = new sqlDAO();
        displayUser(currentUser);
    }

    private void toProfile(User user) throws IOException {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController profileController = fxmlLoader.getController();
        profileController.getUser(user);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    protected void updateProfile() throws IOException {
        String updatedUsername = String.valueOf(usernameField.getText());
        String updatedEmail = String.valueOf(emailField.getText());

        User updatedUser = new User(updatedUsername, updatedEmail, currentUser.getPassword());
        updatedUser.setId(currentUser.getId());
        userDAO.updateUser(updatedUser);
        toProfile(updatedUser);
    }


}
