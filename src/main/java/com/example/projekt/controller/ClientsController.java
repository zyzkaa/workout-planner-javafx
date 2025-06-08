package com.example.projekt.controller;

import com.example.projekt.api.dto.Client;
import com.example.projekt.service.ClientsService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class ClientsController {
    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    @FXML
    private VBox clientList;
    @FXML
    private VBox chatBox;

    private SimpleStringProperty clientId = new SimpleStringProperty();
    private ChatController chatController;
    public static final ClientsService clientsService = new ClientsService();


    @FXML
    public void initialize() {
        setupChat();
        setClients();
        setupClients();
    }

    private void setupChat(){
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/view/chat-view.fxml"));
        try {
            Node loaded = chatLoader.load();
            chatController = chatLoader.getController();
            chatBox.getChildren().add(loaded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupClients() {
        clients.addListener((ListChangeListener<Client>) change -> {
            Platform.runLater(() -> {
                clients.forEach(client -> {
                    clientList.getChildren().add(createClientBox(client.getName(), client.getId()));
                });
            });
        });

        clientId.addListener((observable, oldValue, newValue) -> {
            handleSelectClient(newValue);
        });
    }

    private void changeClientBoxStyle(String selectedClientId) {
        clientList.getChildren().forEach(node -> {
            if (node instanceof VBox clientCard) {
                if (clientCard.getUserData() != null &&
                        clientCard.getUserData().toString().equals(selectedClientId)) {
                    clientCard.getStyleClass().removeAll("client-card");
                    clientCard.getStyleClass().add("client-card-selected");
                } else {
                    clientCard.getStyleClass().removeAll("client-card-selected");
                    clientCard.getStyleClass().add("client-card");
                }
            }
        });
    }

    private void handleSelectClient(String clientId) {
        changeClientBoxStyle(clientId);
        chatController.setClientId(Arrays.stream(clientId.split("/")).toList().getLast());
    }

    private VBox createClientBox(String name, String id) {
        VBox clientBox = new VBox();
        clientBox.setSpacing(8);
        clientBox.getStyleClass().add("client-card");
        clientBox.setUserData(id);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("client-name-label");

        clientBox.getChildren().addAll(nameLabel);
        clientBox.setOnMouseClicked(event -> clientId.set(id));

        return clientBox;
    }


    private void setClients(){
        Thread.startVirtualThread(() -> {
            var fetched = clientsService.getClients();
            fetched.sort(Comparator.comparing(Client::getLastMessage).reversed());
            Platform.runLater(() -> clients.setAll(fetched));
        });
    }
}