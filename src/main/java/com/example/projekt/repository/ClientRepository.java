package com.example.projekt.repository;

import lombok.Getter;

public class ClientRepository {
    @Getter
    private final static ClientRepository instance = new ClientRepository();


}
