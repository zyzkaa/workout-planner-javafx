package com.example.projekt.controller;

import com.example.projekt.AppEventBus;
import com.example.projekt.event.bus.UserLoginEvent;
import com.example.projekt.model.entity.Client;
import com.example.projekt.repository.ClientRepository;
import com.example.projekt.api.GoogleCallbackServer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button googleLoginButton;

    public void handleGoogleLogin() {
        googleLoginButton.setDisable(true);
        Stage stage = (Stage) googleLoginButton.getScene().getWindow();
        stage.setIconified(true);

        try {
            new GoogleCallbackServer().start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
