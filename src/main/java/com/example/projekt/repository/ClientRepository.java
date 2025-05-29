package com.example.projekt.repository;

import com.example.projekt.model.entity.Client;
import com.example.projekt.util.JpaUtil;
import lombok.Getter;

public class ClientRepository {
    @Getter
    private final static ClientRepository instance = new ClientRepository();

    public void add(Client client) {
        JpaUtil.doInTransactionVoid(session -> session.persist(client));
    }
}
