package com.example.addressbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.addressbook.SceneUtils.createScrollableContent;


public class MainApplication extends Application {
    public static final String TITLE = "App Tracker";
    public static final int WIDTH = 750;
    public static final int HEIGHT = 500;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();

        // Wrap the root layout in a ScrollPane
        ScrollPane scrollPane = createScrollableContent(root);

        Scene scene = SceneUtils.createStyledScene(scrollPane, WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
