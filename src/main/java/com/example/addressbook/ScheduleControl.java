package com.example.addressbook;

import com.example.addressbook.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class ScheduleControl {
    private IUserDAO scheduleInterface;
    private User currentUser = DashboardController.currentUser;
    public static final String TITLE = "Tracking App";
    public static final int WIDTH = 750;
    public static final int HEIGHT = 500;

    public ScheduleControl() {
        scheduleInterface = new sqlDAO();
    }

    @FXML
    private Label nameLabel;
    @FXML
    private Button tutorialButton;
    @FXML
    private AnchorPane tutorial;
    @FXML
    private HBox myHBox;
    @FXML
    private Button modifyButton;
    @FXML
    private ColorPicker changeWorkColour;
    @FXML
    private ColorPicker changePersonalColour;

    @FXML
    private void onClickTutorial() {
        tutorial.setVisible(!tutorial.isVisible());
    }

    @FXML
    protected void OnClickModifyButton(ActionEvent event) throws IOException {
        navigateTo("ScheduleModify.fxml");
    }

    private void navigateTo(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
        Parent root = fxmlLoader.load();
        Scene currentScene = myHBox.getScene();
        currentScene.setRoot(root);
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
    public void toGoals() throws IOException {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("goals-view.fxml"));
        Parent root = fxmlLoader.load();
        GoalsController goalsController = fxmlLoader.getController();
        goalsController.getUser(currentUser);
        Scene scene = SceneUtils.createStyledScene(root, MainApplication.WIDTH, MainApplication.HEIGHT);
        stage.setScene(scene);
    }

    private void openScheduleUI(ActionEvent event) throws IOException {
        navigateTo("ScheduleUI.fxml");
    }

    private final Color noneColour = Color.WHITE;
    private Color workColour;
    private Color personalColour;

    @FXML
    private void onChooseWorkColor(ActionEvent event) throws IOException {
        scheduleInterface.UpdateColor(currentUser, "Work", changeWorkColour.getValue());
        openScheduleUI(event);
    }

    @FXML
    private void onChoosePersonalColor(ActionEvent event) throws IOException {
        scheduleInterface.UpdateColor(currentUser, "Personal", changePersonalColour.getValue());
        openScheduleUI(event);
    }

    public void changeColour(String day, int hour, Rectangle date) {
        String purpose = scheduleInterface.getPurpose(currentUser, day, hour);
        switch (purpose) {
            case "Work":
                date.setFill(workColour);
                break;
            case "Personal":
                date.setFill(personalColour);
                break;
            default:
                date.setFill(noneColour);
        }
    }

    private void update() {
        changeMonday();
        changeTuesday();
        changeWednesday();
        changeThursday();
        changeFriday();
        changeSaturday();
        changeSunday();
    }

    @FXML
    private void initialize() {
        workColour = scheduleInterface.getColor(currentUser, "Work");
        personalColour = scheduleInterface.getColor(currentUser, "Personal");
        changeWorkColour.setValue(workColour);
        changePersonalColour.setValue(personalColour);
        update();
        if (tutorial.isVisible()) {
            tutorial.setVisible(true);
        }
    }

    public void changeMonday() {
        changeColour("Monday", 0, Monday0);
        changeColour("Monday", 1, Monday1);
        changeColour("Monday", 2, Monday2);
        changeColour("Monday", 3, Monday3);
        changeColour("Monday", 4, Monday4);
        changeColour("Monday", 5, Monday5);
        changeColour("Monday", 6, Monday6);
        changeColour("Monday", 7, Monday7);
        changeColour("Monday", 8, Monday8);
        changeColour("Monday", 9, Monday9);
        changeColour("Monday", 10, Monday10);
        changeColour("Monday", 11, Monday11);
        changeColour("Monday", 12, Monday12);
        changeColour("Monday", 13, Monday13);
        changeColour("Monday", 14, Monday14);
        changeColour("Monday", 15, Monday15);
        changeColour("Monday", 16, Monday16);
        changeColour("Monday", 17, Monday17);
        changeColour("Monday", 18, Monday18);
        changeColour("Monday", 19, Monday19);
        changeColour("Monday", 20, Monday20);
        changeColour("Monday", 21, Monday21);
        changeColour("Monday", 22, Monday22);
        changeColour("Monday", 23, Monday23);
    }

    public void changeTuesday() {
        changeColour("Tuesday", 0, Tuesday0);
        changeColour("Tuesday", 1, Tuesday1);
        changeColour("Tuesday", 2, Tuesday2);
        changeColour("Tuesday", 3, Tuesday3);
        changeColour("Tuesday", 4, Tuesday4);
        changeColour("Tuesday", 5, Tuesday5);
        changeColour("Tuesday", 6, Tuesday6);
        changeColour("Tuesday", 7, Tuesday7);
        changeColour("Tuesday", 8, Tuesday8);
        changeColour("Tuesday", 9, Tuesday9);
        changeColour("Tuesday", 10, Tuesday10);
        changeColour("Tuesday", 11, Tuesday11);
        changeColour("Tuesday", 12, Tuesday12);
        changeColour("Tuesday", 13, Tuesday13);
        changeColour("Tuesday", 14, Tuesday14);
        changeColour("Tuesday", 15, Tuesday15);
        changeColour("Tuesday", 16, Tuesday16);
        changeColour("Tuesday", 17, Tuesday17);
        changeColour("Tuesday", 18, Tuesday18);
        changeColour("Tuesday", 19, Tuesday19);
        changeColour("Tuesday", 20, Tuesday20);
        changeColour("Tuesday", 21, Tuesday21);
        changeColour("Tuesday", 22, Tuesday22);
        changeColour("Tuesday", 23, Tuesday23);
    }

    public void changeWednesday() {
        changeColour("Wednesday", 0, Wednesday0);
        changeColour("Wednesday", 1, Wednesday1);
        changeColour("Wednesday", 2, Wednesday2);
        changeColour("Wednesday", 3, Wednesday3);
        changeColour("Wednesday", 4, Wednesday4);
        changeColour("Wednesday", 5, Wednesday5);
        changeColour("Wednesday", 6, Wednesday6);
        changeColour("Wednesday", 7, Wednesday7);
        changeColour("Wednesday", 8, Wednesday8);
        changeColour("Wednesday", 9, Wednesday9);
        changeColour("Wednesday", 10, Wednesday10);
        changeColour("Wednesday", 11, Wednesday11);
        changeColour("Wednesday", 12, Wednesday12);
        changeColour("Wednesday", 13, Wednesday13);
        changeColour("Wednesday", 14, Wednesday14);
        changeColour("Wednesday", 15, Wednesday15);
        changeColour("Wednesday", 16, Wednesday16);
        changeColour("Wednesday", 17, Wednesday17);
        changeColour("Wednesday", 18, Wednesday18);
        changeColour("Wednesday", 19, Wednesday19);
        changeColour("Wednesday", 20, Wednesday20);
        changeColour("Wednesday", 21, Wednesday21);
        changeColour("Wednesday", 22, Wednesday22);
        changeColour("Wednesday", 23, Wednesday23);
    }

    public void changeThursday() {
        changeColour("Thursday", 0, Thursday0);
        changeColour("Thursday", 1, Thursday1);
        changeColour("Thursday", 2, Thursday2);
        changeColour("Thursday", 3, Thursday3);
        changeColour("Thursday", 4, Thursday4);
        changeColour("Thursday", 5, Thursday5);
        changeColour("Thursday", 6, Thursday6);
        changeColour("Thursday", 7, Thursday7);
        changeColour("Thursday", 8, Thursday8);
        changeColour("Thursday", 9, Thursday9);
        changeColour("Thursday", 10, Thursday10);
        changeColour("Thursday", 11, Thursday11);
        changeColour("Thursday", 12, Thursday12);
        changeColour("Thursday", 13, Thursday13);
        changeColour("Thursday", 14, Thursday14);
        changeColour("Thursday", 15, Thursday15);
        changeColour("Thursday", 16, Thursday16);
        changeColour("Thursday", 17, Thursday17);
        changeColour("Thursday", 18, Thursday18);
        changeColour("Thursday", 19, Thursday19);
        changeColour("Thursday", 20, Thursday20);
        changeColour("Thursday", 21, Thursday21);
        changeColour("Thursday", 22, Thursday22);
        changeColour("Thursday", 23, Thursday23);
    }

    public void changeFriday() {
        changeColour("Friday", 0, Friday0);
        changeColour("Friday", 1, Friday1);
        changeColour("Friday", 2, Friday2);
        changeColour("Friday", 3, Friday3);
        changeColour("Friday", 4, Friday4);
        changeColour("Friday", 5, Friday5);
        changeColour("Friday", 6, Friday6);
        changeColour("Friday", 7, Friday7);
        changeColour("Friday", 8, Friday8);
        changeColour("Friday", 9, Friday9);
        changeColour("Friday", 10, Friday10);
        changeColour("Friday", 11, Friday11);
        changeColour("Friday", 12, Friday12);
        changeColour("Friday", 13, Friday13);
        changeColour("Friday", 14, Friday14);
        changeColour("Friday", 15, Friday15);
        changeColour("Friday", 16, Friday16);
        changeColour("Friday", 17, Friday17);
        changeColour("Friday", 18, Friday18);
        changeColour("Friday", 19, Friday19);
        changeColour("Friday", 20, Friday20);
        changeColour("Friday", 21, Friday21);
        changeColour("Friday", 22, Friday22);
        changeColour("Friday", 23, Friday23);
    }

    public void changeSaturday() {
        changeColour("Saturday", 0, Saturday0);
        changeColour("Saturday", 1, Saturday1);
        changeColour("Saturday", 2, Saturday2);
        changeColour("Saturday", 3, Saturday3);
        changeColour("Saturday", 4, Saturday4);
        changeColour("Saturday", 5, Saturday5);
        changeColour("Saturday", 6, Saturday6);
        changeColour("Saturday", 7, Saturday7);
        changeColour("Saturday", 8, Saturday8);
        changeColour("Saturday", 9, Saturday9);
        changeColour("Saturday", 10, Saturday10);
        changeColour("Saturday", 11, Saturday11);
        changeColour("Saturday", 12, Saturday12);
        changeColour("Saturday", 13, Saturday13);
        changeColour("Saturday", 14, Saturday14);
        changeColour("Saturday", 15, Saturday15);
        changeColour("Saturday", 16, Saturday16);
        changeColour("Saturday", 17, Saturday17);
        changeColour("Saturday", 18, Saturday18);
        changeColour("Saturday", 19, Saturday19);
        changeColour("Saturday", 20, Saturday20);
        changeColour("Saturday", 21, Saturday21);
        changeColour("Saturday", 22, Saturday22);
        changeColour("Saturday", 23, Saturday23);
    }

    public void changeSunday() {
        changeColour("Sunday", 0, Sunday0);
        changeColour("Sunday", 1, Sunday1);
        changeColour("Sunday", 2, Sunday2);
        changeColour("Sunday", 3, Sunday3);
        changeColour("Sunday", 4, Sunday4);
        changeColour("Sunday", 5, Sunday5);
        changeColour("Sunday", 6, Sunday6);
        changeColour("Sunday", 7, Sunday7);
        changeColour("Sunday", 8, Sunday8);
        changeColour("Sunday", 9, Sunday9);
        changeColour("Sunday", 10, Sunday10);
        changeColour("Sunday", 11, Sunday11);
        changeColour("Sunday", 12, Sunday12);
        changeColour("Sunday", 13, Sunday13);
        changeColour("Sunday", 14, Sunday14);
        changeColour("Sunday", 15, Sunday15);
        changeColour("Sunday", 16, Sunday16);
        changeColour("Sunday", 17, Sunday17);
        changeColour("Sunday", 18, Sunday18);
        changeColour("Sunday", 19, Sunday19);
        changeColour("Sunday", 20, Sunday20);
        changeColour("Sunday", 21, Sunday21);
        changeColour("Sunday", 22, Sunday22);
        changeColour("Sunday", 23, Sunday23);
    }

    @FXML
    private Rectangle Monday0;
    @FXML
    private Rectangle Monday1;
    @FXML
    private Rectangle Monday2;
    @FXML
    private Rectangle Monday3;
    @FXML
    private Rectangle Monday4;
    @FXML
    private Rectangle Monday5;
    @FXML
    private Rectangle Monday6;
    @FXML
    private Rectangle Monday7;
    @FXML
    private Rectangle Monday8;
    @FXML
    private Rectangle Monday9;
    @FXML
    private Rectangle Monday10;
    @FXML
    private Rectangle Monday11;
    @FXML
    private Rectangle Monday12;
    @FXML
    private Rectangle Monday13;
    @FXML
    private Rectangle Monday14;
    @FXML
    private Rectangle Monday15;
    @FXML
    private Rectangle Monday16;
    @FXML
    private Rectangle Monday17;
    @FXML
    private Rectangle Monday18;
    @FXML
    private Rectangle Monday19;
    @FXML
    private Rectangle Monday20;
    @FXML
    private Rectangle Monday21;
    @FXML
    private Rectangle Monday22;
    @FXML
    private Rectangle Monday23;
    @FXML
    private Rectangle Tuesday0;
    @FXML
    private Rectangle Tuesday1;
    @FXML
    private Rectangle Tuesday2;
    @FXML
    private Rectangle Tuesday3;
    @FXML
    private Rectangle Tuesday4;
    @FXML
    private Rectangle Tuesday5;
    @FXML
    private Rectangle Tuesday6;
    @FXML
    private Rectangle Tuesday7;
    @FXML
    private Rectangle Tuesday8;
    @FXML
    private Rectangle Tuesday9;
    @FXML
    private Rectangle Tuesday10;
    @FXML
    private Rectangle Tuesday11;
    @FXML
    private Rectangle Tuesday12;
    @FXML
    private Rectangle Tuesday13;
    @FXML
    private Rectangle Tuesday14;
    @FXML
    private Rectangle Tuesday15;
    @FXML
    private Rectangle Tuesday16;
    @FXML
    private Rectangle Tuesday17;
    @FXML
    private Rectangle Tuesday18;
    @FXML
    private Rectangle Tuesday19;
    @FXML
    private Rectangle Tuesday20;
    @FXML
    private Rectangle Tuesday21;
    @FXML
    private Rectangle Tuesday22;
    @FXML
    private Rectangle Tuesday23;
    @FXML
    private Rectangle Wednesday0;
    @FXML
    private Rectangle Wednesday1;
    @FXML
    private Rectangle Wednesday2;
    @FXML
    private Rectangle Wednesday3;
    @FXML
    private Rectangle Wednesday4;
    @FXML
    private Rectangle Wednesday5;
    @FXML
    private Rectangle Wednesday6;
    @FXML
    private Rectangle Wednesday7;
    @FXML
    private Rectangle Wednesday8;
    @FXML
    private Rectangle Wednesday9;
    @FXML
    private Rectangle Wednesday10;
    @FXML
    private Rectangle Wednesday11;
    @FXML
    private Rectangle Wednesday12;
    @FXML
    private Rectangle Wednesday13;
    @FXML
    private Rectangle Wednesday14;
    @FXML
    private Rectangle Wednesday15;
    @FXML
    private Rectangle Wednesday16;
    @FXML
    private Rectangle Wednesday17;
    @FXML
    private Rectangle Wednesday18;
    @FXML
    private Rectangle Wednesday19;
    @FXML
    private Rectangle Wednesday20;
    @FXML
    private Rectangle Wednesday21;
    @FXML
    private Rectangle Wednesday22;
    @FXML
    private Rectangle Wednesday23;
    @FXML
    private Rectangle Thursday0;
    @FXML
    private Rectangle Thursday1;
    @FXML
    private Rectangle Thursday2;
    @FXML
    private Rectangle Thursday3;
    @FXML
    private Rectangle Thursday4;
    @FXML
    private Rectangle Thursday5;
    @FXML
    private Rectangle Thursday6;
    @FXML
    private Rectangle Thursday7;
    @FXML
    private Rectangle Thursday8;
    @FXML
    private Rectangle Thursday9;
    @FXML
    private Rectangle Thursday10;
    @FXML
    private Rectangle Thursday11;
    @FXML
    private Rectangle Thursday12;
    @FXML
    private Rectangle Thursday13;
    @FXML
    private Rectangle Thursday14;
    @FXML
    private Rectangle Thursday15;
    @FXML
    private Rectangle Thursday16;
    @FXML
    private Rectangle Thursday17;
    @FXML
    private Rectangle Thursday18;
    @FXML
    private Rectangle Thursday19;
    @FXML
    private Rectangle Thursday20;
    @FXML
    private Rectangle Thursday21;
    @FXML
    private Rectangle Thursday22;
    @FXML
    private Rectangle Thursday23;
    @FXML
    private Rectangle Friday0;
    @FXML
    private Rectangle Friday1;
    @FXML
    private Rectangle Friday2;
    @FXML
    private Rectangle Friday3;
    @FXML
    private Rectangle Friday4;
    @FXML
    private Rectangle Friday5;
    @FXML
    private Rectangle Friday6;
    @FXML
    private Rectangle Friday7;
    @FXML
    private Rectangle Friday8;
    @FXML
    private Rectangle Friday9;
    @FXML
    private Rectangle Friday10;
    @FXML
    private Rectangle Friday11;
    @FXML
    private Rectangle Friday12;
    @FXML
    private Rectangle Friday13;
    @FXML
    private Rectangle Friday14;
    @FXML
    private Rectangle Friday15;
    @FXML
    private Rectangle Friday16;
    @FXML
    private Rectangle Friday17;
    @FXML
    private Rectangle Friday18;
    @FXML
    private Rectangle Friday19;
    @FXML
    private Rectangle Friday20;
    @FXML
    private Rectangle Friday21;
    @FXML
    private Rectangle Friday22;
    @FXML
    private Rectangle Friday23;
    @FXML
    private Rectangle Saturday0;
    @FXML
    private Rectangle Saturday1;
    @FXML
    private Rectangle Saturday2;
    @FXML
    private Rectangle Saturday3;
    @FXML
    private Rectangle Saturday4;
    @FXML
    private Rectangle Saturday5;
    @FXML
    private Rectangle Saturday6;
    @FXML
    private Rectangle Saturday7;
    @FXML
    private Rectangle Saturday8;
    @FXML
    private Rectangle Saturday9;
    @FXML
    private Rectangle Saturday10;
    @FXML
    private Rectangle Saturday11;
    @FXML
    private Rectangle Saturday12;
    @FXML
    private Rectangle Saturday13;
    @FXML
    private Rectangle Saturday14;
    @FXML
    private Rectangle Saturday15;
    @FXML
    private Rectangle Saturday16;
    @FXML
    private Rectangle Saturday17;
    @FXML
    private Rectangle Saturday18;
    @FXML
    private Rectangle Saturday19;
    @FXML
    private Rectangle Saturday20;
    @FXML
    private Rectangle Saturday21;
    @FXML
    private Rectangle Saturday22;
    @FXML
    private Rectangle Saturday23;
    @FXML
    private Rectangle Sunday0;
    @FXML
    private Rectangle Sunday1;
    @FXML
    private Rectangle Sunday2;
    @FXML
    private Rectangle Sunday3;
    @FXML
    private Rectangle Sunday4;
    @FXML
    private Rectangle Sunday5;
    @FXML
    private Rectangle Sunday6;
    @FXML
    private Rectangle Sunday7;
    @FXML
    private Rectangle Sunday8;
    @FXML
    private Rectangle Sunday9;
    @FXML
    private Rectangle Sunday10;
    @FXML
    private Rectangle Sunday11;
    @FXML
    private Rectangle Sunday12;
    @FXML
    private Rectangle Sunday13;
    @FXML
    private Rectangle Sunday14;
    @FXML
    private Rectangle Sunday15;
    @FXML
    private Rectangle Sunday16;
    @FXML
    private Rectangle Sunday17;
    @FXML
    private Rectangle Sunday18;
    @FXML
    private Rectangle Sunday19;
    @FXML
    private Rectangle Sunday20;
    @FXML
    private Rectangle Sunday21;
    @FXML
    private Rectangle Sunday22;
    @FXML
    private Rectangle Sunday23;
}
