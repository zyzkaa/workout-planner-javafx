package com.example.projekt;

import com.example.projekt.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import javafx.scene.image.Image;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("/view/login-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("/view/main-layout.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("/view/plan-creator-view.fxml"));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/gym-dumbbell-svgrepo-com.png")));
        stage.getIcons().add(icon);

        Scene scene = new Scene(fxmlLoader.load(), width, height);
        SceneManager sceneManager = new SceneManager(stage);

        stage.setTitle("Gym plan creator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}