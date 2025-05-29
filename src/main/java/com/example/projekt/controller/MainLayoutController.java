package com.example.projekt.controller;

import com.example.projekt.event.ChangeViewEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainLayoutController {
    @FXML
    private AnchorPane contentPane;
    private String oldView = "";
    private String currentView = "";

    @FXML
    public void initialize() {
        showDashboard();

        contentPane.addEventHandler(ChangeViewEvent.CHANGE_VIEW, event -> {
            System.out.println(event.getFxmlPath());
            String path = event.getFxmlPath();

            oldView = currentView;
            currentView = path;

            setContent(event.getFxmlPath());
            event.consume();
        });
    }

    private void setContent(String fxmlPath) {
        try {
            var resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                return;
            }
            Node node = FXMLLoader.load(resource);
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard() {
        setContent("/view/dashboard-view.fxml");
    }

    @FXML
    private void showClients() {
        setContent("/view/ClientsView.fxml");
    }

    @FXML
    private void showWorkouts() {
        setContent("/view/plan-list-view.fxml");
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out...");
    }
}
