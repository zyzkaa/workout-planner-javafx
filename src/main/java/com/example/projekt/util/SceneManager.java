package com.example.projekt.util;

import com.example.projekt.AppEventBus;
import com.example.projekt.event.bus.UserLoginEvent;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
        AppEventBus.getAsyncBus().register(this);
    }

    private void changeScene(String path) {
        Platform.runLater(() -> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
                Parent root = loader.load();

                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Subscribe
    public void onUserLogin(UserLoginEvent event) {
        changeScene("/view/main-layout.fxml");
    }
}
