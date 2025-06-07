package com.example.projekt.controller;

import com.example.projekt.api.dto.Client;
import com.example.projekt.service.ClientsService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class ClientsController {
    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    @FXML
    private VBox clientList;
    @FXML
    private VBox chatBox;

    private SimpleStringProperty clientId = new SimpleStringProperty();
    private ChatController chatController;

    @FXML
    public void initialize() {
        setClients();

        clients.addListener((ListChangeListener<Client>) change -> {
            Platform.runLater(() -> {
                clients.forEach(client -> {
                    clientList.getChildren().add(getClientBox(client.getName(), client.getId()));
                });
            });
        });

        clientId.addListener((observable, oldValue, newValue) -> {
            System.out.println("client id listener");

            handleSelectClient(newValue);
        });

        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/view/chat-view.fxml"));
        chatController = new ChatController();
        chatLoader.setController(chatController);

        try {
            Node loaded = chatLoader.load();
            chatBox.getChildren().add(chatLoader.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleSelectClient(String clientId) {
        System.out.println("handle select client");

        chatController.setClientId(Arrays.stream(clientId.split("/")).toList().getLast());
    }

    private VBox getClientBox(String name, String id) {
        VBox clientBox = new VBox();
        clientBox.setSpacing(5);
        clientBox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #cccccc;");

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        clientBox.getChildren().add(nameLabel);

        clientBox.setOnMouseClicked(event -> {
            clientId.set(id);
        });

        return clientBox;
    }

    private void setClients(){
        Thread.startVirtualThread(() -> {
            var fetched = ClientsService.getClients();
            fetched.sort(Comparator.comparing(Client::getLastMessage).reversed());
            Platform.runLater(() -> clients.setAll(fetched));
        });
    }
}