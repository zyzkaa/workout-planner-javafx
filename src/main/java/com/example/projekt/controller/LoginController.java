package com.example.projekt.controller;

import com.example.projekt.api.GoogleCallbackServer;
import com.example.projekt.service.PlanService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button googleLoginButton;

    @FXML
    public void initialize(){
        Thread.startVirtualThread(() -> {
            PlanService.getInstance().getAll();
        });
    }

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
