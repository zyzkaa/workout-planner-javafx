package com.example.projekt.controller;

import com.example.projekt.model.entity.Client;
import com.example.projekt.repository.ClientRepository;
import javafx.fxml.FXML;

import java.awt.*;

public class ClientController {
    @FXML private TextField firstName;
    private final ClientRepository clientRepository = new ClientRepository();

    @FXML
    private void addClient() {
        String name = "imie";

        var client = new Client(name);
        clientRepository.add(client);
    }
}
