package com.example.addressbook;

import com.example.addressbook.schedule.Setup.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyScheduleControl {

    public String chosenDate;
    private IUserDAO scheduleInterface;
    private final User currentUser = DashboardController.currentUser;
    private final String[] PurposeList = {"None", "Personal", "Work"};

    public ModifyScheduleControl() {
        scheduleInterface = new sqlDAO();
    }

    @FXML
    private Button tutorialButton;
    @FXML
    private AnchorPane tutorial;
    @FXML
    private Label nameLabel;
    @FXML
    private HBox myHBox;
    @FXML
    private RadioButton Monday;
    @FXML
    private RadioButton Tuesday;
    @FXML
    private RadioButton Wednesday;
    @FXML
    private RadioButton Thursday;
    @FXML
    private RadioButton Friday;
    @FXML
    private RadioButton Saturday;
    @FXML
    private RadioButton Sunday;
    @FXML
    private Button accept;
    @FXML
    private Button cancel;
    @FXML
    private ChoiceBox<String> am0;
    @FXML
    private ChoiceBox<String> am1;
    @FXML
    private ChoiceBox<String> am2;
    @FXML
    private ChoiceBox<String> am3;
    @FXML
    private ChoiceBox<String> am4;
    @FXML
    private ChoiceBox<String> am5;
    @FXML
    private ChoiceBox<String> am6;
    @FXML
    private ChoiceBox<String> am7;
    @FXML
    private ChoiceBox<String> am8;
    @FXML
    private ChoiceBox<String> am9;
    @FXML
    private ChoiceBox<String> am10;
    @FXML
    private ChoiceBox<String> am11;
    @FXML
    private ChoiceBox<String> pm0;
    @FXML
    private ChoiceBox<String> pm1;
    @FXML
    private ChoiceBox<String> pm2;
    @FXML
    private ChoiceBox<String> pm3;
    @FXML
    private ChoiceBox<String> pm4;
    @FXML
    private ChoiceBox<String> pm5;
    @FXML
    private ChoiceBox<String> pm6;
    @FXML
    private ChoiceBox<String> pm7;
    @FXML
    private ChoiceBox<String> pm8;
    @FXML
    private ChoiceBox<String> pm9;
    @FXML
    private ChoiceBox<String> pm10;
    @FXML
    private ChoiceBox<String> pm11;

    private final String[] purposeFromDatabase = new String[24];
    private List<String> ListOfPurpose = new ArrayList<>(24);

    @FXML
    private void onClickTutorial() {
        tutorial.setVisible(!tutorial.isVisible());
    }

    private void getFromDatabase(User user) {
        for (int i = 0; i < 24; i++) {
            purposeFromDatabase[i] = scheduleInterface.getPurpose(user, chosenDate, i);
        }
    }

    private void shortenUpdate(ChoiceBox<String> time, int databaseLocation) {
        time.setDisable(false);
        time.setValue(purposeFromDatabase[databaseLocation]);
    }

    private void updateForChoosing() {
        accept.setDisable(false);
        getFromDatabase(currentUser);
        shortenUpdate(am0, 0);
        shortenUpdate(am1, 1);
        shortenUpdate(am2, 2);
        shortenUpdate(am3, 3);
        shortenUpdate(am4, 4);
        shortenUpdate(am5, 5);
        shortenUpdate(am6, 6);
        shortenUpdate(am7, 7);
        shortenUpdate(am8, 8);
        shortenUpdate(am9, 9);
        shortenUpdate(am10, 10);
        shortenUpdate(am11, 11);
        shortenUpdate(pm0, 12);
        shortenUpdate(pm1, 13);
        shortenUpdate(pm2, 14);
        shortenUpdate(pm3, 15);
        shortenUpdate(pm4, 16);
        shortenUpdate(pm5, 17);
        shortenUpdate(pm6, 18);
        shortenUpdate(pm7, 19);
        shortenUpdate(pm8, 20);
        shortenUpdate(pm9, 21);
        shortenUpdate(pm10, 22);
        shortenUpdate(pm11, 23);
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

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
        Parent root = fxmlLoader.load();
        Scene currentScene = nameLabel.getScene();
        currentScene.setRoot(root);
    }

    @FXML
    protected void onChoseMonday() {
        chosenDate = "Monday";
        updateForChoosing();
    }

    @FXML
    protected void onChoseTuesday() {
        chosenDate = "Tuesday";
        updateForChoosing();
    }

    @FXML
    protected void onChoseWednesday() {
        chosenDate = "Wednesday";
        updateForChoosing();
    }

    @FXML
    protected void onChoseThursday() {
        chosenDate = "Thursday";
        updateForChoosing();
    }

    @FXML
    protected void onChoseFriday() {
        chosenDate = "Friday";
        updateForChoosing();
    }

    @FXML
    protected void onChoseSaturday() {
        chosenDate = "Saturday";
        updateForChoosing();
    }

    @FXML
    protected void onChoseSunday() {
        chosenDate = "Sunday";
        updateForChoosing();
    }

    private void addPurpose(ChoiceBox<String> button) {
        ListOfPurpose.add(button.getValue());
    }

    private void getPurpose() {
        addPurpose(am0);
        addPurpose(am1);
        addPurpose(am2);
        addPurpose(am3);
        addPurpose(am4);
        addPurpose(am5);
        addPurpose(am6);
        addPurpose(am7);
        addPurpose(am8);
        addPurpose(am9);
        addPurpose(am10);
        addPurpose(am11);
        addPurpose(pm0);
        addPurpose(pm1);
        addPurpose(pm2);
        addPurpose(pm3);
        addPurpose(pm4);
        addPurpose(pm5);
        addPurpose(pm6);
        addPurpose(pm7);
        addPurpose(pm8);
        addPurpose(pm9);
        addPurpose(pm10);
        addPurpose(pm11);
    }

    @FXML
    protected void onChoseAccept(ActionEvent event) throws IOException {
        getPurpose();
        for (int i = 0; i < 24; i++) {
            Date date = new Date(chosenDate, i, ListOfPurpose.get(i));
            scheduleInterface.updateSchedule(currentUser, date);
        }
        navigateTo("ScheduleUI.fxml");
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) throws IOException {
        navigateTo("ScheduleUI.fxml");
    }

    private void shorten(ChoiceBox<String> button) {
        button.getItems().addAll(PurposeList);
    }

    @FXML
    public void initialize() {
        shorten(am0);
        shorten(am1);
        shorten(am2);
        shorten(am3);
        shorten(am4);
        shorten(am5);
        shorten(am6);
        shorten(am7);
        shorten(am8);
        shorten(am9);
        shorten(am10);
        shorten(am11);
        shorten(pm0);
        shorten(pm1);
        shorten(pm2);
        shorten(pm3);
        shorten(pm4);
        shorten(pm5);
        shorten(pm6);
        shorten(pm7);
        shorten(pm8);
        shorten(pm9);
        shorten(pm10);
        shorten(pm11);
    }
}
