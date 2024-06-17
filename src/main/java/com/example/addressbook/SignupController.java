package com.example.addressbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

public class SignupController {
    @FXML
    private TextField UsernameInput;
    @FXML
    private TextField EmailInput;
    @FXML
    private PasswordField PasswordInput;
    @FXML
    private PasswordField ConfirmPasswordInput;
    @FXML
    private Label errorMessage;
    @FXML
    private Button toLogin;
    @FXML
    private Button SignupButton;

    private IUserDAO userDAO;

    public SignupController() {
        userDAO = new sqlDAO();
    }

    @FXML
    protected void setToLogin() throws IOException {
        Stage stage = (Stage) toLogin.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    protected void toDashboard(User user) throws IOException {
        Stage stage = (Stage) SignupButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dashboard.fxml"));
        Parent root = fxmlLoader.load();
        DashboardController dashboardController = fxmlLoader.getController();
        dashboardController.getUser(user);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void signup() throws IOException, NoSuchAlgorithmException {
        String username = String.valueOf(UsernameInput.getText());
        String email = String.valueOf(EmailInput.getText());
        String password = String.valueOf(PasswordInput.getText());
        String confirm = String.valueOf(ConfirmPasswordInput.getText());
        List<User> existedUsers = userDAO.getAllUsers();
        for (User user : existedUsers) {
            if (Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(),
                    password)) {
                errorMessage.setText("User already exists");
            }
        }
        if (!Objects.equals(password, confirm)) {
            errorMessage.setText("The password needs to be the same with the confirmation");
        } else if (Objects.equals(password, "") || Objects.equals(confirm, "")) {
            errorMessage.setText("Password and confirm fields shouldn't be empty");
        } else if (Objects.equals(username, "")) {
            errorMessage.setText("Username cannot be empty");
        } else if (Objects.equals(email, "")) {
            errorMessage.setText("Email shouldn't be empty");
        } else{
            User newUser = new User(username, password, email);
            userDAO.addUser(newUser);
//            userDAO.createUserTable(newUser);
            //userDAO.insertAppUsage(newUser.getTableName(), "facebook");
            toDashboard(newUser);
        }
    }
}
