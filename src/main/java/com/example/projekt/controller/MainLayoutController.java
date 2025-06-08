package com.example.projekt.controller;

import com.example.projekt.AppEventBus;
import com.example.projekt.AuthSession;
import com.example.projekt.event.ChangeViewEvent;
import com.example.projekt.event.bus.UserLogoutEvent;
import com.example.projekt.service.ClientsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainLayoutController {
    @FXML
    private AnchorPane contentPane;
    private String oldView = "";
    private String currentView = "";
    private final ClientsService clientsService = ClientsService.getInstance();

    @FXML
    public void initialize() {
        showWorkouts();
        loginToFirebase();
        setupContentPane();
    }

    private void setupContentPane(){
        contentPane.addEventHandler(ChangeViewEvent.CHANGE_VIEW, event -> {
            String path = event.getFxmlPath();

            oldView = currentView;
            currentView = path;

            setContent(event.getFxmlPath());
            event.consume();
        });
    }

    private void loginToFirebase() {
        Thread.startVirtualThread(clientsService::loginUser);
    }

    private void setContent(String fxmlPath) {
        try {
            var resource = getClass().getResource(fxmlPath);
            if (resource == null) return;
            Node node = FXMLLoader.load(resource);

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);

            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showClients() {
        setContent("/view/clients-view.fxml");
    }

    @FXML
    private void showWorkouts() {
        setContent("/view/plan-list-view.fxml");
    }

    @FXML
    private void handleLogout() {
        AuthSession.clear();
        AppEventBus.getAsyncBus().post(new UserLogoutEvent());
    }
}
