package com.example.addressbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField username_or_emailInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Label LoginMessage;
    @FXML
    private Button loginButton;
    @FXML
    private Button toSignup;

    private IUserDAO userDAO;

    public LoginController() {
        userDAO = new sqlDAO();
    }

    @FXML
    protected void setToSignup() throws IOException {
        Stage stage = (Stage) toSignup.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("signup-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    protected void toDashboard(User user) throws IOException {
        if (user != null) {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dashboard.fxml"));
            Parent root = fxmlLoader.load();
            // Wrap the root layout in a ScrollPane
            ScrollPane scrollPane = SceneUtils.createScrollableContent(root);

            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.getUser(user);
            dashboardController.displayStoredApp(); // Add this line
            Scene scene = SceneUtils.createStyledScene(scrollPane, MainApplication.WIDTH, MainApplication.HEIGHT);
            stage.setScene(scene);
        } else {
            // Handle the case where the user is not authenticated
            // For example, you can display an error message
            LoginMessage.setText("Failed Login");
        }
    }

    public boolean login() throws IOException, NoSuchAlgorithmException {
        String username_or_email = String.valueOf(username_or_emailInput.getText());
        String password = String.valueOf(passwordInput.getText());
        List<User> signedUsers = userDAO.getAllUsers();
        for (User user : signedUsers) {
            System.out.println(user.getPassword() + user.getEmail());
            if ((Objects.equals(user.getUsername(), username_or_email) ||
                    Objects.equals(user.getEmail(), username_or_email) )&&
                            Objects.equals(user.getPassword(), PasswordHash.encrypt(password)) ) {
                //toProfile(user);
                toDashboard(user);
                return true;
            }
        }
        LoginMessage.setText("Failed Login");
        return false;
    }
}
