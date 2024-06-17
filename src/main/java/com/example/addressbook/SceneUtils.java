package com.example.addressbook;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

import java.util.Objects;
public class SceneUtils {
    public static Scene createStyledScene(Parent root, int width, int height) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("theme.css")).toExternalForm());
        return scene;
    }

    public static ScrollPane createScrollableContent(Parent content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        return scrollPane;
    }
}